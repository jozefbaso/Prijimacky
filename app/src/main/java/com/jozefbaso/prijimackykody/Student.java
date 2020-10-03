package com.jozefbaso.prijimackykody;

import androidx.annotation.NonNull;

public class Student {
    enum Subject {SJL, MAT}
    private String firstName;
    private String lastName;
    private String code;
    private double pointsSjl;
    private double pointsMat;

    public Student(String firstName, String lastName, String code) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.code = code;
        this.pointsMat = -1;
        this.pointsSjl = -1;
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
//TODO vymazat zbytocne metody
    public double getPointsSjl() {
        return pointsSjl;
    }
    public double getPointsMat() {
        return pointsMat;
    }
    public void setPointsSjl(double pointsSjl) {
        this.pointsSjl = pointsSjl;
    }
    public void setPointsMat(double pointsMat) {
        this.pointsMat = pointsMat;
    }

    public void setPoints(double points, Subject subject){
        if(subject == Subject.SJL) this.pointsSjl = points;
        else if(subject == Subject.MAT) this.pointsMat = points;
    }

    @NonNull
    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " " + this.code + " " + this.pointsSjl + " " + this.pointsMat;
    }
}

