package Or_Tree;

import DataStructures.Department;
import Utility.Eval;
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

        Collections.sort(pool);
    }

    public Prob getBestAssignment() {
        Collections.sort(pool);
        return this.pool.get(0);
    }

    public Prob selectRandom() {
        return pool.get(randomChoice.pickIndex());
    }

    public void nextGeneration() {
        ArrayList<Prob> newPool = new ArrayList<>();
        // Crossover All of them
        for (int i = 0; i < this.populationSize; i++) {
            Prob parent_one = selectRandom();
            Prob parent_two = selectRandom();
            Prob child = parent_one.crossover(parent_two, this.department);
            newPool.add(child);
        }

        // Mutate all of them
        for (int i = 0; i < this.populationSize; i++) {
            newPool.get(i).mutate(this.department);
        }

        // Fix All of them
        for (int i = 0; i < this.populationSize; i++) {
            newPool.set(i, OrTreeBasedSearch.fixSample(newPool.get(i), this.department));
        }

        // Sort them
        Collections.sort(pool);
        pool = newPool;
    }

    @Override
    public String toString() {
        return pool.toString();
    }
}
