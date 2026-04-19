package ru.storage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.storage.model.Price;
import ru.storage.services.*;

import javax.validation.Valid;

@Controller
//@RequestMapping("")
public class PriceController {


    private final PriceService priceService;

    private final BoxService boxService; /* service */
    private final ClientService clientService;//
    private final EmployeeService employeeService;//
    private final GeneralService generalService;/* service */

    @Autowired
    PriceController(PriceService priceService,
                    BoxService boxService,
                    ClientService clientService,
                    EmployeeService employeeService,//
                    GeneralService generalService) {

        this.priceService = priceService;

        this.boxService = boxService;//
        this.clientService = clientService;//
        this.employeeService = employeeService;//
        this.generalService = generalService;

    }

    // Страница тарифы
    @GetMapping("/price")
    public String listPrices(Model model) {
        model.addAttribute("content", "price");
        model.addAttribute("prices", priceService.getAll());
        model.addAttribute("allboxes", boxService.getAll());
        model.addAttribute("object", new Price());
        model.addAttribute("saveUrl", "/price/save");
        return "index";
    }

    // форма изменения/добавления данных о боксе
    @GetMapping("/price/fragments/price_edit_modal")
    public String getPriceEditModal(Model model, @RequestParam(required = false) Long id) {
        model.addAttribute("object", priceService.getByIdOrNew(id));
        model.addAttribute("saveUrl", "/price/save");
        model.addAttribute("allboxes", boxService.getAll());
        //model.addAttribute("client", new Client());
        return "price_edit_modal :: content_modal_form";
    }

    // сохранить тариф
    @PostMapping("/price/save")
    public String savePrice(@Valid @ModelAttribute Price object,
                            BindingResult result,
                            Model model) {
        priceService.save(object);

        return "redirect:/price";
    }

    // Удалить тариф
    @DeleteMapping("/price/delete")
    public String deletePrice(@RequestParam Long id) {
        priceService.delete(id);
        return "redirect:/price";
    }
}
