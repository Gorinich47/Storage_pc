package ru.storage.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.storage.model.Box;
import ru.storage.model.Client;
import ru.storage.services.ClientService;
import ru.storage.services.GeneralService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
//@RequestMapping("")
public class ClientController {

    private final ClientService clientService;//
    private final GeneralService generalService;/* service */

    @Autowired
    ClientController(
                     ClientService clientService,
                     GeneralService generalService) {

        this.clientService = clientService;//
        this.generalService = generalService;

    }


    // Страница клиенты
    @GetMapping("/client")
    public String getAllPersons(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "7") int size,
                                @RequestParam(required = false) String searchAll) {

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Order.asc("firstName"),
                        Sort.Order.asc("lastName")).ascending());

        Page<Client> clientPage = clientService.searchOrAll(pageable, searchAll);

        //Page<Client> clientPage = clientService.getAll(pageable);

        model.addAttribute("content", "client");
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
    }

    // форма изменения/добавления данных о боксе
    @GetMapping("/client/fragments/client_edit_modal")
    public String getClientEditModal(Model model, @RequestParam(required = false) Long id) {
        model.addAttribute("object", clientService.getByIdOrNew(id));
        model.addAttribute("saveUrl", "/client/save");
        //model.addAttribute("client", new Client());
        return "client_edit_modal :: content_modal_form";
    }

    @GetMapping("/client/search")
    @ResponseBody
    public List<Client> searchClients(@RequestParam String query) {

        List<Client> clientList = List.of();
        if (query != null || query.trim().length() >= 2) {
            clientList = clientService.searchOrAll(query);
        }

        return clientList;
    }

    @GetMapping("/client/select/{id}")
    @ResponseBody
    public Map<String, Object> selectClient(Model model, @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        Client client = clientService.getByIdOrNew(id);
        List<Box> rentedBoxes = List.of(new Box());

        if (id != null && client.getId() != null) {
//            // Получаем все счета клиента
//            List<Account> accounts = accountService.getByClientId(client.getId());
//            // Собираем все арендованные боксы
//            for (Account account : accounts) {
//                if (account.getBox() != null) {
//                    rentedBoxes.addAll(account.getBox());
//                }
//            }
        }
        // Добавляем отладочную информацию
        System.out.println("Selected client: " + client);
        System.out.println("Rented boxes count: " + rentedBoxes.size());

        //model.addAttribute("selectedClient", client );
        response.put("selectedClient", client);
        response.put("rentedBoxes", rentedBoxes);

        return response;
    }

    @GetMapping("/client/info/{id}")
    @ResponseBody
    public Client getClientInfo(@PathVariable Long id) {
        return clientService.getByIdOrNew(id);
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
