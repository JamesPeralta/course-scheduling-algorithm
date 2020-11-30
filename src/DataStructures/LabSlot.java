package DataStructures;

public class LabSlot {
    private int day;
    private int time;
    private int coursemax;
    private int coursemin;

    public LabSlot(int day, int time, int coursemax, int coursemin) {
        this.day = day;
        this.time = time;
        this.coursemax = coursemax;
        this.coursemin = coursemin;
    }

    public int getCoursemax() {
        return coursemax;
    }

    public int getCoursemin() {
        return coursemin;
    }

    public int getDay() {
        return day;
    }

    public int getTime() {
        return time;
    }
}
