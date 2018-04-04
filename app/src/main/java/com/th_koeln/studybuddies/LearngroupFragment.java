package com.th_koeln.studybuddies;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.th_koeln.studybuddies.MainActivity.databaseActions;

/**
 * Created by Kalaman on 27.03.2018.
 */

public class LearngroupFragment extends Fragment implements DatabaseActions.DBRequestListener{

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView textViewEmptyResult;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_learngroups,container,false);

        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerViewMyLearngroups);
        swipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipeRefresh);
        textViewEmptyResult = (TextView)v.findViewById(R.id.textViewEmptyResult);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        databaseActions.getUserLearngroups(getContext(),LearngroupFragment.this,true);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorThOrange),getResources().getColor(R.color.colorThViolett));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DatabaseActions databaseActions = new DatabaseActions();
                databaseActions.getUserLearngroups(getContext(),LearngroupFragment.this,false);
            }
        });

        return v;
    }

    @Override
    public void onDBRequestFinished(String response) {

        ArrayList<Learngroup> arrayList =  Utilities.parseLearngroups(response);
        swipeRefreshLayout.setRefreshing(false);

        if (arrayList == null || arrayList.size() == 0) {
            textViewEmptyResult.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else{
            textViewEmptyResult.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(new LearngroupRecyclerAdapter(arrayList,false));
        }
    }
}
