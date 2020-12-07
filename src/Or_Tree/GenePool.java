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
    private double mutationRate;

    public GenePool(Department department, ArrayList<Prob> individuals, int populationSize, double mutationRate, double rouletteFactor) {
        this.pool = individuals;
        this.populationSize = populationSize;
        this.department = department;
        this.mutationRate = mutationRate;

        if (pool.size() == 0) {
            while (pool.size() < populationSize) {
                try {
                    pool.add(OrTreeBasedSearch.generateSample(department));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        double[] weights = new double[populationSize];
        weights[0] = 1.0;
        for (int i = 1; i < populationSize; i++) {
            weights[i] = weights[i - 1] * rouletteFactor;
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
//    	System.out.println("starting next generation");
        ArrayList<Prob> newPool = new ArrayList<>();
        // Crossover All of them
        for (int i = 0; i < this.populationSize; i++) {
            Prob parent_one = selectRandom();
            Prob parent_two = selectRandom();
            Prob child = parent_one.crossover(parent_two, this.department);
            newPool.add(child);
        }
        
//        System.out.println("All childern created");
        // Mutate all of them
        for (int i = 0; i < this.populationSize; i++) {
            newPool.get(i).mutate(this.department, mutationRate);
        }
//        System.out.println("All childern Mutated");
        // Fix All of them
        for (int i = 0; i < this.populationSize; i++) {
//        	System.out.println("Fixing the mutation while keeping the assigments the same " + (i+1) + "/" + this.populationSize);
            try {
                newPool.set(i, OrTreeBasedSearch.fixSample(newPool.get(i), this.department));
            } catch (Exception e) {
                Prob newClone = selectRandom();
                newPool.set(i, newClone.clone(department));
            }
        }
//        System.out.println("Mutation crossover is done ");

        // Sort them
        Collections.sort(pool);
        
//        System.out.println("sorted the stuff ");
        pool = newPool;
    }

    @Override
    public String toString() {
        return pool.toString();
    }
}
