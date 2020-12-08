package Utility;

import java.util.*;
import DataStructures.*;
import Or_Tree.Prob;

public class Constr {
    private boolean valid;
    private ArrayList<CourseAssignment> courseAssigments;
    private ArrayList<LabAssignment> labAssigments;
    private HashMap<CourseSlot, Integer> assignedinCourses;
    private HashMap<LabSlot, Integer> assignedinLabs;
    private ArrayList<CourseAssignment> scheduled500;
    private HashMap<ClassElement, Slot> assignedSlot;

    public Constr(Prob prob) {
        this.courseAssigments = prob.getCourses();
        this.labAssigments = prob.getLabs();
        valid = true;

        assignedinCourses = new HashMap<>();
        assignedinLabs = new HashMap<>();
        scheduled500 = new ArrayList<>();

        this.assignedSlot = new  HashMap<>();

		// we need to build our class elements to slot thing so we know 
		for(CourseAssignment courses: this.courseAssigments) {
			//System.out.println(courses);
			//System.out.println(courses.getCourseSlot());
			this.assignedSlot.put(courses.getCourse(), courses.getCurrentSlot());
		}
		// do it for the labs too 
		for(LabAssignment labs: this.labAssigments) {
			this.assignedSlot.put(labs.getLab(), labs.getCurrentSlot());
		}

        if (valid) checkCourseMax();
        if (valid) checkLabMax();
        if (valid) checkAssign();
        if (valid) checkTuesEleven();
        if (valid) checkUnwanted();
        if (valid) checkCompatible();
        if (valid) checkLectureNine();
        if (valid) check500();
    }
    //checks if courseMax is borken
    public void checkCourseMax() {
        for (int i = 0; i < courseAssigments.size(); i++) {
            Integer value = assignedinCourses.get(courseAssigments.get(i).getCourseSlot());
            if (value == null){
                assignedinCourses.put(courseAssigments.get(i).getCourseSlot(), 1); //if new to the hashmap value will be null and we add new key to the hashmap and assign a value of 1
            }
            else{
                assignedinCourses.put(courseAssigments.get(i).getCourseSlot(), assignedinCourses.get(courseAssigments.get(i).getCourseSlot()) + 1); //updates value 
            }
        }

        for (int i = 0; i < courseAssigments.size(); i++){
            if (courseAssigments.get(i).getCourseSlot() == null) {
                continue;
            }

            if(courseAssigments.get(i).getCourseSlot().getCoursemax() < assignedinCourses.get(courseAssigments.get(i).getCourseSlot())){ //check each course in the hashmap and see if coursemax is broken
                valid = false;
                break;
            }
        }
    }

    //check if labMax is broken, similar to courseMax
    public void checkLabMax() {
        for (int i = 0; i < labAssigments.size(); i++) {
            Integer value = assignedinLabs.get(labAssigments.get(i).getLabSlot()); 
            if (value == null){
                assignedinLabs.put(labAssigments.get(i).getLabSlot(), 1);
            }
            else{
                assignedinLabs.put(labAssigments.get(i).getLabSlot(), assignedinLabs.get(labAssigments.get(i).getLabSlot()) + 1);
            }
        }
        for (int i = 0; i < labAssigments.size(); i++){
            if (labAssigments.get(i).getLabSlot() == null) {
                continue;
            }

            if(labAssigments.get(i).getLabSlot().getCoursemax() < assignedinLabs.get(labAssigments.get(i).getLabSlot())){
                valid = false;
                break;
            }
        }
    }
    
    /**
     * This is what i need to check here
     * it breaks on a certain case where tuesady the thing is 15 min longer 
     */
    public void checkAssign(){

    	for(CourseAssignment courseAssigment: courseAssigments) {
    		for(LabAssignment labAssigment: labAssigments) {
    			// null check 
    			if (courseAssigment.getCourseSlot() == null || labAssigment.getLabSlot() == null) {
                    continue;
                }
    			
    			// check to see if the tutrial equals the queore 
    			if(courseAssigment.getCourse().getCourseName().equals(labAssigment.getLab().getOfCourse())) {
    				
    				// if it is not for a specific sections 
    				if(!labAssigment.getLab().getOfSection().equals("")) {
    					// if they are not the same section they can appear at the same time
    					if(!(courseAssigment.getCourse().getSectionString().equals(labAssigment.getLab().getOfSection()))) {
    						continue;
    					}
    					// check to make sure it does not overlap for that course 
    					boolean dayMatch = (courseAssigment.getCourseSlot().getDay()) == (labAssigment.getLabSlot().getDay());
    					// the hour match is a littler trickier 
    					
    					// check to see if the hours match 
    					boolean hourMatch = (courseAssigment.getCourseSlot().getTimeString().split(":")[0]).equals(labAssigment.getLabSlot().getTimeString().split(":")[0]); 
    					if(courseAssigment.getCourseSlot().getDayString().equals("TU")) {
    						// hour and 15 slots exists 
    						 
    						String hourString = courseAssigment.getCourseSlot().getTimeString().split(":")[0];
    				 
    						hourMatch = hourMatch || Integer.toString((Integer.parseInt(hourString)+1)).equals(labAssigment.getLabSlot().getTimeString().split(":")[0]);
    						// check if the tutiral overlaps this hour or next hour 
    						 
    					} 
					if(dayMatch && hourMatch) {
    						 valid = false;
                             			 break;
    					}
					
					if(labAssigment.getLabSlot().getDayString().equals("FR")){
						String hourString = labAssigment.getLabSlot().getTimeString().split(":")[0];
						
						hourMatch = hourMatch || Integer.toString((Integer.parseInt(hourString)+1)).equals(courseAssigment.getCourseSlot().getTimeString().split(":")[0]);
					}
    					if(dayMatch && hourMatch) {
    						 valid = false;
                             			 break;
    					}
    					
    				}else {
    					// now check to see if there is any overlap with the class since they are the same 
    					boolean dayMatch = (courseAssigment.getCourseSlot().getDay()) == (labAssigment.getLabSlot().getDay());
    					// the hour match is a littler trickier 
    					boolean hourMatch = (courseAssigment.getCourseSlot().getTimeString().split(":")[0]).equals(labAssigment.getLabSlot().getTimeString().split(":")[0]);
    					if(courseAssigment.getCourseSlot().getDayString().equals("TU")) {
    				 
    						// hour and 15 slots exists 
    							// so have to check if it overlaps 2 slot 
    						String hourString = courseAssigment.getCourseSlot().getTimeString().split(":")[0];
    						
    						hourMatch = hourMatch || Integer.toString((Integer.parseInt(hourString)+1)).equals(labAssigment.getLabSlot().getTimeString().split(":")[0]);
    					}
    					
    					if(dayMatch && hourMatch) {
    						 valid = false;
                             break;
    					}
					if(labAssigment.getLabSlot().getDayString().equals("FR")){
						String hourString = labAssigment.getLabSlot().getTimeString().split(":")[0];
						
						hourMatch = hourMatch || Integer.toString((Integer.parseInt(hourString)+1)).equals(courseAssigment.getCourseSlot().getTimeString().split(":")[0]);
					}
    					if(dayMatch && hourMatch) {
    						 valid = false;
                             			 break;
    					}
    					
    				}
    			}
        	}
    	}
    }

    public void checkUnwanted(){
        for (int i = 0; i < courseAssigments.size(); i++){
            if(courseAssigments.get(i).getCourseSlot() == null){
                continue;
            }
            else{
                ArrayList<CourseSlot> unwantedList = courseAssigments.get(i).getCourse().getUnwanted();
                for (int j = 0; j < unwantedList.size(); j++){
                    if (courseAssigments.get(i).getCourseSlot().equals(unwantedList.get(j))){
                        valid = false;
                        break;
                    }
                }
            }
            if(valid == false){
                break;
            }
        }
	for (int i = 0; i < labAssigments.size(); i++){
            if(labAssigments.get(i).getLabSlot() == null){
                continue;
            }
            else{
                ArrayList<LabSlot> unwantedList = labAssigments.get(i).getLab().getUnwanted();
                for (int j = 0; j < unwantedList.size(); j++){
                    if (labAssigments.get(i).getLabSlot().equals(unwantedList.get(j))){
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
    /**
     * will need testing but i belive that this should check everything 
     */
    public void checkCompatible(){
    	// for each course assigment to its unwanted pairs 
    	for(CourseAssignment course: courseAssigments) {
    		for(ClassElement pair: course.getCourse().getNonCompatible()) {
    			if(this.assignedSlot.get(pair) == null || course.getCurrentSlot() == null) {
    				continue;
    			}
                String courseName = course.getCourse().getCourseName();
                // If the course is 413, then make sure it and any of it's compatibles aren't on Tuesday 18:00
                if (courseName.equals("CPSC 413") || courseName.equals("CPSC 313")) {
                    boolean checkItself = check813and913(course.getCourse());
                    boolean checkOther = check813and913(pair);

                    if (!checkItself || !checkOther) {
                        return;
                    }
                }

                // get the pair and master slot
                Slot pairSlot = this.assignedSlot.get(pair);
                Slot masterSlot = course.getCurrentSlot();

    			// make sure both of them match
                boolean dayMatch = pairSlot.getDayString().equals(masterSlot.getDayString());
				boolean timeMatch = pairSlot.getTimeString().equals(masterSlot.getTimeString());
				
				// if they are at the same time then this is not valid 
				if(dayMatch && timeMatch) {
					valid = false;
                    break;
				}
    		}
    		if(!valid){
                break;
            }
    	}

    	// then here we check the labs against its unwanted pairs 
    	for(LabAssignment lab: labAssigments) {
    		// for the compadible 
    		for(ClassElement pair: lab.getLab().getNonCompatible()) {
    			if(this.assignedSlot.get(pair) == null || lab.getCurrentSlot() == null) {
    				continue;
    			}
    			// get the pair and master slot 
    			Slot pairSlot = assignedSlot.get(pair);
                Slot masterSlot = lab.getCurrentSlot();
    			// make sure both of them match 
                boolean dayMatch = pairSlot.getDayString().equals(masterSlot.getDayString());
				boolean timeMatch = pairSlot.getTimeString().equals(masterSlot.getTimeString());
				
				// if they are at the same time then this is not valid 
				if(dayMatch && timeMatch) {
					valid = false;
                    break;
				}
    			
    		}
    		if(!valid){
                break;
            }
    	}
    }
    
    
    
    /**
     * assume this is broken 
     */
    //checks if a course is on tuesday at 11:00
    public void checkTuesEleven(){
        for (int i = 0; i < courseAssigments.size(); i++){
            if (courseAssigments.get(i).getCourseSlot() == null) {
                continue;
            }

            if (courseAssigments.get(i).getCourseSlot().getTimeString().equals("11:00") && courseAssigments.get(i).getCourseSlot().getDayString().equals("TU")){ //checks if course is at 11:00 and on a tuesday
                valid = false; 
                break;    
            }
        }
    }
    
    /**
     * assume this is broken 
     */

    //checks if lecture number ends in 9, it must be assigned to an evening slot
    public void checkLectureNine(){
        for (int i = 0; i < courseAssigments.size(); i++){
            if (courseAssigments.get(i).getCourseSlot() == null) {
                continue;
            }
            if (courseAssigments.get(i).getCourse().getSectionNumber() >= 9){
                if(Integer.parseInt(courseAssigments.get(i).getCourseSlot().getTimeString().split(":")[0]) < 18){
                    valid = false;
                    break;
                }
            }
        }
    }

    //checks for 813 and if it is assigned at 18:00, and is not associated with any course or lab for 313
    // TODO: If 313 or any of it's non-compatibles go into 18:00 we say invalid
    public boolean check813and913(ClassElement element){
        Slot pairSlot = this.assignedSlot.get(element);
        if (element.getElementType().equals("Lab")) {
            String dayString = pairSlot.getDayString();
            String timeString = pairSlot.getTimeString();
            if (dayString.equals("TU") && timeString.equals("18:00")) {
                valid = false;
                return false;
            }
        }
        else {
            String dayString = pairSlot.getDayString();
            String timeString = pairSlot.getTimeString();
            if (dayString.equals("TU")) {
                if (timeString.equals("17:00") ||  timeString.equals("18:30"))
                valid = false;
                return false;
            }
        }

        return true;
    }

    public void check500(){
        for (int i = 0; i < courseAssigments.size(); i++){
            if(courseAssigments.get(i).getCourseSlot() == null){
                continue;
            }
            String courseName = courseAssigments.get(i).getCourse().getCourseName();
            char firstDigit = courseName.charAt(5);
            
            if(firstDigit == '5'){
                scheduled500.add(courseAssigments.get(i));
            }   
        }

        for (int i = 0; i < scheduled500.size(); i++){
            for (int j = i + 1; j < scheduled500.size(); j++) {
                if(scheduled500.get(i).getCourseSlot().getTime() == scheduled500.get(j).getCourseSlot().getTime()){
                    if(scheduled500.get(i).getCourseSlot().getDay() == scheduled500.get(j).getCourseSlot().getDay()){
                        valid = false;
                        break;
                    }
                }
            }
        }
    }

    public boolean isValid(){
        return this.valid;
    }
}
