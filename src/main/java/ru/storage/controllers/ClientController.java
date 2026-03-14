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
import ru.storage.services.DemoService;
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
    private SheduleRepository sheduleRepository;
    @Autowired
    private MovementRepository movementRepository;

    @Autowired
    private GeneralService generalService;
    @Autowired
    private BoxService boxService;
    //@Autowired
    //private DemoService demoService;

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
        model.addAttribute("clients", clientRepository.findAllByOrderByFirstNameAscLastNameAsc());
        model.addAttribute("employees", employeeRepository.findAllByOrderByFirstNameAscLastNameAsc());
        model.addAttribute("object", new Box());
        model.addAttribute("saveUrl", "/box/save");

        return "index";
    }


    // Страница Боксы
    // Сохранение бокса
    @PostMapping("/box/save")
    public String saveBox(@ModelAttribute Box object) {
        boxRepository.save(object);
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
        model.addAttribute("object", new Account()); // для формы

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
        model.addAttribute("object", new Account()); // для формы
        model.addAttribute("allboxes", boxRepository.findAllByOrderByIdBoxAsc());
        model.addAttribute("curboxes", boxes);
        model.addAttribute("saveUrl", "/account/save");

        return "account_edit_modal :: content_modal_form";
    }

    // форма изменения/добавления данных о боксе
    @GetMapping("/box/fragments/box_edit_modal")
    public String getBoxEditModal(Model model, @RequestParam Integer id) {
        model.addAttribute("object", boxRepository.findById(id).get());
        model.addAttribute("saveUrl", "/box/save");
        //model.addAttribute("box", new Box());
        return "box_edit_modal :: content_modal_form";
    }


    // Страница счета
    @GetMapping("/account")
    public String listAccounts(Model model) {

        model.addAttribute("content", "account");
        model.addAttribute("accounts", accountRepository.findAll());
        model.addAttribute("allboxes", boxRepository.findAllByOrderByIdBoxAsc());
        model.addAttribute("curboxes", new ArrayList<Box>());
        model.addAttribute("clients", clientRepository.findAllByOrderByFirstNameAscLastNameAsc());
        model.addAttribute("employees", employeeRepository.findAllByOrderByFirstNameAscLastNameAsc());
        model.addAttribute("object", new Account()); // для формы
        model.addAttribute("saveUrl", "/account/save");
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
        } else {
            account = new Account();
        }
        model.addAttribute("clients", clientRepository.findAllByOrderByFirstNameAscLastNameAsc());
        model.addAttribute("employees", employeeRepository.findAllByOrderByFirstNameAscLastNameAsc());
        model.addAttribute("object", account); // для формы
        model.addAttribute("curboxes", boxes);
        model.addAttribute("allboxes", boxRepository.findAllByOrderByIdBoxAsc());
        model.addAttribute("saveUrl", "/account/save");

        return "account_edit_modal :: content_modal_form";
    }

    // сохранение счета
    @PostMapping("/account/save")
    public String saveAccount(
            @ModelAttribute Account object,
            @RequestParam(value = "boxIds", required = false) Integer[] boxIds) {

        object.setBox(boxRepository.findByIdIn(List.of(boxIds)));
        accountRepository.save(object);
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
    public String getAllPersons(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "7") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("firstName"), Sort.Order.asc("lastName")).ascending());

        Page<Client> clientPage = clientRepository.findAll(pageable);

        model.addAttribute("content", "client");
        //model.addAttribute("clients", clientRepository.findAllByOrderByFirstNameAscLastNameAsc());
        model.addAttribute("object", new Client());
        model.addAttribute("saveUrl", "/client/save");
        // Список с пагинацией и сортировкой
        model.addAttribute("clientPage", clientPage);
        model.addAttribute("clients", clientPage.getContent());
        model.addAttribute("currentPage", clientPage.getNumber());
        model.addAttribute("totalPages", clientPage.getTotalPages());
        model.addAttribute("totalElements", clientPage.getTotalElements());
        model.addAttribute("size", size);





        return "index";
        //return "redirect:/client/list";
    }

    // форма изменения/добавления данных о боксе
    @GetMapping("/client/fragments/client_edit_modal")
    public String getClientEditModal(Model model, @RequestParam Long id) {
        model.addAttribute("object", clientRepository.findById(id).get());
        model.addAttribute("saveUrl", "/client/save");
        //model.addAttribute("client", new Client());
        return "client_edit_modal :: content_modal_form";
    }

    // Сохранение клиента
    @PostMapping("/client/save")
    public String saveClient(@ModelAttribute Client object) {
        clientRepository.save(object);
        return "redirect:/client";
    }

    // Удаление бокса
    @GetMapping("/client/delete")
    public String deleteClient(@RequestParam Long id) {
        clientRepository.deleteById(id);
        return "redirect:/client";
    }

    @PostMapping("/client/random")
    public String getRandomClient() {

        for (int i = 0; i < 10; i++) {
            Client randomClient = DemoService.FIO.randomClient();
            clientRepository.save(randomClient);
        }

        return "redirect:/client";
    }

    // Страница сотрудники
    @GetMapping("/employee")
    public String listEmployees(Model model) {
        model.addAttribute("content", "employee");
        model.addAttribute("employees", employeeRepository.findAllByOrderByFirstNameAscLastNameAsc());
        model.addAttribute("object", new Employee());
        model.addAttribute("saveUrl", "/employee/save");
        return "index";
    }

    // форма изменения/добавления данных о боксе
    @GetMapping("/employee/fragments/client_edit_modal")
    public String getEmployeetEditModal(Model model, @RequestParam Long id) {
        model.addAttribute("object", employeeRepository.findById(id).get());
        model.addAttribute("saveUrl", "/employee/save");
        //model.addAttribute("client", new Client());
        return "client_edit_modal :: content_modal_form";
    }

    // сохранение сотрудника
    @PostMapping("/employee/save")
    public String saveEmployee(@ModelAttribute Employee object) {
        employeeRepository.save(object);
        return "redirect:/employee"; //list?openTab=employee";
    }

    // Удаление сотрудникa
    @GetMapping("/employee/delete")
    public String deleteEmployee(@RequestParam Long id) {
        employeeRepository.deleteById(id);
        return "redirect:/employee";
    }

    // Страница тарифы
    @GetMapping("/price")
    public String listPrices(Model model) {
        model.addAttribute("content", "price");
        model.addAttribute("prices", priceRepository.findAllByOrderByBoxAscDateStartAscDateEndAsc());
        model.addAttribute("allboxes", boxRepository.findAllByOrderByIdBoxAsc());
        model.addAttribute("object", new Price());
        model.addAttribute("saveUrl", "/price/save");
        return "index";
    }

    // форма изменения/добавления данных о боксе
    @GetMapping("/price/fragments/price_edit_modal")
    public String getPriceEditModal(Model model, @RequestParam Long id) {
        model.addAttribute("object", priceRepository.findById(id).get());
        model.addAttribute("saveUrl", "/price/save");
        model.addAttribute("allboxes", boxRepository.findAllByOrderByIdBoxAsc());
        //model.addAttribute("client", new Client());
        return "price_edit_modal :: content_modal_form";
    }

    // сохранить тариф
    @PostMapping("/price/save")
    public String savePrice(@Valid @ModelAttribute Price object,
                            BindingResult result,
                            Model model) {
        priceRepository.save(object);

        return "redirect:/price";
    }

    // Удалить тариф
    @GetMapping("/price/delete")
    public String deletePrice(@RequestParam Long id) {
        priceRepository.deleteById(id);
        return "redirect:/price";
    }


    // Страница журнал сотрудников
    @GetMapping("/shedule")
    public String listShedule(Model model) {
        model.addAttribute("content", "shedule");
        model.addAttribute("shedules", sheduleRepository.findAllByOrderByEmployeeAscDateStartAscDateEndAsc());
        model.addAttribute("allEmployees", employeeRepository.findAllByOrderByFirstNameAscLastNameAsc());
        model.addAttribute("object", new Shedule());
        model.addAttribute("saveUrl", "/shedule/save");
        return "index";
    }

    // форма изменения/добавления данных о боксе
    @GetMapping("/shedule/fragments/shedule_edit_modal")
    public String getSheduleEditModal(Model model, @RequestParam Long id) {
        model.addAttribute("object", sheduleRepository.findById(id).get());
        model.addAttribute("saveUrl", "/shedule/save");
        model.addAttribute("allEmployees", employeeRepository.findAllByOrderByFirstNameAscLastNameAsc());

        return "shedule_edit_modal :: content_modal_form";
    }

    // сохранить тариф
    @PostMapping("/shedule/save")
    public String saveShedule(@Valid @ModelAttribute Shedule object,
                              BindingResult result,
                              Model model) {
        sheduleRepository.save(object);

        return "redirect:/shedule";
    }

    // Удалить тариф
    @GetMapping("/shedule/delete")
    public String deleteShedule(@RequestParam Long id) {
        sheduleRepository.deleteById(id);
        return "redirect:/shedule";
    }

}
