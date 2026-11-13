package de.taschenrechner.todoapi.controller.admin;

import de.taschenrechner.todoapi.TodoRepository;
import de.taschenrechner.todoapi.dto.AdminTodoDTO;
import de.taschenrechner.todoapi.exception.TodoNichtGefundenException;
import de.taschenrechner.todoapi.mapper.TodoMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/todos")
public class AdminTodoController {
	
	private final TodoRepository repository;
	
	public AdminTodoController(TodoRepository repository) {
		this.repository = repository;
	}
	
	@GetMapping
	public List<AdminTodoDTO>alleTodosMitNotitzen(){
		return repository.findAll().stream()
				.map(TodoMapper::zuAdminDTO).toList();
	}
	
	@GetMapping("/{id}")
	public AdminTodoDTO einTodo(@PathVariable Long id) {
		return repository.findById(id)
				.map(TodoMapper::zuAdminDTO).orElseThrow(() -> new TodoNichtGefundenException(id));
	}
}
