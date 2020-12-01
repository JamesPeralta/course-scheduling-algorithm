package DataStructures;

public class Course {
    private String course;
    private int section;

    public Course(String course, int section) {
        this.course = course;
        this.section = section;
    }

    public String getCourseName() {
        return course;
    }

    public int getSectionNumber() {
        return section;
    }

    public String getSectionString(){
        return "LEC " + Integer.toString(section);
    }
}
