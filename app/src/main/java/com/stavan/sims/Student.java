package com.stavan.sims;

public class Student {

    private String id;
    private String name;
    private String rollno;
    private String course;

    public Student(String id, String name, String rollno, String course) {
        this.id = id;
        this.name = name;
        this.rollno = rollno;
        this.course = course;
    }

    public Student(String name, String rollno, String course) {
        this.name = name;
        this.rollno = rollno;
        this.course = course;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
