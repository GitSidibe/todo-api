package de.taschenrechner.todoapi.dto;

public record AdminTodoDTO(Long id, String titel, boolean erledigt, String interneNotiz) {}
