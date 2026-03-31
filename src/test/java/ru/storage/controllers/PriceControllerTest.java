package ru.storage.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.storage.model.Price;
import ru.storage.services.GeneralService;
import ru.storage.services.PriceService;
import ru.storage.utilsForTest.TestObjects;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PriceControllerTest {
    TestObjects objects; // Объекты из класса с тестовыми данными, лучше подготовить их заранее и использовать во всех тестах

    @MockitoBean
    private PriceService priceService;
    @MockitoBean
    private GeneralService generalService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objects = new TestObjects();
    }

    @Test
    void listPrices_ReturnsIsOk() throws Exception {
        // Given
        when(priceService.getAll()).thenReturn(objects.getListPrice());

        // When & Then
        mockMvc.perform(get("/price"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("content", "prices", "allboxes", "object", "saveUrl"))
                .andExpect(model().attribute("content", "price"))
                .andExpect(model().attribute("prices", objects.getListPrice()))
                .andExpect(model().attribute("saveUrl", "/price/save"));

        verify(priceService, times(1)).getAll();
    }

    @Test
    void getPriceEditModal_ReturnsIsOk() throws Exception {
        // Given
        when(priceService.getByIdOrNew(1L)).thenReturn(objects.getPrice1());

        // When & Then
        mockMvc.perform(get("/price/fragments/price_edit_modal")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("price_edit_modal :: content_modal_form"))
                .andExpect(model().attributeExists("object", "saveUrl", "allboxes"))
                .andExpect(model().attribute("object", objects.getPrice1()))
                .andExpect(model().attribute("saveUrl", "/price/save"));

        verify(priceService, times(1)).getByIdOrNew(1L);
    }

    @Test
    void getPriceEditModal_NewPrice_ReturnsIsOk() throws Exception {
        // Given
        Price newPrice = objects.getPriceNew();
        when(priceService.getByIdOrNew(null))
                .thenReturn(newPrice);

        // When & Then
        mockMvc.perform(get("/price/fragments/price_edit_modal"))
                .andExpect(status().isOk())
                .andExpect(view().name("price_edit_modal :: content_modal_form"))
                .andExpect(model().attributeExists("object"))
                .andExpect(model().attribute("object", newPrice));

        verify(priceService, times(1)).getByIdOrNew(null);
    }

    @Test
    void savePrice_ReturnsIsFound() throws Exception {
        // Given
        when(priceService.save(any(Price.class)))
                .thenReturn(objects.getPrice1());

        // When & Then
        mockMvc.perform(post("/price/save")
                        .flashAttr("object", objects.getPrice1()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/price"));

        verify(priceService, times(1)).save(any(Price.class));
    }

    @Test
    void deletePrice_ReturnsIsFound() throws Exception {
        // Given
        doNothing().when(priceService).delete(1L);

        // When & Then
        mockMvc.perform(get("/price/delete")
                        .param("id", "1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/price"));

        verify(priceService, times(1)).delete(1L);
    }
}
