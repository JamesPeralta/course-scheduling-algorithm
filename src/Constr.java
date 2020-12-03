import java.util.*;
import Or_Tree.*;
import DataStructures.*;

public class Constr {

    public boolean valid = true;
    private ArrayList<CourseAssignment> course;
    private ArrayList<LabAssignment> lab;

    private int[] assignedInSlotCourses;
    private int[] assignedInSlotLabs;

    public Constr(ArrayList<CourseAssignment> courses, ArrayList<LabAssignment> labs) {
        this.course = courses;
        this.lab = labs;

        assignedInSlotCourses = new int[course.size()];
        assignedInSlotLabs = new int[lab.size()];

        checkCourseMax();
        checkLabMax();
        checkTuesEleven();
        checkLectureNine();
        

    }

    public void checkCourseMax() {
        for (int i = 0; i < course.size(); i++) {
            Integer value = assignedinCourses.get(course.get(i).getCourseSlot());
            if (value == null){
                assignedinCourses.put(course.get(i).getCourseSlot(), 1);
            }
            else{
                assignedinCourses.put(course.get(i).getCourseSlot(), assignedinCourses.get(course.get(i).getCourseSlot()) + 1);
            }
        }
        for (int i = 0; i < course.size(); i++){
            if(course.get(i).getCourseSlot().getCoursemax() < assignedinCourses.get(course.get(i).getCourseSlot())){
                valid = false;
            }
        }
    }

    public void checkLabMax() {
        for (int i = 0; i < lab.size(); i++) {
            Integer value = assignedinLabs.get(lab.get(i).getLabSlot());
            if (value == null){
                assignedinLabs.put(lab.get(i).getLabSlot(), 1);
            }
            else{
                assignedinLabs.put(lab.get(i).getLabSlot(), assignedinLabs.get(lab.get(i).getLabSlot()) + 1);
            }
        }
        for (int i = 0; i < lab.size(); i++){
            if(lab.get(i).getLabSlot().getCoursemax() < assignedinLabs.get(lab.get(i).getLabSlot())){
                valid = false;
            }
        }
    }

    public void checkTuesEleven(){
        for (int i = 0; i < course.size(); i++){
            if (course.get(i).getCourseSlot().getTimeString().equals("11:00") && course.get(i).getCourseSlot().getDayString().equals("TU")){
                valid = false;     
            }
        }
    }

    public void checkLectureNine(){
        for (int i = 0; i < course.size(); i++){
            if (course.get(i).getCourse().getSectionNumber() == 9){
                if(course.get(i).getCourseSlot().getTime() < 18){
                    valid = false;
                }

            }
        }
    }

   public void check813(){
        for (int i = 0; i < course.size(); i++){
            if (course.get(i).getCourse().getCourseName().equals("813")){
                if (course.get(i).getCourseSlot().getTimeString().equals("18:00") && course.get(i).getCourseSlot().getDayString().equals("TU")){
                }
                else{
                    valid = false;
                }
                for(int j = 0; j < course.size(); j++){
                    if(course.get(j).getCourse().getCourseName().equals("313") && course.get(j).getCourseSlot().equals(course.get(i).getCourseSlot())){
                        valid = false;
                    }
                }
                for(int j = 0; j < course.size(); j++){
                    if(lab.get(j).getLab().getOfCourse().equals("313") && lab.get(j).getLabSlot().toString().equals(course.get(i).getCourseSlot().toString())){
                        valid = false;
                    }
                }
            }
        }
    }

    public void check913(){
        for (int i = 0; i < course.size(); i++){
            if (course.get(i).getCourse().getCourseName().equals("913")){
                if (course.get(i).getCourseSlot().getTimeString().equals("18:00") && course.get(i).getCourseSlot().getDayString().equals("TU")){
                }
                else{
                    valid = false;
                }
                for(int j = 0; j < course.size(); j++){
                    if(course.get(j).getCourse().getCourseName().equals("413") && course.get(j).getCourseSlot().equals(course.get(i).getCourseSlot())){
                        valid = false;
                    }
                }
                for(int j = 0; j < course.size(); j++){
                    if(lab.get(j).getLab().getOfCourse().equals("413") && lab.get(j).getLabSlot().toString().equals(course.get(i).getCourseSlot().toString())){
                        valid = false;
                    }
                }
            }
        }
    }

}
