import DataStructures.*;

import java.util.*;
import java.io.*;

public class Parser {
    public static ArrayList<String> days = new ArrayList<>(Arrays.asList(new String[]{"MO", "TU", "FR"}));
    public static ArrayList<String> times = new ArrayList<>(Arrays.asList(new String[]{
        "8:00", "9:00", "9:30", "10:00", "11:00", "12:00", "12:30", "13:00", "14:00", "15:00", "15:30", "16:00", 
        "17:00", "18:00", "18:30", "19:00", "20:00"}));


    private static void parseSlots(BufferedReader reader, Department department, boolean isCourse) throws IOException {
        String line = reader.readLine();
        while(!line.equals("")) {
            String[] row = line.split(",");
            cleanRow(row);
            if(isCourse) department.addCourseSlot(new CourseSlot(days.indexOf(row[0]), times.indexOf(row[1]), 
                                                    Integer.parseInt(row[2]), Integer.parseInt(row[3])));
            else department.addLabSlot(new LabSlot(days.indexOf(row[0]), times.indexOf(row[1]), 
                                                    Integer.parseInt(row[2]), Integer.parseInt(row[3])));
            line = reader.readLine();
        }
    }

    private static Course parseCourse(String str) {
        String[] row = str.split("\\s+");
        cleanRow(row);
        if(row.length > 2) return new Course(row[0] + " " + row[1], Integer.parseInt(row[3]));
        else return new Course(row[0], Integer.parseInt(row[1]));
    }

    private static Lab parseLab(String str) {
        String[] row = str.split("\\s+");
        cleanRow(row);
        if (row.length == 6) {
            String course = row[0] + " " + row[1];
            String section = row[2] + " " + row[3];
            String number = row[5];
            return new Lab(course, section, Integer.parseInt(number));
        }
        else {
            String course = row[0] + " " + row[1];
            String number = row[3];
            return new Lab(course, Integer.parseInt(number));
        }
    }

    private static void parseCourses(BufferedReader reader, Department department) throws IOException{
        String line = reader.readLine();
        while(line.length() > 0) {
            department.addCourse(parseCourse(line));
            line = reader.readLine();
        }
    }

    private static void parseLabs(BufferedReader reader, Department department) throws IOException{
        String line = reader.readLine();
        while(line.length() > 0) {
            department.addLab(parseLab(line));
            line = reader.readLine();
        }
    }
    
    private static void parseCompatibility(BufferedReader reader, Department department) throws IOException {
        String line = reader.readLine();
        while(line.length() > 0) {
            String[] row = line.split(",");
            ClassElement a,b;
            cleanRow(row);

            if(row[0].contains("TUT") || row[0].contains("LAB")) a = department.findLab(parseLab(row[0]));
            else a = department.findCourse(parseCourse(row[0]));

            if(row[1].contains("TUT") || row[1].contains("LAB")) b = department.findLab(parseLab(row[1]));
            else b = department.findCourse(parseCourse(row[1]));

            a.addNonCompatible(b);
            b.addNonCompatible(a);

            line = reader.readLine();
        }
    }

    private static void parseUnwanted(BufferedReader reader, Department department) throws IOException {
        String line = reader.readLine();
        while(line.length() > 0) {
            String[] row = line.split(",");
            cleanRow(row);

            if(row[0].contains("TUT") || row[0].contains("LAB")) {
                department.findLab(parseLab(row[0])).addUnwanted(department.findLabSlot(days.indexOf(row[1]), times.indexOf(row[2])));
            } else department.findCourse(parseCourse(row[0])).addUnwanted(department.findCourseSlot(days.indexOf(row[1]), times.indexOf(row[2])));

            line = reader.readLine();
        }
    }

    private static void parsePreferences(BufferedReader reader, Department department) throws IOException {
        String line = reader.readLine();
        while(line.length() > 0) {
            String[] row = line.split(",");
            cleanRow(row);

            if(row[2].contains("TUT") || row[2].contains("LAB")) {
                department.findLab(parseLab(row[2])).addPreference(
                    department.findLabSlot(days.indexOf(row[0]), times.indexOf(row[1])), Integer.parseInt(row[3]));
            } else department.findCourse(parseCourse(row[2])).addPreference(
                    department.findCourseSlot(days.indexOf(row[0]), times.indexOf(row[1])), Integer.parseInt(row[3]));
            line = reader.readLine();
        }
    }

    private static void parsePairs(BufferedReader reader, Department department) throws IOException {
        String line = reader.readLine();
        while(line.length() > 0) {
            String[] row = line.split(",");
            ClassElement a,b;
            cleanRow(row);

            if(row[0].contains("TUT") || row[0].contains("LAB")) a = department.findLab(parseLab(row[0]));
            else a = department.findCourse(parseCourse(row[0]));

            if(row[1].contains("TUT") || row[1].contains("LAB")) b = department.findLab(parseLab(row[1]));
            else b = department.findCourse(parseCourse(row[1]));

            a.addCompatible(b);
            b.addCompatible(a);

            line = reader.readLine();
        }
    }

    public static Department parse(String pathToFile) {
        Department department = new Department();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToFile));
            String line = reader.readLine();
            while(line != null) {
                line = line.trim();
                if(line.equals("Name:")) {
                    department.setName(reader.readLine());
                }
                else if (line.equals("Course slots:")){
                    parseSlots(reader, department, true);
                }
                else if (line.equals("Lab slots:")) {
                    parseSlots(reader, department, false);
                }
                if(line.equals("Courses:")) {
                    parseCourses(reader, department);
                }
                if(line.equals("Labs:")) {
                    parseLabs(reader, department);
                }
                if(line.equals("Not compatible:")) {
                    parseCompatibility(reader, department);
                }
                if(line.equals("Unwanted:")) {
                    parseUnwanted(reader, department);
                }
                if(line.equals("Preferences:")) {
                    parsePreferences(reader, department);
                }
                if(line.equals("Pair:")) {
                    parsePairs(reader, department);
                }

                line = reader.readLine();
            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("Error reading input file.");
        }

        return department;
    }

    public static void cleanRow(String[] rowArr) {
        for (int i = 0; i < rowArr.length; i++) {
            rowArr[i] = rowArr[i].trim();
        }
    }
}
