package com.example.mojnotatnik.notatki;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mojnotatnik.R;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {

    public static final int WRAP_CONTENT_LENGTH = 50;

    public NoteAdapter(Context context, int resource, List<Note> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_note, null);
        }

        Note note = getItem(position);

        if (note != null) {
            TextView title = convertView.findViewById(R.id.list_note_title);
            TextView date = convertView.findViewById(R.id.list_note_date);
            TextView content = convertView.findViewById(R.id.list_note_content_preview);

            title.setText(note.getTitle());
            date.setText(note.getDateTimeFormatted(getContext()));

            //correctly show preview of the content (not more than 50 char or more than one line!)
            int toWrap = WRAP_CONTENT_LENGTH;
            int lineBreakIndex = note.getContent().indexOf('\n');
            //not an elegant series of if statements...needs to be cleaned up!
            if (note.getContent().length() > WRAP_CONTENT_LENGTH || lineBreakIndex < WRAP_CONTENT_LENGTH) {
                if (lineBreakIndex < WRAP_CONTENT_LENGTH) {
                    toWrap = lineBreakIndex;
                }
                if (toWrap > 0) {
                    content.setText(note.getContent().substring(0, toWrap) + "...");
                } else {
                    content.setText(note.getContent());
                }
            } else {
                //if less than 50 chars...leave it as is :P
                content.setText(note.getContent());
            }
        }

        return convertView;
    }
}
