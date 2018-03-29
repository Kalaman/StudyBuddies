package com.th_koeln.studybuddies;

/**
 * Created by Kalaman on 29.03.2018.
 */

public class Course {
    String name;
    int cid;

    public Course(String name, int cid)
    {
        this.name = name;
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public int getCid() {
        return cid;
    }

    @Override
    public String toString() {
        return name;
    }
}
