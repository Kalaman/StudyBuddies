package com.th_koeln.studybuddies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import mehdi.sakout.fancybuttons.FancyButton;

import static com.th_koeln.studybuddies.MainActivity.databaseActions;

/**
 * Created by Kalaman on 27.03.2018.
 */

public class SearchFragment extends Fragment implements DatabaseActions.DBRequestListener{

    SearchableSpinner courseSpinner;
    SearchableSpinner semesterSpinner;
    FancyButton searchButton;
    RecyclerView recyclerViewResults;
    LinearLayout linearLayoutSearch;
    LinearLayout linearLayoutResults;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search,container,false);

        linearLayoutSearch = (LinearLayout)v.findViewById(R.id.linearLayoutSearch);
        linearLayoutResults = (LinearLayout)v.findViewById(R.id.linearLayoutResult);

        courseSpinner = (SearchableSpinner)v.findViewById(R.id.searchableSpinnerCourse);
        semesterSpinner = (SearchableSpinner)v.findViewById(R.id.searchableSpinnerCourse);
        searchButton = (FancyButton)v.findViewById(R.id.buttonSearch);
        recyclerViewResults = (RecyclerView)v.findViewById(R.id.recyclerViewSearch);

        recyclerViewResults.setLayoutManager(new LinearLayoutManager(getContext()));

        // Spinner Drop down elements
        List<Course> courses = new ArrayList<Course>();
        courses.add(new Course("Algorithmen der Programmierung I (AP1)",1));
        courses.add(new Course("Algorithmik (ALG)",2));
        courses.add(new Course("Computergrafik und Animation (CGA",3));
        courses.add(new Course("Produktion und Logistik (PuL)",4));

        // Creating adapter for spinner
        ArrayAdapter<Course> dataAdapter = new ArrayAdapter<Course>(getActivity(), android.R.layout.simple_spinner_item, courses);
        courseSpinner.setAdapter(dataAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseActions.getLearngroups(getContext(),new DatabaseActions.DBRequestListener() {
                    @Override
                    public void onDBRequestFinished(String response) {
                        LearngroupRecyclerAdapter lra = new LearngroupRecyclerAdapter(Utilities.parseLearngroups(response),true);
                        recyclerViewResults.setAdapter(lra);
                        if (recyclerViewResults.getAdapter().getItemCount() > 0)
                            showResultLayout(true);
                        else
                            Toast.makeText(getContext(),"Keine Suchergebnisse",Toast.LENGTH_SHORT).show();
                    }
                },(Course)courseSpinner.getSelectedItem(),true);
                return;
            }
        });


        return v;
    }

    @Override
    public void onDBRequestFinished(String response) {

    }

    public void showResultLayout (boolean b) {
        linearLayoutResults.setVisibility(b ? View.VISIBLE : View.GONE);
        linearLayoutSearch.setVisibility(b ? View.GONE : View.VISIBLE);
    }
}
