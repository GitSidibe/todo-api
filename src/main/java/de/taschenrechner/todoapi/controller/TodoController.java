package de.taschenrechner.todoapi.controller;

import de.taschenrechner.todoapi.dto.TodoDTO;
import de.taschenrechner.todoapi.Todo;
import de.taschenrechner.todoapi.TodoRepository;
import de.taschenrechner.todoapi.dto.TodoCreateDTO;
import de.taschenrechner.todoapi.dto.TodoUpdateDTO;
import de.taschenrechner.todoapi.exception.TodoNichtGefundenException;
import de.taschenrechner.todoapi.mapper.TodoMapper;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todos")
public class TodoController {

	private final TodoRepository repository;

	public TodoController(TodoRepository repository) {
		this.repository = repository;
	}

	 // GET /todos — alle Todos
	@GetMapping
	public List<TodoDTO> alleTodos() {
		return repository.findAll().stream().map(TodoMapper::zuDTO).toList();
	}

	// GET /todos/{id} — ein Todo
	@GetMapping("/{id}")
	public TodoDTO einTodo(@PathVariable Long id) {
		return repository.findById(id)
				.map(TodoMapper::zuDTO)
				.orElseThrow(() -> new TodoNichtGefundenException(id));
	}

	// POST /todos — neues Todo
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TodoDTO neuesTodo(@Valid @RequestBody TodoCreateDTO dto) {
		Todo todo = TodoMapper.zuEntity(dto);
		Todo gespeichert = repository.save(todo);
		
		return TodoMapper.zuDTO(gespeichert);
	}

	
	// PUT /todos/{id} — Todo aktualisieren
	@PutMapping("/{id}")
	public TodoDTO updateTodo(@PathVariable Long id, @Valid @RequestBody TodoUpdateDTO dto) {
        Todo todo = repository.findById(id)
                .orElseThrow(() -> new TodoNichtGefundenException(id));
        
        todo.setTitel(dto.titel());
        todo.setErledigt(dto.erledigt());
        
        return TodoMapper.zuDTO(repository.save(todo)); 
	}

	// DELETE /todos/{id} — Todo löschen
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void loescheTodo(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new TodoNichtGefundenException(id);
        }
        repository.deleteById(id);
    }
}
