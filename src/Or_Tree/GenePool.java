package Or_Tree;

import DataStructures.Department;
import Utility.RandomChoice;

import java.util.*;

public class GenePool {
    private ArrayList<Prob> pool;
    private RandomChoice randomChoice;
    private int populationSize;
    private Department department;

    public GenePool(Department department, ArrayList<Prob> individuals, int populationSize) {
        this.pool = individuals;
        this.populationSize = populationSize;
        this.department = department;

        if (pool.size() == 0) {
            for (int i = 0; i < populationSize; i++) {
                pool.add(OrTreeBasedSearch.generateSample(department));
            }
        }

        double[] weights = new double[populationSize];
        weights[0] = 1.0;
        for (int i = 1; i < populationSize; i++) {
            weights[i] = weights[i - 1] * 0.90;
        }
        randomChoice = new RandomChoice(weights);

        // Test Individuals
    }

    public Prob selectRandom() {
        return pool.get(randomChoice.pickIndex());
    }

    public void testGeneration() {
        // TODO: Need to implement testing
    }

    public void nextGeneration() {
        ArrayList<Prob> newPool = new ArrayList<>();
        for (int i = 0; i < this.populationSize; i++) {
            Prob parent_one = selectRandom();
            Prob parent_two = selectRandom();
            Prob child = parent_one.crossover(parent_two, this.department);
            newPool.add(child);
        }

        pool = newPool;
        // Test individuals
    }

    @Override
    public String toString() {
        return pool.toString();
    }
}
