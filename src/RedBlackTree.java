public class Tree {
    private Node root;

    private class Node {
        private int value;

        private Node parent;
        private Node leftChild;
        private Node rightChild;
    }

    private Node findLeaf(int value, Node leaf) {
        if (leaf == null) {
            return null;
        }
        if (leaf.value == value) {
            return leaf;
        }
        if (leaf.value > value) {
            findLeaf(value, leaf.leftChild);
        }
        return findLeaf(value, leaf.rightChild);
    }
}
