package DataStructures;

public class LabAssignment implements Comparable<LabAssignment>{
    private Lab lab;
    private LabSlot labSlot;

    public LabAssignment(Lab lab) {
        this.lab = lab;
        this.labSlot = null;
    }

    public Lab getLab() {
        return lab;
    }

    public LabSlot getLabSlot() {
        return labSlot;
    }

    public void assignSlot(LabSlot slot){
        labSlot = slot;
    }
    public void unassignSlot(){
        labSlot = null;
    }

    @Override
    public int compareTo(LabAssignment o) {
        return o.getLab().getFullTutName().compareTo(o.getLab().getFullTutName());
    }
}
