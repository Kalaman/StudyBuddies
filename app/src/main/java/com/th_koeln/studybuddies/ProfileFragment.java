package com.th_koeln.studybuddies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.th_koeln.studybuddies.MainActivity.databaseActions;
import static com.th_koeln.studybuddies.MainActivity.studentName;

public class ProfileFragment extends Fragment implements DatabaseActions.DBRequestListener{

    Button saveChanges;
    EditText editSemester;
    EditText editPhone;
    TextView name;
    TextView description;
    Spinner spinnerProfileStudyprogram;
    Student student;
    ImageView profilePic;
    boolean firstFetch = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile,container,false);
        name = (TextView) v.findViewById(R.id.textViewName);
        description = (TextView) v.findViewById(R.id.textViewName2);
        saveChanges = (Button) v.findViewById(R.id.saveChanges);
        saveChanges.setVisibility(View.GONE);
        editPhone = (EditText) v.findViewById(R.id.editPhone);
        editSemester = (EditText) v.findViewById(R.id.editSemester);
        spinnerProfileStudyprogram = (Spinner) v.findViewById(R.id.spinnerProfileStudyprogram);

        databaseActions.getStudent(getContext(),ProfileFragment.this,true);

        // Rounded Profile picture
        profilePic = v.findViewById(R.id.imageView);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lama);
        RoundedBitmapDrawable mDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        mDrawable.setCircular(true);
        profilePic.setImageDrawable(mDrawable);

        spinnerProfileStudyprogram.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (student != null && !firstFetch) {
                    if (i != student.getStudyProgramID() -1)
                        saveChanges.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editPhone.setCursorVisible(true);
                editSemester.setCursorVisible(true);
                if (!firstFetch)
                    saveChanges.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        editSemester.addTextChangedListener(textWatcher);
        editPhone.addTextChangedListener(textWatcher);


        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseActions databaseActions = new DatabaseActions();
                databaseActions.editProfile(getContext(), new DatabaseActions.DBRequestListener() {
                    @Override
                    public void onDBRequestFinished(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            int success = jsonObject.getInt("success");
                            if (success == 0) {
                                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {};
                        }
                    },student.getName(), Integer.parseInt(editSemester.getText().toString()),spinnerProfileStudyprogram.getSelectedItemPosition()+1, "Schreibt mich an falls jemand noch Probeklausuren benötigt.",editPhone.getText().toString(),true);
                saveChanges.setVisibility(View.GONE);
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onDBRequestFinished(String response) {

        student = Utilities.parseStudent(response);
        name.setText(student.getName(),TextView.BufferType.NORMAL);
        description.setText(student.getDescription(),TextView.BufferType.NORMAL);
        editPhone.setText(String.valueOf(student.getPhone()), TextView.BufferType.EDITABLE);
        editPhone.setCursorVisible(false);
        editSemester.setText(String.valueOf(student.getSemester()), TextView.BufferType.EDITABLE);
        editSemester.setCursorVisible(false);
        spinnerProfileStudyprogram.setSelection(student.getStudyProgramID()-1);
        firstFetch = false;
    }
}
