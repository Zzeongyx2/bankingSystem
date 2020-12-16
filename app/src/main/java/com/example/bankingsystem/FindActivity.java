package com.example.bankingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bankingsystem.Model.Profile;
import com.example.bankingsystem.Model.db.ApplicationDB;

import java.util.ArrayList;

public class FindActivity extends AppCompatActivity {

    private EditText edtName;
    private EditText edtPhoneNum;
    private Button find;
    private EditText edtId;
    private String id;
    private String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String data = intent.getStringExtra("find");

        if (data.equals("findID")) {
            setContentView(R.layout.findid_layout);

            edtName = findViewById(R.id.edt_name);
            edtPhoneNum = findViewById(R.id.edt_phoneNum);
            find = (Button) findViewById(R.id.btn_find);

            find.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View view) {
                    searchId();
                }
            });
        }else{
            setContentView(R.layout.findpassword_layout);

            edtName = findViewById(R.id.edt_name);
            edtPhoneNum = findViewById(R.id.edt_phoneNum);
            edtId = findViewById(R.id.edt_userId);
            find = (Button) findViewById(R.id.btn_find);

            find.setOnClickListener(new Button.OnClickListener(){
                public void onClick(View view){
                    searchPassword();
                }
            });
        }
    }

    private void searchId(){
        ApplicationDB applicationDB = new ApplicationDB(getApplicationContext());
        ArrayList<Profile> profiles = applicationDB.getAllProfiles();

        boolean check = false;

        if (profiles.size() > 0) {
            for (int i = 0; i < profiles.size(); i++) {
                if (edtName.getText().toString().equals(profiles.get(i).getName()) && edtPhoneNum.getText().toString().equals(profiles.get(i).getPhoneNum())) {

                    check = true;
                    id = (String) profiles.get(i).getUserId();
                }
            }
            if (!check) {
                Toast.makeText(getApplicationContext(), "입력한 정보와 일치하는 회원 아이디가 없습니다.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "ID : " + id, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void searchPassword(){
        ApplicationDB applicationDB = new ApplicationDB(getApplicationContext());
        ArrayList<Profile> profiles = applicationDB.getAllProfiles();

        boolean check = false;

        if (profiles.size() > 0) {
            for (int i = 0; i < profiles.size(); i++) {
                if (edtName.getText().toString().equals(profiles.get(i).getName()) && edtPhoneNum.getText().toString().equals(profiles.get(i).getPhoneNum()) &&
                        edtId.getText().toString().equals(profiles.get(i).getUserId())) {

                    check = true;
                    password = (String) profiles.get(i).getPassword();
                }
            }
            if (!check) {
                Toast.makeText(getApplicationContext(), "입력한 정보와 일치하는 회원 비밀번호가 없습니다.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "PASSWORD : " + password, Toast.LENGTH_LONG).show();
            }
        }

    }
}
