package com.th_koeln.studybuddies;

/**
 * Created by alasdair on 03.04.18.
 */

class Student {

    private String name;
    private int semester;
    private int studyProgramID;
    private String description;
    private String phone;

    public Student(String name, int semester, int studyProgramID, String description, String phone) {
        this.name = name;
        this.semester = semester;
        this.studyProgramID = studyProgramID;
        this.description = description;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getStudyProgramID() {
        return studyProgramID;
    }

    public void setStudyProgramID(int studyProgramID) {
        this.studyProgramID = studyProgramID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
