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

    public boolean equals(Course c) {
        return this.course.equals(c.getCourseName()) && (this.section == c.getSectionNumber());
    }

    public void addUnwanted(CourseSlot c) {
        this.unwanted.add(c);
    }

    public void addPreference(CourseSlot cs, int value) {
        if(cs != null) preferences.put(cs, value);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(course).append(" ").append(getSectionString());

        str.append("\n\tPreferred Slots:");

        preferences.forEach((k,v) -> {
            str.append("\n\t\t");
            str.append(k.toStringMin() + " ");
            str.append(v);
        });

        str.append("\n\tUnwanted Slots:");

        for (CourseSlot courseSlot : unwanted) {
            str.append("\n\t\t");
            str.append(courseSlot.toStringMin());
        }

        return str.toString();
    }
}
