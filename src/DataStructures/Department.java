package DataStructures;

import java.util.*;

public class Department {
    private String name;
    private ArrayList<CourseInstance> coursesSections = new ArrayList<>();
    private ArrayList<LabSection> labSections = new ArrayList<>();
    private ArrayList<LabSlot> labSlots = new ArrayList<>();
    private ArrayList<CourseSlot> courseSlots = new ArrayList<>();
    private HashMap<String,ArrayList<CourseInstance>> courses = new HashMap<String,ArrayList<CourseInstance>>();
    private HashMap<String,ArrayList<LabSection>> labs = new HashMap<String,ArrayList<LabSection>>();

    private HashMap<LabSection,LabSlot> assignedLabs = new HashMap<>();
    private HashMap<CourseInstance,CourseSlot> assignedCourses = new HashMap<>();

    public Department() {}

    public void setName(String name) {
        this.name = name;
    }

    public void addCourse(CourseInstance c) {
    	// add the course section 
        coursesSections.add(c);
        
        // add the class 
        
        if(courses.containsKey(c.getCourseName())) {
        	courses.get(c.getCourseName()).add(c);
        }else {
        	courses.put(c.getCourseName(), new ArrayList<CourseInstance>());
        	courses.get(c.getCourseName()).add(c);
        }
    }

    public void addLab(LabSection l) {
        labSections.add(l);
        
        
        // make sure to add it to the lab map 
        if(labs.containsKey(l.getFullTutName())) {
        	labs.get(l.getFullTutName()).add(l);
        }else {
        	labs.put(l.getFullTutName(), new ArrayList<LabSection>());
        	labs.get(l.getFullTutName()).add(l);
        }
    }

    public void addLabSlot(LabSlot ls) {
        labSlots.add(ls);
    }

    public void addCourseSlot(CourseSlot cs) {
        courseSlots.add(cs);
    }

    public void partAssignCourse(CourseInstance ca, CourseSlot cs) {
        assignedCourses.put(ca, cs);
    }

    public void partAssignLab(LabSection la, LabSlot ls) {
        assignedLabs.put(la, ls);
    }

    public ArrayList<CourseInstance> getCourses() {
        ArrayList<CourseInstance> copy = (ArrayList<CourseInstance>) this.coursesSections.clone();
        Collections.shuffle(copy);
        return copy;
    };

    public ArrayList<LabSection> getLabs() {
        ArrayList<LabSection> copy = (ArrayList<LabSection>) this.labSections.clone();
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
        for(CourseInstance i : coursesSections) {
            if(i.equals(c)) return i;
        }
        return null;
    }

    public LabSection findLab(LabSection c) {
        for(LabSection i : labSections) {
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
    public HashMap<String,ArrayList<CourseInstance>> getCourseMap() {
    	return this.courses;
    }
    public HashMap<String,ArrayList<LabSection>>  getLabMap(){
    	return this.labs;
    }

    public HashMap<LabSection,LabSlot> getAssignedLabs() {
        return assignedLabs;
    }

    public HashMap<CourseInstance,CourseSlot> getAssignedCourses() {
        return assignedCourses;
    }
}
