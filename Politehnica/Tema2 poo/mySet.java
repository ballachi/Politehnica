

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

/**
 *
 * @author Marin
 */
public class mySet implements Tip {

    private final LinkedHashSet<Nod> set = new LinkedHashSet<Nod>();
    private Iterator<Nod> it;

    /**
     * Aduga un nod
     *
     * @param data
     */
    @Override
    public void add(Nod data) {

        Iterator<Nod> it2 = set.iterator();
        while (it2.hasNext()) {
            Nod aux = it2.next();

            if (aux.equals(data)) {

                return;
            }

        }
        set.add(data);
        it = set.iterator();

    }

    /**
     * Extrage un nod
     *
     * @param data
     */
    @Override
    public Nod get(int index) {
        Nod a = it.next();
        if (!it.hasNext()) {
            it = set.iterator();
        }
        return a;

    }

    /**
     * Sterge un nod
     *
     * @param nod
     * @return
     */
    @Override
    public boolean remove(Nod nod) {
        Iterator<Nod> iterator = set.iterator();
        while (iterator.hasNext()) {
            Nod aux = iterator.next();
            if (nod.getName().equals(aux.getName())) {

                iterator.remove();
                it = set.iterator();
                return true;

            }
        }

        return false;
    }

    /**
     * Returneaza citi vecini are nodul
     *
     * @return
     */
    @Override
    public int size() {
        return set.size();
    }

    /**
     * Verifica daca nodul are vecin (nodul dat ca parametru)
     *
     * @param nod
     * @return
     */
    @Override
    public boolean verifica(Nod nod) {
        Iterator<Nod> iterator = set.iterator();
        while (iterator.hasNext()) {
            Nod aux = iterator.next();
            if (nod.equals(aux)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void sort() {
        //
    }


}
