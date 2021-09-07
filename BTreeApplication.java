import java.util.ArrayList;

public class BTreeApplication {

    public static void main(String[] args) {
        testCase1();
    }

    public static void testCase1() {
        Student student1 = new Student("a", 822018452, 2.99f);
        Student student2 = new Student("c", 822018431, 3.10f);
        Student student3 = new Student("b", 822018455, 3.5f);
        Student student4 = new Student("d", 822018412, 2.80f);
        Student student5 = new Student("e", 822017897, 4.0f);
        Student student6 = new Student("f", 822017897, 2.74f);
        Student student7 = new Student("g", 822017897, 3.01f);
        Student student8 = new Student("h", 822017897, 3.99f);
        Student student9 = new Student("i", 822017897, 2.55f);
        Student student10 = new Student("j", 822017897, 4.0f);
        Student student11 = new Student("k", 822017897, 3.01f);
        Student student12 = new Student("l", 822017897, 4.0f);
        Student student13 = new Student("m", 822017897, 3.01f);
        Student student14 = new Student("n", 822017897, 2.11f);
        Student student15 = new Student("o", 822017897, 2.01f);


        BTreeNode node = new BTreeNode();
        node = node.insert(student1);
        node = node.insert(student2);
        node = node.insert(student3);
        node.print();
        System.out.println("----------------------------------");
        node = node.insert(student4);
        node = node.insert(student5);
        node = node.insert(student6);
        node = node.insert(student7);
        node = node.insert(student8);
        node = node.insert(student9);
        node = node.insert(student10);
        node = node.insert(student11);
        node = node.insert(student12);
        node = node.insert(student13);
        node = node.insert(student14);
        node.print();
        ArrayList<Student> studentOnProbation = new ArrayList<Student>();
        studentOnProbation = node.searchForProbationStudents(node, studentOnProbation);
        ArrayList<Student> studentWithGoodGPA = new ArrayList<Student>();
        studentWithGoodGPA = node.searchForStudentWithGoodGPA(node, studentWithGoodGPA);
        System.out.println(studentOnProbation);
        System.out.println(studentWithGoodGPA);
        Student targetStudent = new Student();
        targetStudent = node.search(14);
        System.out.println(targetStudent);





        //node.print();

    }
}