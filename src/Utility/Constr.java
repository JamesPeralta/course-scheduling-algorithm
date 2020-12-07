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

        assignedinCourses = new HashMap<CourseSlot, Integer>();
        assignedinLabs = new HashMap<LabSlot, Integer>();
        scheduled500 = new ArrayList<CourseAssignment>();

        this.assignedSlot = new  HashMap<ClassElement,Slot>();
        
        
		// we need to build our class elements to slot thing so we know 
		for(CourseAssignment courses: this.courseAssigments) {
			//System.out.println(courses);
			//System.out.println(courses.getCourseSlot());
			this.assignedSlot.put(courses.getCourse(), courses.getCurrentSlot());
		}
		// do it for the labs too 
		for(var labs: this.labAssigments) {
			this.assignedSlot.put(labs.getLab(), labs.getCurrentSlot());
		}

        if (valid == true) checkCourseMax();
        if (valid == true) checkLabMax();
        if (valid == true) checkAssign();
        if (valid == true) checkTuesEleven();
        if (valid == true) checkUnwanted();
        if (valid == true) checkCompatible();
        if (valid == true) checkLectureNine();
        if (valid == true) check813();
        if (valid == true) check913();
        if (valid == true) check500();
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
    //checks if a course and lab are assigned to the same slot
    public void checkAssign(){
    	
    	// for each course
    		// and for each lab 
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
    					if(courseAssigment.getCourse().getSectionString().equals(labAssigment.getLab().getOfSection())) {
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
    					boolean hourMatch = (courseAssigment.getCourseSlot().getTime()) == (labAssigment.getLabSlot().getTime());
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
    					
    				}
    				
    				// 
    				
    			}
        		
        	}
    	}
//        for(int i = 0; i < courseAssigments.size(); i++){
//            for(int j = 0; j < labAssigments.size(); j++){
//                if (courseAssigments.get(i).getCourseSlot() == null || labAssigments.get(j).getLabSlot() == null) {
//                    continue;
//                }
//                System.out.println("============================================================");
//                System.out.println(courseAssigments.get(i).getCourseSlot().getTimeString());
//                System.out.println(courseAssigments.get(i).getCourseSlot().getTime());
//                System.out.println(labAssigments.get(j).getLabSlot().getTimeString());
//               
//                System.out.println(labAssigments.get(j).getLabSlot().getTime());
//                if((courseAssigments.get(i).getCourseSlot().getDay()) == (labAssigments.get(j).getLabSlot().getDay()) && (courseAssigments.get(i).getCourseSlot().getTime()) == (labAssigments.get(j).getLabSlot().getTime())){ 
//                	//check if the course has the same day and time as a lab
//                    if(courseAssigments.get(i).getCourse().getCourseName().equals(labAssigments.get(j).getLab().getOfCourse())){ 
//                    	//checks if the lab corresponds to the same course and are the same section
//                        if (!labAssigments.get(j).getLab().getOfSection().equals("")){
//                            if (courseAssigments.get(i).getCourse().getSectionString().equals(labAssigments.get(j).getLab().getOfSection())) {
//                                valid = false;
//                                break;
//                            }
//                        }
//                        else {
//                            valid = false;
//                            break;
//                        }
//                    }
//                }
//            }
//        }
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
    	
    	// lets redo this loop
    	
    	// for each course assigment to its unwanted pairs 
    	for(CourseAssignment course: courseAssigments) {
    		// for the compadible 
    		for(ClassElement pair: course.getCourse().getNonCompatible()) {
    			if(this.assignedSlot.get(pair) == null || course.getCurrentSlot() == null) {
    				continue;
    			}
    			// get the pair and master slot 
    			Slot pairSlot = this.assignedSlot.get(pair);
                Slot masterSlot = course.getCurrentSlot();
    			// make sure both of them match 
                //System.out.println(pairSlot);
                //System.out.println(course.getCurrentSlot());
                boolean dayMatch = pairSlot.getDayString().equals(masterSlot.getDayString());
				boolean timeMatch = pairSlot.getTimeString().equals(masterSlot.getTimeString());
				
				// if they are at the same time then this is not valid 
				if(dayMatch && timeMatch) {
					valid = false;
                    break;
				}
    			
    		}
    		if(valid == false){
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
    		if(valid == false){
                break;
            }
    	}
    	
    	
//        for (int i = 0; i < courseAssigments.size(); i++){//checks all compatible for course vs course
//            if(courseAssigments.get(i).getCourse() == null){
//                continue;
//            }
//            ArrayList<ClassElement> nonCompatible = courseAssigments.get(i).getCourse().getNonCompatible();
//            for(int j = 0; j < nonCompatible.size(); j++){
//                
//                Slot pairSlot = assignedSlot.get(nonCompatible.get(j));
//                Slot masterSlot = assignedSlot.get(courseAssigments.get(i));
//                // need to do a null check incase 
//                if(pairSlot.getDay() == masterSlot.getDay()){
//                    if(pairSlot.getTime() == masterSlot.getTime()){
//                        valid = false;
//                        break;
//                    }
//                }
//                
//            }
//            if(valid == false){
//                break;
//            }
//        }
//
//        for (int i = 0; i < labAssigments.size(); i++){ //checks all compatible for lab vs lab
//            if(labAssigments.get(i).getLab() == null){
//                continue;
//            }
//            ArrayList<ClassElement> nonCompatible = labAssigments.get(i).getLab().getNonCompatible();
//            for(int j = 0; j < nonCompatible.size(); j++){
//                 
//                Slot pairSlot = assignedSlot.get(nonCompatible.get(j));
//                Slot masterSlot = assignedSlot.get(labAssigments.get(i));
//
//                if(pairSlot.getDay() == masterSlot.getDay()){
//                    if(pairSlot.getTime() == masterSlot.getTime()){
//                        valid = false;
//                        break;
//                    }
//                }
//                 
//            }
//            if(valid == false){
//                break;
//            }
//        }
//
//        for (int i = 0; i < courseAssigments.size(); i++){ //checks all compatible for lab vs course
//            if(courseAssigments.get(i).getCourse() == null){
//                continue;
//            }
//            ArrayList<ClassElement> nonCompatible = courseAssigments.get(i).getCourse().getNonCompatible();
//            for(int j = 0; j < nonCompatible.size(); j++){
//                
//                Slot pairSlot = assignedSlot.get(nonCompatible.get(j));
//                Slot masterSlot = assignedSlot.get(labAssigments.get(i));
//
//                if(pairSlot.getDay() == masterSlot.getDay()){
//                    if(pairSlot.getTime() == masterSlot.getTime()){
//                        valid = false;
//                        break;
//                    }
//                }
//                  
//            }
//            if(valid == false){
//                break;
//            }
//        }
//
//        for (int i = 0; i < labAssigments.size(); i++){//checks all compatible for course vs lab
//        	if(labAssigments.get(i).getLab() == null){
//                continue;
//            }
//            
//            // why are we getting the lab here based on a course index 
//            ArrayList<ClassElement> nonCompatible = labAssigments.get(i).getLab().getNonCompatible();
//            for(int j = 0; j < nonCompatible.size(); j++){
//                try{
//                    Slot pairSlot = assignedSlot.get(nonCompatible.get(j));
//                    Slot masterSlot = assignedSlot.get(courseAssigments.get(i));
//                    if(pairSlot.getDay() == masterSlot.getDay()){
//                        if(pairSlot.getTime() == masterSlot.getTime()){
//                            valid = false;
//                            break;
//                        }
//                    }
//                }
//                catch(NullPointerException npe){
//                }    
//            }
//            if(valid == false){
//                break;
//            }
//        }
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
                if(courseAssigments.get(i).getCourseSlot().getTime() < 18){
                    valid = false;
                    break;
                }
            }
        }
    }

    //checks for 813 and if it is assigned at 18:00, and is not associated with any course or lab for 313
    public void check813(){
    	for(CourseAssignment courseAssignment: this.courseAssigments) {
    		// null check
    		if(courseAssignment == null) {
    			continue;
    		}
    		//System.out.println(courseAssignment.getCourse().getCourseName());
    		// check if it is 813 course 
    		if(courseAssignment.getCourse().getCourseName().equals("CPSC 813")) {
    			// if it is see if it is in the correct slot 
    			
    			// null check for this part
    			if(courseAssignment.getCourseSlot() != null) {
    				//System.out.println(courseAssignment.getCourseSlot().getDayString());
    				
    				// if it is not assigned to the 1800 slot then we 
    				boolean hourMatch = courseAssignment.getCourseSlot().getTimeString().equals("18:00");
    				boolean dayMatch = courseAssignment.getCourseSlot().getDayString().equals("TU");
    				
    				if(!(hourMatch && dayMatch)) {
    					valid = false;
                        break;
    				}
    				
    				
    			}
    			
    			
    		}

			// check to see that it is not at the same time as the 313 class 
    		if(courseAssignment.getCourse().getCourseName().equals("CPSC 313")) {
    			
    			//	null check 
    			if(courseAssignment.getCourseSlot() != null) {
    				// make sure it is not at the same time 
    				
    				for(CourseAssignment conflictingCourse: this.courseAssigments) {
    					if(conflictingCourse.getCourse().getCourseName().equals("CPSC 813")) {
    		    			// check the slot 
    		    			
    		    			// null check for this part
    		    			if(conflictingCourse.getCourseSlot() != null) {
    		    				
    		    				// make sure they are not on the same slot 
    		    				if(conflictingCourse.getCourseSlot() == courseAssignment.getCourseSlot()) {
    		    					valid = false;
    		                        break;
    		    				}
    		    				
    		    			}
    		    		}
    					
    				}
    				
    			}
    			
    			
    			
    			
    		}
    		
    		
    		
    		
    		
    		
    		
    		
    	}
    	
    	
    	// check to make sure it is not at the same time as the tutorial 
		for(LabAssignment labAssigment: this.labAssigments) {
			// null check and check if it is a 313 tutoral 
			if(labAssigment.getLabSlot() != null &&
					labAssigment.getLab().getTutString().contains("TUT") &&
					labAssigment.getLab().getTutString().contains("CPSC 313")) {
				//System.out.println(labAssigment.getLab().getFullTutName());
				
				// find the 813 and make sure it is not at the same time 
				for(CourseAssignment conflictingCourse: this.courseAssigments) {
					if(conflictingCourse.getCourse().getCourseName().equals("CPSC 813")) {
		    			// check the slot 
		    			
		    			// null check for this part
		    			if(conflictingCourse.getCourseSlot() != null) {
		    				boolean dayMatch = conflictingCourse.getCurrentSlot().getDayString().equals(labAssigment.getCurrentSlot().getDayString());
							boolean timeMatch = conflictingCourse.getCurrentSlot().getTimeString().equals(labAssigment.getCurrentSlot().getTimeString());
							// if it occurs on a differnt slot then we want it too we should add
							// the penalty value 
							
							if(!( dayMatch && timeMatch )){
								// the pair occurs on a different time slot 
								valid = false;
		                        break;
							}
		    			 
		    				
		    			}
		    		}
					
				}
				
			}
		}
    }
    
    //similar to check813 but for 913 
    // 
    public void check913(){
    	//System.out.println("=================================================");
    	// check to see if there is 
    	for(CourseAssignment courseAssignment: this.courseAssigments) {
    		// null check
    		if(courseAssignment == null) {
    			continue;
    		}
    		//System.out.println(courseAssignment.getCourse().getCourseName());
    		// check if it is 913 course 
    		if(courseAssignment.getCourse().getCourseName().equals("CPSC 913")) {
    			// if it is see if it is in the correct slot 
    			
    			// null check for this part
    			if(courseAssignment.getCourseSlot() != null) {
    				//System.out.println(courseAssignment.getCourseSlot().getDayString());
    				
    				// if it is not assigned to the 1800 slot then we 
    				boolean hourMatch = courseAssignment.getCourseSlot().getTimeString().equals("18:00");
    				boolean dayMatch = courseAssignment.getCourseSlot().getDayString().equals("TU");
    				
    				if(!(hourMatch && dayMatch)) {
    					valid = false;
                        break;
    				}
    				
    				
    			}
    			
    			
    		}

			// check to see that it is not at the same time as the 413 class 
    		if(courseAssignment.getCourse().getCourseName().equals("CPSC 413")) {
    			//	null check 
    			if(courseAssignment.getCourseSlot() != null) {
    				// make sure it is not at the same time 
    				
    				for(CourseAssignment conflictingCourse: this.courseAssigments) {
    					if(conflictingCourse.getCourse().getCourseName().equals("CPSC 913")) {
    		    			// check the slot 
    		    			 
    		    			// null check for this part
    		    			if(conflictingCourse.getCourseSlot() != null) {
    		    				boolean dayMatch = conflictingCourse.getCurrentSlot().getDayString().equals(courseAssignment.getCurrentSlot().getDayString());
    							boolean timeMatch = conflictingCourse.getCurrentSlot().getTimeString().equals(courseAssignment.getCurrentSlot().getTimeString());
    		    				// make sure they are not on the same slot 
    		    				if(( dayMatch && timeMatch )) {
    		    					valid = false;
    		                        break;
    		    				}
    		    				
    		    			}
    		    		}
    				}
    			}
    		}
    	}
    	
    	
    	// check to make sure it is not at the same time as the tutorial 
		for(LabAssignment labAssigment: this.labAssigments) {
			// null check and check if it is a 413 tutoral 
			if(labAssigment.getLabSlot() != null &&
					labAssigment.getLab().getTutString().contains("TUT") &&
					labAssigment.getLab().getTutString().contains("CPSC 413")) {
				//System.out.println(labAssigment.getLab().getFullTutName());
				
				// find the 913 and make sure it is not at the same time 
				for(CourseAssignment conflictingCourse: this.courseAssigments) {
					if(conflictingCourse.getCourse().getCourseName().equals("CPSC 913")) {
		    			// check the slot 
		    			
		    			// null check for this part
		    			if(conflictingCourse.getCourseSlot() != null) {
		    				
		    				// make sure they are not on the same slot  TODO this may not actual check
		    				if(conflictingCourse.getCurrentSlot() == labAssigment.getCurrentSlot()) {
		    					valid = false;
		                        break;
		    				}
		    				
		    			}
		    		}
					
				}
				
			}
		}

    }
    /**
     * assume this is broken 
     */
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
