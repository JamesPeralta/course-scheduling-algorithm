package Or_Tree;
import DataStructures.*;
import java.util.ArrayList;
import java.util.Collections;

public class Prob {
    private ArrayList<CourseAssignment> courses;
    private ArrayList<LabAssignment> labs;

    public Prob(Department department) {
        this.courses = new ArrayList<CourseAssignment>();
        this.labs = new ArrayList<LabAssignment>();

        setUnassignedCourses(department.getCourses());
        setUnassignedLabs(department.getLabs());
    }

    private void setUnassignedCourses(ArrayList<Course> courses) {
        for (Course course: courses) {
            this.courses.add(new CourseAssignment(course));
        }
    }

    private void setUnassignedLabs(ArrayList<Lab> labs) {
        for (Lab lab: labs) {
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
        if (courses.get(courses.size()-1).getCourseSlot() == null) {
            return false;
        }
        return true;
    }

    public Boolean labsFilled() {
        if (labs.get(labs.size()-1).getLabSlot() == null) {
            return false;
        }
        return true;
    }

    public String toString(){
        Collections.sort(courses);
        Collections.sort(labs);
        String output = "";
        output += "________________________________\n";
        for (CourseAssignment course: this.courses) {
            String courseName = course.getCourse().getCourseName();
            String courseNumber = course.getCourse().getSectionString();
            String assignmentDay = course.getCourseSlot() != null ? course.getCourseSlot().getDayString() : "None";
            String assignmentTime = course.getCourseSlot() != null ? course.getCourseSlot().getTimeString() : "None";

            if (assignmentDay.equals("None")) {
                output += courseName + " " + courseNumber + ": " + assignmentDay + "\n";
            }
            else {
                output += courseName + " " + courseNumber + ": " + assignmentDay + " " + assignmentTime + "\n";
            }
        }

        for (LabAssignment lab: this.labs) {
            String labCourse = lab.getLab().getOfCourse();
            String labSection = lab.getLab().getOfSection();
            String labNumber = lab.getLab().getTutString();
            String assignmentDay = lab.getLabSlot() != null ? lab.getLabSlot().getDayString() : "None";
            String assignmentTime = lab.getLabSlot() != null ? lab.getLabSlot().getTimeString() : "None";

            String labString = "";
            if (labSection == null) {
                labString = labCourse + " " + labNumber;
            }
            else {
                labString = labCourse + " " + labSection + " " + labNumber;
            }

            if (assignmentDay.equals("None")) {
                output += labString + ": " + assignmentDay + "\n";
            }
            else {
                output += labString + ": " + assignmentDay + " " + assignmentTime + "\n";
            }
        }
        output += "________________________________\n";
        return output;
    }
}
