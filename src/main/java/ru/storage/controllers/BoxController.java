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

@Controller
//@RequestMapping("")
public class BoxController {

    private final AccountService accountService;//
    private final BoxService boxService; /* service */
    private final ClientService clientService;//
    private final EmployeeService employeeService;//
    private final GeneralService generalService;/* service */

    @Autowired
    BoxController(AccountService accountService,
                  BoxService boxService,
                  ClientService clientService,
                  EmployeeService employeeService,//
                  GeneralService generalService) {

        this.accountService = accountService;
        this.boxService = boxService;//
        this.clientService = clientService;//
        this.employeeService = employeeService;//
        this.generalService = generalService;
    }

    // Страница Боксы


    // Аренда бокса или боксов
    @GetMapping("/box/rent")
    public String rentBox(Model model, @RequestParam Long id) {

        //boxRepository.deleteById(id);
        model.addAttribute("content", "account");
        model.addAttribute("accounts", accountService.getAll());
        model.addAttribute("boxes", boxService.getByListId(id));
        model.addAttribute("clients", clientService.getAll());
        model.addAttribute("employees", employeeService.getAll());
        model.addAttribute("object", new Account()); // для формы

        // Добавляем параметр для открытия модального окна
        model.addAttribute("openAccountModal", 1);
        return "index";
    }

    // Фрагменты Бокс
    // форма добавление счета (аренда)
    @GetMapping("/box/fragments/account_edit_modal")
    public String getAccountEditModal(Model model, @RequestParam Long id) {
        model.addAttribute("clients", clientService.getAll());
        model.addAttribute("employees", employeeService.getAll());
        model.addAttribute("object", new Account()); // для формы
        model.addAttribute("allboxes", boxService.getAll());
        model.addAttribute("curboxes", boxService.getByListId(id));
        model.addAttribute("saveUrl", "/account/save");

        return "account_edit_modal :: content_modal_form";
    }

    // форма изменения/добавления данных о боксе
    @GetMapping("/box/fragments/box_edit_modal")
    public String getBoxEditModal(Model model, @RequestParam Long id) {
        model.addAttribute("object", boxService.getByIdOrNew(id));
        model.addAttribute("saveUrl", "/box/save");
        //model.addAttribute("box", new Box());
        return "box_edit_modal :: content_modal_form";
    }

    // Сохранение бокса
    @PostMapping("/box/save")
    public String saveBox(@ModelAttribute Box object) {
        boxService.save(object);
        return "redirect:/";
    }

    // Удаление бокса
    @GetMapping("/box/delete")
    public String deleteBox(@RequestParam Long id) {
        boxService.delete(id);
        return "redirect:/";
    }

}
