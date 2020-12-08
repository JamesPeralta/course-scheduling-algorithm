package DataStructures;

import java.util.*;

public class ClassElement {
    private ArrayList<ClassElement> nonCompatible = new ArrayList<>();
    // these are the ones that should happen at the same time but dont have too ? 
    private ArrayList<ClassElement> compatible = new ArrayList<>();
    private String elementType;

    public ClassElement() {
    	
    }

    public void setElementType(String s){
        elementType = s;
    }

    public String getElementType(){
        return elementType;
    }

    public void addNonCompatible(ClassElement e) {
        this.nonCompatible.add(e);
    }

    public void addCompatible(ClassElement e) {
        this.compatible.add(e);
    }

    public ArrayList<ClassElement> getNonCompatible() {
        ArrayList<ClassElement> copy = (ArrayList<ClassElement>) this.nonCompatible.clone();
        Collections.shuffle(copy);
        return copy;
    }
    
    // get the compadible slots 
    public ArrayList<ClassElement> getCompatible() {
        return this.compatible;
    }
}
