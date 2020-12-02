package DataStructures;

public class Lab {
    private String ofCourse;
    private String ofSection;
    private int tut;

    public Lab(String course, String section, int tut) {
        this.ofCourse = course;
        this.ofSection = section;
        this.tut = tut;
    }

    public Lab(String course, int tut) {
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
}
