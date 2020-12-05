package DataStructures;

import java.util.*;
import Utility.Static;

public class Slot {

    private int day;
    private int time;
    private int max;
    private int min;

//    public Slot(int day, int time, int max, int min) {
//        this.day = day;
//        this.time = time;
//        this.max = max;
//        this.min = min;
//    }

    public int getDay() {
        return day;
    }

    public int getTime() {
        return time;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public String getDayString() {return Static.days.get(day);}

    public String getTimeString() {return Static.times.get(time);}
}
