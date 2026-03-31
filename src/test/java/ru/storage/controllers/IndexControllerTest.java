package ru.storage.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.storage.model.Box;
import ru.storage.services.BoxService;
import ru.storage.services.ClientService;
import ru.storage.services.EmployeeService;
import ru.storage.services.GeneralService;
import ru.storage.utilsForTest.TestObjects;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

public class IndexControllerTest {

    TestObjects objects; // Объекты из класса с тестовыми данными, лучше подготовить их заранее и использовать во всех тестах

    @MockitoBean
    private BoxService boxService;
    @MockitoBean
    private ClientService clientService;
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
    void index_ReturnsIsOk() throws Exception {


        // Given
        List<Box> boxList = objects.getListBox();
        int page = 0, size = 6;
        String searchAll = null;//
        Page<Box> boxPage = objects.getPageBox();
        when(boxService.searchOrAll(page, size, searchAll))
                .thenReturn(boxPage);
        when(clientService.getAll()).thenReturn(List.of());
        when(employeeService.getAll()).thenReturn(List.of());
        when(clientService.getAll())
                .thenReturn(objects.getListClient());
        when(employeeService.getAll())
                .thenReturn(objects.getListEmployee());

        // When & Then
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))

                .andExpect(model().attribute("content", "box_front"))
                .andExpect(model().attribute("content_modal_form", "box_edit_modal"))
                .andExpect(model().attribute("boxPage", boxPage))
                .andExpect(model().attribute("boxes", boxPage.getContent()))
                .andExpect(model().attribute("currentPage", boxPage.getNumber()))
                .andExpect(model().attribute("totalPages", boxPage.getTotalPages()))
                .andExpect(model().attribute("totalElements", boxPage.getTotalElements()))

                .andExpect(model().attribute("size", size))
                .andExpect(model().attribute("search", (String) null))
                .andExpect(model().attributeExists("object"))
                .andExpect(model().attribute("saveUrl", "/box/save"))

                .andExpect(model().attributeExists("clients", "employees", "object"))
//                .andExpect(model().attributeExists("content", "content_modal_form", "boxPage", "boxes",
//                        "currentPage", "totalPages", "totalElements", "size", "search",
//                        "clients", "employees", "object", "saveUrl"))
        ;

        verify(boxService, times(1)).searchOrAll(page, size, searchAll);
        verify(clientService, times(1)).getAll();
        verify(employeeService, times(1)).getAll();
    }

    @Test
    void index_withSearchParameter_ReturnsIsOk() throws Exception {

        // Given
        Page<Box> boxPage = objects.getPageBox();
        String searchQuery = "B001";
        when(boxService.searchOrAll(0, 6, searchQuery)).thenReturn(boxPage);
        when(clientService.getAll()).thenReturn(List.of());
        when(employeeService.getAll()).thenReturn(List.of());

        // When & Then
        mockMvc.perform(get("/")
                        .param("searchAll", searchQuery))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("search", searchQuery))
                .andExpect(model().attribute("boxPage", boxPage));

        verify(boxService, times(1)).searchOrAll(0, 6, searchQuery);
    }

    @Test
    void index_withCustomPageSize_ReturnsIsOk() throws Exception {
        // Given
        List<Box> boxList = objects.getListBox();
        int customSize = 12;
        Page<Box> customPage = new PageImpl<>(boxList, PageRequest.of(0, customSize), boxList.size());

        when(boxService.searchOrAll(0, customSize, null)).thenReturn(customPage);
        when(clientService.getAll()).thenReturn(List.of());
        when(employeeService.getAll()).thenReturn(List.of());

        // When & Then
        mockMvc.perform(get("/")
                        .param("size", String.valueOf(customSize)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("size", customSize))
                .andExpect(model().attribute("boxPage", customPage));

        verify(boxService, times(1)).searchOrAll(0, customSize, null);
    }

    @Test
    void index_withPagination_ReturnsIsOk() throws Exception {
        // Given
        int page = 1;
        int size = 6;
        Page<Box> nextPage = new PageImpl<>(List.of(), PageRequest.of(page, size), 0);

        when(boxService.searchOrAll(page, size, null)).thenReturn(nextPage);
        when(clientService.getAll()).thenReturn(List.of());
        when(employeeService.getAll()).thenReturn(List.of());

        // When & Then
        mockMvc.perform(get("/")
                        .param("page", String.valueOf(page)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("currentPage", page))
                .andExpect(model().attribute("boxPage", nextPage));

        verify(boxService, times(1)).searchOrAll(page, size, null);
    }
}
