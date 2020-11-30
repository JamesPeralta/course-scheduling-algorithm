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

    public ArrayList<Course> getCourses() {return courses; };

    public ArrayList<Lab> getLabs() {return labs; };

    public ArrayList<CourseSlot> getCourseSlots() {return courseSlots; };

    public ArrayList<LabSlot> getLabSlots() {return labSlots; };
}
