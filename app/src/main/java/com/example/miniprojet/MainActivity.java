package com.example.miniprojet;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NoteAdapter.NoteActionListener {
    private NoteAdapter noteAdapter;
    private ArrayList<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView notesListView = findViewById(R.id.listView);
        Button addNoteButton = findViewById(R.id.addNoteButton);

        notes = new ArrayList<>();
        noteAdapter = new NoteAdapter(this, notes, this);
        notesListView.setAdapter(noteAdapter);

        addNoteButton.setOnClickListener(v -> showAddNoteDialog());
    }

    private void showAddNoteDialog() {
        final EditText titleInput = new EditText(this);
        titleInput.setHint("Titre");

        final EditText descriptionInput = new EditText(this);
        descriptionInput.setHint("Description");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(titleInput);
        layout.addView(descriptionInput);

        new AlertDialog.Builder(this)
                .setTitle("Ajouter une nouvelle note")
                .setView(layout)
                .setPositiveButton("Ajouter", (dialog, which) -> {
                    String title = titleInput.getText().toString();
                    String description = descriptionInput.getText().toString();
                    if (!title.isEmpty() && !description.isEmpty()) {
                        Note note = new Note(title, description);
                        notes.add(note);
                        noteAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Annuler", null)
                .create().show();
    }


    @Override
    public void onNoteDelete(int position) {
        notes.remove(position);
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void supAll(MenuItem item) {
        final ListView listView = findViewById(R.id.listView);
        NoteAdapter listNotes = (NoteAdapter) listView.getAdapter();
        listNotes.clear();
    }

    @Override
    public void onNoteEdit(int position) {
        final Note note = notes.get(position);

        // Créer un layout pour la boîte de dialogue
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Créer un champ de texte pour le titre
        final EditText titleInput = new EditText(this);
        titleInput.setInputType(InputType.TYPE_CLASS_TEXT);
        titleInput.setHint("Titre");
        titleInput.setText(note.getTitle()); // Pré-remplir avec le titre actuel
        layout.addView(titleInput);

        // Créer un champ de texte pour la description
        final EditText descriptionInput = new EditText(this);
        descriptionInput.setInputType(InputType.TYPE_CLASS_TEXT);
        descriptionInput.setHint("Description");
        descriptionInput.setText(note.getDescription()); // Pré-remplir avec la description actuelle
        layout.addView(descriptionInput);

        // Créer et afficher la boîte de dialogue
        new AlertDialog.Builder(this)
                .setTitle("Modifier la note")
                .setView(layout)
                .setPositiveButton("Sauvegarder", (dialog, which) -> {
                    // Récupérer et mettre à jour les valeurs
                    String newTitle = titleInput.getText().toString();
                    String newDescription = descriptionInput.getText().toString();
                    note.setTitle(newTitle);
                    note.setDescription(newDescription);
                    noteAdapter.notifyDataSetChanged(); // Notifier l'adaptateur du changement
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

}
