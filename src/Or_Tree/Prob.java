package Or_Tree;
import DataStructures.*;
import java.util.ArrayList;

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

    public String toString(){
        String output = "";
        output += "________________________________\n";
        for (CourseAssignment course: this.courses) {
            String courseName = course.getCourse().getCourseName();
            String courseNumber = course.getCourse().getSectionString();
            String assignmentDay = course.getCourseSlot() != null ? Integer.toString(course.getCourseSlot().getDay()) : "None";
            String assignmentTime = course.getCourseSlot() != null ? Integer.toString(course.getCourseSlot().getTime()) : "None";

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
            String assignmentDay = lab.getLabSlot() != null ? Integer.toString(lab.getLabSlot().getDay()) : "None";
            String assignmentTime = lab.getLabSlot() != null ? Integer.toString(lab.getLabSlot().getTime()) : "None";

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
