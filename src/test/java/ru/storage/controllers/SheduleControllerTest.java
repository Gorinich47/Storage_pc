package ru.storage.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.storage.model.Shedule;
import ru.storage.services.GeneralService;
import ru.storage.services.SheduleService;
import ru.storage.utilsForTest.TestObjects;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SheduleControllerTest {
    TestObjects objects; // Объекты из класса с тестовыми данными, лучше подготовить их заранее и использовать во всех тестах

    @MockitoBean
    private SheduleService sheduleService;
    @MockitoBean
    private GeneralService generalService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objects = new TestObjects();
    }

    @Test
    void listShedule_ReturnsIsOk() throws Exception {
        // Given
        when(sheduleService.getAll()).thenReturn(objects.getListShedule());

        // When & Then
        mockMvc.perform(get("/shedule"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("content", "shedules", "allEmployees", "object", "saveUrl"))
                .andExpect(model().attribute("content", "shedule"))
                .andExpect(model().attribute("shedules", objects.getListShedule()))
                .andExpect(model().attribute("saveUrl", "/shedule/save"));

        verify(sheduleService, times(1)).getAll();
    }

    @Test
    void getSheduleEditModal_ReturnsIsOk() throws Exception {
        // Given
        when(sheduleService.getByIdOrNew(1L)).thenReturn(objects.getShedule1());

        // When & Then
        mockMvc.perform(get("/shedule/fragments/shedule_edit_modal")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("shedule_edit_modal :: content_modal_form"))
                .andExpect(model().attributeExists("object", "saveUrl", "allEmployees"))
                .andExpect(model().attribute("object", objects.getShedule1()))
                .andExpect(model().attribute("saveUrl", "/shedule/save"));

        verify(sheduleService, times(1)).getByIdOrNew(1L);
    }

    @Test
    void getSheduleEditModal_NewPrice_ReturnsIsOk() throws Exception {
        // Given
        Shedule newShedule = objects.getSheduleNew();
        when(sheduleService.getByIdOrNew(null))
                .thenReturn(newShedule);

        // When & Then
        mockMvc.perform(get("/shedule/fragments/shedule_edit_modal"))
                .andExpect(status().isOk())
                .andExpect(view().name("shedule_edit_modal :: content_modal_form"))
                .andExpect(model().attributeExists("object"))
                .andExpect(model().attribute("object", newShedule));

        verify(sheduleService, times(1)).getByIdOrNew(null);
    }

    @Test
    void saveShedule_ReturnsIsFound() throws Exception {
        // Given
        when(sheduleService.save(any(Shedule.class)))
                .thenReturn(objects.getShedule2());

        // When & Then
        mockMvc.perform(post("/shedule/save")
                        .flashAttr("object", objects.getShedule2()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/shedule"));

        verify(sheduleService, times(1)).save(any(Shedule.class));
    }

    @Test
    void deleteShedule_ReturnsIsFound() throws Exception {
        // Given
        doNothing().when(sheduleService).delete(1L);

        // When & Then
        mockMvc.perform(get("/shedule/delete")
                        .param("id", "1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/shedule"));

        verify(sheduleService, times(1)).delete(1L);
    }
}
