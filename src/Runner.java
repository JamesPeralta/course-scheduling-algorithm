import DataStructures.Course;
import DataStructures.*;
import Or_Tree.GenePool;
import Or_Tree.OrTreeBasedSearch;

import java.util.Collections;

public class Runner {
    public static void main(String[] args) {
        Department department = Parser.parse("./src/Search_Instances/CustomTest1.txt");
        GenePool pool = new GenePool(department, 5);
        System.out.println(pool);
    }
}
