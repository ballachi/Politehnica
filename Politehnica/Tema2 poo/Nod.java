
/**
 * Clasa nod este facuta pentru polimorfizm
 *
 * @author Marin
 */
public interface Nod   {

    /**
     * returneaza numele
     *
     * @return numele
     */
    String getName() ;

    /**
     * Adauga legatura
     *
     * @param nod
     */
    public void addLegaturi(Nod nod) ;

    /**
     * Aceasta metoda intoarce true daca nodu curent are legatura cu nodul dat
     * ca parametru
     *
     * @return
     */
    public boolean legat(Nod nodPrecedent) ;

    /**
     * Metoda va intoarce tipul(iteratia) nodului
     *
     * @return
     */
    int getVersion() ;

    /**
     * Aceasta metoda intoarce de fiecare data un nod nou
     * Daca sunt la ultimu element il setez la inceput
     * @return
     */
    public Nod nextNod() ;

    /**
     * Aceasta metoda intoarce numaru de vecini
     *
     * @return
     */
    public int size() ;

    /**
     * aceasta metoda va returna o valoare boolean daca va returna true insemna
     * ca am trecut prin ea
     *
     * @return
     */
    public boolean Vazut() ;

    public void setVazut() ;
    /**
     * Aceasta metoda intoarce id
     *
     * @return
     */
    public int getId() ;

    /**
     * sterge legatura
     *
     * @param nod
     */
    public void stergeMuchie(Nod nod) ;

    /**
     * sorteaza tipul 
     */
    public void sort();    
    
}
