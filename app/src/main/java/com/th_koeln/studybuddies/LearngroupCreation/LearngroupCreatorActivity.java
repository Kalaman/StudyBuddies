package com.th_koeln.studybuddies.LearngroupCreation;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.th_koeln.studybuddies.Course;
import com.th_koeln.studybuddies.DatabaseActions;
import com.th_koeln.studybuddies.LearngroupManagementFragment;
import com.th_koeln.studybuddies.MainActivity;
import com.th_koeln.studybuddies.MeetingPoint;
import com.th_koeln.studybuddies.R;
import com.th_koeln.studybuddies.RegisterActivity;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.th_koeln.studybuddies.MainActivity.databaseActions;

/**
 * Created by Kalaman on 18.04.2018.
 */

public class LearngroupCreatorActivity extends AppCompatActivity implements DatabaseActions.DBRequestListener{

    ImageView buttonCancel;
    String meetingDate;
    String timeStart;
    String timeEnd;
    LinearLayout timePickLayout;
    TextView textViewSelectedTime;
    Spinner courseSpinner;
    Spinner meetingpointSpinner;
    FancyButton buttonCreateLearngroup;

    EditText editTextTitle;
    EditText editTextDescription;
    EditText editTextMaxStudent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learngroupcreator);
        buttonCancel = (ImageView) findViewById(R.id.buttonCancel);
        timePickLayout = (LinearLayout) findViewById(R.id.linearLayoutTimePick);
        textViewSelectedTime= (TextView)findViewById(R.id.textViewMeetingtime);
        courseSpinner = (Spinner)findViewById(R.id.spinnerCourse);
        meetingpointSpinner = (Spinner)findViewById(R.id.spinnerMeetingpoints);
        buttonCreateLearngroup = (FancyButton)findViewById(R.id.buttonCreateLearngroup);

        editTextTitle = (EditText)findViewById(R.id.editTextTitle);
        editTextDescription = (EditText)findViewById(R.id.editTextDescription);
        editTextMaxStudent = (EditText)findViewById(R.id.editTextMaxStudent);


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        timePickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(LearngroupCreatorActivity.this);
            }
        });

        buttonCreateLearngroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.databaseActions.createGroup(LearngroupCreatorActivity.this,LearngroupCreatorActivity.this,editTextTitle.getText().toString(),
                        editTextDescription.getText().toString(), MainActivity.studentName,String.valueOf(courseSpinner.getSelectedItemPosition()+1),meetingDate + " " + timeStart + ":00",meetingDate + " " + timeEnd + ":00",String.valueOf(meetingpointSpinner.getSelectedItemPosition()+1),editTextMaxStudent.getText().toString(),false);
            }
        });

        // Spinner Drop down elements
        List<Course> courses = new ArrayList<Course>();
        courses.add(new Course("Algorithmen der Programmierung I (AP1)",1));
        courses.add(new Course("Algorithmik (ALG)",2));
        courses.add(new Course("Computergrafik und Animation (CGA",3));
        courses.add(new Course("Produktion und Logistik (PuL)",4));


        // Meetingpoint Drop down elements
        List<MeetingPoint> meetingPoint = new ArrayList<MeetingPoint>();
        meetingPoint.add(new MeetingPoint("Container",1));
        meetingPoint.add(new MeetingPoint("Bibliothek",2));
        meetingPoint.add(new MeetingPoint("Eingang Mensa",3));
        meetingPoint.add(new MeetingPoint("Eingang Ferchau Gebäude",4));
        meetingPoint.add(new MeetingPoint("Lernraum 2105",5));

        // Creating adapter for spinner
        ArrayAdapter<Course> dataAdapter = new ArrayAdapter<Course>(this, android.R.layout.simple_spinner_item, courses);
        ArrayAdapter<MeetingPoint> dataAdapter2 = new ArrayAdapter<MeetingPoint>(this, android.R.layout.simple_spinner_item, meetingPoint);

        courseSpinner.setAdapter(dataAdapter);
        meetingpointSpinner.setAdapter(dataAdapter2);
    }

    @Override
    public void onDBRequestFinished(String response) {
        Log.d("LearngroupCreator",response);
    }

    private void showDatePicker(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.datepicker_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        Button ok = (Button) dialog.findViewById(R.id.okButton);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meetingDate = String.format("%04d-%02d-%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth());
                dialog.dismiss();
                showTimePicker(v.getContext(), true);
            }
        });

        dialog.show();
    }

    private void showTimePicker(final Context context, final boolean start) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.timepicker_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        TextView textView = (TextView) dialog.findViewById(R.id.textViewTime);

        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        Button ok = (Button) dialog.findViewById(R.id.okButton);

        if (!start)
            textView.setText("Endzeit");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT < 23){
                    if (start)
                        timeStart = String.format("%02d:%02d",timePicker.getCurrentHour(),timePicker.getCurrentMinute());
                    else
                        timeEnd = String.format("%02d:%02d",timePicker.getCurrentHour(),timePicker.getCurrentMinute());
                } else {
                    if (start)
                        timeStart = String.format("%02d:%02d",timePicker.getHour(),timePicker.getMinute());
                    else
                        timeEnd = String.format("%02d:%02d",timePicker.getHour(),timePicker.getMinute());
                }
                dialog.dismiss();
                if (start)
                    showTimePicker(v.getContext(),false);
                else
                    textViewSelectedTime.setText(meetingDate + " " + timeStart + " bis " + timeEnd);
            }
        });

        dialog.show();
    }
}