package DataStructures;

public class Lab {
    private String ofCourse;
    private String ofSection;
    private int tut;

    public Lab(String course, String section, int tut) {
        this.ofCourse = course;
        this.ofSection = section;
        this.tut = tut;
    }

    public Lab(String course, int tut) {
        this.ofCourse = course;
        this.tut = tut;
    }

}
