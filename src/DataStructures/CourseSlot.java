package DataStructures;

import java.util.*;
import Utility.Static;

public class CourseSlot extends Slot{

    private int day;
    private int start_time;
    private int end_time;
    private int coursemax;
    private int coursemin;
    private int assigned = 0;

    public CourseSlot(int day, int time, int coursemax, int coursemin) {
        this.day = day;
        this.start_time = time;
        this.end_time = Static.courseEndTimes[day][time];
        this.coursemax = coursemax;
        this.coursemin = coursemin;
    }

    public int getDay() {
        return day;
    }

    public int getTime() {
        return start_time;
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
