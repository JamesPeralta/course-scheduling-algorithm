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
import Or_Tree.Prob;
 
public class Eval {
	
	// our courses and labs 
	private ArrayList<CourseAssignment> courses;
    private ArrayList<LabAssignment> labs;
    public static HashMap<String, ArrayList<String>> params = new HashMap<String, ArrayList<String>>();
    
    
    // we need the array list of the slots so we can get the min and max 
   
    
    private HashMap<ClassElement,Slot> assigments; 
    
    // here are the course elements 
   
    
    // the department 
    private Department department;
    
    private int bound;
    
    
    // here are the different penalties for each bound
    private int pen_coursemin ;
    private int pen_labsmin ;
    private int pen_pair ;
    private int pen_section ;;

    // Different weights for each soft constraint
	private int wMinFilled  ;
	private int wPref  ;
	private int wPair;
	private int wSecDiff ;

	public Eval(Prob prob, Department department){
		 
		// make sure to get the values 
		pen_coursemin = Integer.parseInt(Eval.params.get("pen_coursemin").get(0));
	    pen_labsmin = Integer.parseInt(Eval.params.get("pen_labsmin").get(0));
	    pen_pair = Integer.parseInt(Eval.params.get("pen_pair").get(0));
	    pen_section = Integer.parseInt(Eval.params.get("pen_section").get(0));;

	    // Different weights for each soft constraint
		wMinFilled = Integer.parseInt(Eval.params.get("wMinFilled").get(0));;
		wPref = Integer.parseInt(Eval.params.get("wPerf").get(0));;
		wPair = Integer.parseInt(Eval.params.get("wPair").get(0));;
		wSecDiff = Integer.parseInt(Eval.params.get("wSecDiff").get(0));;
		
		
		this.courses = prob.getCourses();
		this.labs = prob.getLabs();
		this.department = department;

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
		int evalMinFilled = this.checkNumOfAssigment() * wMinFilled;
		int evalPref = this.checkPreference() * wPref;
		
		int evalPair = this.checkPaired() * wPair;
		
		int evalSecDiff = this.checkSimilarSections() * wSecDiff;
		
		
		// add the bounds with the weight 
		this.bound = evalMinFilled + evalPref + evalPair + evalSecDiff;
	}
	
	// the only public 
	public int getBound() {
		return this.bound;
	}
	
	private int checkNumOfAssigment() {
		int localBound = 0;
//		System.out.println("=========================================");

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
//				System.out.println("We found a new one so set it to 1");
				coursesPer.put(courseSlot, 1);
			}
		}
//		System.out.println(coursesPer);
		// modify this loop so that it counts all the slots not just the ones in the maop 
		for(CourseSlot slot: this.department.getCourseSlots()) {
//			System.out.println(slot);
//			System.out.println(coursesPer.get(slot));
//			System.out.println(slot.getCoursemin());
			
			// if the slot does not exists then we need to see if its min is bigger than 0
			if(!coursesPer.containsKey(slot)) {
				
				if(slot.getCoursemin() != 0) {
					localBound += this.pen_coursemin;
				}
			}else if(slot.getCoursemin() > coursesPer.get(slot)) {
				localBound += this.pen_coursemin;
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
		for(LabSlot slot : this.department.getLabSlots()) {
			// if we did not find anything assigned to that slot it was a 0 
			if(!labsPer.containsKey(slot)) {
				// if the min courses is not 0 then we broke that constraint 
				if(slot.getCoursemin() != 0) {
					localBound += this.pen_labsmin;
				}
			}else if(slot.getCoursemin() > labsPer.get(slot)) {
				localBound += this.pen_labsmin;
			}
		}

		return localBound;
	}

	private int checkPreference() {
		int localSum = 0;
		// we need to make a map of list that tells us for each course slot what 
		// is the relavent thing 
		HashMap<CourseSlot, HashMap<CourseInstance,Integer>> courseSlotMap = new HashMap<CourseSlot, HashMap<CourseInstance,Integer>>();
		for(CourseAssignment course: this.courses) {
			// each course can have multiple scores but we want it tied to the same thing 
			
			//CourseSlot slot =  course.getCourseSlot();
			HashMap<CourseSlot, Integer> perference = course.getCourse().getPreference();
			
			for(CourseSlot slot: perference.keySet()) {
				// for each slot pereence per course we should add it to the proper thing 
				if(courseSlotMap.containsKey(slot)) {
					courseSlotMap.get(slot).put(course.getCourse(),perference.get(slot) );   
				}else {
					courseSlotMap.put(slot, new HashMap<CourseInstance,Integer>());
					courseSlotMap.get(slot).put(course.getCourse(),perference.get(slot) ); 
				}
			}
			
			
		}
		//System.out.println(slotMap);
		
		
		// for each slot we need to sum 
		for(CourseSlot slot: courseSlotMap.keySet()) {
			CourseAssignment currenAssigment = null;
			// get the current course in that slot 
			for(CourseAssignment course: this.courses) {
				if(course.getCourseSlot() == slot) {
					currenAssigment = course;
					break;
				}
			}
			
			// this part sums up all of the scores for that slot 
			Object[] keys = courseSlotMap.get(slot).keySet().toArray();
			int[] scores = new int[keys.length];
			for(int i =0; i<keys.length;i++) {
				scores[i] = courseSlotMap.get(slot).get(keys[i]);
			}
			
			// now with this lists we should sum the array and then subtract the score of 
			// the current one 
			int sum = 0;
			for(int i =0; i<keys.length;i++) {
				sum += scores[i];
			}
			
			// now if the currentAssigment to this slot exists in the course map the 
			// we should substract its score 
			if(currenAssigment != null &&  courseSlotMap.get(slot).containsKey(currenAssigment.getCourse())) {
				sum -= courseSlotMap.get(slot).get(currenAssigment.getCourse());
			}

			localSum += sum;
		}
		
		
		
		// now we need to check each labs slot 
		HashMap<LabSlot, HashMap<LabSection,Integer>> labSlotMap = new HashMap<LabSlot, HashMap<LabSection,Integer>>();
		for(LabAssignment lab: this.labs) {
			HashMap<LabSlot, Integer> perference = lab.getLab().getPreference();
			
			for(LabSlot slot: perference.keySet()) {
				// for each slot pereence per course we should add it to the proper thing 
				if(labSlotMap.containsKey(slot)) {
					labSlotMap.get(slot).put(lab.getLab(),perference.get(slot) );   
				}else {
					labSlotMap.put(slot, new HashMap<LabSection,Integer>());
					labSlotMap.get(slot).put(lab.getLab(),perference.get(slot) ); 
				}
			}
			
		}
		
		// now i have that list we need to sum up the scores
		// for each map 
		for(LabSlot slot: labSlotMap.keySet()) {
			LabAssignment currenAssigment = null;
			// get the current course in that slot 
			for(LabAssignment lab: this.labs) {
				if(lab.getLabSlot() == slot) {
					currenAssigment = lab;
					break;
				}
			}
			
			// now that we have found its assigment 
			Object[] keys = labSlotMap.get(slot).keySet().toArray();
			int[] scores = new int[keys.length];
			for(int i =0; i<keys.length;i++) {
				scores[i] = labSlotMap.get(slot).get(keys[i]);
			}
			
			// now with this lists we should sum the array and then subtract the score of 
			// the current one 
			int sum = 0;
			for(int i =0; i<keys.length;i++) {
				sum += scores[i];
			}
			
			// now if the currentAssigment to this slot exists in the course map the 
			// we should substract its score 
			if(currenAssigment != null &&  labSlotMap.get(slot).containsKey(currenAssigment.getLab())) {
				sum -= labSlotMap.get(slot).get(currenAssigment.getLab());
			}
			
			localSum += sum;
		}
		

		return localSum;
	}
	
	/**
	 * todo is test this thing right here lol 
	 * test case 
	 */
	private int checkPaired() {
		int localBound = 0;

		// for each course assigment
		for(CourseAssignment courseAssigment: this.courses) {
			// check to see if there are classes that should be compadible to this
			if(courseAssigment.getCourse().getCompatible().size() > 0 ) {
				
				// for each one see if it is at the same time as its pair 
				for(ClassElement pair:  courseAssigment.getCourse().getCompatible()) {
					// need to find where it is assigned 
					Slot pairSlot = this.assigments.get(pair);
					
					// then we can check it to the assugment slot
					Slot masterSlot =  courseAssigment.getCurrentSlot();
					if(masterSlot == null || pairSlot==null) {
						continue;
					}
					// then see if the 2 slots are the same 
					boolean dayMatch = pairSlot.getDayString().equals(masterSlot.getDayString());
					boolean timeMatch = pairSlot.getTimeString().equals(masterSlot.getTimeString());
					// if it occurs on a differnt slot then we want it too we should add
					// the penalty value 
					
					if(!( dayMatch && timeMatch )){
						// the pair occurs on a different time slot 
						localBound += this.pen_pair;
					}
				}
			}
		}
		
		// repeart the same thing above but for labs 
		for(LabAssignment labAssigment: this.labs) {
			// see if each one has some comparable
			// then we should check to see if 
			if(labAssigment.getLab().getCompatible().size() > 0) {
				for(ClassElement pair:  labAssigment.getLab().getCompatible()) {
					Slot pairSlot = this.assigments.get(pair);
					
					Slot masterSlot =  labAssigment.getCurrentSlot();
					if(masterSlot == null|| pairSlot==null) {
						continue;
					}
					
					boolean dayMatch = pairSlot.getDayString().equals(masterSlot.getDayString());
					boolean timeMatch = pairSlot.getTimeString().equals(masterSlot.getTimeString());
					
					if (!(dayMatch && timeMatch)){
						// the lab does not appear at the same time so we igore it
						localBound += this.pen_pair;
					}
				}
			}
		}

		return localBound;
	}
	/**
	 * Checks for similar sections and avoids checking the same pair of course sections twice 
	 * 
	 * there might be an issue compairing the courses, in that case we need to go and change it 
	 * to the same comparison as above 
	 */
	private int checkSimilarSections() {
		int localBound = 0;

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
								localBound += this.pen_section;
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
								localBound += this.pen_section;
							}
						}
					}
				}
			}
		}

		return localBound;
	}
}
