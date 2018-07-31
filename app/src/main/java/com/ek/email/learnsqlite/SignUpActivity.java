package com.ek.email.learnsqlite;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class SignUpActivity extends AppCompatActivity implements SignUpListener {

    private SignUpPresenter signUpPresenter;
    EditText username_edt, password_edt;
    String username, password;
    int fiyat;
    Button save_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        save_btn = findViewById(R.id.activity_signup_save_btn);
        username_edt = findViewById(R.id.activity_signup_username_edt);
        password_edt = findViewById(R.id.activity_signup_pass_edt);



        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(password_edt.getText()) || TextUtils.isEmpty(username_edt.getText())) {
                    Toast.makeText(SignUpActivity.this, "Please fill in the blanks", Toast.LENGTH_SHORT).show();
                } else {
                    username = username_edt.getText().toString().trim();
                    password = password_edt.getText().toString().trim();

                    signUpPresenter = new SignUpPresenter(SignUpActivity.this, username, password, SignUpActivity.this);
                    signUpPresenter.openDB();
                    signUpPresenter.addUser();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();
    }


    @Override
    public void didDatasList(int Id, List<String> name_list, List<String> username_list, List<String> pass_list, List<String> notes_list, List<String> photo_list) {

    }

    @Override
    public void addDatasToDb() {
        Toast.makeText(this, "Added datas", Toast.LENGTH_SHORT).show();
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SignUpActivity.this, ListNotesActivity.class));
                finish();
            }
        }, 1000);


    }
}
