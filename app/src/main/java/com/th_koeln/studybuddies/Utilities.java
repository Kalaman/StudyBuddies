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
                learngroups.add(new Learngroup(jsonObject.getString("LID"),
                        jsonObject.getString("Title"),
                        jsonObject.getString("Description"),
                        jsonObject.getString("MeetingtimeFrom"),
                        jsonObject.getString("MeetingtimeTo"),
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

    public static Student parseStudent (String response) {

        try{
            JSONObject jsonObject = new JSONArray(response).getJSONObject(0);

            return new Student(jsonObject.getString("Student"),
                    jsonObject.getInt("Semester"),
                    jsonObject.getInt("StudyProgramID"),
                    jsonObject.getString("Description"),
                    jsonObject.getString("Phone"));

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<MeetingPoint> parseMeetingPoints (JSONArray response) {

        try{
            ArrayList<MeetingPoint> meetingPoints = new ArrayList<>();

            JSONArray jsonArray = response;

            for (int i=0;i<jsonArray.length();++i) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                meetingPoints.add(new MeetingPoint(jsonObject.getInt("CPID"),
                        jsonObject.getInt("MPID"),
                        jsonObject.getString("Campus"),
                        jsonObject.getString("MeetingPoint")));
            }

            return meetingPoints;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    /*
        Finds a learngroup with a given lid out of an array
     */

    public static Learngroup findLearngroup(ArrayList<Learngroup> learngroups, String lid){
        for (Learngroup learngroup : learngroups) {
            if (lid.equals(learngroup.getLid()))
                return learngroup;
        }
        return null;
    }
}
