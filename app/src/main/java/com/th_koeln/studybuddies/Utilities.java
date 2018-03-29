package com.th_koeln.studybuddies;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Kalaman on 29.03.2018.
 */

public class Utilities {

    public static ArrayList<Learngroup> parseLearngroups (String response) {
        try{
            ArrayList<Learngroup> learngroups = new ArrayList<>();

            JSONArray jsonArray = new JSONArray(response);

            for (int i=0;i<jsonArray.length();++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                learngroups.add(new Learngroup(jsonObject.getString("Title"),
                        jsonObject.getString("Description"),
                        jsonObject.getString("Meetingtime"),
                        jsonObject.getString("Location"),
                        jsonObject.getString("MaxStudentCount"),
                        jsonObject.getString("Course"),
                        jsonObject.getString("Campus"),
                        jsonObject.getString("StudentCount")));
            }

            return learngroups;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
