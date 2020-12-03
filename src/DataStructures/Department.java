package DataStructures;

import java.util.*;

public class Department {
    private String name;
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<Lab> labs = new ArrayList<>();
    private ArrayList<LabSlot> labSlots = new ArrayList<>();
    private ArrayList<CourseSlot> courseSlots = new ArrayList<>();

    public Department() {}

    public void setName(String name) {
        this.name = name;
    }

    public void addCourse(Course c) {
        courses.add(c);
    }

    public void addLab(Lab l) {
        labs.add(l);
    }

    public void addLabSlot(LabSlot ls) {
        labSlots.add(ls);
    }

    public void addCourseSlot(CourseSlot cs) {
        courseSlots.add(cs);
    }

    public ArrayList<Course> getCourses() {
        ArrayList<Course> copy = (ArrayList<Course>) this.courses.clone();
        Collections.shuffle(copy);
        return copy;
    };

    public ArrayList<Lab> getLabs() {
        ArrayList<Lab> copy = (ArrayList<Lab>) this.labs.clone();
        Collections.shuffle(copy);
        return copy;
    };

    public ArrayList<CourseSlot> getCourseSlots() {
        ArrayList<CourseSlot> copy = (ArrayList<CourseSlot>) this.courseSlots.clone();
        Collections.shuffle(copy);
        return copy;
    };

    public ArrayList<LabSlot> getLabSlots() {
        ArrayList<LabSlot> copy = (ArrayList<LabSlot>) this.labSlots.clone();
        Collections.shuffle(copy);
        return copy;
    };

    public Course findCourse(Course c) {
        for(Course i : courses) {
            if(i.equals(c)) return i;
        }
        return null;
    }

    public Lab findLab(Lab c) {
        for(Lab i : labs) {
            if(i.equals(c)) return i;
        }
        return null;
    }

    public CourseSlot findCourseSlot(int day, int time) {
        for(CourseSlot i : courseSlots) {
            if(i.equalByValue(day, time)) return i;
        }
        return null;
    }

    public LabSlot findLabSlot(int day, int time) {
        for(LabSlot i : labSlots) {
            if(i.equalByValue(day, time)) return i;
        }
        return null;
    }
}
