package com.ek.email.learnsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SignUpPresenter {
    String name, username, pass, notes;
    int photo;

    Context context;
    DatabaseHelper dbh;
    SQLiteDatabase db;
    List<String> name_list = new ArrayList<>();
    List<String> username_list = new ArrayList<>();
    List<String> notes_list = new ArrayList<>();
    List<String> photo_list = new ArrayList<>();
    List<String> pass_list = new ArrayList<>();
    SignUpListener signUpListener;
    int user_id;


    public SignUpPresenter(Context context, String username, String pass, SignUpListener signUpListener) {

        this.username = username;
        this.pass = pass;
        this.context = context;
        this.signUpListener = signUpListener;
        dbh = new DatabaseHelper(context);

    }

    public void openDB() {
        db = dbh.getWritableDatabase();
    }

    public void closeDB() {
        dbh.close();
    }

    public void addUser() {
        try {
//kullanıcı varmı kontrol et
            String[] table_list = {"ID"};
            String selection = "USERNAME" + " = ?";
            String[] selectionArgs = {username};
            Cursor cursor = db.query("USERS",
                    table_list,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null);

            int cursorCount = cursor.getCount();
            cursor.moveToFirst();
            if (cursorCount == 0) {
                //kayıt yap
                ContentValues con = new ContentValues();
                con.put("USERNAME", username);
                con.put("PASS", pass);
                db.insert("USERS", null, con);
                con.clear();
                closeDB();
                openDB();
                cursor.close();

                //kullanıcı id sini çek shared a kaydet
                String[] table_list1 = {"ID"};
                String selection1 = "USERNAME" + " = ? AND " + "PASS" + " = ?";
                String[] selectionArgs1 = {username, pass};
                Cursor cursor1 = db.query("USERS", table_list1, selection1, selectionArgs1, null, null, null);
                cursor1.moveToFirst();
                while (!cursor.isAfterLast()) {
                    user_id = cursor1.getInt(0);
                    cursor1.moveToNext();
                }
                cursor1.close();
                closeDB();
                SharedPreferences sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("user_id", user_id);
                editor.apply();
                signUpListener.addDatasToDb();

            } else {
                Toast.makeText(context, "User already exists", Toast.LENGTH_SHORT).show();
            }

        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }


    }

}
