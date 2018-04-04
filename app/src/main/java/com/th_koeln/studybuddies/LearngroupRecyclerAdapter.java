package com.th_koeln.studybuddies;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kalaman on 28.03.2018.
 */

public class LearngroupRecyclerAdapter extends RecyclerView.Adapter<LearngroupRecyclerAdapter.LearngroupViewHolder>{

    ArrayList<Learngroup> arrayListLearngroup;
    boolean showButton;

    public LearngroupRecyclerAdapter(ArrayList <Learngroup> arrayList, boolean showButton) {
        arrayListLearngroup = arrayList;
        this.showButton = showButton;
    }

    @Override
    public LearngroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_learngroup, parent, false);
        return new LearngroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LearngroupViewHolder holder, int position) {
        Learngroup currentLearngroup = arrayListLearngroup.get(position);
        if (showButton)
            holder.getAddButton().setVisibility(View.VISIBLE);
        else
            holder.getAddButton().setVisibility(View.GONE);

        holder.lid = currentLearngroup.getLid();
        holder.textViewTitle.setText(currentLearngroup.getTitle());
        holder.textViewDescription.setText(currentLearngroup.getDescription());
        holder.textViewCourse.setText(currentLearngroup.getCourse());
        holder.textViewLocation.setText(currentLearngroup.getCampus() + ", " + currentLearngroup.getLocation());
        holder.textViewStudentCount.setText(currentLearngroup.getStudentCount() + "/" + currentLearngroup.getMaxstudent() + " Teilnehmer");
        holder.textViewDatetime.setText(currentLearngroup.getDatetime());
    }

    @Override
    public int getItemCount() {
        return arrayListLearngroup.size();
    }

    public void addSpeechDialog (Learngroup learngroup) {
        arrayListLearngroup.add(0,learngroup);
        notifyItemInserted(0);
    }

    class LearngroupViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewLocation;
        TextView textViewStudentCount;
        TextView textViewCourse;
        TextView textViewDatetime;
        Button addLearngroup;
        String lid;
        View view;

        public LearngroupViewHolder(final View itemView) {
            super(itemView);
            addLearngroup = (Button) itemView.findViewById(R.id.add_learngroup);
            textViewTitle = (TextView)itemView.findViewById(R.id.textViewLearngroupTitle);
            textViewDescription = (TextView)itemView.findViewById(R.id.textViewLearngroupDescription);
            textViewLocation = (TextView)itemView.findViewById(R.id.textViewLocation);
            textViewStudentCount = (TextView)itemView.findViewById(R.id.textViewStudentCount);
            textViewCourse = (TextView)itemView.findViewById(R.id.textViewCourse);
            textViewDatetime = (TextView)itemView.findViewById(R.id.textViewDatetime);
            view = itemView;

            addLearngroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseActions databaseActions = new DatabaseActions();
                    databaseActions.joinGroup(itemView.getContext(), new DatabaseActions.DBRequestListener() {
                        @Override
                        public void onDBRequestFinished(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String message = jsonObject.getString("message");
                                int success = jsonObject.getInt("success");

                                Toast.makeText(itemView.getContext(),message,Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {};
                        }
                    },MainActivity.studentName, lid, true);
                }
            });
        }

        public Button getAddButton(){
            return addLearngroup;
        }

    }
}
