package com.ek.email.learnsqlite;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    CircleImageView profile_image;
    EditText name_edt, pass_edt, name_enabled_edt;
    Button save_btn;
    private int PICK_IMAGE_REQUEST = 1;
    int userId;
    ProfilePresenter profilePresenter;
    List<String> info_list = new ArrayList<>();
    private String photo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainprofile);
        profile_image = findViewById(R.id.activity_profile_profile_img);
        name_edt = findViewById(R.id.activity_profile_name_edt);
        name_enabled_edt = findViewById(R.id.activity_profile_name_text);
        pass_edt = findViewById(R.id.activity_profile_pass_edt);
        save_btn = findViewById(R.id.activity_profile_save_btn);
        profile_image.setClickable(true);
//aldık göstercez
        SharedPreferences sharedPref = ProfileActivity.this.getSharedPreferences("user", Context.MODE_PRIVATE);
        userId = sharedPref.getInt("user_id", 0);


        profilePresenter = new ProfilePresenter(ProfileActivity.this);
        info_list = profilePresenter.getInfoList("" + userId);
        try {
            if (!TextUtils.isEmpty(info_list.get(0))) {
                byte[] decodedString = Base64.decode(info_list.get(0), Base64.DEFAULT);
                Bitmap image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                profile_image.setImageBitmap(image);
            }

        } catch (Exception ex) {
            Toast.makeText(this, "error: " + ex, Toast.LENGTH_SHORT).show();
        }
        if (!TextUtils.isEmpty(info_list.get(1))) {
            name_enabled_edt.setText(info_list.get(1));
            name_edt.setText(info_list.get(1));
        }


        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(name_edt.getText()) || TextUtils.isEmpty(pass_edt.getText())) {
                    Toast.makeText(ProfileActivity.this, "Please fill in the blanks", Toast.LENGTH_SHORT).show();
                } else {

                        profilePresenter = new ProfilePresenter(ProfileActivity.this);
                        profilePresenter.setInfos(userId + "", photo, name_edt.getText().toString().trim(), pass_edt.getText().toString().trim());
                        name_enabled_edt.setText(name_edt.getText().toString().trim());
                        startActivity(new Intent(ProfileActivity.this, ListNotesActivity.class));
                        finish();

                }

            }
        });


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
                    } else {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case RESULT_OK:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                profile_image.setImageBitmap(bitmap);


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                String b64Image = Base64.encodeToString(b, Base64.DEFAULT);
                photo = b64Image;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileActivity.this, ListNotesActivity.class));
        finish();
    }

}
