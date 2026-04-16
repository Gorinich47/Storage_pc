package ru.storage.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.storage.model.Account;
import ru.storage.model.Box;
import ru.storage.services.*;

import java.util.ArrayList;
import java.util.List;

@Controller
//@RequestMapping("")
public class BoxController {

    private final AccountService accountService;//
    private final BoxService boxService; /* service */
    private final ClientService clientService;//
    private final EmployeeService employeeService;//
    private final GeneralService generalService;/* service */

    @Autowired
    BoxController(AccountService accountService,
                  BoxService boxService,
                  ClientService clientService,
                  EmployeeService employeeService,//
                  GeneralService generalService) {

        this.accountService = accountService;
        this.boxService = boxService;//
        this.clientService = clientService;//
        this.employeeService = employeeService;//
        this.generalService = generalService;
    }

    // Страница Боксы
    @GetMapping("/box")
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "5") int size,
                        @RequestParam(required = false) String searchAll) {

        Page<Box> boxPage = boxService.searchOrAll(page, size, searchAll);

        model.addAttribute("content", "box");
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
        model.addAttribute("clients", null);//clientService.getAll());
        model.addAttribute("employees", employeeService.getAll());
        model.addAttribute("object", new Box());
        model.addAttribute("saveUrl", "/box/save");
        model.addAttribute("labelInput", "");

        return "index";
    }

    // Аренда бокса или боксов
    @GetMapping("/box/rent")
    public String rentBox(Model model, @RequestParam Long id) {

        //boxRepository.deleteById(id);
        model.addAttribute("content", "account");
        model.addAttribute("accounts", accountService.getAll());
        model.addAttribute("boxes", boxService.getByListId(id));
        model.addAttribute("clients", clientService.getAll());
        model.addAttribute("employees", employeeService.getAll());
        model.addAttribute("object", new Account()); // для формы

        // Добавляем параметр для открытия модального окна
        model.addAttribute("openAccountModal", 1);
        return "index";
    }

    @PostMapping("/box/rent/{id}")
    public String addRentBox(Model model, @PathVariable Long id, HttpSession session) {
        // Получаем текущий список из сессии или создаем новый
        List<Box> selectedBoxes = (List<Box>) session.getAttribute("selectedBoxes");
        if (selectedBoxes == null) {
            selectedBoxes = new ArrayList<>();
        }

        // Проверяем, нет ли его уже в списке, и добавляем (поиск в базе только для получения данных объекта)
        if (selectedBoxes.isEmpty() || selectedBoxes.stream().noneMatch(b -> b.getId().equals(id))) {
            Box box = boxService.getByIdOrNew(id);
            selectedBoxes.add(box);
        }

        session.setAttribute("selectedBoxes", selectedBoxes);
        model.addAttribute("rentedBoxes", selectedBoxes);

        return "box_front :: rented-boxes-fragment";
    }

    @PostMapping("/box/rent-remove/{id}")
    public String removeRentBox(Model model, @PathVariable Long id, HttpSession session) {
        // Получаем текущий список из сессии или создаем новый
        List<Box> selectedBoxes = (List<Box>) session.getAttribute("selectedBoxes");
        if (selectedBoxes == null) {
            selectedBoxes = new ArrayList<>();
        }

        // Проверяем, нет ли его уже в списке, и добавляем (поиск в базе только для получения данных объекта)
        if (!selectedBoxes.isEmpty()) {
            selectedBoxes.removeIf(b -> b.getId().equals(id));
        }

        session.setAttribute("selectedBoxes", selectedBoxes);
        model.addAttribute("rentedBoxes", selectedBoxes);

        return "box_front :: rented-boxes-fragment";
    }

    // Фрагменты Бокс
    // форма добавление счета (аренда)
    @GetMapping("/box/fragments/account_edit_modal")
    public String getAccountEditModal(Model model,
                                      @RequestParam(required = false) Long[] id) {

        model.addAttribute("clients", clientService.getAll());
        model.addAttribute("employees", employeeService.getAll());
        model.addAttribute("object", new Account()); // для формы
        model.addAttribute("allboxes", boxService.getAll());
        model.addAttribute("curboxes", boxService.getByListId(List.of(id)));
        model.addAttribute("saveUrl", "/account/save");

        return "account_edit_modal :: content_modal_form";
    }

    // форма изменения/добавления данных о боксе
    @GetMapping("/box/fragments/box_edit_modal")
    public String getBoxEditModal(Model model, @RequestParam(required = false) Long id) {
        model.addAttribute("object", boxService.getByIdOrNew(id));
        model.addAttribute("saveUrl", "/box/save");
        //model.addAttribute("box", new Box());
        return "box_edit_modal :: content_modal_form";
    }

    @GetMapping("/box/fragments/box-grid")
    public String getBoxGridFragment(Model model,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "6") int size,
                                     @RequestParam(required = false) String search) {

        Page<Box> boxPage = boxService.searchOrAll(page, size, search);

        model.addAttribute("boxes", boxPage.getContent());
        model.addAttribute("currentPage", boxPage.getNumber());
        model.addAttribute("totalPages", boxPage.getTotalPages());

        return "box_front :: box-grid-fragment";
    }


    // Сохранение бокса
    @PostMapping("/box/save")
    public String saveBox(@ModelAttribute Box object) {
        boxService.save(object);
        return "redirect:/";
    }

    // Удаление бокса
    @DeleteMapping("/box/delete")
    public String deleteBox(@RequestParam Long id) {
        boxService.delete(id);
        return "redirect:/";
    }

}
