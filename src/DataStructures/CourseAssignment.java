package DataStructures;

public class CourseAssignment {
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
}
