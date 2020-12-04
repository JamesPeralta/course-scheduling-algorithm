package Or_Tree;
import DataStructures.*;
import Utility.Constr;

import java.util.*;

public class OrTreeBasedSearch {
    public static Prob generateSample(Department department){
        Set<CourseSlot> courseSlots  = new HashSet<>(department.getCourseSlots());
        Set<LabSlot> labSlots = new HashSet<>(department.getLabSlots());
        Prob newInstance = new Prob(department);
        int maxDepth = courseSlots.size();
        erw(newInstance, courseSlots, labSlots, 0, maxDepth);

        return newInstance;
    }

    public static Boolean erw(Prob prob, Set<CourseSlot> courseSlots, Set<LabSlot> labSlots, int depth, int maxCourses){
        Constr constr = new Constr(prob.getCourses(), prob.getLabs());
        if (!constr.isValid()) {
            return false;
        }
        if (prob.coursesFilled() && prob.labsFilled()) {
            return true;
        }

        if (!prob.coursesFilled()) {
            List<CourseSlot> copy = new ArrayList<>(courseSlots);
            Collections.shuffle(copy);
            for (CourseSlot courseSlot: copy) {
                prob.assignCourse(depth, courseSlot);
                if (erw(prob, courseSlots, labSlots, depth + 1, maxCourses)){
                    return true;
                }
                prob.unassignCourse(depth);
            }
        }
        else {
            List<LabSlot> copy = new ArrayList<>(labSlots);
            Collections.shuffle(copy);
            for (LabSlot labSlot: copy) {
                prob.assignLab(depth - maxCourses, labSlot);
                if (erw(prob, courseSlots, labSlots, depth + 1, maxCourses)){
                    return true;
                }
                prob.unassignLab(depth - maxCourses);
            }
        }

        return false;
    }
}
