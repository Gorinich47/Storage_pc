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
import ru.storage.services.BoxService;
import ru.storage.services.ClientService;
import ru.storage.services.EmployeeService;
import ru.storage.services.GeneralService;

@Controller
//@RequestMapping("")
public class ClientController {

    private final BoxService boxService; /* service */
    private final ClientService clientService;//
    private final EmployeeService employeeService;//
    private final GeneralService generalService;/* service */

    @Autowired
    ClientController(
                     BoxService boxService,
                     ClientService clientService,
                     EmployeeService employeeService,//
                     GeneralService generalService) {

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

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Order.asc("firstName"),
                        Sort.Order.asc("lastName")).ascending());

        Page<Client> clientPage = clientService.getAll(pageable);

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
        model.addAttribute("object", clientService.getByIdOrNew(id));
        model.addAttribute("saveUrl", "/client/save");
        //model.addAttribute("client", new Client());
        return "client_edit_modal :: content_modal_form";
    }

    // Сохранение клиента
    @PostMapping("/client/save")
    public String saveClient(@ModelAttribute Client object) {
        clientService.save(object);
        return "redirect:/client";
    }

    // Удаление бокса
    @GetMapping("/client/delete")
    public String deleteClient(@RequestParam Long id) {
        clientService.delete(id);
        return "redirect:/client";
    }

    @PostMapping("/client/random")
    public String getRandomClient() {
        clientService.generateClients();
        return "redirect:/client";
    }








}
