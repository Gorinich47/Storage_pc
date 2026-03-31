package ru.storage.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.storage.model.Movement;
import ru.storage.services.*;

import javax.validation.Valid;

@Controller
//@RequestMapping("")
public class MovementController {

    private final AccountService accountService;
    private final BoxService boxService;
    private final ClientService clientService;//
    private final EmployeeService employeeService;//
    private final GeneralService generalService;/* service */
    private final MovementService movementService;

    @Autowired
    MovementController(
            AccountService accountService,
            BoxService boxService,
            ClientService clientService,
            EmployeeService employeeService,//
            GeneralService generalService,
            MovementService movementService) {

        this.accountService = accountService;
        this.boxService = boxService;//
        this.clientService = clientService;//
        this.employeeService = employeeService;//
        this.generalService = generalService;
        this.movementService = movementService;

    }

    // Страница тарифы
    @GetMapping("/movement")
    public String listMovements(Model model) {
        model.addAttribute("content", "movement");
        model.addAttribute("movements", movementService.getAll());

        model.addAttribute("accounts", accountService.getAll());
        model.addAttribute("clients", clientService.getAll());
        model.addAttribute("employees", employeeService.getAll());
        model.addAttribute("object", new Movement());
        model.addAttribute("saveUrl", "/movement/save");
        return "index";
    }

    // форма изменения/добавления данных о боксе
    @GetMapping("/movement/fragments/movement_edit_modal")
    public String getMovementEditModal(Model model, @RequestParam(required = false) Long id) {
        model.addAttribute("object", movementService.getByIdOrNew(id));
        model.addAttribute("saveUrl", "/movement/save");
        model.addAttribute("accounts", accountService.getAll());
        model.addAttribute("clients", clientService.getAll());
        model.addAttribute("employees", employeeService.getAll());
        return "movement_edit_modal :: content_modal_form";
    }

    // сохранить тариф
    @PostMapping("/movement/save")
    public String saveMovement(@Valid @ModelAttribute Movement object,
                               BindingResult result,
                               Model model) {
        movementService.save(object);

        return "redirect:/movement";
    }

    // Удалить тариф
    @GetMapping("/movement/delete")
    public String deleteMovement(@RequestParam Long id) {
        movementService.delete(id);
        return "redirect:/movement";
    }
}
