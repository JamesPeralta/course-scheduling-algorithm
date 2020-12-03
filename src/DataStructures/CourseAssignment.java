package DataStructures;

import java.util.*;

public class CourseAssignment implements Comparable<CourseAssignment> {
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

    public void assignSlot(CourseSlot slot){
        courseSlot = slot;
    }
    public void unassignSlot(){
        courseSlot = null;
    }

    @Override
    public int compareTo(CourseAssignment o) {
        return o.getCourse().getCourseName().compareTo(this.course.getCourseName());
    }
}
