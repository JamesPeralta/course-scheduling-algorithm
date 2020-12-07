import DataStructures.*;
import Or_Tree.GenePool;
import Or_Tree.OrTreeBasedSearch;
import Or_Tree.Prob;

import java.util.ArrayList;

public class Runner {
    private static int generations = 100;
    private static int populationSize = 50;
    private static double mutationRate = 0.20;
    private static double rouletteFactor = 0.80;

    public static void main(String[] args) throws Exception {
        Department department = Parser.parse("./src/Tests/eval4.txt");
        Prob newProb = OrTreeBasedSearch.generateSample(department);
        System.out.println(newProb);
        GenePool pool = null;
        try {
            pool = new GenePool(department, new ArrayList<>(), populationSize, mutationRate, rouletteFactor);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Courses parsed and Gene pool is good");
        Prob bestIndividual = pool.getBestAssignment();
        for (int i = 0; i < generations; i++) {

            System.out.println("Generation: " + Integer.toString(i));
            System.out.println(bestIndividual);
            try {
                pool.nextGeneration();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Prob latestBest = pool.getBestAssignment();

            if (bestIndividual.getFitness() > latestBest.getFitness()) {
                bestIndividual = latestBest;
            }
        }

        System.out.println("OPTIMAL Found");
        System.out.println(bestIndividual);
    }
}
