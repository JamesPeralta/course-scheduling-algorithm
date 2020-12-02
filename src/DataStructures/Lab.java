package DataStructures;

import java.util.*;

public class Lab extends ClassElement {
    private String ofCourse;
    private String ofSection;
    private int tut;
    private HashMap<LabSlot,Integer> preferences = new HashMap<>();

    public Lab(String course, String section, int tut) {
        this.ofCourse = course;
        this.ofSection = section;
        this.tut = tut;
    }

    public Lab(String course, int tut) {
        this.ofCourse = course;
        this.tut = tut;
    }

    public String getOfCourse() {
        return ofCourse;
    }

    public String getOfSection() {
        return ofSection;
    }

    public int getTut() {
        return tut;
    }

    public String getTutString() {
        return "TUT " + Integer.toString(tut);
    }

    public boolean equals(Lab l) {
        return this.ofCourse.equals(l.getOfCourse()) && this.ofSection.equals(l.getOfSection()) && (tut == l.getTut());
    }
}
