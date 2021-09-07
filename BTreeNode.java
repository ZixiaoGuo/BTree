import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Author: Zixiao Guo
 * RedId: 822029189
 * CS635 Assignment 1
 * Section 2
 * 9/7/2021
 */
public class BTreeNode {

    /**
     * The order of b-tree is 3
     */
    private static final int M = 3;
    LinkedList<Student> students;       // entry of students in one node
    BTreeNode parentNode;
    LinkedList<BTreeNode> childrenNode;

    /**
     * Creates new node of b-tree and instantiate its student entries and child nodes
     */
    public BTreeNode() {
        this.students = new LinkedList<Student>();
        this.childrenNode = new LinkedList<BTreeNode>();
    }

    /**
     * Constructs a new node and assign the parent pointer to the node passed in
     * @param parent pointer of parent node
     */
    public BTreeNode(BTreeNode parent) {
        this();
        this.parentNode = parent;
    }

    /**
     * Insert student object to the b-tree, if node is empty, instantiate the children nodes
     * search from the root to determine which node to insert, then insert the node
     * if new node is generated during the insertion, we need to find the new root to return
     * @param student student object to insert into
     * @return returns the root node of the b-tree
     */
    public BTreeNode insert(Student student) {
        if(isEmpty()) {
            students.add(student);
            childrenNode.add(new BTreeNode(this));
            childrenNode.add(new BTreeNode(this));
            return this;
        }
        BTreeNode p = getRoot().search(student);
        insertNode(p.parentNode, student, new BTreeNode());
        return getRoot();
    }

    /**
     * Insert student object into the student entries of the target node
     * we also need to add the additional child node to fit the increase of the node
     * @param node target node to insert student object
     * @param student student to insert into the target node
     * @param extraChildNode extra child node comes with the new student entry
     */
    private void insertNode(BTreeNode node, Student student, BTreeNode extraChildNode) {
        int valueIndex = 0;
        while(valueIndex < node.students.size() && node.students.get(valueIndex)
                .getName().compareTo(student.getName()) < 0) {
            valueIndex++;
        }
        //TODO: make a function to compare student name for code abstraction
        node.students.add(valueIndex, student);

        //insert additional child node to fit the increase
        extraChildNode.parentNode = node;
        node.childrenNode.add(valueIndex+1, extraChildNode);

        // if size is greater or equal to order, need to generate new nodes
        if(node.students.size() > M-1) {
            //TODO: make a seperate function to make it more OOP
            /*
             since this is an order 3 b-tree, when the new node need to be generated,
             the middle student entry get promoted, which index equals to M/2 = 1
             */
            int upIndex = 1;
            Student studentPromoted = node.students.get(upIndex);

            // instantiate a new node and moves the entries and child nodes into it
            BTreeNode rightNode = new BTreeNode();
            rightNode.students = new LinkedList(node.students.subList(upIndex+1, M));
            rightNode.childrenNode = new LinkedList(node.childrenNode.subList(upIndex+1, M+1));
            for(BTreeNode rChild : rightNode.childrenNode) {
                rChild.parentNode = rightNode;
            }

            // remove previously assigned node, if the node is root node, generate new node as root
            node.students = new LinkedList(node.students.subList(0, upIndex));
            node.childrenNode = new LinkedList(node.childrenNode.subList(0, upIndex+1));
            if(node.parentNode == null) {
                node.parentNode = new BTreeNode();
                node.parentNode.students.add(studentPromoted);
                node.parentNode.childrenNode.add(node);
                node.parentNode.childrenNode.add(rightNode);
                rightNode.parentNode = node.parentNode;
                return;
            }
            insertNode(node.parentNode, studentPromoted, rightNode);
        }
    }

    private BTreeNode spilitNode (BTreeNode node) {
        if(node.students.size() > M-1) {
            //TODO: make a seperate function to make it more OOP
            /*
             since this is an order 3 b-tree, when the new node need to be generated,
             the middle student entry get promoted, which index equals to M/2 = 1
             */
            int upIndex = 1;
            Student studentPromoted = node.students.get(upIndex);

            // instantiate a new node and moves the entries and child nodes into it
            BTreeNode rightNode = new BTreeNode();
            rightNode.students = new LinkedList(node.students.subList(upIndex+1, M));
            rightNode.childrenNode = new LinkedList(node.childrenNode.subList(upIndex+1, M+1));
            for(BTreeNode rChild : rightNode.childrenNode) {
                rChild.parentNode = rightNode;
            }

            // remove previously assigned node, if the node is root node, generate new node as root
            node.students = new LinkedList(node.students.subList(0, upIndex));
            node.childrenNode = new LinkedList(node.childrenNode.subList(0, upIndex+1));
            if(node.parentNode == null) {
                node.parentNode = new BTreeNode();
                node.parentNode.students.add(studentPromoted);
                node.parentNode.childrenNode.add(node);
                node.parentNode.childrenNode.add(rightNode);
                rightNode.parentNode = node.parentNode;
                return;
            }
            insertNode(node.parentNode, studentPromoted, rightNode);
        }
    }

    /**
     * Recursively search the target entry inside the b-tree
     * if found the target, return the current node, otherwise call the method on the children
     * if the result is empty node, return the empty node
     * @param target the target student object
     * @return returns the search result
     */
    public BTreeNode search(Student target) {
        if(isEmpty()) {
            return this;
        }
        int valueIndex = 0;
        while(valueIndex < students.size() && students.get(valueIndex).getName().compareTo(target.getName()) <= 0) {
            if(students.get(valueIndex).getName().compareTo(target.getName()) == 0) {
                return this;
            }
            valueIndex++;
        }
        return childrenNode.get(valueIndex).search(target);
    }

    /**
     * Finds the root node
     * @return the root node
     */
    public BTreeNode getRoot() {
        BTreeNode p = this;
        while(!p.isRoot()) {
            p = p.parentNode;
        }
        return p;
    }

    /**
     * Checks if a node is empty
     * @return returns true if node is empty, false otherwise
     */
    public boolean isEmpty() {
        if(students == null || students.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Check if current node is root
     * @return
     */
    public boolean isRoot() {
        return parentNode == null;
    }

    /**
     * Print the tree structure
     */
    public void print() {
        printNode(this, 0);
    }

    private void printNode(BTreeNode node, int depth) {
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < depth; i++) {
            sb.append("|    ");
        }
        if(depth > 0) {
            sb.append("|----");
        }
        sb.append(node.students);
        System.out.println(sb.toString());
        for(BTreeNode child : node.childrenNode) {
            printNode(child, depth+1);
        }
    }

    public Student search (int index) {
        ArrayList<Student> students = new ArrayList<Student>();
        students = search(index, students);
        if (index > students.size()) {
            throw new IndexOutOfBoundsException();
        }
        else {
            Collections.sort(students, Comparator.comparing(Student::getName));
            return students.get(index -1);
        }
    }

    private ArrayList<Student> search(int index, ArrayList<Student> students) {

        if (!this.isEmpty()) {
            for (BTreeNode childNode : this.childrenNode) {
                childNode.search(index, students);
            }
            for (Student student : this.students) {
                students.add(student);
            }

        }

        return students;
    }

    public ArrayList<Student> searchForProbationStudents (BTreeNode node, ArrayList<Student> studentsOnProbation) {
        if (!node.isEmpty()) {
            for (BTreeNode childNode : node.childrenNode) {
                childNode.searchForProbationStudents(childNode, studentsOnProbation);
            }
            for (Student student : this.students) {
                if (student.getGpa() < 2.85f) {
                    studentsOnProbation.add(student);
                }
            }

        }
        Collections.sort(studentsOnProbation, Comparator.comparing(Student::getName));
        return studentsOnProbation;
    }

    public ArrayList<Student> searchForStudentWithGoodGPA (BTreeNode node, ArrayList<Student> goodGPAStudents) {
        if (!node.isEmpty()) {
            for (BTreeNode childNode : node.childrenNode) {
                childNode.searchForStudentWithGoodGPA(childNode, goodGPAStudents);
            }
            for (Student student : this.students) {
                if (student.getGpa() == 4.0f) {
                    goodGPAStudents.add(student);
                }
            }

        }
        Collections.sort(goodGPAStudents, Comparator.comparing(Student::getName));
        return goodGPAStudents;
    }




    /*
    public void searchForProbationStudents () {
        if (!this.isEmpty()) {
            for (BTreeNode childNode : this.childrenNode) {
                childNode.searchForProbationStudents();
            }
        }
    }

     */
}