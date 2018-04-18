package com.th_koeln.studybuddies.LearngroupCreation;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.th_koeln.studybuddies.LearngroupManagementFragment;
import com.th_koeln.studybuddies.R;

import static com.th_koeln.studybuddies.MainActivity.databaseActions;

/**
 * Created by Kalaman on 18.04.2018.
 */

public class LearngroupCreatorActivity extends AppCompatActivity {

    ImageView buttonCancel;
    String meetingDate;
    String timeStart;
    String timeEnd;
    LinearLayout timePickLayout;
    TextView textViewSelectedTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learngroupcreator);
        buttonCancel = (ImageView) findViewById(R.id.buttonCancel);
        timePickLayout = (LinearLayout) findViewById(R.id.linearLayoutTimePick);
        textViewSelectedTime= (TextView)findViewById(R.id.textViewMeetingtime);

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