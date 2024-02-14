package com.example.miniprojet;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
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
        final EditText noteInput = new EditText(this);
        noteInput.setInputType(InputType.TYPE_CLASS_TEXT);

        new AlertDialog.Builder(this)
                .setTitle("Nouvelle note")
                .setView(noteInput)
                .setPositiveButton("Ajouter", (dialog, which) -> {
                    String noteText = noteInput.getText().toString();
                    if (!noteText.isEmpty()) {
                        notes.add(new Note(noteText));
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
    public void onNoteEdit(int position) {
        Note note = notes.get(position);
        final EditText noteInput = new EditText(this);
        noteInput.setInputType(InputType.TYPE_CLASS_TEXT);
        noteInput.setText(note.getText());

        new AlertDialog.Builder(this)
                .setTitle("Modifier la note")
                .setView(noteInput)
                .setPositiveButton("Sauvegarder", (dialog, which) -> {
                    String noteText = noteInput.getText().toString();
                    if (!noteText.isEmpty()) {
                        note.setText(noteText);
                        noteAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Annuler", null)
                .create().show();
    }
}
