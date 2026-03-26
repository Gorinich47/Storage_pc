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
import ru.storage.services.*;

import java.util.ArrayList;

@Controller
//@RequestMapping("")
public class AccountController {

    private final AccountService accountService;
    private final BoxService boxService; /* service */
    private final ClientService clientService;//
    private final EmployeeService employeeService;//
    private final GeneralService generalService;/* service */

    @Autowired
    AccountController(AccountService accountService,
                      BoxService boxService,
                      ClientService clientService,
                      EmployeeService employeeService,//
                      GeneralService generalService
    ) {

        this.accountService = accountService;
        this.boxService = boxService;//
        this.clientService = clientService;//
        this.employeeService = employeeService;//
        this.generalService = generalService;
    }

    // Страница счета
    @GetMapping("/account")
    public String listAccounts(Model model) {

        model.addAttribute("content", "account");
        model.addAttribute("accounts", accountService.getAll());
        model.addAttribute("allboxes", boxService.getAll());
        model.addAttribute("curboxes", new ArrayList<Box>());
        model.addAttribute("clients", clientService.getAll());
        model.addAttribute("employees", employeeService.getAll());
        model.addAttribute("object", new Account()); // для формы
        model.addAttribute("saveUrl", "/account/save");
        model.addAttribute("openAccountModal", 1001);
        return "index";
    }

    // форма редактирования счета
    @GetMapping("/account/fragments/account_edit_modal")
    public String getAccountEditModal(Model model, @RequestParam(required = false) Long id) {

        Account account = accountService.getByIdOrNew(id);

        model.addAttribute("clients", clientService.getAll());
        model.addAttribute("employees", employeeService.getAll());
        model.addAttribute("object", account); // для формы
        model.addAttribute("curboxes", account.getBox());
        model.addAttribute("allboxes", boxService.getAll());
        model.addAttribute("saveUrl", "/account/save");

        return "account_edit_modal :: content_modal_form";
    }

    // сохранение счета
    @PostMapping("/account/save")
    public String saveAccount(
            @ModelAttribute Account object,
            @RequestParam(value = "boxIds", required = false) Long[] boxIds) {

        accountService.save(object, boxIds);

        return "redirect:/account"; // /list?openTab=account";
    }

    // Удаление счета
    @GetMapping("/account/delete")
    public String deleteAccount(@RequestParam Long id) {
        if (id != null) {
            accountService.delete(id);
        } // иначе должно быть исключение MissingServletRequestParameterException()
        return "redirect:/account";
    }



}
