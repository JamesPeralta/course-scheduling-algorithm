package Utility;

import java.util.*;

public class Static {
    public static ArrayList<String> days = new ArrayList<>(Arrays.asList(new String[]{"MO", "TU", "FR"}));
    public static ArrayList<String> times = new ArrayList<>(Arrays.asList(new String[]{
            "8:00", "9:00", "9:30", "10:00", "11:00", "12:00", "12:30", "13:00", "14:00", "15:00", "15:30", "16:00", 
            "17:00", "18:00", "18:30", "19:00", "20:00", "21:00"}));

    public static int[][] labEndTimes = new int[][] {
        {1, 3, -1, 4, 5, 7, -1, 8, 9, 11, -1, 12, 13, 15, -1, 16, 17, -1},
        {1, 3, -1, 4, 5, 7, -1, 8, 9, 11, -1, 12, 13, 15, -1, 16, 17, -1},
        {3, -1, -1, 5, -1, 8, -1, -1, 11, -1, -1, 13, -1, 16, -1, -1, -1, -1}
    };

    public static int[][] courseEndTimes = new int[][] {
        {1, 3, -1, 4, 5, 7, -1, 8, 9, 11, -1, 12, 13, 15, -1, 16, 17, -1},
        {2, -1, 4, -1, 6, -1, 8, -1, 10, -1, 12, -1, 14, -1, 16, -1, -1, -1},
    };

    public static void printAllCourseSlots() {
        for(int i = 0; i < courseEndTimes.length; i++) {
            System.out.println(days.get(i));
            for(int j = 0; j < courseEndTimes[i].length; j++) {
                if(courseEndTimes[i][j] != -1) System.out.print(times.get(j) + "-" + times.get(courseEndTimes[i][j]) + ", ");
            }
            System.out.println();
        }
    }

    public static void printAllLabSlots() {
        for(int i = 0; i < labEndTimes.length; i++) {
            System.out.println(days.get(i));
            for(int j = 0; j < labEndTimes[i].length; j++) {
                if(labEndTimes[i][j] != -1) System.out.print(times.get(j) + "-" + times.get(labEndTimes[i][j]) + ", ");
            }
            System.out.println();
        }
    }


}
