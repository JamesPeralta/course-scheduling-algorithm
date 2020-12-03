package DataStructures;

import java.util.ArrayList;
import java.util.Arrays;

public class LabSlot extends Slot{
    public static ArrayList<String> days = new ArrayList<>(Arrays.asList(new String[]{"MO", "TU", "FR"}));
    public static ArrayList<String> times = new ArrayList<>(Arrays.asList(new String[]{
            "8:00", "9:00", "9:30", "10:00", "11:00", "12:00", "12:30", "13:00", "14:00", "15:00", "15:30", "16:00",
            "17:00", "18:00", "18:30", "19:00", "20:00"}));

    private int day;
    private int time;
    private int coursemax;
    private int coursemin;
    private int assigned;

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

    public int getAssigned() {
        return assigned;
    }

    public String getDayString() {return days.get(day);}

    public String getTimeString() {return times.get(time);}

    public boolean equalByValue(int day, int time) {
        return this.day == day && this.time == time;
    }

    @Override
    public String toString() {
        return toStringMin() + ", " + coursemax + ", " + coursemin;
    }

    public String toStringMin() {
        return days.get(day) + ", " + times.get(time);
    }
}
