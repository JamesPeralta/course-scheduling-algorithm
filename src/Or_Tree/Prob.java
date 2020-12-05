package Or_Tree;
import DataStructures.*;
import Utility.Constr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Prob implements Comparable<Prob>{
    private ArrayList<CourseAssignment> courses;
    private ArrayList<LabAssignment> labs;
    private double fitness;

    public Prob(Department department) {
        this.courses = new ArrayList<CourseAssignment>();
        this.labs = new ArrayList<LabAssignment>();
        this.fitness = 0;

        setUnassignedCourses(department.getCourses());
        setUnassignedLabs(department.getLabs());
    }

    /*
    Getters and Setters
     */
    public HashMap<CourseInstance, CourseSlot> getCourseAssignments() {
        HashMap<CourseInstance, CourseSlot> assignmentMap = new HashMap<>();
        for (CourseAssignment assignment: courses) {
            if (assignment.getCourseSlot() != null) {
                assignmentMap.put(assignment.getCourse(), assignment.getCourseSlot());
            }
        }

        return assignmentMap;
    }

    public HashMap<LabSection, LabSlot> getLabAssignments() {
        HashMap<LabSection, LabSlot> assignmentMap = new HashMap<>();
        for (LabAssignment assignment: labs) {
            if (assignment.getLabSlot() != null) {
                assignmentMap.put(assignment.getLab(), assignment.getLabSlot());
            }
        }

        return assignmentMap;
    }

    public CourseAssignment getCourse(int index) {
        return courses.get(index);
    }

    public LabAssignment getLab(int index) {
        return labs.get(index);
    }

    public void setFitness(double fitness){
        this.fitness = fitness;
    }

    public double getFitness(){
        return this.fitness;
    }

    private void setUnassignedCourses(ArrayList<CourseInstance> courses) {
        for (CourseInstance course: courses) {
            this.courses.add(new CourseAssignment(course));
        }
    }

    private void setUnassignedLabs(ArrayList<LabSection> labs) {
        for (LabSection lab: labs) {
            this.labs.add(new LabAssignment(lab));
        }
    }

    public void assignCourse(int index, CourseSlot slot) {
        courses.get(index).assignSlot(slot);
    }

    public ArrayList<CourseAssignment> getCourses() {
        return courses;
    }

    public ArrayList<LabAssignment> getLabs() {
        return labs;
    }

    public void unassignCourse(int index){
        courses.get(index).unassignSlot();
    }

    public void assignLab(int index, LabSlot slot) {
        labs.get(index).assignSlot(slot);
    }

    public void unassignLab(int index){
        labs.get(index).unassignSlot();
    }

    public Boolean coursesFilled() {
        int coursesFilledCount = 0;
        for (CourseAssignment courseAssignment: courses) {
            if (courseAssignment.getCourseSlot() != null) {
                coursesFilledCount += 1;
            }
        }

        if (coursesFilledCount == courses.size()) {
            return true;
        }
        else {
            return false;
        }
    }

    public Boolean labsFilled() {
        int labsFilledCount = 0;
        for (LabAssignment labAssignment: labs) {
            if (labAssignment.getLabSlot() != null) {
                labsFilledCount += 1;
            }
        }

        if (labsFilledCount == labs.size()) {
            return true;
        }
        else {
            return false;
        }
    }

    /*
    Genetic Operators
    */
    public Prob crossover(Prob mate, Department department) {
        Prob child = new Prob(department);
        this.orderProb();
        mate.orderProb();
        child.orderProb();

        // Crossover Course Slots
        for (int i = 0; i < courses.size(); i++) {
            if (i < (courses.size() / 2)) {
                child.assignCourse(i, this.courses.get(i).getCourseSlot());
            }
            else {
                child.assignCourse(i, mate.courses.get(i).getCourseSlot());
            }
        }

        // Crossover Lab Slots
        for (int i = 0; i < labs.size(); i++) {
            if (i < (courses.size() / 2)) {
                child.assignLab(i, this.labs.get(i).getLabSlot());
            }
            else {
                child.assignLab(i, mate.labs.get(i).getLabSlot());
            }
        }

        return child;
    }

    public void mutate(Department department) {
        Random rand = new Random();
        ArrayList<CourseSlot> courseSlots = department.getCourseSlots();
        ArrayList<LabSlot> labSlots = department.getLabSlots();

        // Mutate Course Slots
        for (int i = 0; i < courses.size(); i++) {
            if (rand.nextDouble() < 0.07) {
                this.assignCourse(i, courseSlots.get(rand.nextInt(courseSlots.size())));
            }
        }

        // Mutate Lab Slots
        for (int i = 0; i < labs.size(); i++) {
            if (rand.nextDouble() < 0.07) {
                this.assignLab(i, labSlots.get(rand.nextInt(labSlots.size())));
            }
        }
    }

    public void orderProb() {
        Collections.sort(courses);
        Collections.sort(labs);
    }

    public String toString(){
        orderProb();
        String output = "";
        output += "________________________________\n";
        Constr constr = new Constr(this);
        output += "Valid: " + constr.isValid() + "\n";
        output += "Fitness: " + Double.toString(fitness) + "\n";
        for (CourseAssignment course: this.courses) {
            String semiCourseName = course.getCourse().getCourseName();
            String courseSection = course.getCourse().getSectionString();
            String courseName = course.getCourse().getFullCourseName();
            String courseAssignment = course.getAssignmentAsString();
            output += courseName + " : " + courseAssignment + "\n";
            for (LabAssignment lab: this.labs) {
                if (lab.getLab().getOfCourse().equals(semiCourseName)) {
                    String labSection = lab.getLab().getOfSection();
                    if (labSection.equals("") || labSection.equals(courseSection)) {
                        String labName = lab.getLab().getFullTutName();
                        String labAssignment = lab.getAssignmentAsString();
                        output += labName + " : " + labAssignment + "\n";
                    }
                }
            }
        }
        output += "________________________________\n";
        return output;
    }

    @Override
    public int compareTo(Prob o) {
        return (int) (this.fitness - o.fitness);
    }
}
