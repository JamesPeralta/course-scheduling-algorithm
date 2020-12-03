package DataStructures;

import java.util.ArrayList;
import java.util.Arrays;

public class Slot {
    public static ArrayList<String> days = new ArrayList<>(Arrays.asList(new String[]{"MO", "TU", "FR"}));
    public static ArrayList<String> times = new ArrayList<>(Arrays.asList(new String[]{
            "8:00", "9:00", "9:30", "10:00", "11:00", "12:00", "12:30", "13:00", "14:00", "15:00", "15:30", "16:00",
            "17:00", "18:00", "18:30", "19:00", "20:00"}));

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

    public String getDayString() {return days.get(day);}

    public String getTimeString() {return times.get(time);}
}
