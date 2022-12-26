import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
public class RedBlackTree<T extends Comparable<T>> implements IRedBlackTree<T>, Iterable<T>, Iterator<T> {

    enum NodeColors {
        RED,
        BLACK,
        NONE
    }

    private Node root;
    /**
     * Ограничитель, который обозначает нулевую ссылку.
     */
    private Node nil;
    private Node current;

    public class Node {

        private T value;
        private NodeColors color;

        private Node parent;

        private Node leftChild;
        private Node rightChild;

        public Node() {
            value = null;
            color = NodeColors.NONE;
            parent = null;
            leftChild = null;
            rightChild = null;
        }

        public Node(T value, NodeColors color) {
            this.value = value;
            this.color = color;
            parent = nil;
            leftChild = nil;
            rightChild = nil;
        }
        // ctor для создания копии
        public Node(Node node) {
            value = node.value;
            color = node.color;
            parent = node.parent;
            leftChild = node.leftChild;
            rightChild = node.rightChild;
        }

        public boolean isFree() {
            return value == null || value == nil;
        }

        public boolean isLeftFree() {
            return leftChild == null || leftChild == nil;
        }

        public boolean isRightFree() {
            return rightChild == null || rightChild == nil;
        }

        public boolean isParentFree() {
            return parent == null || parent == nil;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node node) {
            parent = node;
        }

        public Node getLeft() {
            return leftChild;
        }

        public void setLeft(Node node) {
            leftChild = node;
            if(leftChild != null && leftChild != nil) leftChild.parent = this;
        }

        public Node getRight() {
            return rightChild;
        }

        public void setRight(Node node) {
            rightChild = node;
            if(rightChild != null && rightChild != nil) rightChild.parent = this;
        }

        public boolean isBlack() {
            return color == NodeColors.BLACK;
        }

        public void makeBlack() {
            color = NodeColors.BLACK;
        }

        public boolean isRed() {
            return color == NodeColors.RED;
        }

        public void makeRed() {
            color = NodeColors.RED;
        }

        public NodeColors getColor() {
            return color;
        }

        public void setColor(NodeColors color) {
            this.color = color;
        }

        /**
         * Возвращет "дедушку" узла дерева.
         */
        public Node getGrandfatherNode() {
            if(parent != null && parent != nil)
                return parent.parent;
            return null;
        }

        /**
         * Возвращает следующий по значению узел дерева.
         */
        public Node getSuccessor()
        {
            Node temp = null;
            Node node = this;
            if(!node.isRightFree()) {
                temp = node.getRight();
                while(!temp.isLeftFree())
                    temp = temp.getLeft();
                return temp;
            }
            temp = node.getParent();
            while(temp != nil && node == temp.getRight()) {
                node = temp;
                temp = temp.getParent();
            }
            return temp;
        }

        public String getColorName() {
            return ((isBlack()) ? "B" : "R");
        }

    }


    private boolean isRemoved;

    /**
     * Конструктор по-умолчанию.
     */
    public RedBlackTree() {
        root = new Node();
        nil = new Node();
        nil.color = NodeColors.BLACK;
        root.parent = nil;
        root.rightChild = nil;
        root.leftChild = nil;
        isRemoved = false;
    }

    /**
     * Левый поворот дерева tree относительно узла node.
     */
    private static <T extends Comparable<T>> void leftRotate(RedBlackTree<T> tree, RedBlackTree<T>.Node node) {
        RedBlackTree<T>.Node nodeParent = node.getParent();
        RedBlackTree<T>.Node nodeRight = node.getRight();
        if(nodeParent != tree.nil) {
            if(nodeParent.getLeft() == node)
                nodeParent.setLeft(nodeRight);
            else
                nodeParent.setRight(nodeRight);
        }
        else {
            tree.root = nodeRight;
            tree.root.setParent(tree.nil);
        }
        node.setRight(nodeRight.getLeft());
        nodeRight.setLeft(node);
    }

    /**
     * Правый поворот дерева tree относительно узла node.
     */
    private static <T extends Comparable<T>> void rightRotate(RedBlackTree<T> tree, RedBlackTree<T>.Node node) {
        RedBlackTree<T>.Node nodeParent = node.getParent();
        RedBlackTree<T>.Node nodeLeft = node.getLeft();
        if(nodeParent != tree.nil) {
            if(nodeParent.getLeft() == node)
                nodeParent.setLeft(nodeLeft);
            else
                nodeParent.setRight(nodeLeft);
        }
        else {
            tree.root = nodeLeft;
            tree.root.setParent(tree.nil);
        }
        node.setLeft(nodeLeft.getRight());
        nodeLeft.setRight(node);
    }


    public static <T extends Comparable<T>> void printTree(RedBlackTree<T> tree) {
        ArrayList<RedBlackTree<T>.Node> nodes = new ArrayList<>();
        nodes.add(0, tree.root);
        printNodes(tree, nodes);
    }

    private static <T extends Comparable<T>> void printNodes(RedBlackTree<T> tree, ArrayList<RedBlackTree<T>.Node> nodes) {
        int childsCounter = 0;
        int nodesAmount = nodes.size();
        ArrayList<RedBlackTree<T>.Node> childs = new ArrayList<>();

        for(int i = 0; i < nodesAmount; i++) {
            if(nodes.get(i) != null && nodes.get(i) != tree.nil) {
                System.out.print("(" + nodes.get(i).getValue().toString() + "," + nodes.get(i).getColorName() + ")");
                if(!nodes.get(i).isLeftFree()) {
                    childs.add(nodes.get(i).getLeft());
                    childsCounter++;
                }
                else
                    childs.add(null);
                if(!nodes.get(i).isRightFree()) {
                    childs.add(nodes.get(i).getRight());
                    childsCounter++;
                }
                else
                    childs.add(null);
            }
            else {
                System.out.print("(nil)");
            }
        }
        System.out.print("\n");

        if(childsCounter != 0)
            printNodes(tree, childs);
    }

    @Override
    public void addNode(T o) {
        Node node = root, temp = nil;
        Node newNode = new Node((T)o, NodeColors.RED);
        while(node != null && node != nil && !node.isFree()) {
            temp = node;
            if(newNode.getValue().compareTo(node.getValue()) < 0)
                node = node.getLeft();
            else
                node = node.getRight();
        }
        newNode.setParent(temp);
        if(temp == nil)
            root.setValue(newNode.getValue());
        else {
            if(newNode.getValue().compareTo(temp.getValue()) < 0)
                temp.setLeft(newNode);
            else
                temp.setRight(newNode);
        }
        newNode.setLeft(nil);
        newNode.setRight(nil);
        rebalanceTreeAfterAdding(newNode);
    }

    /**
     Перебалансировка дерева при добавлении новой ноды.
     */
    private void rebalanceTreeAfterAdding(Node node) {
        Node temp;
        while(!node.isParentFree() && node.getParent().isRed()) {
            if(node.getParent() == node.getGrandfatherNode().getLeft()) {
                temp = node.getGrandfatherNode().getRight();
                if(temp.isRed()) {
                    temp.makeBlack();
                    node.getParent().makeBlack();
                    node.getGrandfatherNode().makeRed();
                    node = node.getGrandfatherNode();
                }
                else {
                    if(node == node.getParent().getRight()) {
                        node = node.getParent();
                        leftRotate(this, node);
                    }
                    node.getParent().makeBlack();
                    node.getGrandfatherNode().makeRed();
                    rightRotate(this, node.getGrandfatherNode());
                }
            }
            else {
                temp = node.getGrandfatherNode().getLeft();
                if(temp.isRed()) {
                    temp.makeBlack();
                    node.getParent().makeBlack();
                    node.getGrandfatherNode().makeRed();
                    node = node.getGrandfatherNode();
                }
                else {
                    if(node == node.getParent().getLeft()) {
                        node = node.getParent();
                        rightRotate(this, node);
                    }
                    node.getParent().makeBlack();
                    node.getGrandfatherNode().makeRed();
                    leftRotate(this, node.getGrandfatherNode());
                }
            }
        }
        root.makeBlack();
    }

    @Override
    public boolean removeNode(T o) {
        return remove(findNode(o));
    }


    private boolean remove(Node node)
    {
        Node temp = nil, successor = nil;

        if(node == null || node == nil)
            return false;

        if(node.isLeftFree() || node.isRightFree())
            successor = node;
        else
            successor = node.getSuccessor();

        if(!successor.isLeftFree())
            temp = successor.getLeft();
        else
            temp = successor.getRight();
        temp.setParent(successor.getParent());

        if(successor.isParentFree())
            root = temp;
        else if(successor == successor.getParent().getLeft())
            successor.getParent().setLeft(temp);
        else
            successor.getParent().setRight(temp);

        if(successor != node) {
            node.setValue(successor.getValue());
        }
        if(successor.isBlack())
            rebalanceTreeAfterRemoving(temp);
        return true;
    }

    /**
     Перебалансировка после удаления ноды.
     */
    private void rebalanceTreeAfterRemoving(Node node)
    {
        Node temp;
        while(node != root && node.isBlack()) {
            if(node == node.getParent().getLeft()) {
                temp = node.getParent().getRight();
                if(temp.isRed()) {
                    temp.makeBlack();
                    node.getParent().makeRed();
                    leftRotate(this, node.getParent());
                    temp = node.getParent().getRight();
                }
                if(temp.getLeft().isBlack() && temp.getRight().isBlack()) {
                    temp.makeRed();
                    node = node.getParent();
                }
                else {
                    if(temp.getRight().isBlack()) {
                        temp.getLeft().makeBlack();
                        temp.makeRed();
                        rightRotate(this, temp);
                        temp = node.getParent().getRight();
                    }
                    temp.setColor(node.getParent().getColor());
                    node.getParent().makeBlack();
                    temp.getRight().makeBlack();
                    leftRotate(this, node.getParent());
                    node = root;
                }
            }
            else {
                temp = node.getParent().getLeft();
                if(temp.isRed()) {
                    temp.makeBlack();
                    node.getParent().makeRed();
                    rightRotate(this, node.getParent());
                    temp = node.getParent().getLeft();
                }
                if(temp.getLeft().isBlack() && temp.getRight().isBlack()) {
                    temp.makeRed();
                    node = node.getParent();
                }
                else {
                    if(temp.getLeft().isBlack()) {
                        temp.getRight().makeBlack();
                        temp.makeRed();
                        leftRotate(this, temp);
                        temp = node.getParent().getLeft();
                    }
                    temp.setColor(node.getParent().getColor());
                    node.getParent().makeBlack();
                    temp.getLeft().makeBlack();
                    rightRotate(this, node.getParent());
                    node = root;
                }
            }
        }
        node.makeBlack();
    }

    @Override
    public boolean isContain(T o) {
        return (findNode(o) != nil);
    }

    private Node findNode(T o) {
        Node node = root;
        while(node != null && node != nil && node.getValue().compareTo(o) != 0) {
            if(node.getValue().compareTo(o) > 0)
                node = node.getLeft();
            else
                node = node.getRight();
        }
        return node;
    }


    private Node getFirst()
    {
        Node node = root;
        while(node.getLeft() != null && node.getLeft() != nil) {
            if(!node.isLeftFree())
                node = node.getLeft();
        }
        return node;
    }

    @Override
    public Iterator<T> iterator() {
        current = null;
        isRemoved = false;
        return this;
    }

    @Override
    public boolean hasNext() {
        if(current != null) {
            if(!isRemoved) {
                RedBlackTree<T>.Node node = current.getSuccessor();
                return (node != null && node != nil);
            }
            return (current != nil);
        }
        else {
            return (root != null && !root.isFree());
        }
    }

    @Override
    public T next() {
        if(current != null) {
            if(!isRemoved)
                current = current.getSuccessor();
            else
                isRemoved = false;
        }
        else {
            current = getFirst();
        }
        if(current == null || current == nil)
            throw new NoSuchElementException();
        return current.getValue();
    }

    @Override
    public void remove() {
        if(current != null && !isRemoved) {
            remove(current);
            isRemoved = true;
        }
        else
            throw new IllegalStateException();
    }
}