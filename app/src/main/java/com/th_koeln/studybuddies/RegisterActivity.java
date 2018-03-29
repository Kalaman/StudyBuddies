package com.th_koeln.studybuddies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Kalaman on 29.03.2018.
 */

public class RegisterActivity extends AppCompatActivity{

    FancyButton buttonRegister;
    EditText editTextStudentname;
    EditText editTextSemester;
    Spinner spinnerStudyprogram;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        buttonRegister = (FancyButton)findViewById(R.id.buttonRegister);
        editTextStudentname = (EditText) findViewById(R.id.editTextStudentname);
        editTextSemester = (EditText)findViewById(R.id.editTextSemester);
        spinnerStudyprogram = (Spinner) findViewById(R.id.spinnerStudyprogram);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!editTextSemester.getText().toString().equals("")&& !editTextStudentname.getText().toString().equals("")){
                    DatabaseActions databaseActions = new DatabaseActions();
                    databaseActions.registerUser(RegisterActivity.this, new DatabaseActions.DBRequestListener() {
                        @Override
                        public void onDBRequestFinished(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String message = jsonObject.getString("message");
                                int success = jsonObject.getInt("success");

                                Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_SHORT).show();
                                if (success == 0) {
                                    SplashActivity.prefManager.writeSharedBoolean("not_firststart",true);
                                    SplashActivity.prefManager.writeSharedString("studentname",editTextStudentname.getText().toString());
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    finish();
                                }
                            }catch (Exception e){e.printStackTrace();}
                        }
                    },editTextStudentname.getText().toString(),editTextSemester.getText().toString(),spinnerStudyprogram.getSelectedItemPosition()+1,true);
                }
                else {
                    Toast.makeText(RegisterActivity.this,"Bitte füllen Sie alle Felder aus",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
