package de.taschenrechner.todoapi.mapper;

import de.taschenrechner.todoapi.Todo;
import de.taschenrechner.todoapi.dto.AdminTodoDTO;
import de.taschenrechner.todoapi.dto.TodoCreateDTO;
import de.taschenrechner.todoapi.dto.TodoDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TodoMapperTest {

    @Test
    void zuDTO_mapptAlleFelder() {
        Todo todo = new Todo("Test", false);
        todo.setId(1L);

        TodoDTO dto = TodoMapper.zuDTO(todo);

        assertEquals(1L, dto.id());
        assertEquals("Test", dto.titel());
        assertFalse(dto.erledigt());
    }

    @Test
    void zuAdminDTO_mapptInterneNotiz() {
        Todo todo = new Todo("Test", true);
        todo.setId(1L);
        todo.setInterneNotiz("Admin-Notiz");

        AdminTodoDTO dto = TodoMapper.zuAdminDTO(todo);

        assertEquals(1L, dto.id());
        assertEquals("Test", dto.titel());
        assertTrue(dto.erledigt());
        assertEquals("Admin-Notiz", dto.interneNotiz());
    }

    @Test
    void zuEntity_mapptCreateDTO() {
        TodoCreateDTO dto = new TodoCreateDTO("Neues Todo", true);

        Todo todo = TodoMapper.zuEntity(dto);

        assertEquals("Neues Todo", todo.getTitel());
        assertTrue(todo.isErledigt());
        assertNull(todo.getId());
    }
}