package Utility;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import DataStructures.ClassElement;
import DataStructures.CourseAssignment;
import DataStructures.CourseInstance;
import DataStructures.CourseSlot;
import DataStructures.Department;
import DataStructures.LabAssignment;
import DataStructures.LabSection;
import DataStructures.LabSlot;
import DataStructures.Slot;

// the eval class
public class Eval {
	
	// our courses and labs 
	private ArrayList<CourseAssignment> courses;
    private ArrayList<LabAssignment> labs;
    
    
    // we need the array list of the slots so we can get the min and max 
   
    
    private HashMap<ClassElement,Slot> assigments; 
    
    // here are the course elements 
   
    
    // the department 
    private Department department;
    
    private int bound;
    
    
    // here are the different penalties for each bound
    private int pen_coursemin = 0;
    private int pen_labsmin = 0;
    private int pen_pair = 0;
    private int pen_prefernce = 0;
    /**
     * treated as a calculated value for now since depending on 
     * the position of who wanted the presence we can assign a different score to it 
     * @param position position in the list 
     * @param n number of items in the list 
     * @return
     */
    private int pen_prefernce_calculated(int position,int n) {
    	return  this.pen_prefernce * ( (position-1) / n);
    }
    
    // 
    private int pen_section = 0;

	public Eval(ArrayList<CourseAssignment> courses, ArrayList<LabAssignment> labs, Department department){
		this.courses = courses;
		this.labs = labs;
		this.department = department;
		this.bound = 0;
		
		// Check courses
		this.checkNumOfAssigment();
		this.checkPreference();
//		this.checkPaired();
//		this.checkSimilarSections();
	}
	
	// the only public 
	public int getBound() {
		return this.bound;
	}
	
	private void checkNumOfAssigment() {
		HashMap<CourseSlot, Integer> coursesPer = new HashMap<>();
		for(CourseAssignment assigment: this.courses) {
			CourseSlot courseSlot = assigment.getCourseSlot();
			if (courseSlot == null) {
				continue;
			}

			if(coursesPer.containsKey(courseSlot)) {
				coursesPer.put(courseSlot, coursesPer.get(courseSlot) + 1);
			}
			else {
				coursesPer.put(courseSlot, 1);
			}
		}
		for(CourseSlot slot: coursesPer.keySet()) {
			if(slot.getCoursemin() > coursesPer.get(slot)) {
				this.bound += this.pen_coursemin;
			}
		}

		HashMap<LabSlot, Integer> labsPer = new HashMap<>();
		for(LabAssignment assigment: this.labs) {
			LabSlot labSlot = assigment.getLabSlot();
			if (labSlot == null) {
				continue;
			}

			if(labsPer.containsKey(labSlot)) {
				labsPer.put(labSlot, labsPer.get(labSlot) + 1);
			}
			else {
				labsPer.put(labSlot, 1);
			}
		}
		for(LabSlot slot : labsPer.keySet()) {
			if(slot.getCoursemin() > labsPer.get(slot)) {
				this.bound += this.pen_labsmin;
			}
		}
	}

	private void checkPreference() {
		for(CourseAssignment course: this.courses) {
			CourseSlot slot =  course.getCourseSlot();

			if (slot == null) {
				continue;
			}

			if(course.getCourse().getPreference().containsKey(slot)) {
				Object[] keys = course.getCourse().getPreference().keySet().toArray();
				int[] scores = new int[keys.length];
				for(int i =0; i<keys.length;i++) {
					scores[i] = course.getCourse().getPreference().get(keys[i]);
				}
				
				// now that its sorted we need to find its places 
				int sum = 0;
				int targetScore = course.getCourse().getPreference().get(slot);
				for(int i =0; i<keys.length;i++) {
					// we found it 
					if(scores[i] != targetScore) {
						sum += scores[i];
					}
					
					
					
				}
				
				// add the penalty, for now the penalty is caluclated more so 
				// we can easly change this in a function above 
				this.bound += sum;
			}
		}
		
		
	}

	private void checkPaired() {
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

	private void checkSimilarSections() {
		for(String course: this.department.getCourseMap().keySet()) {
			ArrayList<CourseInstance> courseSet = this.department.getCourseMap().get(course);
			// check all the courses 
			for(int i=0;i<courseSet.size();i++) {
				// we want to make sure we only check each one once and not muliple 
				// or else we might get a compound effect for the first ones 
				for(int j=i;j<courseSet.size();j++) {
					// just dont check yourself i dont want to get any duplicate values 
					if(i!=j) {
						
						// if they are at the same time then we need to add the bound 
						
						Slot course1 = null;
						Slot course2 = null;
						
						for(CourseAssignment thing : this.courses) {
							// check to see if it is either 
							if(thing.getCourse() == courseSet.get(i)) {
								course1 = thing.getCurrentSlot();
							}else if(thing.getCourse() == courseSet.get(j)) {
								course2 = thing.getCurrentSlot();
							}
							if(course1 != null && course2 != null) {
								break;
							}
							
						}
						
						// now check to see if the courses are the same ones 
						if(course1 != null && course2 != null) {
							if(course1 == course2) {
								this.bound += this.pen_section;
							}
						}
					}
				}
			}
			
		}

		// now lets check all of the labs together 
		for(String lab: this.department.getLabMap().keySet()) {
			ArrayList<LabSection> labSet = this.department.getLabMap().get(lab);
			for(int i=0;i<labSet.size();i++) {
				// we want to make sure we only check each one once and not muliple 
				// or else we might get a compound effect for the first ones 
				for(int j=i;j<labSet.size();j++) {
					// just dont check yourself i dont want to get any duplicate values 
					if(i!=j) {
						
						Slot lab1 = null;
						Slot lab2 = null;
						for(LabAssignment thing : this.labs) {
							if(thing.getLab() == labSet.get(i)) {
								lab1 = thing.getCurrentSlot();
							}else if(thing.getLab() == labSet.get(j)) {
								lab2 = thing.getCurrentSlot();
							}
							if(lab1 != null && lab2 != null) {
								break;
							}
						}
						
						// now check to see if the courses are the same ones 
						if(lab1 != null && lab2 != null) {
							if(lab1 == lab2) {
								this.bound += this.pen_section;
							}
						}
					}
				}
			}
		}
	}
	
	
}
