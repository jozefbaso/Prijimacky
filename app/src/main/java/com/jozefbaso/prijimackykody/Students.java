package com.jozefbaso.prijimackykody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Students {
    public Map<String, Student> Students;

    public Students() {
        this.Students =  new LinkedHashMap<>();
    }

    public void addStudent(Student student){
        Students.put(student.getCode(), student);
    }

    //zda sa mi toto ako zbytocna metoda, da sa ist priamo cez Studenta...
    public void addPoints(String code, double points, Student.Subject currentSubject){
            Objects.requireNonNull(Students.get(code)).setPoints(points, currentSubject);
    }

}