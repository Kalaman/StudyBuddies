package com.th_koeln.studybuddies;

/**
 * Created by alasdair on 16.04.18.
 */

public class MeetingPoint {

    int cpid, mpid;
    String campus, meetingpoint;

    public MeetingPoint(int cpid, int mpid, String campus, String meetingpoint) {
        this.cpid = cpid;
        this.mpid = mpid;
        this.campus = campus;
        this.meetingpoint = meetingpoint;
    }

    public int getCpid() {
        return cpid;
    }

    public void setCpid(int cpid) {
        this.cpid = cpid;
    }

    public int getMpid() {
        return mpid;
    }

    public void setMpid(int mpid) {
        this.mpid = mpid;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getMeetingpoint() {
        return meetingpoint;
    }

    public void setMeetingpoint(String meetingpoint) {
        this.meetingpoint = meetingpoint;
    }
}
