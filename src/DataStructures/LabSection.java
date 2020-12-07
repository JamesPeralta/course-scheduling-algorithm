package DataStructures;

import java.util.*;

public class LabSection extends ClassElement {
    private String ofCourse = "";
    private String ofSection = "";
    private int tut;
    private HashMap<LabSlot,Integer> preferences = new HashMap<>();
    private ArrayList<LabSlot> unwanted = new ArrayList<>();


    public LabSection(String course, String section, int tut) {
        this.ofCourse = course;
        this.ofSection = section;
        this.tut = tut;
    }

    public LabSection(String course, int tut) {
        this.ofCourse = course;
        this.tut = tut;
    }

    public String getOfCourse() {
        return ofCourse;
    }

    public String getOfSection() {
        return ofSection;
    }

    public String getFullTutName() {
        String labCourse = getOfCourse();
        String labSection = getOfSection();
        String labNumber = getTutString();

        String labString = "";
        if (labSection == null) {
            labString = labCourse + " " + labNumber;
        }
        else {
            labString = labCourse + " " + labSection + " " + labNumber;
        }

        return labString;
    }

    public int getTut() {
        return tut;
    }

    public String getTutString() {
        return "TUT " + Integer.toString(tut);
    }

    public boolean equals(LabSection l) {
        return this.ofCourse.equals(l.getOfCourse()) && this.ofSection.equals(l.getOfSection()) && (tut == l.getTut());
    }

    public void addUnwanted(LabSlot ls) {
        this.unwanted.add(ls);
    }
    
    public ArrayList<LabSlot> getUnwanted(){
        return this.unwanted;
    }

    public void addPreference(LabSlot ls, int value) {
        if(ls != null) preferences.put(ls, value);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(getFullTutName());

        str.append("\n\tPreferred Slots:");

        preferences.forEach((k,v) -> {
            str.append("\n\t\t");
            str.append(k.toStringMin() + " ");
            str.append(v);
        });

        str.append("\n\tUnwanted Slots:");

        for (LabSlot labSlot : unwanted) {
            str.append("\n\t\t");
            str.append(labSlot.toStringMin());
        }

        return str.toString();
    }
    
    public HashMap<LabSlot,Integer> getPreference() {
    	return this.preferences;
    }
}
