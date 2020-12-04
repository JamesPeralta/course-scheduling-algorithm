package Utility;

import java.util.*;
import DataStructures.*;

public class Constr {

    private boolean valid;
    private ArrayList<CourseAssignment> course;
    private ArrayList<LabAssignment> lab;

    private HashMap<CourseSlot, Integer> assignedinCourses;
    private HashMap<LabSlot, Integer> assignedinLabs;
    private ArrayList<CourseSlot> scheduled500;

    public Constr(ArrayList<CourseAssignment> courses, ArrayList<LabAssignment> labs) {
        this.course = courses;
        this.lab = labs;
        valid = true;

        assignedinCourses = new HashMap<CourseSlot, Integer>();
        assignedinLabs = new HashMap<LabSlot, Integer>();
        scheduled500 = new ArrayList<CourseSlot>();

        if (valid == true) checkCourseMax();
        if (valid == true) checkLabMax();
        if (valid == true) checkAssign();
        if (valid == true) checkTuesEleven();
        if (valid == true) checkUnwanted();
        if (valid == true) checkLectureNine();
        if (valid == true) check813();
        if (valid == true) check913();
        if (valid == true) check500();
    }
    //checks if courseMax is borken
    public void checkCourseMax() {
        for (int i = 0; i < course.size(); i++) {
            Integer value = assignedinCourses.get(course.get(i).getCourseSlot());
            if (value == null){
                assignedinCourses.put(course.get(i).getCourseSlot(), 1); //if new to the hashmap value will be null and we add new key to the hashmap and assign a value of 1
            }
            else{
                assignedinCourses.put(course.get(i).getCourseSlot(), assignedinCourses.get(course.get(i).getCourseSlot()) + 1); //updates value 
            }
        }

        for (int i = 0; i < course.size(); i++){
            if (course.get(i).getCourseSlot() == null) {
                continue;
            }

            if(course.get(i).getCourseSlot().getCoursemax() < assignedinCourses.get(course.get(i).getCourseSlot())){ //check each course in the hashmap and see if coursemax is broken
                valid = false;
                break;
            }
        }
    }

    //check if labMax is broken, similar to courseMax
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
            if (lab.get(i).getLabSlot() == null) {
                continue;
            }

            if(lab.get(i).getLabSlot().getCoursemax() < assignedinLabs.get(lab.get(i).getLabSlot())){
                valid = false;
                break;
            }
        }
    }

    //checks if a course and lab are assigned to the same slot
    public void checkAssign(){
        for(int i = 0; i < course.size(); i++){
            for(int j = 0; j < lab.size(); j++){
                if (course.get(i).getCourseSlot() == null || lab.get(j).getLabSlot() == null) {
                    continue;
                }

                if((course.get(i).getCourseSlot().getDay()) == (lab.get(j).getLabSlot().getDay()) && (course.get(i).getCourseSlot().getTime()) == (lab.get(j).getLabSlot().getTime())){ //check if the course has the same day and time as a lab
                    if(course.get(i).getCourse().getCourseName().equals(lab.get(j).getLab().getOfCourse())){ //checks if the lab corresponds to the same course and are the same section
                        if (!lab.get(j).getLab().getOfSection().equals("")){
                            if (course.get(i).getCourse().getSectionString().equals(lab.get(j).getLab().getOfSection())) {
                                valid = false;
                                break;
                            }
                        }
                        else {
                            valid = false;
                            break;
                        }
                    }
                }
            }
        }
    }

    public void checkUnwanted(){
        for (int i = 0; i < course.size(); i++){
            if(course.get(i).getCourseSlot() == null){
                continue;
            }
            else{
                ArrayList<CourseSlot> unwantedList = course.get(i).getCourse().getUnwanted();
                for (int j = 0; j < unwantedList.size(); j++){
                    if (course.get(i).getCourseSlot().equals(unwantedList.get(j))){
                        valid = false;
                        break;
                    }
                }
            }
            if(valid == false){
                break;
            }
        }

    }

    //checks if a course is on tuesday at 11:00
    public void checkTuesEleven(){
        for (int i = 0; i < course.size(); i++){
            if (course.get(i).getCourseSlot() == null) {
                continue;
            }

            if (course.get(i).getCourseSlot().getTimeString().equals("11:00") && course.get(i).getCourseSlot().getDayString().equals("TU")){ //checks if course is at 11:00 and on a tuesday
                valid = false; 
                break;    
            }
        }
    }

    //checks if lecture number ends in 9, it must be assigned to an evening slot
    public void checkLectureNine(){
        for (int i = 0; i < course.size(); i++){
            if (course.get(i).getCourse() == null) {
                continue;
            }

            if (course.get(i).getCourse().getSectionNumber() == 9){
                if(course.get(i).getCourseSlot().getTime() < 18){
                    valid = false;
                    break;
                }

            }
        }
    }

    //checks for 813 and if it is assigned at 18:00, and is not associated with any course or lab for 313
    public void check813(){
        for (int i = 0; i < course.size(); i++){
            if (course.get(i).getCourse() == null) {
                continue;
            }

            if (course.get(i).getCourse().getCourseName().equals("813")){
                if (course.get(i).getCourseSlot().getTimeString().equals("18:00") && course.get(i).getCourseSlot().getDayString().equals("TU")){ //checks if course is assigned to 18:00 and on tuesday
                }
                else{
                    valid = false;
                    break;
                }
                for(int j = 0; j < course.size(); j++){
                    if(course.get(j).getCourse().getCourseName().equals("313") && course.get(j).getCourseSlot().equals(course.get(i).getCourseSlot())){//checks if 313 course is assigned to the same slot
                        valid = false;
                        break;
                    }
                }
                for(int j = 0; j < course.size(); j++){
                    if(lab.get(j).getLab().getOfCourse().equals("313") && lab.get(j).getLabSlot().toString().equals(course.get(i).getCourseSlot().toString())){//checks if 313 lab is assigned to the same slot
                        valid = false;
                        break;
                    }
                }
            }
        }
    }
    
    //similar to check813 but for 913 
    public void check913(){
        for (int i = 0; i < course.size(); i++){
            if (course.get(i).getCourse() == null) {
                continue;
            }

            if (course.get(i).getCourse().getCourseName().equals("913")){
                if (course.get(i).getCourseSlot().getTimeString().equals("18:00") && course.get(i).getCourseSlot().getDayString().equals("TU")){//checks if course is assigned to 18:00 and on tuesday
                }
                else{
                    valid = false;
                    break;
                }
                for(int j = 0; j < course.size(); j++){
                    if(course.get(j).getCourse().getCourseName().equals("413") && course.get(j).getCourseSlot().equals(course.get(i).getCourseSlot())){//checks if 413 course is assigned to the same slot
                        valid = false;
                        break;
                    }
                }
                for(int j = 0; j < course.size(); j++){
                    if(lab.get(j).getLab().getOfCourse().equals("413") && lab.get(j).getLabSlot().toString().equals(course.get(i).getCourseSlot().toString())){//checks if 413 lab is assigned to the same slot
                        valid = false;
                        break;
                    }
                }
            }
        }
    }
    public void check500(){
        for (int i = 0; i < course.size(); i++){
            if(course.get(i).getCourse() == null){
                continue;
            }
            String courseName = course.get(i).getCourse().getCourseName();
            char firstDigit = courseName.charAt(0);
            if(firstDigit == '5'){
                scheduled500.add(course.get(i).getCourseSlot());
            }
        }
        for (int i = 0; i < scheduled500.size(); i++){
            for (int j = 0; j < scheduled500.size(); j++){
                if(scheduled500.get(i).equals(scheduled500.get(j))){
                    valid = false;
                    break;
                }
            }
        }
    }

    public boolean isValid(){
        return this.valid;
    }
}


