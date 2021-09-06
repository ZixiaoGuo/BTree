import java.util.LinkedList;

/**
 * Author:  bielu
 * Date:    2018/3/13 23:12
 * Desc:    M阶B-Tree的节点
 */
public class BTreeNode {
    /**
     * B树的阶
     */
    int M;

    /**
     * 关键字列表
     */
    LinkedList<Student> students;

    /**
     * 父节点
     */
    BTreeNode parent;

    /**
     * 孩子列表
     */
    LinkedList<BTreeNode> children;

    /**
     * 构造一棵空的B-树
     */
    private BTreeNode() {
        this.students = new LinkedList<Student>();
        this.children = new LinkedList<BTreeNode>();
    }

    /**
     * 构造一棵空的m阶B-树
     *
     * @param m B-树的阶
     */
    public BTreeNode(int m) {
        this();
        if(m < 3) {
            throw new RuntimeException("The order of B-Tree should be greater than 2.");
        }
        this.M = m;
    }

    /**
     * 根据父节点构造一个空的孩子节点
     *
     * @param parent 父节点
     */
    public BTreeNode(BTreeNode parent) {
        this(parent.M);
        this.parent = parent;
    }


    public BTreeNode insert(Student student) {
        if(isEmpty()) {
            students.add(student);
            children.add(new BTreeNode(this));
            children.add(new BTreeNode(this));
            return this;
        }
        BTreeNode p = getRoot().search(student);
        if(!p.isEmpty()) {
            throw new RuntimeException("cannot insert duplicate key to B-Tree, key: " + student);
        }
        insertNode(p.parent, student, new BTreeNode(p.M));
        return getRoot();
    }

    private void insertNode(BTreeNode node, Student student, BTreeNode eNode) {
        int valueIndex = 0;
        while(valueIndex < node.students.size() && node.students.get(valueIndex).getName().compareTo(student.getName()) < 0) {
            valueIndex++;
        }
        node.students.add(valueIndex, student);
        eNode.parent = node;
        node.children.add(valueIndex+1, eNode);
        if(node.students.size() > M-1) {
            // 获取上升关键字
            int upIndex = M/2;
            Student up = node.students.get(upIndex);
            // 当前节点分为左右两部分，左部的parent不变，右部的parent放在上升关键字右侧
            BTreeNode rNode = new BTreeNode(M);
            rNode.students = new LinkedList(node.students.subList(upIndex+1, M));
            rNode.children = new LinkedList(node.children.subList(upIndex+1, M+1));
            /*  由于rNode.children是从node.children分离出来的,其parent仍指向node，
                所以需要将rNode.children的parent改为指向rNode
             */
            for(BTreeNode rChild : rNode.children) {
                rChild.parent = rNode;
            }
            node.students = new LinkedList(node.students.subList(0, upIndex));
            node.children = new LinkedList(node.children.subList(0, upIndex+1));
            // 从根节点中上升，选取上升关键字作为新的根节点
            if(node.parent == null) {
                node.parent = new BTreeNode(M);
                node.parent.students.add(up);
                node.parent.children.add(node);
                node.parent.children.add(rNode);
                rNode.parent = node.parent;
                return;
            }
            // 父节点增加关键字，递归调用
            insertNode(node.parent, up, rNode);
        }
    }

    /**
     * 从当前节点往下查找目标值target
     *
     * @param target
     * @return 找到则返回找到的节点，不存在则返回叶子节点
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
        return children.get(valueIndex).search(target);
    }

    /**
     * 获取根节点
     *
     * @return 根节点
     */
    public BTreeNode getRoot() {
        BTreeNode p = this;
        while(!p.isRoot()) {
            p = p.parent;
        }
        return p;
    }

    /**
     * 判断当前节点是否是空节点
     *
     * @return 空节点返回true, 非空节点返回false
     */
    public boolean isEmpty() {
        if(students == null || students.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前节点是否是根节点
     *
     * @return 是根节点返回true, 不是返回false
     */
    public boolean isRoot() {
        return parent == null;
    }

    /*
     * 清空当前节点, 保留父关系
     */
    public void clear() {
        students.clear();
        children.clear();
    }

    /**
     * 以当前节点为根，在控制台打印B-树
     */
    public void print() {
        printNode(this, 0);
    }

    /**
     * 控制台打印节点的递归调用
     *
     * @param node 要打印节点
     * @param depth 递归深度
     */
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
        for(BTreeNode child : node.children) {
            printNode(child, depth+1);
        }
    }
}