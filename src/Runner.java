import DataStructures.Department;
import Or_Tree.OrTreeBasedSearch;

public class Runner {
    public static void main(String[] args) {
        Department department = Parser.parse("./src/Search_Instances/ShortExample.txt");
        OrTreeBasedSearch.generateSample(department);
    }
}
