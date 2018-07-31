package com.ek.email.learnsqlite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddNotesActivity extends AppCompatActivity implements AddNotesListener {

    EditText editText;
    Button saveButton, deleteButton;
    int userId;
    AddNotePresenter addNotePresenter;
    List<String>notes_list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        editText = findViewById(R.id.activity_addNote_edt);
        saveButton=findViewById(R.id.activity_addnote_save_btn);
        deleteButton=findViewById(R.id.activity_addNote_delete_button);
        SharedPreferences sharedPref = AddNotesActivity.this.getSharedPreferences("user", Context.MODE_PRIVATE);
        userId = sharedPref.getInt("user_id", 0);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(editText.getText())) {
                    Toast.makeText(AddNotesActivity.this, "save", Toast.LENGTH_SHORT).show();
                    notes_list.add(editText.getText().toString().trim());
                    addNotePresenter = new AddNotePresenter(AddNotesActivity.this, "" + userId, notes_list, AddNotesActivity.this);
                    addNotePresenter.openDB();
                    addNotePresenter.updateUser();
                }else{
                    Toast.makeText(AddNotesActivity.this, "Please fill in the blanks", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteButton.setEnabled(false);
        deleteButton.setClickable(false);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddNotesActivity.this, ListNotesActivity.class));
        finish();
    }


    @Override
    public void notedAdded(boolean added) {

    }
}
