package com.example.miniprojet;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {
    private NoteActionListener actionListener;

    public NoteAdapter(Context context, List<Note> notes, NoteActionListener actionListener) {
        super(context, 0, notes);
        this.actionListener = actionListener;
    }


    public interface NoteActionListener {
        void onNoteDelete(int position);
        void onNoteEdit(int position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Note note = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.note_item, parent, false);
        }

        TextView noteText = convertView.findViewById(R.id.noteText);
        TextView titleText = convertView.findViewById(R.id.noteTitleText);
        Button editButton = convertView.findViewById(R.id.editButton);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        noteText.setText(note.getDescription());
        titleText.setText(note.getTitle());

        editButton.setOnClickListener(v -> actionListener.onNoteEdit(position));
        deleteButton.setOnClickListener(v -> actionListener.onNoteDelete(position));


        convertView.setOnClickListener(v -> {
            Note note2 = getItem(position);
            new AlertDialog.Builder(getContext())
                    .setTitle(note2.getTitle())
                    .setMessage(note2.getDescription())
                    .setPositiveButton("OK", null)
                    .show();}
        );


        return convertView;
    }
}

