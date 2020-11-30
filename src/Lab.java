public class Lab {
    private Course ofCourse;
    private int tut;

    public Lab(Course c, int tut) {
        this.tut = tut;
        this.ofCourse = c;
    }

    public Course getOfCourse() {
        return ofCourse;
    }

    public int getTut() {
        return tut;
    }
}
