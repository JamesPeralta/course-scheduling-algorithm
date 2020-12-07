
import DataStructures.*;
import Or_Tree.GenePool;
import Or_Tree.OrTreeBasedSearch;
import Or_Tree.Prob;
import Utility.Eval;

import java.util.ArrayList;
import java.util.HashMap;




public class Runner {
    private static int generations = 100;
    private static int populationSize = 50;
    private static double mutationRate = 0.20;
    private static double rouletteFactor = 0.80;
    
    

    public static void main(String[] args) throws Exception {
    	
    	// parse commadn line args  

    	ArrayList<String> options = null;
    	for (int i = 0; i < args.length; i++) {
    	    final String a = args[i];

    	    if (a.charAt(0) == '-') {
    	        if (a.length() < 2) {
    	            System.err.println("Error at argument " + a);
    	            return;
    	        }

    	        options = new ArrayList<>();
    	        Eval.params.put(a.substring(1), options);
    	    }
    	    else if (options != null) {
    	        options.add(a);
    	    }
    	    else {
    	        System.err.println("Illegal parameter usage");
    	        return;
    	    }
    	} 
    	
    	
    	// do a check to see if there are cil args 
    	if(!(Eval.params.containsKey("case") && Eval.params.get("case").size() > 0 )){
    		System.out.println("No case Specified");
    		return;
        	
    	}
    	
    	if(!(Eval.params.containsKey("pen_coursemin") && Eval.params.get("pen_coursemin").size() > 0 )){
    		System.out.println("No pen_coursemin Specified");
    		return;
        	
    	}
    	
    	if(!(Eval.params.containsKey("pen_labsmin") && Eval.params.get("pen_labsmin").size() > 0 )){
    		System.out.println("No pen_labsmin Specified");
    		return;
        	
    	}
    	
    	if(!(Eval.params.containsKey("pen_pair") && Eval.params.get("pen_pair").size() > 0 )){
    		System.out.println("No pen_pair Specified");
    		return;
        	
    	}
    	
    	if(!(Eval.params.containsKey("pen_section") && Eval.params.get("pen_section").size() > 0 )){
    		System.out.println("No pen_section Specified");
    		return;
        	
    	}
    	
    	if(!(Eval.params.containsKey("wMinFilled") && Eval.params.get("wMinFilled").size() > 0 )){
    		System.out.println("No wMinFilled Specified");
    		return;
        	
    	}
    	
    	if(!(Eval.params.containsKey("wPerf") && Eval.params.get("wPerf").size() > 0 )){
    		System.out.println("No wPerf Specified");
    		return;
        	
    	}
    	
    	if(!(Eval.params.containsKey("wPair") && Eval.params.get("wPair").size() > 0 )){
    		System.out.println("No wPair Specified");
    		return;
        	
    	}
    	
        Department department = Parser.parse(Eval.params.get("case").get(0));
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
