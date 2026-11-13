package de.taschenrechner.todoapi.exception;

public class TodoNichtGefundenException extends RuntimeException  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TodoNichtGefundenException(Long id) {
		super("Todo nicht gefunden: " + id);
	}
}
