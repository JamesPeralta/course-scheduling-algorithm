import DataStructures.*;

import java.util.*;
import java.io.*;
import Utility.Static;

public class Parser {

    private static void parseSlots(BufferedReader reader, Department department, boolean isCourse) throws IOException {
        String line = reader.readLine();
        while(line.trim().length() > 0) {
            String[] row = line.split(",");
            cleanRow(row);
            if(isCourse) department.addCourseSlot(new CourseSlot(Static.days.indexOf(row[0]), Static.times.indexOf(row[1]), 
                                                    Integer.parseInt(row[2]), Integer.parseInt(row[3])));
            else department.addLabSlot(new LabSlot(Static.days.indexOf(row[0]), Static.times.indexOf(row[1]), 
                                                    Integer.parseInt(row[2]), Integer.parseInt(row[3])));
            line = reader.readLine();
        }
    }

    private static CourseInstance parseCourse(String str) {
        String[] row = str.split("\\s+");
        cleanRow(row);
        if(row.length > 2) return new CourseInstance(row[0] + " " + row[1], Integer.parseInt(row[3]));
        else return new CourseInstance(row[0], Integer.parseInt(row[1]));
    }

    private static LabSection parseLab(String str) {
        String[] row = str.split("\\s+");
        cleanRow(row);
        if (row.length == 6) {
            String course = row[0] + " " + row[1];
            String section = row[2] + " " + row[3];
            String number = row[5];
            return new LabSection(course, section, Integer.parseInt(number));
        }
        else {
            String course = row[0] + " " + row[1];
            String number = row[3];
            return new LabSection(course, Integer.parseInt(number));
        }
    }

    private static void parseCourses(BufferedReader reader, Department department) throws IOException{
        String line = reader.readLine();
        while(line.trim().length() > 0) {
            department.addCourse(parseCourse(line));
            line = reader.readLine();
        }
    }

    private static void parseLabs(BufferedReader reader, Department department) throws IOException{
        String line = reader.readLine();
        while(line.trim().length() > 0) {
            department.addLab(parseLab(line));
            line = reader.readLine();
        }
    }
    
    private static void parseCompatibility(BufferedReader reader, Department department) throws IOException {
        String line = reader.readLine();
        while(line.trim().length() > 0) {
            String[] row = line.split(",");
            ClassElement a,b;
            cleanRow(row);

            if(isTutorial(row[0])) a = department.findLab(parseLab(row[0]));
            else a = department.findCourse(parseCourse(row[0]));

            if(isTutorial(row[1])) b = department.findLab(parseLab(row[1]));
            else b = department.findCourse(parseCourse(row[1]));

            a.addNonCompatible(b);
            b.addNonCompatible(a);

            line = reader.readLine();
        }
    }

    private static void parseUnwanted(BufferedReader reader, Department department) throws IOException {
        String line = reader.readLine();
        while(line.trim().length() > 0) {
            String[] row = line.split(",");
            cleanRow(row);

            if(isTutorial(row[0])) {
                department.findLab(parseLab(row[0])).addUnwanted(
                    department.findLabSlot(Static.days.indexOf(row[1]), Static.times.indexOf(row[2])));
            } else department.findCourse(parseCourse(row[0])).addUnwanted(
                    department.findCourseSlot(Static.days.indexOf(row[1]), Static.times.indexOf(row[2])));

            line = reader.readLine();
        }
    }

    private static void parsePreferences(BufferedReader reader, Department department) throws IOException {
        String line = reader.readLine();
        while(line.trim().length() > 0) {
            String[] row = line.split(",");
            cleanRow(row);

            if(isTutorial(row[2])) {
                department.findLab(parseLab(row[2])).addPreference(
                    department.findLabSlot(Static.days.indexOf(row[0]), Static.times.indexOf(row[1])), Integer.parseInt(row[3]));
            } else department.findCourse(parseCourse(row[2])).addPreference(
                    department.findCourseSlot(Static.days.indexOf(row[0]), Static.times.indexOf(row[1])), Integer.parseInt(row[3]));
            line = reader.readLine();
        }
    } 

    private static void parsePairs(BufferedReader reader, Department department) throws IOException {
        String line = reader.readLine();
        while(line.trim().length() > 0) {
            String[] row = line.split(",");
            ClassElement a,b;
            cleanRow(row);

            if(isTutorial(row[0])) a = department.findLab(parseLab(row[0]));
            else a = department.findCourse(parseCourse(row[0]));

            if(isTutorial(row[1])) b = department.findLab(parseLab(row[1]));
            else b = department.findCourse(parseCourse(row[1]));
            
            a.addCompatible(b);
//            b.addCompatible(a);

            line = reader.readLine();
        }
    }

    private static void parsePartAssign(BufferedReader reader, Department department) throws IOException{
        String line = reader.readLine();
        while(line != null) {
            if(line.trim().length() == 0) return;
            String[] row = line.split(",");
            cleanRow(row);

            if(isTutorial(row[0])) {
                department.partAssignLab(department.findLab(parseLab(row[0])), 
                    department.findLabSlot(Static.days.indexOf(row[1]), Static.times.indexOf(row[2])));
            }
            else {
                department.partAssignCourse(department.findCourse(parseCourse(row[0])), 
                    department.findCourseSlot(Static.days.indexOf(row[1]), Static.times.indexOf(row[2])));
            }

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
                else if(line.equals("Courses:")) {
                    parseCourses(reader, department);
                }
                else if(line.equals("Labs:")) {
                    parseLabs(reader, department);
                }
                else if(line.equals("Not compatible:")) {
                    parseCompatibility(reader, department);
                }
                else if(line.equals("Unwanted:")) {
                    parseUnwanted(reader, department);
                }
                else if(line.equals("Preferences:")) {
                    parsePreferences(reader, department);
                }
                else if(line.equals("Pair:")) {
                    parsePairs(reader, department);
                }
                else if(line.equals("Partial assignments:")) {
                    parsePartAssign(reader, department);
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

    public static boolean isTutorial(String str) {
        return str.contains("TUT") || str.contains("LAB");
    }
}
