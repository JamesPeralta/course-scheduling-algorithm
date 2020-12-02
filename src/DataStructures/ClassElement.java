package DataStructures;

import java.util.*;

public class ClassElement {
    private ArrayList<ClassElement> nonCompatible = new ArrayList<>();

    public ClassElement() {

    }

    public void addNonCompatible(ClassElement e) {
        this.nonCompatible.add(e);
    }

    public ArrayList<ClassElement> getNonCompatible() {
        ArrayList<ClassElement> copy = (ArrayList<ClassElement>) this.nonCompatible.clone();
        Collections.shuffle(copy);
        return copy;
    }
}
