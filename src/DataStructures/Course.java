package DataStructures;

import java.util.*;

public class Course extends ClassElement {
    private String course;
    private int section;    
    private HashMap<CourseSlot,Integer> preferences = new HashMap<>();
    private ArrayList<CourseSlot> unwanted = new ArrayList<>();


    public Course(String course, int section) {
        this.course = course;
        this.section = section;
    }

    public String getCourseName() {
        return course;
    }

    public int getSectionNumber() {
        return section;
    }

    public String getSectionString() {
        return "LEC " + Integer.toString(section);
    }

    public String toString() {
        return course + " " + section;
    }

    public boolean equals(Course c) {
        return this.course.equals(c.getCourseName()) && (this.section == c.getSectionNumber());
    }

    public void addUnwanted(CourseSlot c) {
        this.unwanted.add(c);
    }

    public void addPreference(CourseSlot ls, int value) {
        preferences.put(ls, value);
    }

}
