
public class Student {

    private String name;
    private int redId;
    private float gpa;

    public Student(String name, int redId, float gpa) {
        this.name = name;
        this.redId = redId;
        this.gpa = gpa;
    }

    public Student() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRedId() {
        return redId;
    }

    public void setRedId(int redId) {
        this.redId = redId;
    }

    public float getGpa() {
        return gpa;
    }

    public void setGpa(float gpa) {
        this.gpa = gpa;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", redId=" + redId +
                ", gpa=" + gpa +
                '}';
    }
}