package com.ek.email.learnsqlite;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class UpdatePresenter {
    String name, notes;

    Context context;
    DatabaseHelper dbh;
    SQLiteDatabase db;
    private List<String> notes_list;
    AddNotesListener addNotesListener;
    String ID;
    int noteId;


    public UpdatePresenter(Context context, String ID, List<String> notes_list, int noteId, AddNotesListener addNotesListener) {

        this.ID = ID;
        this.notes_list = notes_list;
        this.noteId = noteId;
        this.context = context;
        this.addNotesListener = addNotesListener;
        dbh = new DatabaseHelper(context);

    }

    public UpdatePresenter(Context context, String ID, int noteId, AddNotesListener addNotesListener) {

        this.ID = ID;
        this.context = context;
        this.addNotesListener = addNotesListener;
        dbh = new DatabaseHelper(context);
        this.noteId = noteId;

    }

    public void openDB() {
        db = dbh.getWritableDatabase();
    }

    public void closeDB() {
        dbh.close();
    }

    int id;
    String json;

    public void updateUser() {

        String[] table_list = {"ID", "NOTES"};
        String selection = "ID" + " = ?";
        String[] selectionArgs = {ID};
        Cursor cursor = db.query("USERS", table_list, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            id = cursor.getInt(0);
            json = cursor.getString(1);


            cursor.moveToNext();
        }

        //Toast.makeText(this.context, " gelenler" +json, Toast.LENGTH_LONG).show();

        try {
            if (json != null) {
                List<String> note = new Gson().fromJson(json, List.class);

                note.set(noteId, notes_list.get(0));
                notes = new Gson().toJson(note);
            } else {
                notes = new Gson().toJson(notes_list);
            }

        } catch (Exception ex) {
            Toast.makeText(context, "hata", Toast.LENGTH_SHORT).show();
        }
        openDB();
        ContentValues values = new ContentValues();
        values.put("Notes", notes);

        try {
            db.update("USERS", values, "ID" + " = ?",
                    new String[]{ID});
            context.startActivity(new Intent(context, ListNotesActivity.class));
            ((Activity) context).finish();
        } catch (Exception ex) {
            Toast.makeText(context, "Hata!", Toast.LENGTH_SHORT).show();
        }
        closeDB();
    }


    public void deleteNote() {
        String[] table_list = {"ID", "NOTES"};
        String selection = "ID" + " = ?";
        String[] selectionArgs = {ID};
        Cursor cursor = db.query("USERS", table_list, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            id = cursor.getInt(0);
            json = cursor.getString(1);


            cursor.moveToNext();
        }

        //Toast.makeText(this.context, " gelenler" +json, Toast.LENGTH_LONG).show();
        if (json != null) {
            try {

                List<String> note = new Gson().fromJson(json, List.class);

                note.remove(noteId);
                notes = new Gson().toJson(note);


                Toast.makeText(context, "Removed datas", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, ListNotesActivity.class));
            } catch (Exception ex) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
            openDB();
            ContentValues values = new ContentValues();
            values.put("Notes", notes);

            // updating row
            try {
                db.update("USERS", values, "ID" + " = ?",
                        new String[]{ID});
                context.startActivity(new Intent(context, ListNotesActivity.class));
                ((Activity) context).finish();
            } catch (Exception ec) {
                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
            }


        }

        closeDB();
    }




}
