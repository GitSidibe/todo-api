package de.taschenrechner.todoapi;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titel;
    private boolean erledigt;

   // NEU: Interne Notiz — nur für Admins sichtbar
    private String interneNotiz;
    
    public Todo() {
    }

    public Todo(String titel, boolean erledigt) {
        this.titel = titel;
        this.erledigt = erledigt;
    }

    public Long getId() { return id; }
    
    public void setId(Long id) { this.id = id; }
    
    public String getTitel() { return titel; }
    
    public void setTitel(String titel) { this.titel = titel; }
    
    public boolean isErledigt() { return erledigt; }
    
    public void setErledigt(boolean erledigt) { this.erledigt = erledigt; }
    
    public String getInterneNotiz() { return interneNotiz; }
    public void setInterneNotiz(String interneNotiz) { this.interneNotiz = interneNotiz; }
}