import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

import DataStructures.CourseAssignment;
import DataStructures.CourseSlot;
import DataStructures.LabAssignment;
import DataStructures.LabSlot;

// the eval class
public class Eval {
	
	// our courses and labs 
	private ArrayList<CourseAssignment> courses;
    private ArrayList<LabAssignment> labs;
    
    
    // we need the array list of the slots so we can get the min and max 
    private ArrayList<CourseSlot> courseSlots;
    private ArrayList<LabSlot> labSlots;
    
    private int bound;
    
    private int pen_coursemin = 0;
    private int pen_labsmin = 0;
    
    
    
	Eval(ArrayList<CourseSlot> courseSlots,ArrayList<LabSlot> labSlot,ArrayList<CourseAssignment> courses,ArrayList<LabAssignment> labs){
		// asign the things 
		this.courseSlots = courseSlots;
		this.labSlots = labSlot;
		this.courses = courses;
		this.labs = labs;
		
		// inialize the bound value 
		this.bound = 0;
		
		
		// check the course max and mins
		this.checkNumOfAssigment();
		

		// professor preforence for slots 
		this.checkPreference();
		
		
		// see if courses are schdule for the same slot
		this.checkPaired();
		
		// differn sections hsould be schudled at differnt times 
		this.checkSimislarSections();
		
	}
	
	private void checkNumOfAssigment() {
		
		// create a dictionary where were get the number of courses per slot 
		HashMap<CourseSlot, Integer> coursesPer = new HashMap<CourseSlot, Integer>();
		for(CourseAssignment assigment: this.courses) {
			if(coursesPer.containsKey(assigment.getCourseSlot())) {
				// incrment the value by one 
				coursesPer.put(
						assigment.getCourseSlot(), 
						coursesPer.get(assigment.getCourseSlot()) + 1
						);
			}else {
				// set the key to one 
				coursesPer.put(
						assigment.getCourseSlot(), 
						 1
						);
			}
			
		}
		
		// now iterate though all the keys and add to the bound based on how many 
		// are broken 
		for(CourseSlot slot :coursesPer.keySet()) {
			
			
			// check the course min and add the bound  
			if(slot.getCoursemin() > coursesPer.get(slot)) {
				this.bound += this.pen_coursemin;
			}
			
		}
		
		
		
		// now check the labs 
		HashMap<LabSlot, Integer> labsPer = new HashMap<LabSlot, Integer>();
		for(LabAssignment assigment: this.labs) {
			if(labsPer.containsKey(assigment)) {
				labsPer.put(
						assigment.getLabSlot(),
						labsPer.get(assigment.getLabSlot()) + 1
						);
			}else {
				labsPer.put(
						assigment.getLabSlot(),
						 1
						);
			}
		}
		
		// now see if they reach the lab min 
		for(LabSlot slot : labsPer.keySet()) {
			if(slot.getCoursemin() > labsPer.get(slot)) {
				this.bound += this.pen_labsmin;
			}
		}
	}
	/**
	 * goal is to see 
	 */
	private void checkPreference() {
		
	}
	
	/**
	 * 
	 */
	private void checkPaired() {
		
		// we will get a list of pairs 
		
		// we need to loop though the list of pairs 
		
		
	}
	/**
	 * 
	 */
	private void checkSimislarSections() {
		// we need to make sure that all of a section is not at the same time 
		
	}
	
	
}
