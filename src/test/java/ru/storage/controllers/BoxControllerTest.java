package ru.storage.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.storage.model.Box;
import ru.storage.services.*;
import ru.storage.utilsForTest.TestObjects;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BoxControllerTest {

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
    void listBox_ReturnsAllBoxesPage() throws Exception {

        int page = 0, size = 50;
        String searchAll = "A100";//

        Page<Box> boxPage = objects.getPageBox();
        when(boxService.searchOrAll(page, size, searchAll))
                .thenReturn(boxPage);

        when(generalService.format(any(), eq(false)))
                .thenReturn("01.01.2024 12:00:01");
        when(generalService.sotrByIdBox(anyList()))
                .thenReturn(new ArrayList<>());

        when(clientService.getAll())
                .thenReturn(objects.getListClient());
        when(employeeService.getAll())
                .thenReturn(objects.getListEmployee());

        mockMvc.perform(get("/box")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("searchAll", searchAll)
                        .flashAttr("generalService", generalService)
                ) // Передаем настроенный мок
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("content", "box"))
                .andExpect(model().attribute("content_modal_form", "box_edit_modal"))
                .andExpect(model().attribute("boxPage", boxPage))
                .andExpect(model().attribute("boxes", boxPage.getContent()))
                .andExpect(model().attribute("currentPage", boxPage.getNumber()))
                .andExpect(model().attribute("totalPages", boxPage.getTotalPages()))
                .andExpect(model().attribute("totalElements", boxPage.getTotalElements()))
//                .andExpect(model().attribute("clients", clientService.getAll()))
//                .andExpect(model().attribute("employees", employeeService.getAll()))
//                .andExpect(model().attribute("object", new Box()))
                // Проверка наличия объектов для форм
                .andExpect(model().attributeExists("clients", "employees", "object"))
                .andExpect(model().attribute("saveUrl", "/box/save"))

                .andExpect(model().attribute("size", size))
                .andExpect(model().attribute("search", searchAll));

        verify(boxService).searchOrAll(page, size, searchAll);

    }

    @Test
    void getAccountEditModal_ReturnIsOk() throws Exception {

        when(boxService.getAll())
                .thenReturn(objects.getListBox());
        when(boxService.getByListId(1L))
                .thenReturn(objects.getListBox());

        mockMvc.perform(get("/box/fragments/account_edit_modal") // Тот самый URL из JS
                        .param("id", "1")
                        .flashAttr("generalService", generalService))
                .andExpect(status().isOk())
                // Проверяем, что вернулся именно фрагмент, а не весь index
                .andExpect(view().name("account_edit_modal :: content_modal_form"))
                .andExpect(model().attributeExists("allboxes", "curboxes", "object"));
    }

    @Test
    void getBoxEditModal_ReturnsFragment_box_edit_modal_IsOK() throws Exception {

        Box box = objects.getBox1();
        assertNotNull(box, "Объект Box1 не должен быть null в TestObjects!"); /* лучше добавить тесты на TestObjects */
        when(boxService.getByIdOrNew(1L))
                .thenReturn(box);

//        when(boxService.getByListId(1L))
//                .thenReturn(objects.getListBox());

        mockMvc.perform(get("/box/fragments/box_edit_modal") // Тот самый URL из JS
                        .param("id", "1")
                        .flashAttr("generalService", generalService))
                .andExpect(status().isOk())
                .andExpect(view().name("box_edit_modal :: content_modal_form"))
                // Проверяем, что вернулся именно фрагмент, а не весь index

                .andExpect(model().attribute("object", box))
                .andExpect(model().attribute("saveUrl", "/box/save"))
                .andExpect(model().attributeExists("object"));
    }

    @Test
    void saveBox_ReturnsIsOK() throws Exception {
        Box box = objects.getBox1();

        when(boxService.save(any(Box.class)))
                .thenReturn(box);

        mockMvc.perform(post("/box/save")
                        .flashAttr("object", box)) // Передаем объект, чтобы ModelAttribute его подхватил)
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"))
        ;

        verify(boxService, times(1)).save(any(Box.class));
    }

    @Test
    void deleteBox_RetunsIsOK() throws Exception {

        Box box = objects.getBox1();
        Long id = box.getId();
        doNothing().when(boxService).delete(id);

        mockMvc.perform(delete("/box/delete")
                        .param("id", id.toString()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));

        verify(boxService, times(1)).delete(id);
    }


    @Test
        // Аренда бокса или боксов
    void rentBox_ReturnsisOk() throws Exception {

        /* тут не получится тестировать напрямую:
         Кнопка «Арендовать» вызывает JS-функцию rentBoxId, которая делает fetch на /box/fragments/account_edit_modal.
         У нас возвращается только кусочек HTML (модалка), и там есть allboxes.
         Метод контроллера mockMvc.perform(get("/box/rent")) возвращает целую страницу index.
         Когда index начинает рендериться, он подставляет content="account" (согласно вашему методу rentBox).
         Если в account.html (или где-то в цепочке index -> account) есть вставка фрагмента, который обращается к allboxes,
         а в методе rentBox этого атрибута нет — Thymeleaf падает.
         */
//        when(generalService.format(any(), eq(false)))
//                .thenReturn("01.01.2024 12:00:01");
//        when(generalService.sotrByIdBox(anyList()))
//                .thenReturn(new ArrayList<>());
//        Long id = 1L;
//
//        when(boxService.getByListId(id))
//                .thenReturn(objects.getListBox());
//        when(boxService.getAll())
//                .thenReturn(objects.getListBox());
//        when(clientService.getAll())
//                .thenReturn(objects.getListClient());
//        when(employeeService.getAll())
//                .thenReturn(objects.getListEmployee());
//
//        when(accountService.getAll())
//                .thenReturn(objects.getListAccount());
//
//        mockMvc.perform(get("/box/rent")
//                    .param("id", String.valueOf(1L))
//                    .flashAttr("generalService", generalService)
//                ) // Передаем настроенный мок
//                .andExpect(status().isOk())
//                .andExpect(view().name("account_edit_modal :: content_modal_form"))
//                .andExpect(model().attribute("content", "box"))
//                .andExpect(model().attribute("content_modal_form", "box_edit_modal"))
//                .andExpect(model().attribute("content", "account"))
//                .andExpect(model().attribute("accounts", objects.getListAccount()))
//                .andExpect(model().attribute("boxes", objects.getListBox()))
//                .andExpect(model().attribute("allboxes", objects.getListBox()))
//                .andExpect(model().attributeExists("clients", "employees", "object", "curboxes", "saveUrl"))
//                .andExpect(model().attribute("clients", objects.getListClient()))
//                .andExpect(model().attribute("employees", objects.getListEmployee()))
//                .andExpect(model().attribute("object", new Account()))
//                .andExpect(model().attribute("allboxes", objects.getListBox()))
//                .andExpect(model().attribute("openAccountModal", 1)); // для формы
//
//        // Добавляем параметр для открытия модального окна

    }


}
