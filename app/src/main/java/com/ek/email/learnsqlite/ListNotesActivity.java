package com.ek.email.learnsqlite;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class ListNotesActivity extends AppCompatActivity implements SignUpListener {

    private ListView listView;
    ListPresenter listPresenter;
    ImageView addListBtn;
    Button profile_button;
    EditText search_edt;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);
        listView = findViewById(R.id.list_view);
        addListBtn = findViewById(R.id.activity_list_add_img);
        profile_button = findViewById(R.id.activity_listnotes_profile_btn);
        search_edt = findViewById(R.id.activity_list_search_edt);
        addListBtn.setClickable(true);
        SharedPreferences sharedPref = ListNotesActivity.this.getSharedPreferences("user", Context.MODE_PRIVATE);
        userId = sharedPref.getInt("user_id", 0);
        listPresenter = new ListPresenter(this, "" + userId, this);
        listPresenter.openDB();
        listPresenter.getUserList();

        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListNotesActivity.this, ProfileActivity.class));
                finish();
            }
        });


        addListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ListNotesActivity.this, AddNotesActivity.class));
                finish();
            }
        });

        search_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                List<String>searching_notes=new ArrayList<>();
                listPresenter=new ListPresenter(ListNotesActivity.this);
                searching_notes = listPresenter.searchNotes(search_edt.getText().toString().trim(),""+userId);
                if(searching_notes!=null){

                    final ArrayAdapter<String> adapter = new ArrayAdapter<>(ListNotesActivity.this, R.layout.list_item, searching_notes);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            startActivity(new Intent(ListNotesActivity.this, UpdateNotesActivity.class).putExtra("note", listView.getItemAtPosition(i).toString())
                                    .putExtra("noteId", i));
                        }
                    });
                }
            }
        });


    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ListNotesActivity.this);
        builder.setTitle("LogOut");
        builder.setMessage("Do you wanna logout?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                startActivity(new Intent(ListNotesActivity.this, MainActivity.class));
                finish();
            }
        });

        builder.show();

    }


    @Override
    public void didDatasList(int Id, List<String> name_list, List<String> username_list, List<String> pass_list, List<String> notes_list, List<String> photo_list) {
        final List<String> items = new ArrayList<>();
        int sayi = 0;
        if (notes_list != null) {
            for (int i = 0; i < notes_list.size(); i++) {
                sayi += 1;
                items.add(notes_list.get(i));
            }
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, items);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    startActivity(new Intent(ListNotesActivity.this, UpdateNotesActivity.class).putExtra("note", listView.getItemAtPosition(i).toString())
                            .putExtra("noteId", i));
                }
            });
        }
    }


    @Override
    public void addDatasToDb() {

    }
}
