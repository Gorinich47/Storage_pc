package ru.storage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.storage.model.Box;
import ru.storage.services.BoxService;
import ru.storage.services.ClientService;
import ru.storage.services.EmployeeService;
import ru.storage.services.GeneralService;

@Controller
@RequestMapping("/")
public class IndexController {

    private final BoxService boxService; /* service */
    private final ClientService clientService;//
    private final EmployeeService employeeService;//
    private final GeneralService generalService;/* service */

    @Autowired
    IndexController(
                    BoxService boxService,
                    ClientService clientService,
                    EmployeeService employeeService,//
                    GeneralService generalService) {

        this.boxService = boxService;//
        this.clientService = clientService;//
        this.employeeService = employeeService;//
        this.generalService = generalService;

    }

    // Главная страница
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "6") int size,
                        @RequestParam(required = false) String searchAll) {

        Page<Box> boxPage = boxService.searchOrAll(page, size, searchAll);

        model.addAttribute("content", "box_front");
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
        model.addAttribute("clients", clientService.getAll());
        model.addAttribute("employees", employeeService.getAll());
        model.addAttribute("object", new Box());
        model.addAttribute("saveUrl", "/box/save");

        return "index";
    }
}

