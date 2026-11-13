package de.taschenrechner.todoapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.taschenrechner.todoapi.Todo;
import de.taschenrechner.todoapi.TodoRepository;
import de.taschenrechner.todoapi.dto.TodoCreateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoRepository repository;

    // ObjectMapper direkt erstellen
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAll_gibtListeZurueck() throws Exception {
        when(repository.findAll()).thenReturn(List.of(
            new Todo("Test 1", false),
            new Todo("Test 2", true)
        ));

        mockMvc.perform(get("/todos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getById_mitUnbekannterId_gibt404() throws Exception {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/todos/999"))
            .andExpect(status().isNotFound());
    }

    @Test
    void create_mitLeeremTitel_gibt400() throws Exception {
        TodoCreateDTO dto = new TodoCreateDTO("", false);

        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void create_mitGueltigenDaten_gibt201() throws Exception {
        TodoCreateDTO dto = new TodoCreateDTO("Test", false);
        Todo gespeichert = new Todo("Test", false);
        gespeichert.setId(1L);

        when(repository.save(any(Todo.class))).thenReturn(gespeichert);

        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.titel").value("Test"));
    }
}