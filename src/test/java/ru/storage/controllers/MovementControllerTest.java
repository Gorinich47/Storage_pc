package ru.storage.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.storage.model.Movement;
import ru.storage.services.GeneralService;
import ru.storage.services.MovementService;
import ru.storage.utilsForTest.TestObjects;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MovementControllerTest {

    TestObjects objects; // Объекты из класса с тестовыми данными, лучше подготовить их заранее и использовать во всех тестах

    @MockitoBean
    private MovementService movementService;
    @MockitoBean
    private GeneralService generalService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objects = new TestObjects();
    }

    @Test
    void listMovements_ReturnIsOk() throws Exception {
        // Given
        when(movementService.getAll()).thenReturn(objects.getListMovement());

        // When & Then
        mockMvc.perform(get("/movement"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("content", "movements", "object", "saveUrl"))
                .andExpect(model().attribute("content", "movement"))
                .andExpect(model().attribute("movements", objects.getListMovement()))
                .andExpect(model().attributeExists("object"))
                .andExpect(model().attribute("saveUrl", "/movement/save"));

        verify(movementService, times(1)).getAll();
    }

    @Test
    void getMovementEditModal_ReturnIsOk() throws Exception {
        // Given
        when(movementService.getByIdOrNew(1L)).thenReturn(objects.getMovement1());

        // When & Then
        mockMvc.perform(get("/movement/fragments/movement_edit_modal")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("movement_edit_modal :: content_modal_form"))
                .andExpect(model().attributeExists("object", "saveUrl"))
                .andExpect(model().attribute("object", objects.getMovement1()))
                .andExpect(model().attribute("saveUrl", "/movement/save"));

        verify(movementService, times(1)).getByIdOrNew(1L);
    }

    @Test
    void getMovementEditModal_NewMovement_ReturnIsOk() throws Exception {
        // Given
        when(movementService.getByIdOrNew(null)).thenReturn(objects.getMovementNew());

        // When & Then
        mockMvc.perform(get("/movement/fragments/movement_edit_modal"))
                .andExpect(status().isOk())
                .andExpect(view().name("movement_edit_modal :: content_modal_form"))
                .andExpect(model().attributeExists("object"))
                .andExpect(model().attribute("object", objects.getMovementNew()));

        verify(movementService, times(1)).getByIdOrNew(null);
    }

    @Test
    void saveMovement_ReturnIsFound() throws Exception {
        // Given
        when(movementService.save(any(Movement.class)))
                .thenReturn(objects.getMovement2());

        // When & Then
        mockMvc.perform(post("/movement/save")
                        .flashAttr("object", objects.getMovement2()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/movement"));

        verify(movementService, times(1)).save(any(Movement.class));
    }

    @Test
    void deleteMovement_ReturnIsFound() throws Exception {
        // Given
        doNothing().when(movementService).delete(1L);

        // When & Then
        mockMvc.perform(get("/movement/delete")
                        .param("id", "1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/movement"));

        verify(movementService, times(1)).delete(1L);
    }
}
