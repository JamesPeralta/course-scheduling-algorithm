package DataStructures;

import java.util.*;

public class CourseAssignment implements Comparable<CourseAssignment>, Assigment {
    private Course course;
    private CourseSlot courseSlot;
    

    public CourseAssignment(Course course) {
        this.course = course;
        this.courseSlot = null;
    }

    public Course getCourse() {
        return course;
    }

    public CourseSlot getCourseSlot() {
        return courseSlot;
    }
    
    

    public String getAssignmentAsString() {
        String assignmentDay = getCourseSlot() != null ? getCourseSlot().getDayString() : "None";
        String assignmentTime = getCourseSlot() != null ? getCourseSlot().getTimeString() : "None";

        if (assignmentDay.equals("None")) {
            return assignmentDay;
        }
        else {
            return assignmentDay + ", " + assignmentTime;
        }
    }

    public void assignSlot(CourseSlot slot){
        courseSlot = slot;
    }
    public void unassignSlot(){
        courseSlot = null;
    }

    @Override
    public int compareTo(CourseAssignment o) {
        return this.course.getFullCourseName().compareTo(o.getCourse().getFullCourseName());
    }
    @Override
	public Slot getCurrentSlot() {
		// need to return the Slot Object so we can compare it 
		return (Slot) this.courseSlot;
	}
}
