import DataStructures.*;
import Or_Tree.GenePool;
import java.util.ArrayList;

public class Runner {
    public static void main(String[] args) {
        Department department = Parser.parse("./src/Search_Instances/CustomTest1.txt");
        GenePool pool = new GenePool(department, new ArrayList<>(), 3);
        System.out.println(pool);
    }
}
