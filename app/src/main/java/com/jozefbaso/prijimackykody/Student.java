package com.jozefbaso.prijimackykody;

import androidx.annotation.NonNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class Student {
    enum Subject {SJL, MAT}

    private String firstName;
    private String lastName;
    private String code;
    private Map<Subject, String> points;


    public Student(String firstName, String lastName, String code, String sjl, String mat) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.code = code;
        this.points = new LinkedHashMap<>();
        this.points.put(Subject.SJL, sjl);
        this.points.put(Subject.MAT, mat);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCode() {
        return code;
    }

    public String getPoints(Subject subject) {
        return this.points.get(subject);
    }

    public void setPoints(String points, Subject subject) {
        this.points.put(subject, points);
    }

    @NonNull
    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " " + this.code + " " + this.points.get(Subject.SJL) + " " + this.points.get(Subject.MAT);
    }
}

