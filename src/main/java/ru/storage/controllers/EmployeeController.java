package ru.storage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.storage.model.Employee;
import ru.storage.repo.*;
import ru.storage.services.BoxService;
import ru.storage.services.ClientService;
import ru.storage.services.EmployeeService;
import ru.storage.services.GeneralService;

@Controller
//@RequestMapping("")
public class EmployeeController {

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
    EmployeeController(AccountRepository accountRepository,
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

}
