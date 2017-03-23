
/**
 *
 * @author Marin
 */
public class NodC implements Nod {

    private final String name;
    private Tip noduriAdiacenta;
    private int curentIndex;
    private boolean vazut;     //va fi setata in caz ca am trecut prin nod pentru serializare
    private final int id;

    /**
     * aceasta metoda va returna o valoare boolean daca va returna true insemna
     * ca am trecut prin ea
     *
     * @return
     */
    @Override
    public boolean Vazut() {
        return vazut;
    }

    @Override
    public void setVazut() {
        vazut = true;
    }

    /**
     * Verific de ce tip este nodul si creez noduri Adiacenta de tipul respectiv
     *
     * @param name
     * @param id
     */
    public NodC(String name, int id) {
        this.name = name;
        this.id = id;

        switch (Graf.getIteratieC()) {
            case 1:
                noduriAdiacenta = new Lista();
                break;
            case 2:
                noduriAdiacenta = new Vector();
                break;
            case 3:
                noduriAdiacenta = new mySet();
                break;
            default:
                break;
        }

    }

    /**
     * Adauga legatura
     *
     * @param nod
     */
    @Override
    public void addLegaturi(Nod nod) {
        noduriAdiacenta.add(nod);

    }

    /**
     * returneaza numele
     *
     * @return numele
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Metoda va intoarce tipul(iteratia) nodului
     *
     * @return
     */
    @Override
    public int getVersion() {
        return Graf.getIteratieC();
    }

    /**
     * Aceasta metoda intoarce de fiecare data un nod nou.
     * Daca sunt la ultimu element il setez la inceput
     * @return
     */
    @Override
    public Nod nextNod() {

        if (curentIndex < noduriAdiacenta.size()) {
            if (curentIndex == noduriAdiacenta.size() - 1) {
                curentIndex = 0;
                return noduriAdiacenta.get(noduriAdiacenta.size() - 1);
            }
            return noduriAdiacenta.get(curentIndex++);
        }
        return null;

    }

    /**
     * Aceasta metoda intoarce numaru de vecini
     *
     * @return
     */
    @Override
    public int size() {

        return noduriAdiacenta.size();

    }

    /**
     * Aceasta metoda intoarce id
     *
     * @return
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * sterge legatura
     *
     * @param nod
     */
    @Override
    public void stergeMuchie(Nod nod) {

        noduriAdiacenta.remove(nod);

    }

    /**
     * Aceasta metoda intoarce true daca nodu curent are legatura cu nodul dat
     * ca parametru
     *
     * @param nod
     * @return
     */
    @Override
    public boolean legat(Nod nod) {

        return noduriAdiacenta.verifica(nod);
    }
    
    /**
     * sorteaza tipul 
     */
      @Override
    public void sort(){
        noduriAdiacenta.sort();
    }
}
