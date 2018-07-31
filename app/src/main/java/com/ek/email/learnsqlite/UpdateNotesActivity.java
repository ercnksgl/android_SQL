package com.ek.email.learnsqlite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UpdateNotesActivity extends AppCompatActivity implements AddNotesListener {

    EditText editText;
    Button saveButton, deleteButton;
    int userId,noteId;
    UpdatePresenter updatePresenter;
    List<String>notes_list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_notes);

        editText = findViewById(R.id.activity_updateNote_edt);
        saveButton=findViewById(R.id.activity_updatenote_save_btn);
        deleteButton=findViewById(R.id.activity_updateNote_delete_button);
        SharedPreferences sharedPref = UpdateNotesActivity.this.getSharedPreferences("user", Context.MODE_PRIVATE);
        userId = sharedPref.getInt("user_id", 0);


        Bundle extras = getIntent().getExtras();
        String note= extras.getString("note");
        noteId=extras.getInt("noteId");
        editText.setText(note);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(editText.getText())) {
                    Toast.makeText(UpdateNotesActivity.this, "Added", Toast.LENGTH_SHORT).show();
                    notes_list.add(editText.getText().toString().trim());
                    updatePresenter = new UpdatePresenter(UpdateNotesActivity.this, "" + userId, notes_list, noteId, UpdateNotesActivity.this);
                    updatePresenter.openDB();
                    updatePresenter.updateUser();
                }else{
                    Toast.makeText(UpdateNotesActivity.this, "Please fill in the blanks", Toast.LENGTH_SHORT).show();
                }
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePresenter = new UpdatePresenter(UpdateNotesActivity.this, "" + userId,noteId ,UpdateNotesActivity.this);
                updatePresenter.openDB();
                updatePresenter.deleteNote();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UpdateNotesActivity.this, ListNotesActivity.class));
        finish();
    }


    @Override
    public void notedAdded(boolean added) {

    }
}
