import java.util.*;
import java.io.*;

public class Parser {
    public static ArrayList<String> days = new ArrayList<>(Arrays.asList(new String[]{"MO", "TU", "FR"}));
    public static ArrayList<String> times = new ArrayList<>(Arrays.asList(new String[]{
        "8:00", "9:00", "9:30", "10:00", "11:00", "12:00", "12:30", "13:00", "14:00", "15:00", "15:30", "16:00", 
        "17:00", "18:00", "18:30", "19:00", "20:00"}));


    private static void parseSlots(BufferedReader reader, Department department, boolean isCourse) throws IOException {
        String line = reader.readLine();
        while(line.length() > 0) {
            String[] row = line.split(",");
            if(isCourse) department.addCourseSlot(new CourseSlot(days.indexOf(row[0]), times.indexOf(row[1]), 
                                                    Integer.parseInt(row[2]), Integer.parseInt(row[3])));
            else department.addLabSlot(new LabSlot(days.indexOf(row[0]), times.indexOf(row[1]), 
                                                    Integer.parseInt(row[2]), Integer.parseInt(row[3])));
        }
    }

    public static Department parse(String pathToFile) {
        Department department = new Department();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToFile));
            String line = reader.readLine();
            while(line != null) {
                if(line.equals("Name:")) department.setName(reader.readLine());
                if(line.equals("Course slots:")) parseSlots(reader, department, true);
                if(line.equals("Lab slots:")) parseSlots(reader, department, false);
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
}
