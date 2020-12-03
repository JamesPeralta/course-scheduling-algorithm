package Or_Tree;

import DataStructures.Department;
import Utility.RandomChoice;

import java.util.*;

public class GenePool {
    private ArrayList<Prob> pool;
    private RandomChoice randomChoice;

    public GenePool(Department department, int populationSize) {
        pool = new ArrayList<Prob>();
        // Generate Samples
        for (int i = 0; i < populationSize; i++) {
            pool.add(OrTreeBasedSearch.generateSample(department));
        }

        // Sort by fitness
        Collections.sort(pool);

        // Generate random choice weight
        double[] weights = new double[populationSize];
        weights[0] = 1.0;
        for (int i = 1; i < populationSize; i++) {
            weights[i] = weights[i - 1] * 0.90;
        }
        randomChoice = new RandomChoice(weights);
    }

    public Prob selectRandom() {
        return pool.get(randomChoice.pickIndex());
    }

    @Override
    public String toString() {
        return pool.toString();
    }
}
