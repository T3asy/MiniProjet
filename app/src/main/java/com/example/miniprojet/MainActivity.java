package com.example.miniprojet;





import static android.app.DownloadManager.COLUMN_DESCRIPTION;
import static android.app.DownloadManager.COLUMN_ID;
import static android.app.DownloadManager.COLUMN_TITLE;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.NoteActionListener {
    private NoteAdapter noteAdapter;
    NotesDbHelper dbHelper = new NotesDbHelper(this);

    private ArrayList<Note> notes;
    private final String FILENAME="testfile_note.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView notesListView = findViewById(R.id.listView);
        Button addNoteButton = findViewById(R.id.addNoteButton);

        notes = new ArrayList<>();
        noteAdapter = new NoteAdapter(this, notes, this);
        notesListView.setAdapter(noteAdapter);

        List<Note> list = getAllNotes();
        notes.addAll(list);
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
                        long noteId = addNoteBDD(title,description);
                        Note note = new Note(noteId,title, description);
                        notes.add(note);
                        noteAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Annuler", null)
                .create().show();
    }


    @Override
    public void onNoteDelete(int position) {
        final Note note = notes.get(position);
        long noteId = note.getId();
        notes.remove(position);
        noteAdapter.notifyDataSetChanged();
        deleteNote(noteId);
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
        List<Note> list = getAllNotes();
        for (int i=0;i<list.size();i++){
            deleteNote(list.get(i).getId());
        }
    }

    @Override
    public void onNoteEdit(int position) {
        final Note note = notes.get(position);
        long noteId = note.getId();

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
                    String newTitle = titleInput.getText().toString();
                    String newDescription = descriptionInput.getText().toString();
                    note.setTitle(newTitle);
                    note.setDescription(newDescription);
                    updateNote(noteId,newTitle,newDescription);
                    noteAdapter.notifyDataSetChanged();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    public long addNoteBDD(String title, String description) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);

        long newRowId = db.insert(dbHelper.getTableName(), null, values);

        return newRowId;
    }


    public void updateNote(long noteId, String newTitle, String newDescription) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, newTitle);
        values.put(COLUMN_DESCRIPTION, newDescription);

        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(noteId) };

        int count = db.update(
                dbHelper.getTableName(),
                values,
                selection,
                selectionArgs);
    }

    public void deleteNote(long noteId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Définir la clause "where" pour spécifier quelle note supprimer.
        String selection = COLUMN_ID + " = ?";
        // Spécifier les arguments dans un tableau pour la clause "where".
        String[] selectionArgs = { String.valueOf(noteId) };
        // Exécuter la commande SQL de suppression.
        db.delete( dbHelper.getTableName(), selection, selectionArgs);
    }



    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_TITLE,
                COLUMN_DESCRIPTION
        };

        Cursor cursor = db.query(
                dbHelper.getTableName(),   // La table à interroger
                projection,            // Les colonnes à retourner
                null,             // Les colonnes pour la clause WHERE
                null,          // Les valeurs pour la clause WHERE
                null,          // Ne pas grouper les lignes
                null,           // Ne pas filtrer par groupe de lignes
                null);          // L'ordre de tri

        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String itemTitle = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
            String itemDescription = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
            notes.add(new Note(itemId,itemTitle, itemDescription));
        }
        cursor.close();

        return notes;
    }


}
