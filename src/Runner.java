import DataStructures.*;
import Or_Tree.GenePool;
import Or_Tree.OrTreeBasedSearch;
import Or_Tree.Prob;

import java.util.ArrayList;

public class Runner {
    private static int generations = 200;

    public static void main(String[] args) {
        Department department = Parser.parse("./src/Abel_Instances/813.txt");
        GenePool pool = new GenePool(department, new ArrayList<>(), 50);
        Prob bestIndividual = pool.getBestAssignment();
        for (int i = 0; i < generations; i++) {
            System.out.println("Generation: " + Integer.toString(i));
            System.out.println(bestIndividual);
            pool.nextGeneration();
            Prob latestBest = pool.getBestAssignment();

            if (bestIndividual.getFitness() > latestBest.getFitness()) {
                bestIndividual = latestBest;
            }
        }

        System.out.println("OPTIMAL Found");
        System.out.println(bestIndividual);
    }
}
