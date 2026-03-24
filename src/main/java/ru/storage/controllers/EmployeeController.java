package ru.storage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.storage.model.Employee;
import ru.storage.services.EmployeeService;
import ru.storage.services.GeneralService;

@Controller
//@RequestMapping("")
public class EmployeeController {


    private final EmployeeService employeeService;//
    private final GeneralService generalService;/* service */

    @Autowired
    EmployeeController(
                       EmployeeService employeeService,//
                       GeneralService generalService) {

        this.employeeService = employeeService;//
        this.generalService = generalService;

    }

    // Страница сотрудники
    @GetMapping("/employee")
    public String listEmployees(Model model) {
        model.addAttribute("content", "employee");
        model.addAttribute("employees", employeeService.getAll());
        model.addAttribute("object", new Employee());
        model.addAttribute("saveUrl", "/employee/save");
        return "index";
    }

    // форма изменения/добавления данных о боксе
    @GetMapping("/employee/fragments/client_edit_modal")
    public String getEmployeetEditModal(Model model, @RequestParam Long id) {
        model.addAttribute("object", employeeService.getByIdOrNew(id));
        model.addAttribute("saveUrl", "/employee/save");
        //model.addAttribute("client", new Client());
        return "client_edit_modal :: content_modal_form";
    }

    // сохранение сотрудника
    @PostMapping("/employee/save")
    public String saveEmployee(@ModelAttribute Employee object) {
        employeeService.save(object);
        return "redirect:/employee"; //list?openTab=employee";
    }

    // Удаление сотрудникa
    @GetMapping("/employee/delete")
    public String deleteEmployee(@RequestParam Long id) {
        employeeService.delete(id);
        return "redirect:/employee";
    }

}
