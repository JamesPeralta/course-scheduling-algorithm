import DataStructures.*;
import Or_Tree.GenePool;
import java.util.ArrayList;

public class Runner {
    public static void main(String[] args) {
        Department department = Parser.parse("./src/Search_Instances/CustomTest3.txt");
        GenePool pool = new GenePool(department, new ArrayList<>(), 1);
        System.out.println(pool);
    }
}
