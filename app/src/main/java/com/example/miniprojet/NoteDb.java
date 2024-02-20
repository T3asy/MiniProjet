package com.example.miniprojet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NoteDb extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NOTES = "notes";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TITLE + " TEXT NOT NULL," +
                    COLUMN_DESCRIPTION + " TEXT NOT NULL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NOTES;

    public NoteDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public NoteDb(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Cette base de données est seulement un cache pour les données en ligne, donc sa politique de mise à jour
        // est simplement de supprimer les données et de recommencer
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void addNote(String title, String description) {
        // Obtenir la base de données en écriture
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);

        long newRowId = db.insert(TABLE_NOTES, null, values);
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_TITLE,
                COLUMN_DESCRIPTION
        };

        Cursor cursor = db.query(
                TABLE_NOTES,   // La table à interroger
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
            notes.add(new Note(itemTitle, itemDescription));
        }
        cursor.close();

        return notes;
    }
}
