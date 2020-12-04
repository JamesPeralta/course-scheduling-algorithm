package Or_Tree;
import DataStructures.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Prob implements Comparable<Prob>{
    private ArrayList<CourseAssignment> courses;
    private ArrayList<LabAssignment> labs;
    private double fitness;

    public Prob(Department department) {
        this.courses = new ArrayList<CourseAssignment>();
        this.labs = new ArrayList<LabAssignment>();
        Random rand = new Random();
        fitness = rand.nextInt(100);

        setUnassignedCourses(department.getCourses());
        setUnassignedLabs(department.getLabs());
    }

    /*
    Getters and Setters
     */
    private void setUnassignedCourses(ArrayList<CourseInstance> courses) {
        for (CourseInstance course: courses) {
            this.courses.add(new CourseAssignment(course));
        }
    }

    private void setUnassignedLabs(ArrayList<LabSection> labs) {
        for (LabSection lab: labs) {
            this.labs.add(new LabAssignment(lab));
        }
    }

    public void assignCourse(int index, CourseSlot slot) {
        courses.get(index).assignSlot(slot);
    }

    public ArrayList<CourseAssignment> getCourses() {
        return courses;
    }

    public ArrayList<LabAssignment> getLabs() {
        return labs;
    }

    public void unassignCourse(int index){
        courses.get(index).unassignSlot();
    }

    public void assignLab(int index, LabSlot slot) {
        labs.get(index).assignSlot(slot);
    }

    public void unassignLab(int index){
        labs.get(index).unassignSlot();
    }

    public Boolean coursesFilled() {
        if (courses.get(courses.size()-1).getCourseSlot() == null) {
            return false;
        }
        return true;
    }

    public Boolean labsFilled() {
        if (labs.get(labs.size()-1).getLabSlot() == null) {
            return false;
        }
        return true;
    }

    /*
    Genetic Operators
    */
    public Prob crossover(Prob mate, Department department) {
        Prob child = new Prob(department);
        this.orderProb();
        mate.orderProb();
        child.orderProb();

        // Crossover Course Slots
        for (int i = 0; i < courses.size(); i++) {
            if (i < (courses.size() / 2)) {
                child.assignCourse(i, this.courses.get(i).getCourseSlot());
            }
            else {
                child.assignCourse(i, mate.courses.get(i).getCourseSlot());
            }
        }

        // Crossover Lab Slots
        for (int i = 0; i < labs.size(); i++) {
            if (i < (courses.size() / 2)) {
                child.assignLab(i, this.labs.get(i).getLabSlot());
            }
            else {
                child.assignLab(i, mate.labs.get(i).getLabSlot());
            }
        }

        return child;
    }

    public void mutate(Department department) {
        Random rand = new Random();
        ArrayList<CourseSlot> courseSlots = department.getCourseSlots();
        ArrayList<LabSlot> labSlots = department.getLabSlots();

        // Mutate Course Slots
        for (int i = 0; i < courses.size(); i++) {
            if (rand.nextDouble() < 0.20) {
                this.assignCourse(i, courseSlots.get(rand.nextInt(courseSlots.size())));
            }
        }

        // Mutate Lab Slots
        for (int i = 0; i < labs.size(); i++) {
            if (rand.nextDouble() < 0.20) {
                this.assignLab(i, labSlots.get(rand.nextInt(labSlots.size())));
            }
        }
    }

    public void orderProb() {
        Collections.sort(courses);
        Collections.sort(labs);
    }
    
    
    /**
     * will return the bound depending on how many soft contrainsts it breaks 
     * 
     * @return
     */
    public int eval() {
    	
    	int pen_coursemin = 0;
    	
    	int pen_labsmin = 0;
    	
    	// each slot will have a courseMin(s) and a labMax(s)
    	
    	// for everone of our slots 
    	for(CourseAssignment assign : this.courses) {
    		
    		
    	}
    	
    	// for each of the labs slots 
    	for(LabAssignment assign: this.labs) {
    		
    	}
    	
    	
    	
    	
    	
    	int bound = 0;
    	
    	
    	
    	
    	
    	
    	
    	
    	return bound;
    }
    
    

    public String toString(){
        orderProb();
        String output = "";
        output += "________________________________\n";
        output += "Fitness: " + Double.toString(fitness) + "\n";
        for (CourseAssignment course: this.courses) {
            String courseName = course.getCourse().getCourseName();
            String courseNumber = course.getCourse().getSectionString();
            String assignmentDay = course.getCourseSlot() != null ? course.getCourseSlot().getDayString() : "None";
            String assignmentTime = course.getCourseSlot() != null ? course.getCourseSlot().getTimeString() : "None";

            if (assignmentDay.equals("None")) {
                output += courseName + " " + courseNumber + ": " + assignmentDay + "\n";
            }
            else {
                output += courseName + " " + courseNumber + ": " + assignmentDay + " " + assignmentTime + "\n";
            }
        }

        for (LabAssignment lab: this.labs) {
            String labCourse = lab.getLab().getOfCourse();
            String labSection = lab.getLab().getOfSection();
            String labNumber = lab.getLab().getTutString();
            String assignmentDay = lab.getLabSlot() != null ? lab.getLabSlot().getDayString() : "None";
            String assignmentTime = lab.getLabSlot() != null ? lab.getLabSlot().getTimeString() : "None";

            String labString = "";
            if (labSection == null) {
                labString = labCourse + " " + labNumber;
            }
            else {
                labString = labCourse + " " + labSection + " " + labNumber;
            }

            if (assignmentDay.equals("None")) {
                output += labString + ": " + assignmentDay + "\n";
            }
            else {
                output += labString + ": " + assignmentDay + " " + assignmentTime + "\n";
            }
        }
        output += "________________________________\n";
        return output;
    }

    @Override
    public int compareTo(Prob o) {
        return (int) (this.fitness - o.fitness);
    }
}
