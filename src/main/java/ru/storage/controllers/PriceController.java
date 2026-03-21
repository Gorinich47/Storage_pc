package ru.storage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.storage.model.Price;
import ru.storage.repo.*;
import ru.storage.services.BoxService;
import ru.storage.services.ClientService;
import ru.storage.services.EmployeeService;
import ru.storage.services.GeneralService;

import javax.validation.Valid;

@Controller
//@RequestMapping("")
public class PriceController {

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
    PriceController(AccountRepository accountRepository,
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

    // Страница тарифы
    @GetMapping("/price")
    public String listPrices(Model model) {
        model.addAttribute("content", "price");
        model.addAttribute("prices", priceRepository.findAllByOrderByBoxAscDateStartAscDateEndAsc());
        model.addAttribute("allboxes", boxRepository.findAllByOrderByIdBoxAsc());
        model.addAttribute("object", new Price());
        model.addAttribute("saveUrl", "/price/save");
        return "index";
    }

    // форма изменения/добавления данных о боксе
    @GetMapping("/price/fragments/price_edit_modal")
    public String getPriceEditModal(Model model, @RequestParam Long id) {
        model.addAttribute("object", priceRepository.findById(id).get());
        model.addAttribute("saveUrl", "/price/save");
        model.addAttribute("allboxes", boxRepository.findAllByOrderByIdBoxAsc());
        //model.addAttribute("client", new Client());
        return "price_edit_modal :: content_modal_form";
    }

    // сохранить тариф
    @PostMapping("/price/save")
    public String savePrice(@Valid @ModelAttribute Price object,
                            BindingResult result,
                            Model model) {
        priceRepository.save(object);

        return "redirect:/price";
    }

    // Удалить тариф
    @GetMapping("/price/delete")
    public String deletePrice(@RequestParam Long id) {
        priceRepository.deleteById(id);
        return "redirect:/price";
    }
}
