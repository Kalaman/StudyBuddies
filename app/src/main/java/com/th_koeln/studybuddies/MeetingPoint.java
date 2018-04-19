package com.th_koeln.studybuddies;

/**
 * Created by alasdair on 16.04.18.
 */

public class MeetingPoint {
    private int mpid;
    private String name;

    public MeetingPoint(String name,int mpid) {
        this.mpid = mpid;
        this.name = name;
    }

    public int getMpid() {
        return mpid;
    }

    public void setMpid(int mpid) {
        this.mpid = mpid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
