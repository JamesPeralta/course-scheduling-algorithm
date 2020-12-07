package DataStructures;

public class Iterator {
    private int count;

    public Iterator() {
        count = 0;
    }

    public void incrementCount() {
        count += 1;
    }

    public int getCount() {
        return count;
    }
}
