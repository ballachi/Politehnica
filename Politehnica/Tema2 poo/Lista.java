
/**
 *
 * @author Marin
 */
public class Lista implements Tip {

    int counter;
    private NodeList head;

    // Default constructor
    public Lista() {

    }

    /**
     * Aduga un nod
     *
     * @param data
     */
    @Override
    public void add(Nod data) {
        if (head == null) {
            head = new NodeList(data);
            incrementCounter();
        } else {
            NodeList Temp = new NodeList(data);
            NodeList Current = head;

            if (Current != null && !Current.data.equals(data)) {

                while (Current.getNext() != null) {
                    Current = Current.getNext();
                    if (Current.data.equals(data)) {
                        return;
                    }
                }

                if (!Current.data.equals(Temp.data)) {

                    Current.setNext(Temp);
                    incrementCounter();
                }
            }
        }

    }

    private int getCounter() {
        return counter;
    }

    private void incrementCounter() {
        counter++;
    }

    private void decrementCounter() {
        counter--;
    }

    /**
     * Extrage un nod dupa index
     *
     * @param data
     */
    @Override
    public Nod get(int index) {
        if (index < 0) {
            return null;
        }
        NodeList Current = null;

        if (head != null) {

            if (index != 0) {
                Current = head.getNext();

                for (int i = 0; i < index - 1; i++) {
                    if (Current.getNext() == null) {
                        return null;
                    }

                    Current = Current.getNext();
                }

                return Current.getData();
            }
            return head.getData();
        }
        return null;

    }

    /**
     * Returneaza citi vecini are nodul
     *
     * @return
     */
    @Override
    public int size() {
        return getCounter();
    }

    /**
     * Sterge un nod
     *
     * @param nod
     * @return
     */
    @Override
    public boolean remove(Nod nod) {

        NodeList Current = head;
        if (head != null) {
            for (int i = 0; !Current.data.equals(nod); i++) {
                if (Current.getNext() == null) {
                    return false;
                }

                Current = Current.getNext();
            }
            Current.setNext(Current.getNext().getNext());

            decrementCounter();
            return true;

        }
        return false;
    }

    /**
     * Verifica daca nodul are vecin (nodul dat ca parametru)
     *
     * @param nod
     * @return
     */
    @Override
    public boolean verifica(Nod nod) {

        NodeList Current = head;
        if (head != null && nod != null) {
            for (int i = 0; !Current.data.equals(nod); i++) {
                if (Current.getNext() == null) {
                    return false;
                }

                Current = Current.getNext();
            }

            return true;

        }
        return false;

    }

    @Override
    public void sort() {
        NodeList Current = head;
        if (head != null) {
            while (Current != null) {
                NodeList Currentaux = Current.getNext();
                while (Currentaux != null) {

                    if (Current.getData().getId() > Currentaux.getData().getId()) {
                        Nod aux = Current.getData();
                        Current.data = Currentaux.getData();
                        Currentaux.data = aux;
                    }
                    Currentaux = Currentaux.getNext();

                }
                Current = Current.getNext();
            }

        }
    }

}
