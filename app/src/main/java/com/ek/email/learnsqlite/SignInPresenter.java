package com.ek.email.learnsqlite;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SignInPresenter {
    String name, username, pass, notes;
    int photo;

    Context context;
    DatabaseHelper dbh;
    SQLiteDatabase db;
    List<String> name_list = new ArrayList<>();
    List<String> username_list = new ArrayList<>();
    List<Integer> notes_list = new ArrayList<>();
    List<String> photo_list = new ArrayList<>();
    List<String> pass_list = new ArrayList<>();
    SignInListener signInListener;

    public SignInPresenter(Context context, SignInListener signInListener) {
        this.context = context;
        this.signInListener = signInListener;
        dbh = new DatabaseHelper(context);
    }


    public SignInPresenter(Context context, String username, String pass, SignInListener signInListener) {

        this.username = username;
        this.pass = pass;
        this.context = context;
        this.signInListener = signInListener;
        dbh = new DatabaseHelper(context);

    }

    public void openDB() {
        db = dbh.getWritableDatabase();
    }

    public void closeDB() {
        dbh.close();
    }

    int id;
    public void Login() {
        String[] table_list = {"ID","NAME", "USERNAME", "PASS", "NOTES", "PHOTO"};
        String selectionUserName = "USERNAME" + " = ?";
        String selectionPass = "PASS" + " = ?";
        String[] selectionArgsU = {username};
        String[] selectionArgsP = {pass};
        Cursor cursor = db.query("USERS",
                table_list,
                selectionUserName,
                selectionArgsU,
                null,
                null,
                null);

        Cursor cursor1 = db.query("USERS",
                table_list,
                selectionPass,
                selectionArgsP,
                null,
                null,
                null);

        int cursorCountUserName = cursor.getCount();
        int cursorCountPass = cursor1.getCount();
        cursor.moveToFirst();
        if (cursorCountUserName > 0 && cursorCountPass > 0) {
            signInListener.isLoged(true,"Login Successful!");

        }else if(cursorCountUserName > 0 && cursorCountPass == 0){
            signInListener.isLoged(false,"Incorrect Password!");
        }else if(cursorCountUserName == 0 && cursorCountPass > 0){
            signInListener.isLoged(false,"Incorrect Username!");
        }else{
            signInListener.isLoged(false,"Error!");
        }




        while (!cursor.isAfterLast()) {
            id=cursor.getInt(0);
            SharedPreferences sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("user_id", id);
            editor.apply();
            cursor.moveToNext();
        }



        cursor.close();



        closeDB();
    }


}
