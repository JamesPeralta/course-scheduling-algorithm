import DataStructures.Department;

public class Runner {
    public static void main(String[] args) {
        Department department = Parser.parse("./src/Search_Instances/ShortExample.txt");
        System.out.println(department.getCourses());
        System.out.println(department.getLabs());
        System.out.println(department.getCourseSlots());
        System.out.println(department.getLabSlots());
    }
}
