package com.th_koeln.studybuddies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kalaman on 28.03.2018.
 */

public class LearngroupRecyclerAdapter extends RecyclerView.Adapter<LearngroupRecyclerAdapter.LearngroupViewHolder>{

    ArrayList<Learngroup> arrayListLearngroup;

    public LearngroupRecyclerAdapter(ArrayList <Learngroup> arrayList) {
        arrayListLearngroup = arrayList;
    }

    @Override
    public LearngroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_learngroup, parent, false);

        return new LearngroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LearngroupViewHolder holder, int position) {
        Learngroup currentLearngroup = arrayListLearngroup.get(position);

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
        View view;

        public LearngroupViewHolder(View itemView) {
            super(itemView);
            textViewTitle = (TextView)itemView.findViewById(R.id.textViewLearngroupTitle);
            textViewDescription = (TextView)itemView.findViewById(R.id.textViewLearngroupDescription);
            textViewLocation = (TextView)itemView.findViewById(R.id.textViewLocation);
            textViewStudentCount = (TextView)itemView.findViewById(R.id.textViewStudentCount);
            textViewCourse = (TextView)itemView.findViewById(R.id.textViewCourse);
            textViewDatetime = (TextView)itemView.findViewById(R.id.textViewDatetime);

            view = itemView;
        }
    }
}
