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
    private int pen_pair = 2;
  
    private int pen_section = 20;

	public Eval(ArrayList<CourseAssignment> courses, ArrayList<LabAssignment> labs, Department department){
		this.courses = courses;
		this.labs = labs;
		this.department = department;
		this.bound = 0;
		
		
		this.assigments = new  HashMap<ClassElement,Slot>();
		// we need to build our class elements to slot thing so we know 
		for(CourseAssignment course: this.courses) {
			this.assigments.put(course.getCourse(), course.getCurrentSlot());
		}
		// do it for the labs too 
		for(var lab: this.labs) {
			this.assigments.put(lab.getLab(), lab.getCurrentSlot());
		}
		
		
		
		
		// Check courses
		this.checkNumOfAssigment(); 
		this.checkPreference(); 
		this.checkPaired();
		this.checkSimilarSections();
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
		System.out.println("Checking Prefernece");
		
		// we need to make a map of list that tells us for each course slot what 
		// is the relavent thing 
		HashMap<CourseSlot, HashMap<CourseInstance,Integer>> slotMap = new HashMap<CourseSlot, HashMap<CourseInstance,Integer>>();
		for(CourseAssignment course: this.courses) {
			// each course can have multiple scores but we want it tied to the same thing 
			
			//CourseSlot slot =  course.getCourseSlot();
			HashMap<CourseSlot, Integer> perference = course.getCourse().getPreference();
			
			for(CourseSlot slot: perference.keySet()) {
				// for each slot pereence per course we should add it to the proper thing 
				if(slotMap.containsKey(slot)) {
					slotMap.get(slot).put(course.getCourse(),perference.get(slot) );   
				}else {
					slotMap.put(slot, new HashMap<CourseInstance,Integer>());
					slotMap.get(slot).put(course.getCourse(),perference.get(slot) ); 
				}
			}
			
		}
		
		
		// for each slot we need to sum 
		for(CourseSlot slot: slotMap.keySet()) {
			CourseAssignment currenAssigment = null;
			// get the current course in that slot 
			for(CourseAssignment course: this.courses) {
				if(course.getCourseSlot() == slot) {
					currenAssigment = course;
					break;
				}
			}
			
			// this part sums up all of the scores for that slot 
			Object[] keys = slotMap.get(slot).keySet().toArray();
			int[] scores = new int[keys.length];
			for(int i =0; i<keys.length;i++) {
				scores[i] = slotMap.get(slot).get(keys[i]);
				//System.out.println(scores[i]);
			}
			
			// now with this lists we should sum the array and then subtract the score of 
			// the current one 
			int sum = 0;
			for(int i =0; i<keys.length;i++) {
				sum += scores[i];
			}
			
			// now if the currentAssigment to this slot exists in the course map the 
			// we should substract its score 
			if(currenAssigment != null &&  slotMap.get(slot).containsKey(currenAssigment.getCourse())) {
				sum -= slotMap.get(slot).get(currenAssigment.getCourse());
			}
			
			this.bound += sum;
			
		}
		
	
		
		

		
		
	}
	
	/**
	 * todo is test this thing right here lol 
	 * test case 
	 */
	private void checkPaired() {
		System.out.println("Chceking Pairs");
		// for each course assigment
		for(CourseAssignment courseAssigment: this.courses) {
			// check to see if there are classes that should be compadible to this  
			System.out.println("Checking the pairs for "+  courseAssigment.getCourse().getFullCourseName());
			if(courseAssigment.getCourse().getCompatible().size() > 0 ) {
				
				// for each one see if it is at the same time as its pair 
				for(ClassElement pair:  courseAssigment.getCourse().getCompatible()) {
					System.out.println("asdasdasda");
					System.out.println(pair);
					// need to find where it is assigned 
					Slot pairSlot = this.assigments.get(pair);
					
					// then we can check it to the assugment slot
					Slot masterSlot =  courseAssigment.getCurrentSlot();
					
					// then see if the 2 slots are the same 
					boolean dayMatch = pairSlot.getDayString() == masterSlot.getDayString();
					boolean timeMatch = pairSlot.getTimeString() == masterSlot.getTimeString();
					System.out.println("1"+  pairSlot);
					System.out.println("2"+masterSlot);
					// if it occurs on a differnt slot then we want it too we should add
					// the penalty value 
					
					if(!( dayMatch && timeMatch )){
						System.out.println("They do not match");
						// the pair occurs on a different time slot 
						this.bound += this.pen_pair;
					}
				}
			}
		}
		// repeart the same thing above but for labs 
		for(LabAssignment labAssigment: this.labs) {
			// see if each one has some comparable 
			System.out.println("Checking the pairs for "+  labAssigment.getLab().getFullTutName());
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
	 * Checks for similar sections and avoids checking the same pair of course sections twice 
	 * 
	 * there might be an issue compairing the courses, in that case we need to go and change it 
	 * to the same comparison as above 
	 */
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
							// if the are at the same time then we add the penalty since we want them 
							// at seperate times 
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
