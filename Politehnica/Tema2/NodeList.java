
/**
 *
 * @author Marin
 */
public class NodeList {

    NodeList next;

    Nod data;

    public NodeList(Nod dataValue) {
        next = null;
        data = dataValue;
    }

    public NodeList(Nod dataValue, NodeList nextValue) {
        next = nextValue;
        data = dataValue;
    }

    /**
     * Returneaza nodul
     * @return 
     */
    public Nod getData() {
        return data;
    }
    /**
     * returneaza elementul urmator din lista
     * @return 
     */
    public NodeList getNext() {
        return next;
    }
    /**
     * 
     * @param nextValue 
     */
    public void setNext(NodeList nextValue) {
        next = nextValue;
    }

}
