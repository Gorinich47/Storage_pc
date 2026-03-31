package ru.storage.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.storage.model.Client;
import ru.storage.services.ClientService;
import ru.storage.services.GeneralService;
import ru.storage.utilsForTest.TestObjects;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTest {

    TestObjects objects; // Объекты из класса с тестовыми данными, лучше подготовить их заранее и использовать во всех тестах

    @MockitoBean
    private ClientService clientService;
    @MockitoBean
    private GeneralService generalService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objects = new TestObjects();
    }

    @Test
    void getAllPersons_ReturnsIsOk() throws Exception {

        int page = 0, size = 5;// Тестовые данные для пагинации
        Page<Client> clientPage = objects.getPageClient();
        // Given
        when(generalService.format(any(), eq(false))).thenReturn("01.01.2024 12:00:01");
        when(generalService.sotrByIdBox(anyList())).thenReturn(new ArrayList<>());

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Order.asc("firstName"),
                        Sort.Order.asc("lastName")).ascending());

        when(clientService.getAll(pageable)).thenReturn(objects.getPageClient());

        mockMvc.perform(get("/client")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .flashAttr("generalService", generalService)
                ) // Передаем настроенный мок
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("content", "client"))
                .andExpect(model().attribute("object", new Client()))
                // Список с пагинацией и сортировкой
                .andExpect(model().attribute("clientPage", clientPage))
                .andExpect(model().attribute("clients", clientPage.getContent()))
                .andExpect(model().attribute("currentPage", clientPage.getNumber()))
                .andExpect(model().attribute("totalPages", clientPage.getTotalPages()))
                .andExpect(model().attribute("totalElements", clientPage.getTotalElements()))
                .andExpect(model().attribute("size", size));

        verify(clientService).getAll(pageable);
    }

    @Test
    void getClientEditModal_ReturnsIsOk() throws Exception {
        // Given
        Client testClient = objects.getClient1();
        when(clientService.getByIdOrNew(1L)).thenReturn(testClient);

        // When & Then
        mockMvc.perform(get("/client/fragments/client_edit_modal")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("client_edit_modal :: content_modal_form"))
                .andExpect(model().attributeExists("object", "saveUrl"))
                .andExpect(model().attribute("object", testClient))
                .andExpect(model().attribute("saveUrl", "/client/save"));

        verify(clientService, times(1)).getByIdOrNew(1L);
    }

    @Test
    void getClientEditModal_NewClient_ReturnsIsOk() throws Exception {
        // Given
        Client newClient = objects.getClient1();
        when(clientService.getByIdOrNew(null)).thenReturn(newClient);

        // When & Then
        mockMvc.perform(get("/client/fragments/client_edit_modal")
                        .param("id", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("client_edit_modal :: content_modal_form"))
                .andExpect(model().attributeExists("object"))
                .andExpect(model().attribute("object", newClient));

        verify(clientService, times(1)).getByIdOrNew(null);
    }

    @Test
    void saveClient_ReturnsIsFound() throws Exception {
        // Given
        Client client = objects.getClient1();
        when(clientService.save(any(Client.class)))
                .thenReturn(client);

        // When & Then
        mockMvc.perform(post("/client/save")
                        .flashAttr("object", client))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/client"));

        verify(clientService, times(1)).save(any(Client.class));
    }

    @Test
    void deleteClient_ReturnsIsFound() throws Exception {
        // Given
        doNothing().when(clientService).delete(1L);

        // When & Then
        mockMvc.perform(get("/client/delete")
                        .param("id", "1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/client"));

        verify(clientService, times(1)).delete(1L);
    }

    @Test
    void getRandomClient_ReturnsIsFound() throws Exception {
        // Given
        doNothing().when(clientService).generateClients();

        // When & Then
        mockMvc.perform(post("/client/random"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/client"));

        verify(clientService, times(1)).generateClients();
    }

//    @Test
//    void getAllPersons_WithSearch_ShouldReturnFilteredResults() throws Exception {
//        // Given
//        int page = 0, size = 5;
//        Page<Client> clientPage = objects.getPageClient();
//        String searchQuery = "test";
//
//        when(generalService.format(any(), eq(false))).thenReturn("01.01.2024 12:00:01");
//        when(generalService.sotrByIdBox(anyList())).thenReturn(new ArrayList<>());
//        when(clientService.search(searchQuery, PageRequest.of(page, size,
//                Sort.by(Sort.Order.asc("firstName"), Sort.Order.asc("lastName")).ascending())))
//                .thenReturn(clientPage);
//
//        // When & Then
//        mockMvc.perform(get("/client")
//                        .param("page", String.valueOf(page))
//                        .param("size", String.valueOf(size))
//                        .param("search", searchQuery))
//                .andExpect(status().isOk())
//                .andExpect(view().name("index"))
//                .andExpect(model().attribute("content", "client"))
//                .andExpect(model().attribute("clientPage", clientPage))
//                .andExpect(model().attribute("clients", clientPage.getContent()));
//
//        verify(clientService, times(1)).search(eq(searchQuery), any(Pageable.class));
//    }
}
