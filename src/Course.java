public class Course {
    private String subject;
    private int course;
    private int section;

    public Course(String subject, int course, int section) {
        this.subject = subject;
        this.course = course;
        this.section = section;
    }

    public Course(String subject, int course) {
        this.subject = subject;
        this.course = course;
    }
}
