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
import ru.storage.repo.*;
import ru.storage.services.BoxService;
import ru.storage.services.ClientService;
import ru.storage.services.EmployeeService;
import ru.storage.services.GeneralService;

import javax.validation.Valid;

@Controller
//@RequestMapping("")
public class SheduleController {

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
    SheduleController(AccountRepository accountRepository,
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
