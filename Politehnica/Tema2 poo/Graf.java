
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Marin
 */
public class Graf {

    private final ArrayList<Nod> NodeLists = new ArrayList<Nod>();     //lista cu toate nodurile din graf
    private static int iteratieA, iteratieB, iteratieC;

    boolean erorA, erorB, erorC;     //Pentru a afla daca cast a dat eroare

    public boolean isErorA() {
        return erorA;
    }

    public boolean isErorB() {
        return erorB;
    }

    public boolean isErorC() {
        return erorC;
    }

    /**
     * Seteaza iteratia
     *
     * @param iteratieA
     * @param iteratieB
     * @param iteratieC
     */
    public void setAllIteratie(int iteratieA, int iteratieB, int iteratieC) {

        erorA = false;
        erorB = false;
        erorC = false;
        if (Graf.iteratieA > iteratieA) {

            erorA = true;
        }
        Graf.iteratieA = iteratieA;

        if (Graf.iteratieB > iteratieB) {
            erorB = true;
        }
        Graf.iteratieB = iteratieB;

        if (Graf.iteratieC > iteratieC) {

            erorC = true;
        }
        Graf.iteratieC = iteratieC;

    }

    /**
     *
     * @return iteratie pentru nodul A
     */
    public static int getIteratieA() {
        return iteratieA;
    }

    /**
     *
     * @return iteratie pentru nodul B
     */
    public static int getIteratieB() {
        return iteratieB;
    }

    /**
     *
     * @return iteratie pentru nodul C
     */
    public static int getIteratieC() {
        return iteratieC;
    }

    /**
     * Adauga un nod in graf
     *
     * @param a
     */
    public void add(Nod a) {
        NodeLists.add(a);
    }

    /**
     * Cauta nodul dupa nume
     *
     * @param name
     * @return
     */
    public Nod getNode(String name) {
        for (int i = 0; i < NodeLists.size(); i++) {
            if (NodeLists.get(i).getName().equals(name)) {

                return NodeLists.get(i);
            }
        }
        return null;
    }

    /**
     * Cauta nodul dupa id
     *
     * @param id
     * @return
     */
    public Nod getNodeId(int id) {

        for (int i = 0; i < NodeLists.size(); i++) {
            if (NodeLists.get(i).getId() == id) {
                return NodeLists.get(i);
            }
        }
        return null;
    }

    /**
     * adauga legaturi pentru ambele noduri
     *
     * @param name1
     * @param name2
     */
    public void addLegatura(String name1, String name2) {
        Nod aux = getNode(name1);
        Nod aux2 = getNode(name2);
        aux2.addLegaturi(aux);
        aux.addLegaturi(aux2);
    }

    public void addLegatura(String name1, Nod nod) {
        Nod aux = getNode(name1);
        nod.addLegaturi(aux);
        aux.addLegaturi(nod);
    }

    /**
     * Sterge legaturile din ambele noduri
     *
     * @param name1
     * @param name2
     */
    public void stergeMuchie(String name1, String name2) {
        Nod aux = getNode(name1);
        Nod aux2 = getNode(name2);
        aux2.stergeMuchie(aux);
        aux.stergeMuchie(aux2);

    }

    /**
     * Sterge un nod din Graf
     *
     * @param name
     */
    public void stergeNod(String name) {
        Nod nod = getNode(name);
        for (int i = 0; i < nod.size(); i++) {
            Nod aux = nod.nextNod();
            aux.stergeMuchie(nod);
        }
        NodeLists.remove(nod);
    }

    public  void sort() {
        for (int i = 0; i < NodeLists.size(); i++) {
            for (int j = i; j < NodeLists.size(); j++) {
                if (NodeLists.get(i).getId() > NodeLists.get(j).getId()) {
                    Nod aux = NodeLists.get(i);
                    NodeLists.set(i, NodeLists.get(j));
                    NodeLists.set(j, aux);
                    //break;
                }
            }
             NodeLists.get(i).sort();
            
        }
        
        for(int i = 0; i < NodeLists.size(); i++){
          //  System.out.println(""+NodeLists.get(i).getId());
        }
    }
}
