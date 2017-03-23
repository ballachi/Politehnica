
import java.util.ArrayList;

/**
 *
 * @author Marin
 */
public class Vector implements Tip {

    private final ArrayList<Nod> vector = new ArrayList<Nod>();

    /**
     * Aduga un nod
     *
     * @param data
     */
    @Override
    public void add(Nod data) {
        if (vector.indexOf(data) < 0) {
            vector.add(data);
        }
    }

    /**
     * Extrage un nod dupa index
     *
     * @param data
     */
    @Override
    public Nod get(int index) {
        return vector.get(index);
    }

    /**
     * Sterge un nod
     *
     * @param nod
     * @return
     */
    @Override
    public boolean remove(Nod nod) {
        vector.remove(nod);
        return true;
    }

    /**
     * Returneaza citi vecini are nodul
     *
     * @return
     */
    @Override
    public int size() {
        return vector.size();
    }

    /**
     * Verifica daca nodul are vecin (nodul dat ca parametru)
     *
     * @param nod
     * @return
     */
    @Override
    public boolean verifica(Nod nod) {
        return vector.contains(nod);
    }

    @Override
    public void sort(){
            for (int i = 0; i < vector.size(); i++) {
            for (int j = i; j < vector.size(); j++) {
                if (vector.get(i).getId() > vector.get(j).getId()) {
                    Nod aux = vector.get(i);
                    vector.set(i, vector.get(j));
                    vector.set(j, aux);
                }
            }
        }
        
    }
}
