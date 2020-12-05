package DataStructures;

import java.util.ArrayList;
import java.util.Arrays;
import Utility.Static;

public class LabSlot extends Slot{

    private int day;
    private int start_time;
    private int coursemax;
    private int coursemin;
    private int assigned;

    public LabSlot(int day, int time, int coursemax, int coursemin) {
        this.day = day;
        this.start_time = time;
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
        return start_time;
    }

    public int getAssigned() {
        return assigned;
    }

    public String getDayString() {return Static.days.get(day);}

    public String getTimeString() {return Static.times.get(start_time);}

    public boolean equalByValue(int day, int time) {
        return this.day == day && this.start_time == time;
    }

    @Override
    public String toString() {
        return toStringMin() + ", " + coursemax + ", " + coursemin;
    }

    public String toStringMin() {
        return Static.days.get(day) + ", " + Static.times.get(start_time);
    }
}
