import DataStructures.Course;
import DataStructures.*;
import Or_Tree.OrTreeBasedSearch;

public class Runner {
    public static void main(String[] args) {
        Department department = Parser.parse("./src/Search_Instances/ShortExample.txt");
        for (Course c : department.getCourses()) {
                System.out.println(c);
        }
        System.out.println(OrTreeBasedSearch.generateSample(department));
    }
}
