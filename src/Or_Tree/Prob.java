package Or_Tree;
import DataStructures.*;
import java.util.ArrayList;

public class Prob {
    private ArrayList<CourseAssignment> unassignedCourses;
    private ArrayList<LabAssignment> unassignedLabs;
    private ArrayList<CourseAssignment> assignedCourses;
    private ArrayList<LabAssignment> assignedLabs;
    private ArrayList<CourseSlot> courseSlotsLeft;
    private ArrayList<LabSlot> labSlotsLeft;

    public Prob(Department department) {
        this.unassignedCourses = new ArrayList<CourseAssignment>();
        this.unassignedLabs = new ArrayList<LabAssignment>();
        this.assignedCourses = new ArrayList<CourseAssignment>();
        this.assignedLabs = new ArrayList<LabAssignment>();
        this.courseSlotsLeft = new ArrayList<CourseSlot>();
        this.labSlotsLeft = new ArrayList<LabSlot>();

        setUnassignedCourses(department.getCourses());
        setUnassignedLabs(department.getLabs());
    }

    private void setUnassignedCourses(ArrayList<Course> courses) {
        for (Course course: courses) {
            this.unassignedCourses.add(new CourseAssignment(course));
        }
    }

    private void setUnassignedLabs(ArrayList<Lab> labs) {
        for (Lab lab: labs) {
            this.unassignedLabs.add(new LabAssignment(lab));
        }
    }

    private void setCourseSlots(ArrayList<CourseSlot> courseSlots) {
        for (CourseSlot courseSlot: courseSlots) {
            this.courseSlotsLeft.add(courseSlot);
        }
    }

    private void setLabSlots(ArrayList<LabSlot> labSlots) {
        for (LabSlot labSlot: labSlots) {
            this.labSlotsLeft.add(labSlot);
        }
    }
}
