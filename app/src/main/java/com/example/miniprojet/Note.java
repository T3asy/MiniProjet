package com.example.miniprojet;

public class Note {
    private long id; // Assurez-vous que cela correspond au schéma de votre base de données
    private String title;
    private String description;

    // Constructeur avec ID
    public Note(long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    // Getters et Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}



