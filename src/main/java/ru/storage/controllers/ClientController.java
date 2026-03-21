package ru.storage.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.storage.model.Client;
import ru.storage.repo.*;
import ru.storage.services.BoxService;
import ru.storage.services.ClientService;
import ru.storage.services.EmployeeService;
import ru.storage.services.GeneralService;

@Controller
//@RequestMapping("")
public class ClientController {


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
    ClientController(AccountRepository accountRepository,
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


    // Страница клиенты
    @GetMapping("/client")
    public String getAllPersons(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "7") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("firstName"), Sort.Order.asc("lastName")).ascending());

        Page<Client> clientPage = clientRepository.findAll(pageable);

        model.addAttribute("content", "client");
        //model.addAttribute("clients", clientRepository.findAllByOrderByFirstNameAscLastNameAsc());
        model.addAttribute("object", new Client());
        model.addAttribute("saveUrl", "/client/save");
        // Список с пагинацией и сортировкой
        model.addAttribute("clientPage", clientPage);
        model.addAttribute("clients", clientPage.getContent());
        model.addAttribute("currentPage", clientPage.getNumber());
        model.addAttribute("totalPages", clientPage.getTotalPages());
        model.addAttribute("totalElements", clientPage.getTotalElements());
        model.addAttribute("size", size);

        return "index";
        //return "redirect:/client/list";
    }

    // форма изменения/добавления данных о боксе
    @GetMapping("/client/fragments/client_edit_modal")
    public String getClientEditModal(Model model, @RequestParam Long id) {
        model.addAttribute("object", clientRepository.findById(id).get());
        model.addAttribute("saveUrl", "/client/save");
        //model.addAttribute("client", new Client());
        return "client_edit_modal :: content_modal_form";
    }

    // Сохранение клиента
    @PostMapping("/client/save")
    public String saveClient(@ModelAttribute Client object) {
        clientRepository.save(object);
        return "redirect:/client";
    }

    // Удаление бокса
    @GetMapping("/client/delete")
    public String deleteClient(@RequestParam Long id) {
        clientRepository.deleteById(id);
        return "redirect:/client";
    }

    @PostMapping("/client/random")
    public String getRandomClient() {

        clientService.generateClients();

        return "redirect:/client";
    }








}
