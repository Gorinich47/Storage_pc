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

import java.util.ArrayList;
import java.util.List;

@Controller
//@RequestMapping("")
public class AccountController {

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
    AccountController(AccountRepository accountRepository,
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



}
