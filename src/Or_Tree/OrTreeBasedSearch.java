package Or_Tree;
import CustomExceptions.NoValidAssignmentException;
import CustomExceptions.RunningTooLongException;
import DataStructures.*;
import DataStructures.Iterator;
import Utility.Constr;
import Utility.Eval;

import java.util.*;

public class OrTreeBasedSearch {
    public static Prob generateSample(Department department) throws Exception {
        Set<CourseSlot> courseSlots  = new HashSet<>(department.getCourseSlots());
        Set<LabSlot> labSlots = new HashSet<>(department.getLabSlots());
        Prob newInstance;
        HashMap<CourseInstance, CourseSlot> partAssignCourses = department.getAssignedCourses();
        HashMap<LabSection, LabSlot> partAssignLabs = department.getAssignedLabs();

        while (true) {
            try {
                newInstance = new Prob(department);
                Iterator iterator = new Iterator();
                Boolean found = erw(newInstance, courseSlots, labSlots, new HashMap<>(), new HashMap<>(), partAssignCourses, partAssignLabs, iterator);
                if (!found) {
                    throw new NoValidAssignmentException("Cannot find a valid assignment");
                }
                break;
            }
            catch (RunningTooLongException e) {
                System.out.println("Running too long.");
            }
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
        
        // these are all linear an non looping
        HashMap<CourseInstance, CourseSlot> courseAssignments = prob.getCourseAssignments();
        HashMap<LabSection, LabSlot> labAssignments = prob.getLabAssignments();
        HashMap<CourseInstance, CourseSlot> partAssignCourses = department.getAssignedCourses();
        HashMap<LabSection, LabSlot> partAssignLabs = department.getAssignedLabs();

        Prob newInstance = new Prob(department);
        Iterator iterator = new Iterator();

        try {
            Boolean found = erw(newInstance, courseSlots, labSlots, courseAssignments, labAssignments, partAssignCourses, partAssignLabs, iterator);
            if (!found) {
                throw new NoValidAssignmentException("Cannot find a valid assignment");
            }
        }
        catch (RunningTooLongException e) {
            throw new InvalidPropertiesFormatException("Child is too hard to fix.");
        }

        Eval eval = new Eval(newInstance, department);
        newInstance.setFitness(eval.getBound());
        return newInstance;
    }

    public static Boolean erw(Prob prob,
                              Set<CourseSlot> courseSlots,
                              Set<LabSlot> labSlots,
                              HashMap<CourseInstance, CourseSlot> courseMatch,
                              HashMap<LabSection, LabSlot> labMatch,
                              HashMap<CourseInstance, CourseSlot> partAssignCourses,
                              HashMap<LabSection, LabSlot> partAssignLabs,
                              Iterator depth) throws RunningTooLongException {

        depth.incrementCount();

        if (depth.getCount() % 1000000 == 0) {
            System.out.println(depth.getCount());
            System.out.println(prob);
        }

        Constr constr = new Constr(prob);
        if (!constr.isValid()) {
            return false;
        }

        if (prob.coursesFilled() && prob.labsFilled()) {
            return true;
        }
        

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
                if (erw(prob, courseSlots, labSlots, courseMatch, labMatch, partAssignCourses, partAssignLabs, depth)){
                    return true;
                }
                assign.unassignSlot();
            }

            // get a new random assigment for this course
            List<CourseSlot> copy = new ArrayList<>(courseSlots);
            Collections.shuffle(copy);
            for (CourseSlot courseSlot: copy) {
                assign.assignSlot(courseSlot);
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

            // Try matching parent
            if (labMatch.containsKey(assign.getLab())) {
                assign.assignSlot(labMatch.get(assign.getLab()));
                if (erw(prob, courseSlots, labSlots, courseMatch, labMatch, partAssignCourses, partAssignLabs, depth)){
                    return true;
                }
                assign.unassignSlot();
            }

            // Try new random slot for this assignment
            List<LabSlot> copy = new ArrayList<>(labSlots);
            Collections.shuffle(copy);
            for (LabSlot labSlot: copy) {
                assign.assignSlot(labSlot);

                if (erw(prob, courseSlots, labSlots, courseMatch, labMatch, partAssignCourses, partAssignLabs, depth)){
                    return true;
                }
                assign.unassignSlot();
            }
        }

        return false;
    }
}
