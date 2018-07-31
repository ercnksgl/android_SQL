package com.ek.email.learnsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ProfilePresenter {
    String name;

    Context context;
    DatabaseHelper dbh;
    SQLiteDatabase db;
    private List<String> notes_list;
    AddNotesListener addNotesListener;
    String ID;


    public ProfilePresenter(Context context) {


        this.context = context;
        dbh = new DatabaseHelper(context);

    }

    public void openDB() {
        db = dbh.getReadableDatabase();
    }

    public void closeDB() {
        dbh.close();
    }


    List<String> information_list = new ArrayList<>();
    String photo_str;

    public List<String> getInfoList(String id) {
        openDB();
        String[] table_list = {"ID", "PHOTO", "NAME"};
        String selection = "ID" + " = ?";
        String[] selectionArgs = {id};
        Cursor cursor = db.query("USERS", table_list, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            photo_str = cursor.getString(1);
            name = cursor.getString(2);
            cursor.moveToNext();
        }information_list.clear();
        if (photo_str != null && name != null) {

            information_list.add(photo_str);
            information_list.add(name);
        } else {
            information_list.add("");
            information_list.add("");
        }
        return information_list;
    }

    public void setInfos(String mID, String photo_url, String name, String password) {
        openDB();
        ContentValues values = new ContentValues();
        if(!TextUtils.isEmpty(photo_url)){
            values.put("PHOTO", photo_url);
        }

        values.put("NAME", name);
        values.put("PASS", password);

        // updating row
        db.update("USERS", values, "ID" + " = ?",
                new String[]{mID});
        closeDB();

    }


}
