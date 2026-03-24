package ru.storage.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.storage.services.*;

@Controller
//@RequestMapping("")
public class MovementController {

    private final BoxService boxService; /* service */
    private final ClientService clientService;//
    private final EmployeeService employeeService;//
    private final GeneralService generalService;/* service */
    private final MovementService movementService;

    @Autowired
    MovementController(
            BoxService boxService,
            ClientService clientService,
            EmployeeService employeeService,//
            GeneralService generalService,
            MovementService movementService) {

        this.boxService = boxService;//
        this.clientService = clientService;//
        this.employeeService = employeeService;//
        this.generalService = generalService;
        this.movementService = movementService;

    }
}
