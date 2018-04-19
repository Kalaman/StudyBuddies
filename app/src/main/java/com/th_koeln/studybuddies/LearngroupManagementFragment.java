package com.th_koeln.studybuddies;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.th_koeln.studybuddies.MainActivity.databaseActions;
import static com.th_koeln.studybuddies.MainActivity.studentName;

/**
 * Created by alasdair on 04.04.18.
 */

public class LearngroupManagementFragment extends Fragment implements DatabaseActions.DBRequestListener{

    private Learngroup learngroup;
    private TextView textViewLocation;
    private TextView textViewStudentCount;
    private TextView textViewCourse;
    private TextView textViewDatetime;
    private TextView textTimeRemaining;
    private TextView textTimeRemainingSub;
    private FancyButton newSession;
    private FancyButton cancelSession;
    private Button leaveGroup, editGroup;
    private long difference;
    private String date, timeStart, timeEnd;
    private MeetingPoint meetingPoint;
    private CountDownTimer countDownTimer;
    private Spinner campusSpinner, meetingPointSpinner;
    private ArrayList<MeetingPoint> meetingPoints;
    String meetingFrom = "2018-04-13 10:15:00",meetingTo = "2018-04-13 12:15:00";

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_learngroup_management, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(learngroup.getTitle());

        textViewLocation = v.findViewById(R.id.textViewLocation);
        textViewDatetime = v.findViewById(R.id.textViewDatetime);
        textTimeRemaining = v.findViewById(R.id.textTimeRemaining);
        textTimeRemainingSub = v.findViewById(R.id.textTimeRemainingSub);
        newSession = (FancyButton) v.findViewById(R.id.buttonSessionNew);
        cancelSession = (FancyButton) v.findViewById(R.id.buttonSessionCancel);
        textViewLocation.setText(learngroup.getCampus() + ", " + learngroup.getLocation());
        textViewDatetime.setText(learngroup.getDatetimeFrom());
        leaveGroup = (Button) v.findViewById(R.id.leave_learngroup);
        editGroup = (Button) v.findViewById(R.id.edit_learngroup);

        startCountDown();

        newSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(view.getContext());
            }
        });

        cancelSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                meetingFrom = learngroup.getFormattedDateTimeFrom();
                meetingTo = "2018-04-13 " + String.format("%02d:%02d:%02d",cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),0);
                databaseActions.editSession(getContext(),LearngroupManagementFragment.this,learngroup.getLid(),meetingFrom,meetingTo,"cancel",true);
            }
        });

        leaveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                databaseActions.leaveGroup(getContext(),LearngroupManagementFragment.this,studentName,learngroup.getLid(),true);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Möchtest du die Gruppe wirklich verlassen?").setPositiveButton("Ja", dialogClickListener)
                        .setNegativeButton("Nein", dialogClickListener).show();
            }
        });

        editGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.edit_learngroup_dialog);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(true);

                final EditText editTitle = (EditText) dialog.findViewById(R.id.edit_title);
                final EditText editDescription = (EditText) dialog.findViewById(R.id.edit_description);
                campusSpinner = (Spinner) dialog.findViewById(R.id.spinnerCampus);

                campusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    String[] meetingpointsGummersbach = {"Container","Bibliothek","Eingang Mensa","Eingang Ferchau Gebäude"};
                    String[] meetingpointsDeutz = {"Bibliothek","Eingang Mensa","Lernraum 1303"};
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ArrayAdapter<String> dataAdapter;
                        if (i == 0) {
                            dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,meetingpointsGummersbach);
                        } else {
                            dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,meetingpointsDeutz);
                        }

                        meetingPointSpinner.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                meetingPointSpinner = (Spinner) dialog.findViewById(R.id.spinnerMeetingPoint);
                databaseActions.getMeetingpoint(getContext(),LearngroupManagementFragment.this,learngroup.getLid(),false);

                editTitle.setText(learngroup.getTitle());
                editDescription.setText(learngroup.getDescription());

                Button ok = (Button) dialog.findViewById(R.id.okButton);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mpid;
                        if (campusSpinner.getSelectedItemPosition() == 0) {
                            learngroup.setTitle(editTitle.getText().toString());
                            learngroup.setDescription(editDescription.getText().toString());
                            ((MainActivity) getActivity()).setActionBarTitle(learngroup.getTitle());
                            mpid = String.valueOf(meetingPointSpinner.getSelectedItemPosition()+1);
                        } else {
                            mpid = String.valueOf(meetingPointSpinner.getSelectedItemPosition()+5);
                        }

                        databaseActions.editGroup(getContext(), new DatabaseActions.DBRequestListener() {
                            @Override
                            public void onDBRequestFinished(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String message = jsonObject.getString("message");
                                    int success = jsonObject.getInt("success");

                                } catch (Exception e) {};
                            }
                        },learngroup.getLid(),editTitle.getText().toString(),editDescription.getText().toString(),String.valueOf(campusSpinner.getSelectedItemPosition()+1),mpid,true);
                        dialog.dismiss();
                    }
                });

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.show();
                dialog.getWindow().setAttributes(lp);
            }
        });

        return v;

    }

    public Learngroup getLearngroup() {
        return learngroup;
    }

    public void setLearngroup(Learngroup learngroup) {
        this.learngroup = learngroup;
    }

    @Override
    public void onDBRequestFinished(String response) {
        JSONObject jsonObject = null;

        try {
            JSONArray array = new JSONArray(response);
            jsonObject = array.getJSONObject(0);
            int cpid = jsonObject.getInt("CPID");
            int mpid = jsonObject.getInt("MPID");
            String mp = jsonObject.getString("MeetingPoint");
            String campus = jsonObject.getString("Campus");
            meetingPoints = Utilities.parseMeetingPoints(jsonObject.getJSONArray("MeetingPoints"));
            meetingPoint = new MeetingPoint(cpid,mpid,campus,mp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (meetingPoint != null){
            campusSpinner.setSelection(meetingPoint.getCpid()-1);
            String[] meetingpointsGummersbach = {"Container","Bibliothek","Eingang Mensa","Eingang Ferchau Gebäude"};
            String[] meetingpointsDeutz = {"Bibliothek","Eingang Mensa","Lernraum 1303"};
            ArrayAdapter<String> dataAdapter;
            if (meetingPoint.getCpid() == 1)
                dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,meetingpointsGummersbach);
            else
                dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,meetingpointsDeutz);
            meetingPointSpinner.setAdapter(dataAdapter);
            dataAdapter.notifyDataSetChanged();
        }

        try {
            jsonObject = new JSONObject(response);
            int success = jsonObject.getInt("success");
            String message = jsonObject.getString("message");

            if (success == 0) {
                if (message.equals("Lerngruppe erfolgreich verlassen")) {
                    AppCompatActivity activity = (AppCompatActivity) getContext();
                    LearngroupFragment learngroupFragment = new LearngroupFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace( R.id.content_main, learngroupFragment).addToBackStack(null).commit();
                    Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
                } else {
                    learngroup.setDatetimeFrom(meetingFrom);
                    learngroup.setDatetimeTo(meetingTo);
                    textViewDatetime.setText(learngroup.getDatetimeFrom());
                    startCountDown();
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public long getTimeDifference(String time1, String time2){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date from = null, to = null;
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        long difference;

        try {
            from = simpleDateFormat.parse(time1);
            to = simpleDateFormat.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (now.getTime() > from.getTime() && now.getTime() < to.getTime()) {
            difference = to.getTime() - now.getTime();
            cancelSession.setVisibility(View.VISIBLE);
            newSession.setVisibility(View.GONE);
        } else {
            difference = -1;
            cancelSession.setVisibility(View.GONE);
            newSession.setVisibility(View.VISIBLE);
        }

        return difference;
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
                        timeStart = String.format("%02d:%02d:%02d",timePicker.getCurrentHour(),timePicker.getCurrentMinute(),0);
                    else
                        timeEnd = String.format("%02d:%02d:%02d",timePicker.getCurrentHour(),timePicker.getCurrentMinute(),0);
                } else {
                    if (start)
                        timeStart = String.format("%02d:%02d:%02d",timePicker.getHour(),timePicker.getMinute(),0);
                    else
                        timeEnd = String.format("%02d:%02d:%02d",timePicker.getHour(),timePicker.getMinute(),0);
                }
                if(!start) {
                    meetingFrom = date + " " + timeStart;
                    meetingTo = date + " " + timeEnd;
                    databaseActions.editSession(getContext(),LearngroupManagementFragment.this,learngroup.getLid(),meetingFrom,meetingTo,"create",true);
                }
                dialog.dismiss();
                if (start)
                    showTimePicker(v.getContext(),false);
            }
        });

        dialog.show();
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
                date = String.format("%04d-%02d-%02d",datePicker.getYear(),datePicker.getMonth()+1,datePicker.getDayOfMonth());
                dialog.dismiss();
                showTimePicker(v.getContext(),true);
            }
        });

        dialog.show();
    }

    private void startCountDown(){
        difference = getTimeDifference(learngroup.getDatetimeFrom(),learngroup.getDatetimeTo());

        if (countDownTimer != null)
            countDownTimer.cancel();

        countDownTimer = new CountDownTimer(difference, 1) {

            public void onTick(long millisUntilFinished) {

                String remaining = String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)-
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                );
                textTimeRemaining.setText(remaining);
            }

            public void onFinish() {
                textTimeRemaining.setText("Session vorbei");
                textTimeRemainingSub.setVisibility(View.GONE);
            }

        };
        countDownTimer.start();
    }
}
