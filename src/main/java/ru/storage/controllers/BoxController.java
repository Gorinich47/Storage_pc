package ru.storage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.storage.model.Account;
import ru.storage.model.Box;
import ru.storage.repo.*;
import ru.storage.services.BoxService;
import ru.storage.services.ClientService;
import ru.storage.services.EmployeeService;
import ru.storage.services.GeneralService;

import java.util.List;

@Controller
//@RequestMapping("")
public class BoxController {

    private final AccountRepository accountRepository;
    private final BoxRepository boxRepository;

    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;

    private final MovementRepository movementRepository;
    private final PriceRepository priceRepository;
    private final SheduleRepository sheduleRepository;

    private final BoxService boxService; /* service */
    private final ClientService clientService;//
    private final EmployeeService employeeService;//
    private final GeneralService generalService;/* service */

    @Autowired
    BoxController(AccountRepository accountRepository,
                  BoxRepository boxRepository,
                  ClientRepository clientRepository,
                  EmployeeRepository employeeRepository,
                  MovementRepository movementRepository,
                  PriceRepository priceRepository,
                  SheduleRepository sheduleRepository,
                  BoxService boxService,
                  ClientService clientService,
                  EmployeeService employeeService,//
                  GeneralService generalService) {

        this.accountRepository = accountRepository;
        this.boxRepository = boxRepository;
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.movementRepository = movementRepository;
        this.priceRepository = priceRepository;
        this.sheduleRepository = sheduleRepository;

        this.boxService = boxService;//
        this.clientService = clientService;//
        this.employeeService = employeeService;//
        this.generalService = generalService;

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

}
