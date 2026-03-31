package ru.storage.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.storage.model.Employee;
import ru.storage.services.EmployeeService;
import ru.storage.services.GeneralService;
import ru.storage.utilsForTest.TestObjects;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    TestObjects objects; // Объекты из класса с тестовыми данными, лучше подготовить их заранее и использовать во всех тестах

    @MockitoBean
    private EmployeeService employeeService;
    @MockitoBean
    private GeneralService generalService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objects = new TestObjects();
    }

    @Test
    void listEmployees_ReturnsIsOk() throws Exception {
        // Given
        when(employeeService.getAll()).thenReturn(objects.getListEmployee());

        // When & Then
        mockMvc.perform(get("/employee"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("content", "employees", "object", "saveUrl"))
                .andExpect(model().attribute("content", "employee"))
                .andExpect(model().attribute("employees", objects.getListEmployee()))
                .andExpect(model().attributeExists("object"))
                .andExpect(model().attribute("saveUrl", "/employee/save"));

        verify(employeeService, times(1)).getAll();
    }

    @Test
    void getEmployeetEditModal_ReturnsIsOk() throws Exception {
        // Given
        when(employeeService.getByIdOrNew(1L)).thenReturn(objects.getEmployee1());

        // When & Then
        mockMvc.perform(get("/employee/fragments/client_edit_modal")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("client_edit_modal :: content_modal_form"))
                .andExpect(model().attributeExists("object", "saveUrl"))
                .andExpect(model().attribute("object", objects.getEmployee1()))
                .andExpect(model().attribute("saveUrl", "/employee/save"));

        verify(employeeService, times(1)).getByIdOrNew(1L);
    }

    @Test
    void getEmployeetEditModal_NewEmployee_ReturnsIsOk() throws Exception {
        // Given
        Employee newEmployee = objects.getEmployeeNew();
        when(employeeService.getByIdOrNew(null)).thenReturn(newEmployee);

        // When & Then
        mockMvc.perform(get("/employee/fragments/client_edit_modal"))
                .andExpect(status().isOk())
                .andExpect(view().name("client_edit_modal :: content_modal_form"))
                .andExpect(model().attributeExists("object"))
                .andExpect(model().attribute("object", newEmployee));

        verify(employeeService, times(1)).getByIdOrNew(null);
    }

    @Test
    void saveEmployee_ReturnsIsFound() throws Exception {

        Employee newEmployee = objects.getEmployeeNew();
        // Given
        when(employeeService.save(any(Employee.class)))
                .thenReturn(newEmployee);

        // When & Then
        mockMvc.perform(post("/employee/save")
                        .flashAttr("object", newEmployee))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/employee"));

        verify(employeeService, times(1)).save(any(Employee.class));
    }

    @Test
    void deleteEmployee_ReturnsIsFound() throws Exception {
        // Given
        doNothing().when(employeeService).delete(1L);

        // When & Then
        mockMvc.perform(get("/employee/delete")
                        .param("id", "1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/employee"));

        verify(employeeService, times(1)).delete(1L);
    }
}
