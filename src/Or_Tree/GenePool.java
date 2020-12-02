package Or_Tree;

import DataStructures.Department;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GenePool {
    private ArrayList<Prob> pool;
    private ArrayList<Double> weights;

    public GenePool(Department department) {
        pool = new ArrayList<Prob>();
        // Generate Samples
        for (int i = 0; i < 50; i++) {
            pool.add(OrTreeBasedSearch.generateSample(department));
        }

        // Generate weights for roulette
        weights = new ArrayList<Double>();
        weights.add((double) 1);
        for (int i = 1; i < 50; i++) {
            weights.add(weights.get(i - 1) * 0.90);
        }
    }
}
