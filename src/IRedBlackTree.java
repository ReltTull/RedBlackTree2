public interface IRedBlackTree<T extends Comparable<T>> {


    void addNode(T o);
    
    boolean removeNode(T o);

    boolean isContain(T o);
}