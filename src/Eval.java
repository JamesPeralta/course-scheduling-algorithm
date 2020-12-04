import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

import DataStructures.ClassElement;
import DataStructures.CourseAssignment;
import DataStructures.CourseSlot;
import DataStructures.LabAssignment;
import DataStructures.LabSlot;
import DataStructures.Slot;

// the eval class
public class Eval {
	
	// our courses and labs 
	private ArrayList<CourseAssignment> courses;
    private ArrayList<LabAssignment> labs;
    
    
    // we need the array list of the slots so we can get the min and max 
    private ArrayList<CourseSlot> courseSlots;
    private ArrayList<LabSlot> labSlots;
    
    private HashMap<ClassElement,Slot> assigments; 
    
    // here are the course elements 
    private ClassElement classElements;
    
    private int bound;
    
    
    // here are the differnent penalties for each bound
    private int pen_coursemin = 0;
    private int pen_labsmin = 0;
    private int pen_pair = 0;
    private int pen_section = 0;
    
    
    
    /**
     * extend to inculde pairs, and prefences
     * @param courseSlots
     * @param labSlot
     * @param courses
     * @param labs
     */
	Eval(ArrayList<CourseSlot> courseSlots,
			ArrayList<LabSlot> labSlot,
			ArrayList<CourseAssignment> courses,
			ArrayList<LabAssignment> labs,
			ClassElement classElements){
		// asign the things 
		this.courseSlots = courseSlots;
		this.labSlots = labSlot;
		this.courses = courses;
		this.labs = labs;
		this.classElements = classElements;
		
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
	 * Certain professors that often teach certain courses have certain preferences regarding in 
	 * which time slots their courses and labs should be scheduled. Naturally, we see this as something 
	 * that should be treated as soft constraint. Depending on a to-be-determined ranking scheme, each 
	 * professor will be awarded a certain set of ranking points and he/she can distribute these points over 
	 * pairs of (course/lab, time slots). Formally, we assume a function preference: (Courses + Labs) x Slots -> 
	 * Natural numbers that reports those preferences.
	 */
	private void checkPreference() {
		
		
		
	}
	
	/**
	 * TODO need to make sure that it does no 
	 */
	private void checkPaired() {
		
		// we will get a list of pairs 
		// for each class see if it has a pair then see if the pair is at the same slot
		
		
		
		for(CourseAssignment courseAssigment: this.courses) {
			// check to see if there are classes that should be compadible to this  
			 
			if(courseAssigment.getCourse().getCompatible().size() > 0 ) {
				// for each one see if it is at the same time as its pair 
				for(ClassElement pair:  courseAssigment.getCourse().getCompatible()) {
					// need to find where it is assigned 
					Slot pairSlot = this.assigments.get(pair);
					
					
					
					// then we can check it to the assugment slot 
					
					Slot masterSlot =  courseAssigment.getCurrentSlot();
					
					
					// then see if the 2 slots are the same 
					boolean dayMatch = pairSlot.getDayString() == masterSlot.getDayString();
					boolean timeMatch = pairSlot.getTimeString() == masterSlot.getTimeString();
					if(dayMatch && timeMatch) {
						// then we have found that the pair is at the same time 
						
					}else {
						// the pair occurs on a differnt time slot 
						this.bound += this.pen_pair;
					}
					
					
					
				}
//				
			}
		}
		// we need to loop though the list
			// do the same for the labs 
		for(LabAssignment labAssigment: this.labs) {
			// see if each one has some comparables 
			
			// then we should check to see if 
			if(labAssigment.getLab().getCompatible().size() > 0) {
				for(ClassElement pair:  labAssigment.getLab().getCompatible()) {
					Slot pairSlot = this.assigments.get(pair);
					
					Slot masterSlot =  labAssigment.getCurrentSlot();
					
					
					boolean dayMatch = pairSlot.getDayString() == masterSlot.getDayString();
					boolean timeMatch = pairSlot.getTimeString() == masterSlot.getTimeString();
					
					if(dayMatch && timeMatch) {
						// then we have found that the pair is at the same time 
						
						// 
					}else {
						// the lab does not appear at the same time so we igore it 
						this.bound += this.pen_pair;
						
					}
					
					
				}
			}
				
		}
		
		
	}
	/**
	 * Different sections of a course should be scheduled at different times. 
	 * For each pair of sections that is scheduled into the same slot, we add a penalty 
	 * pen_section to the Eval-value of an assignment assign.
	 */
	private void checkSimislarSections() {
		
		// example we have l01, l02, l03
		
		/**
		 * say that l01 and l02 are at the same time 
		 * then we give a penalty for l01 and l02 but not when checking l02 to l02
		 */
		
		
	}
	
	
}
