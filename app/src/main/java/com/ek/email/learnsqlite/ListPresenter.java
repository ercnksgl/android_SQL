package com.ek.email.learnsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ListPresenter {
    String name, username, pass, notes;
    int photo;

    Context context;
    DatabaseHelper dbh;
    SQLiteDatabase db;
    List<String> name_list = new ArrayList<>();
    List<String> username_list = new ArrayList<>();
    List<String> not_list = new ArrayList<>();
    List<String> photo_list = new ArrayList<>();
    List<String> pass_list = new ArrayList<>();
    SignUpListener signUpListener;
    String ID;


    public ListPresenter(Context context, String ID, SignUpListener signUpListener) {

        this.ID = ID;
        this.context = context;
        this.signUpListener = signUpListener;
        dbh = new DatabaseHelper(context);

    }

    public ListPresenter(Context context) {

        this.context = context;
        dbh = new DatabaseHelper(context);

    }

    public void openDB() {
        db = dbh.getWritableDatabase();
    }

    public void closeDB() {
        dbh.close();
    }

    int id;
    private String json;

    public void getUserList() {
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

            List<String> notes = new Gson().fromJson(json, List.class);

            not_list = notes;
            //Toast.makeText(this.context, " gelenler" + notes.get(i), Toast.LENGTH_SHORT).show();

        } catch (Exception ec) {
            Toast.makeText(context, "Error! ", Toast.LENGTH_SHORT).show();

        }
        signUpListener.didDatasList(id, name_list, username_list, pass_list, not_list, photo_list);

        cursor.close();
        closeDB();

    }

    List<String> searching_notes = new ArrayList<>();

    public List<String> searchNotes(String msj, String Id) {
        openDB();
        String[] table_list = {"ID", "NOTES"};
        String selection = "ID" + " = ?";
        String[] selectionArgs = {Id};
        Cursor cursor = db.query("USERS", table_list, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            id = cursor.getInt(0);
            json = cursor.getString(1);

            cursor.moveToNext();
        }

        //Toast.makeText(this.context, " gelenler" +json, Toast.LENGTH_LONG).show();
        if (json == null) {

            return null;
        } else {
            try {

                List<String> notes = new Gson().fromJson(json, List.class);

                not_list = notes;
                //Toast.makeText(this.context, " gelenler" + notes.get(i), Toast.LENGTH_SHORT).show();

            } catch (Exception ec) {
                Toast.makeText(context, "Error! ", Toast.LENGTH_SHORT).show();

            }

            for (int i = 0; i < not_list.size(); i++) {
                if (not_list.get(i).contains(msj)) {
                    searching_notes.add(not_list.get(i));
                }
            }


            return searching_notes;
        }

    }


}
