package ru.storage.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.storage.model.*;
import ru.storage.repo.*;
import ru.storage.services.BoxService;
import ru.storage.services.GeneralService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
//@RequestMapping("/")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BoxRepository boxRepository;
    @Autowired
    private PriceRepository priceRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private GeneralService generalService;
    @Autowired
    private BoxService boxService;

    // Главная страница
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "5") int size,
                        @RequestParam(required = false) String searchAll) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("idBox").ascending());

        Page<Box> boxPage;
        if (searchAll != null && !searchAll.trim().isEmpty()) {
            boxPage = boxRepository.findBySearchIgnoreCase(searchAll, pageable);
        } else {
            boxPage = boxRepository.findAllByOrderByIdBoxAsc(pageable);
        }

        model.addAttribute("content", "box");
        model.addAttribute("content_modal_form", "box_edit_modal");

        // Список с пагинацией и сортировкой
        model.addAttribute("boxPage", boxPage);
        model.addAttribute("boxes", boxPage.getContent());
        model.addAttribute("currentPage", boxPage.getNumber());
        model.addAttribute("totalPages", boxPage.getTotalPages());
        model.addAttribute("totalElements", boxPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("search", searchAll);

        // Данные для форм
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("box", new Box());

        return "index";
    }


    // Страница Боксы
    // Сохранение бокса
    @PostMapping("/box/save")
    public String saveBox(@ModelAttribute Box box) {
        boxRepository.save(box);
        return "redirect:/";
    }

    // Удаление бокса
    @GetMapping("/box/delete")
    public String deleteBox(@RequestParam Integer id) {
        boxRepository.deleteById(id);
        return "redirect:/";
    }

    // Аренда бокса или боксов
    @GetMapping("/box/rent")
    public String rentBox(Model model, @RequestParam Integer id) {
        List<Box> boxes = boxRepository.findByIdIn(List.of(id));
        //boxRepository.deleteById(id);
        model.addAttribute("content", "account");
        model.addAttribute("accounts", accountRepository.findAll());
        model.addAttribute("boxes", boxes);
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("account", new Account()); // для формы

        // Добавляем параметр для открытия модального окна
        model.addAttribute("openAccountModal", 1);
        return "index";
    }

    // Фрагменты Бокс
    // форма добавление счета (аренда)
    @GetMapping("/box/fragments/account_edit_modal")
    public String getAccountEditModal(Model model, @RequestParam Integer id) {
        List<Box> boxes = List.of(boxRepository.findById(id).get());
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("account", new Account()); // для формы
        model.addAttribute("allboxes", boxRepository.findAllByOrderByIdBoxAsc());
        model.addAttribute("curboxes", boxes);

        return "account_edit_modal :: content_modal_form";
    }

    // форма изменения/добавления данных о боксе
    @GetMapping("/box/fragments/box_edit_modal")
    public String getBoxEditModal(Model model, @RequestParam Integer id) {
        model.addAttribute("box", boxRepository.findById(id).get());

        model.addAttribute("box", new Box());
        return "box_edit_modal :: content_modal_form";
    }


    // Страница счета
    @GetMapping("/account")
    public String listAccounts(Model model) {

        model.addAttribute("content", "account");
        model.addAttribute("accounts", accountRepository.findAll());
        model.addAttribute("allboxes", boxRepository.findAllByOrderByIdBoxAsc());
        model.addAttribute("curboxes", new ArrayList<Box>());
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("account", new Account()); // для формы
        model.addAttribute("openAccountModal", 1001);
        return "index";
    }

    // форма редактирования счета
    @GetMapping("/account/fragments/account_edit_modal")
    public String getAccountEditModal(Model model, @RequestParam Long id) {
        List<Box> boxes = new ArrayList<Box>();
        Account account = accountRepository.findById(id).orElse(null);
        if (account != null) {
            boxes = account.getBox();
        }
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("account", new Account()); // для формы
        model.addAttribute("curboxes", boxes);
        model.addAttribute("allboxes", boxRepository.findAllByOrderByIdBoxAsc());

        return "account_edit_modal :: content_modal_form";
    }

    // сохранение счета
    @PostMapping("/account/save")
    public String saveAccount(
            @ModelAttribute Account account,
            @RequestParam(value = "boxIds", required = false) Integer[] boxIds) {

        account.setBox(boxRepository.findByIdIn(List.of(boxIds)));
        accountRepository.save(account);
        return "redirect:/account"; // /list?openTab=account";
    }

    // Удаление счета
    @GetMapping("/account/delete")
    public String deleteAccount(@RequestParam Long id) {
        accountRepository.deleteById(id);
        return "redirect:/account";
    }


    // Страница клиенты
    @GetMapping("/client")
    public String getAllPersons(Model model) {
        model.addAttribute("content", "client");
        model.addAttribute("clients", clientRepository.findAllByOrderByFirstNameAscLastNameAsc());
        model.addAttribute("client", new Client());
        return "index";
        //return "redirect:/client/list";
    }

    // форма изменения/добавления данных о боксе
    @GetMapping("/client/fragments/client_edit_modal")
    public String getClientEditModal(Model model, @RequestParam Long id) {
        model.addAttribute("client", clientRepository.findById(id).get());
        model.addAttribute("client", new Client());
        return "client_edit_modal :: content_modal_form";
    }

    // Сохранение клиента
    @PostMapping("/client/save")
    public String saveClient(@ModelAttribute Client box) {
        clientRepository.save(box);
        return "redirect:/client";
    }

    // Удаление бокса
    @GetMapping("/client/delete")
    public String deleteClient(@RequestParam Long id) {
        clientRepository.deleteById(id);
        return "redirect:/client";
    }


    // Страница сотрудники
    @GetMapping("/employee")
    public String listEmployees(Model model) {
        model.addAttribute("content", "employee");
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("employee", new Employee());
        return "index";
    }

    // сохранение сотрудника
    @PostMapping("/employee/save")
    public String saveEmployee(@ModelAttribute Employee employee) {
        employeeRepository.save(employee);
        return "redirect:/employee"; //list?openTab=employee";
    }

    // Удаление сотрудникa
    @GetMapping("/employee/delete")
    public String deleteEmployee(@RequestParam Long id) {
        employeeRepository.deleteById(id);
        return "redirect:/employee"; //list?openTab=employee";
    }


    // Страница тарифы
    @GetMapping("/price")
    public String listPrices(Model model) {
        model.addAttribute("content", "price");
        model.addAttribute("prices", priceRepository.findAllByOrderByBoxAscDateStartAscDateEndAsc());
        model.addAttribute("allboxes", boxRepository.findAllByOrderByIdBoxAsc());
        model.addAttribute("newPrice", new Price());
        return "index";
    }

    // сохранить тариф
    @PostMapping("/price/save")
    public String savePrice(@Valid @ModelAttribute Price newPrice,
                            BindingResult result,
                            Model model) {
        priceRepository.save(newPrice);

        return "redirect:/price";
    }

    // Удалить тариф
    @GetMapping("/price/delete")
    public String deletePrice(@RequestParam Long id) {
        priceRepository.deleteById(id);
        return "redirect:/price";
    }


}
