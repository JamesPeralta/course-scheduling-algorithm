package DataStructures;

import java.util.*;
import Utility.Static;

public class CourseSlot extends Slot{

    private int day;
    private int time;
    private int coursemax;
    private int coursemin;
    private int assigned = 0;

    public CourseSlot(int day, int time, int coursemax, int coursemin) {
        this.day = day;
        this.time = time;
        this.coursemax = coursemax;
        this.coursemin = coursemin;
    }

    public int getDay() {
        return day;
    }

    public int getTime() {
        return time;
    }

    public int getCoursemax() {
        return coursemax;
    }

    public int getCoursemin() {
        return coursemin;
    }

    public int getAssigned() {
        return assigned;
    }

    public String getDayString() {return Static.days.get(day);}

    public String getTimeString() {return Static.times.get(time);}

    public boolean equalByValue(int day, int time) {
        return this.day == day && this.time == time;
    }

    @Override
    public String toString() {
        return toStringMin() + ", " + coursemax + ", " + coursemin;
    }

    public String toStringMin() {
        return Static.days.get(day) + ", " + Static.times.get(time);
    }
}
