package Or_Tree;
import DataStructures.*;
import DataStructures.Iterator;
import Utility.Constr;
import Utility.Eval;

import java.util.*;

public class OrTreeBasedSearch {
    public static Prob generateSample(Department department) throws Exception {
        Set<CourseSlot> courseSlots  = new HashSet<>(department.getCourseSlots());
        Set<LabSlot> labSlots = new HashSet<>(department.getLabSlots());
        Prob newInstance = new Prob(department);
        Iterator iterator = new Iterator();
        HashMap<CourseInstance, CourseSlot> partAssignCourses = department.getAssignedCourses();
        HashMap<LabSection, LabSlot> partAssignLabs = department.getAssignedLabs();
        Boolean found = erw(newInstance, courseSlots, labSlots, new HashMap<>(), new HashMap<>(), partAssignCourses, partAssignLabs, iterator);

        if (!found) {
            throw new Exception("Can't make valid instance");
        }

        Eval eval = new Eval(newInstance, department);
        newInstance.setFitness(eval.getBound());
        return newInstance;
    }
    
    /**
     * this will make an infinite loop sometime 
     * @param prob
     * @param department
     * @return
     */
    public static Prob fixSample(Prob prob, Department department) throws Exception {
    	// inializers these are good 
        Set<CourseSlot> courseSlots  = new HashSet<>(department.getCourseSlots());
        Set<LabSlot> labSlots = new HashSet<>(department.getLabSlots());
//        System.out.println("creating a new instance based of the departments");
        // this will run in TODO find out 
        Prob newInstance = new Prob(department);
        
        // these are all linear an non looping
        HashMap<CourseInstance, CourseSlot> courseAssignments = prob.getCourseAssignments();
        HashMap<LabSection, LabSlot> labAssignments = prob.getLabAssignments();
//        System.out.println("Preforming ERW");

        Iterator iterator = new Iterator();
        HashMap<CourseInstance, CourseSlot> partAssignCourses = department.getAssignedCourses();
        HashMap<LabSection, LabSlot> partAssignLabs = department.getAssignedLabs();
        Boolean found = erw(newInstance, courseSlots, labSlots, courseAssignments, labAssignments, partAssignCourses, partAssignLabs, iterator);
        if (!found) {
            throw new Exception("Can't fix this instance");
        }
        // eval runs in a known time 
//        System.out.println("Running eval");
        Eval eval = new Eval(newInstance, department);
//        System.out.println("Settings the fitness of the new instance ");
        newInstance.setFitness(eval.getBound());
//        System.out.println("Done fixing sample");
        return newInstance;
    }
    /**
     * 
     * @param prob
     * @param courseSlots
     * @param labSlots
     * @param depth
     * @param courseMatch
     * @param labMatch
     * @return
     */
    public static Boolean erw(Prob prob,
                              Set<CourseSlot> courseSlots,
                              Set<LabSlot> labSlots,
                              HashMap<CourseInstance, CourseSlot> courseMatch,
                              HashMap<LabSection, LabSlot> labMatch,
                              HashMap<CourseInstance, CourseSlot> partAssignCourses,
                              HashMap<LabSection, LabSlot> partAssignLabs,
                              Iterator depth){

        depth.incrementCount();
        if (depth.getCount() > 50000) {
            return false;
        }

    	//System.out.println("running ERW");
    	// figure out if we have a valid assigment here 
        Constr constr = new Constr(prob);
        if (!constr.isValid()) {
//        	System.out.println("invalid " + Integer.toString(depth.getCount()));
            return false;
        }
        
        // make sure we have assgined all the classes here 
        if (prob.coursesFilled() && prob.labsFilled()) {
            return true;
        }
        
        
        // 
        if (!prob.coursesFilled()) {
            // Try assigning what it was matched to before
            CourseAssignment assign = prob.getCourse();

            // If theres a part assign we need to make this assignment no matter what.
            if (partAssignCourses.containsKey(assign.getCourse())) {
                assign.assignSlot(partAssignCourses.get(assign.getCourse()));
                return erw(prob, courseSlots, labSlots, courseMatch, labMatch, partAssignCourses, partAssignLabs, depth);
            }

            // when the assign in not in the course match map 
            if (courseMatch.containsKey(assign.getCourse())) {
                assign.assignSlot(courseMatch.get(assign.getCourse()));
//                System.out.println("1");
                // recursivel call 
                if (erw(prob, courseSlots, labSlots, courseMatch, labMatch, partAssignCourses, partAssignLabs, depth)){
                    return true;
                }
                assign.unassignSlot();
            }

            // get a new random assigment for 
            List<CourseSlot> copy = new ArrayList<>(courseSlots);
            Collections.shuffle(copy);
            for (CourseSlot courseSlot: copy) {
                assign.assignSlot(courseSlot);
                // recursive call
                if (erw(prob, courseSlots, labSlots, courseMatch, labMatch,partAssignCourses, partAssignLabs, depth)){
                    return true;
                }
                assign.unassignSlot();
            }
        }
        else {
            LabAssignment assign = prob.getLab();

            // If theres a part assign we need to make this assignment no matter what.
            if (partAssignLabs.containsKey(assign.getLab())) {
                assign.assignSlot(partAssignLabs.get(assign.getLab()));
                return erw(prob, courseSlots, labSlots, courseMatch, labMatch, partAssignCourses, partAssignLabs, depth);
            }

            if (labMatch.containsKey(assign.getLab())) {
                assign.assignSlot(labMatch.get(assign.getLab()));
                // recursive call 
//                System.out.println("3");
                if (erw(prob, courseSlots, labSlots, courseMatch, labMatch, partAssignCourses, partAssignLabs, depth)){
                    return true;
                }
                assign.unassignSlot();
            }

            List<LabSlot> copy = new ArrayList<>(labSlots);
            Collections.shuffle(copy);
            for (LabSlot labSlot: copy) {
                assign.assignSlot(labSlot);
                // recursivel call 
//                System.out.println("4");
                if (erw(prob, courseSlots, labSlots, courseMatch, labMatch, partAssignCourses, partAssignLabs, depth)){
                    return true;
                }
                assign.unassignSlot();
            }
        }

        return false;
    }
}
