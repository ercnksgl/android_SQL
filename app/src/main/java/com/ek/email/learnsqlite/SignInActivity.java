package com.ek.email.learnsqlite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.UniversalTimeScale;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class SignInActivity extends AppCompatActivity implements SignInListener {

    private SignInPresenter signInPresenter;
    EditText username_edt, password_edt;
    String username, password;
    int fiyat;
    Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        login_btn = findViewById(R.id.activity_signin_btn);
        username_edt = findViewById(R.id.activity_signin_username_edt);
        password_edt = findViewById(R.id.activity_signin_pass_edt);
        signInPresenter = new SignInPresenter(this, this);
        signInPresenter.openDB();


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(password_edt.getText()) || TextUtils.isEmpty(username_edt.getText())) {
                    Toast.makeText(SignInActivity.this, "Alanlar boş bırakılamaz", Toast.LENGTH_SHORT).show();
                } else {
                    username = username_edt.getText().toString().trim();
                    password = password_edt.getText().toString().trim();

                    signInPresenter = new SignInPresenter(SignInActivity.this, username, password, SignInActivity.this);
                    signInPresenter.openDB();
                    signInPresenter.Login();

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        finish();
    }


    @Override
    public void isLoged(boolean isLogedMsg,String msg) {

        if (isLogedMsg) {

            Toast.makeText(SignInActivity.this, msg, Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SignInActivity.this, ListNotesActivity.class));
                    finish();
                }
            }, 500);
        } else {
            Toast.makeText(SignInActivity.this, msg, Toast.LENGTH_SHORT).show();
        }


    }
}
