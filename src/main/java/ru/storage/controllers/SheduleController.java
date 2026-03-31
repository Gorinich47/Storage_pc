package ru.storage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.storage.model.Shedule;
import ru.storage.services.EmployeeService;
import ru.storage.services.GeneralService;
import ru.storage.services.SheduleService;

import javax.validation.Valid;

@Controller
//@RequestMapping("")
public class SheduleController {


    private final SheduleService sheduleService;
    private final EmployeeService employeeService;//
    private final GeneralService generalService;/* service */

    @Autowired
    SheduleController(EmployeeService employeeService,
                      GeneralService generalService,
                      SheduleService sheduleService) {

        this.employeeService = employeeService;//
        this.generalService = generalService;
        this.sheduleService = sheduleService;

    }

    // Страница журнал сотрудников
    @GetMapping("/shedule")
    public String listShedule(Model model) {
        model.addAttribute("content", "shedule");
        model.addAttribute("shedules", sheduleService.getAll());
        model.addAttribute("allEmployees", employeeService.getAll());
        model.addAttribute("object", new Shedule());
        model.addAttribute("saveUrl", "/shedule/save");
        return "index";
    }

    // форма изменения/добавления данных о боксе
    @GetMapping("/shedule/fragments/shedule_edit_modal")
    public String getSheduleEditModal(Model model, @RequestParam(required = false) Long id) {
        model.addAttribute("object", sheduleService.getByIdOrNew(id));
        model.addAttribute("saveUrl", "/shedule/save");
        model.addAttribute("allEmployees", employeeService.getAll());

        return "shedule_edit_modal :: content_modal_form";
    }

    // сохранить тариф
    @PostMapping("/shedule/save")
    public String saveShedule(@Valid @ModelAttribute Shedule object,
                              BindingResult result,
                              Model model) {
        sheduleService.save(object);

        return "redirect:/shedule";
    }

    // Удалить тариф
    @GetMapping("/shedule/delete")
    public String deleteShedule(@RequestParam Long id) {
        sheduleService.delete(id);
        return "redirect:/shedule";
    }
}
