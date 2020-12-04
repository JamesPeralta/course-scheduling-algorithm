package DataStructures;

import java.util.*;

public class Department {
    private String name;
    private ArrayList<CourseInstance> courses = new ArrayList<>();
    private ArrayList<LabSection> labs = new ArrayList<>();
    private ArrayList<LabSlot> labSlots = new ArrayList<>();
    private ArrayList<CourseSlot> courseSlots = new ArrayList<>();
    

    public Department() {}

    public void setName(String name) {
        this.name = name;
    }

    public void addCourse(CourseInstance c) {
        courses.add(c);
    }

    public void addLab(LabSection l) {
        labs.add(l);
    }

    public void addLabSlot(LabSlot ls) {
        labSlots.add(ls);
    }

    public void addCourseSlot(CourseSlot cs) {
        courseSlots.add(cs);
    }

    public ArrayList<CourseInstance> getCourses() {
        ArrayList<CourseInstance> copy = (ArrayList<CourseInstance>) this.courses.clone();
        Collections.shuffle(copy);
        return copy;
    };

    public ArrayList<LabSection> getLabs() {
        ArrayList<LabSection> copy = (ArrayList<LabSection>) this.labs.clone();
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

    public CourseInstance findCourse(CourseInstance c) {
        for(CourseInstance i : courses) {
            if(i.equals(c)) return i;
        }
        return null;
    }

    public LabSection findLab(LabSection c) {
        for(LabSection i : labs) {
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
