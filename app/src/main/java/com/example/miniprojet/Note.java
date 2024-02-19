package com.example.miniprojet;

public class Note {
    private String title; // Titre de la note
    private String description; // Description de la note

    // Constructeur mis à jour
    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Getters et setters pour les nouveaux champs
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

    // Assurez-vous de mettre à jour toute autre méthode nécessaire...
}


