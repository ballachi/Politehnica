

/**
 *
 * @author Marin
 */
public interface Tip {

    /**
     * Aduga un nod
     *
     * @param data
     */
    public void add(Nod data);

    /**
     * Extrage un nod dupa index
     *
     * @param data
     */
    public Nod get(int index);

    
    /**
     * Sterge un nod
     * @param nod
     * @return 
     */
    public boolean remove(Nod nod);

    
    /**
     * Returneaza citi vecini are nodul
     * @return 
     */
    public int size();

    /** 
     * Verifica daca nodul are  vecin (nodul dat ca parametru)
     * @param nod
     * @return 
     */
    public boolean verifica(Nod nod);
    
    
    public void sort();

}
