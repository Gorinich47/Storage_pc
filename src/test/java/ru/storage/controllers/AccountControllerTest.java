package ru.storage.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.storage.model.Account;
import ru.storage.services.*;
import ru.storage.utilsForTest.TestObjects;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(ru.storage.controllers.AccountController.class)
//@Import(SecurityConfig.class)
//@Import(GeneralService.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    TestObjects objects; // Объекты из класса с тестовыми данными, лучше подготовить их заранее и использовать во всех тестах
    @MockitoBean
    private AccountService accountService;
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
    void listAccounts_shouldReturnAccountsPageWithAllData() throws Exception {
        // Given
        when(generalService.format(any(), eq(false))).thenReturn("01.01.2024 12:00:01");
        when(generalService.sotrByIdBox(anyList())).thenReturn(new ArrayList<>());
        when(accountService.getAll()).thenReturn(List.of(objects.getAccount()));

        when(boxService.getAll()).thenReturn(objects.getListBox());
        when(clientService.getAll()).thenReturn(objects.getListClient());
        when(employeeService.getAll()).thenReturn(objects.getListEmployee());

        mockMvc.perform(get("/account")
                        .flashAttr("generalService", generalService)
                ) // Передаем настроенный мок
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("content", "account"))
                .andExpect(model().attributeExists("accounts", "allboxes"));

        //verify(accountService).getAll();
    }

    @Test
    void getAccountEditModal_shouldReturnEditFormWithAccountData() throws Exception {
        // Given
        when(generalService.format(any(), eq(false))).thenReturn("01.01.2024 12:00:01");
        when(accountService.getByIdOrNew(1L)).thenReturn(objects.getAccount());
        when(boxService.getAll()).thenReturn(objects.getListBox());
        when(clientService.getAll()).thenReturn(objects.getListClient());
        when(employeeService.getAll()).thenReturn(objects.getListEmployee());

        // When & Then
        mockMvc.perform(get("/account/fragments/account_edit_modal")
                        .param("id", "1")
                        .flashAttr("generalService", generalService))
                .andExpect(status().isOk())
                .andExpect(view().name("account_edit_modal :: content_modal_form"))
                .andExpect(model().attributeExists("clients", "employees", "object", "curboxes", "allboxes", "saveUrl"))
                .andExpect(model().attribute("object", objects.getAccount()))
                .andExpect(model().attribute("curboxes", objects.getAccount().getBox()))
                .andExpect(model().attribute("allboxes", objects.getListBox()));

        verify(accountService, times(1)).getByIdOrNew(1L);
        verify(boxService, times(1)).getAll();
        verify(clientService, times(1)).getAll();
        verify(employeeService, times(1)).getAll();
    }

    @Test
    void getAccountEditModal_withNullId_shouldReturnNewAccount() throws Exception {
        // Given
        when(generalService.format(any(), eq(false))).thenReturn("01.01.2024 12:00:01");
        when(accountService.getByIdOrNew(null)).thenReturn(objects.getAccountNew());
        when(boxService.getAll()).thenReturn(objects.getListBox());
        when(clientService.getAll()).thenReturn(objects.getListClient());
        when(employeeService.getAll()).thenReturn(objects.getListEmployee());

        // When & Then
        mockMvc.perform(get("/account/fragments/account_edit_modal")
                        //.param("id", "") // Передаем пустой параметр, который Spring приведете к null для Long
                        //.flashAttr("generalService", generalService)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("account_edit_modal :: content_modal_form"))
                .andExpect(model().attributeExists("clients", "employees", "object", "curboxes", "allboxes", "saveUrl"))
                .andExpect(model().attribute("object", objects.getAccountNew()))
        ;

        verify(accountService, times(1)).getByIdOrNew(null);
    }

    @Test
    void saveAccount_shouldSaveAccountWithBoxes() throws Exception {
        // Given
        doNothing().when(accountService).save(any(Account.class), any());

        // When & Then
        mockMvc.perform(post("/account/save")
                        .param("id", "1")
                        .param("boxIds", "1", "2"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/account"));

        verify(accountService, times(1)).save(any(Account.class), any());
    }

    @Test
    void saveAccount_withoutBoxIds_shouldSaveAccountWithoutBoxes() throws Exception {
        // Given
        doNothing().when(accountService).save(any(Account.class), any());

        // When & Then
        mockMvc.perform(post("/account/save")
                        .param("id", "1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/account"));

        verify(accountService, times(1)).save(any(Account.class), any());
    }

    @Test
    void deleteAccount_shouldDeleteAccountById() throws Exception {
        // Given
        doNothing().when(accountService).delete(1L);

        // When & Then
        mockMvc.perform(get("/account/delete")
                        .param("id", "1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/account"));

        verify(accountService, times(1)).delete(1L);
    }

    @Test
    void deleteAccount_withoutId_shouldNotCallDelete() throws Exception {

        mockMvc.perform(get("/account/delete"))
                .andExpect(status().isBadRequest());

        verify(accountService, never()).delete(anyLong());
    }

}

