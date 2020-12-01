package Or_Tree;
import DataStructures.*;

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
        if (prob.coursesFilled() && prob.labsFilled()) {
            return true;
        }

        if (!prob.coursesFilled()) {
            List<CourseSlot> copy = new ArrayList<>(courseSlots);
            Collections.shuffle(copy);
            for (CourseSlot courseSlot: copy) {
                prob.assignCourse(depth, courseSlot);
//                courseSlots.remove(courseSlot);
                if (erw(prob, courseSlots, labSlots, depth + 1, maxCourses)){
                    return true;
                }
//                courseSlots.add(courseSlot);
                prob.unassignCourse(depth);
            }
        }
        else {
            List<LabSlot> copy = new ArrayList<>(labSlots);
            Collections.shuffle(copy);
            for (LabSlot labSlot: copy) {
                prob.assignLab(depth - maxCourses - 1, labSlot);
//                labSlots.remove(labSlot);
                if (erw(prob, courseSlots, labSlots, depth + 1, maxCourses)){
                    return true;
                }
//                labSlots.add(labSlot);
                prob.unassignLab(depth - maxCourses - 1);
            }
        }

        return false;
    }
}
