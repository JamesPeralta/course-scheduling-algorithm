package Or_Tree;
import DataStructures.*;
import Utility.Constr;
import Utility.Eval;

import java.util.*;

public class OrTreeBasedSearch {
    public static Prob generateSample(Department department){
        Set<CourseSlot> courseSlots  = new HashSet<>(department.getCourseSlots());
        Set<LabSlot> labSlots = new HashSet<>(department.getLabSlots());
        Prob newInstance = new Prob(department);
        int maxDepth = department.getCourses().size();
        erw(newInstance, courseSlots, labSlots, 0, maxDepth, new HashMap<>(), new HashMap<>());

        Eval eval = new Eval(newInstance, department);
        newInstance.setFitness(eval.getBound());
        return newInstance;
    }

    public static Prob fixSample(Prob prob, Department department){
        Set<CourseSlot> courseSlots  = new HashSet<>(department.getCourseSlots());
        Set<LabSlot> labSlots = new HashSet<>(department.getLabSlots());
        Prob newInstance = new Prob(department);
        int maxDepth = department.getCourses().size();

        HashMap<CourseInstance, CourseSlot> courseAssignments = prob.getCourseAssignments();
        HashMap<LabSection, LabSlot> labAssignments = prob.getLabAssignments();
        erw(newInstance, courseSlots, labSlots, 0, maxDepth, courseAssignments, labAssignments);
        Eval eval = new Eval(newInstance, department);
        newInstance.setFitness(eval.getBound());

        return newInstance;
    }

    public static Boolean erw(Prob prob,
                              Set<CourseSlot> courseSlots,
                              Set<LabSlot> labSlots,
                              int depth,
                              int maxCourses,
                              HashMap<CourseInstance, CourseSlot> courseMatch,
                              HashMap<LabSection, LabSlot> labMatch){

        Constr constr = new Constr(prob);
        if (!constr.isValid()) {
            return false;
        }
        if (prob.coursesFilled() && prob.labsFilled()) {
            return true;
        }

        if (!prob.coursesFilled()) {
            // Try assigning what it was matched to before
            CourseAssignment assign = prob.getCourse(depth);
            if (courseMatch.containsKey(assign.getCourse())) {
                prob.assignCourse(depth, courseMatch.get(assign.getCourse()));
                if (erw(prob, courseSlots, labSlots, depth + 1, maxCourses, courseMatch, labMatch)){
                    return true;
                }
                prob.unassignCourse(depth);
            }

            List<CourseSlot> copy = new ArrayList<>(courseSlots);
            Collections.shuffle(copy);
            for (CourseSlot courseSlot: copy) {
                prob.assignCourse(depth, courseSlot);
                if (erw(prob, courseSlots, labSlots, depth + 1, maxCourses, courseMatch, labMatch)){
                    return true;
                }
                prob.unassignCourse(depth);
            }
        }
        else {
            LabAssignment assign = prob.getLab(depth - maxCourses);
            if (labMatch.containsKey(assign.getLab())) {
                prob.assignLab(depth - maxCourses, labMatch.get(assign.getLab()));
                if (erw(prob, courseSlots, labSlots, depth + 1, maxCourses, courseMatch, labMatch)){
                    return true;
                }
                prob.unassignLab(depth - maxCourses);
            }

            List<LabSlot> copy = new ArrayList<>(labSlots);
            Collections.shuffle(copy);
            for (LabSlot labSlot: copy) {
                prob.assignLab(depth - maxCourses, labSlot);
                if (erw(prob, courseSlots, labSlots, depth + 1, maxCourses, courseMatch, labMatch)){
                    return true;
                }
                prob.unassignLab(depth - maxCourses);
            }
        }

        return false;
    }
}
