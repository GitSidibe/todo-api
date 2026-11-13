package de.taschenrechner.todoapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class TodoRepositoryTest {

    @Autowired
    private TodoRepository repository;

    @Test
    void save_erstelltTodoMitId() {
        Todo todo = new Todo("Test", false);

        Todo gespeichert = repository.save(todo);

        assertNotNull(gespeichert.getId());
        assertEquals("Test", gespeichert.getTitel());
    }

    @Test
    void findById_findetTodo() {
        Todo gespeichert = repository.save(new Todo("Test", false));

        Optional<Todo> gefunden = repository.findById(gespeichert.getId());

        assertTrue(gefunden.isPresent());
        assertEquals("Test", gefunden.get().getTitel());
    }

    @Test
    void findAll_gibtAlleTodosZurueck() {
        repository.save(new Todo("Test 1", false));
        repository.save(new Todo("Test 2", true));

        List<Todo> todos = repository.findAll();

        assertEquals(2, todos.size());
    }

    @Test
    void deleteById_loeschtTodo() {
        Todo gespeichert = repository.save(new Todo("Test", false));

        repository.deleteById(gespeichert.getId());

        assertFalse(repository.existsById(gespeichert.getId()));
    }
}