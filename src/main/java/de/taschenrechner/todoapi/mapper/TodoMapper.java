package de.taschenrechner.todoapi.mapper;

import de.taschenrechner.todoapi.Todo;
import de.taschenrechner.todoapi.dto.AdminTodoDTO;
import de.taschenrechner.todoapi.dto.TodoCreateDTO;
import de.taschenrechner.todoapi.dto.TodoDTO;

public class TodoMapper {
	// Entity → DTO
	public static TodoDTO zuDTO(Todo todo) {
		return new TodoDTO(todo.getId(), todo.getTitel(), todo.isErledigt());
	}
	
	
	// Entity → AdminDTO (für Admins)
    public static AdminTodoDTO zuAdminDTO(Todo todo) {
        return new AdminTodoDTO(
            todo.getId(), 
            todo.getTitel(), 
            todo.isErledigt(),
            todo.getInterneNotiz()
        );
    }
	
	
	// Entity → DTO (für normale Benutzer)
	public static Todo zuEntity(TodoCreateDTO dto) {
		Todo todo = new Todo();
		todo.setTitel(dto.titel());
		todo.setErledigt(dto.erledigt());
		return todo;
	}
	
}
