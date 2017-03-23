
import java.util.ArrayList;

/**
 *
 * @author Marin
 * @param <T>
 */
public class variabile<T> {

    private final ArrayList variabile = new ArrayList();
    private final ArrayList name = new ArrayList();
    private static final variabile instance = new variabile();
    private static final Factory factory = new Factory();


    private variabile() {
    }

    /**
     * 
     * @return Instance
     */
    public static variabile getInstance() {
        return instance;
    }
    /**
     * Metoda intoarce o variabila dupa nume
     * @param name
     * @return 
     */
    public VariablePart getNod(String name) {
        if (name.equals("+") || name.equals("-") || name.equals("*") || name.equals("/")) {
            return (VariablePart) variabile.get(0);
        }
        int index = this.name.indexOf(name);
        if (index >= 0) {
            return (VariablePart) variabile.get(index);
        }
        return null;
    }

    /**
     * Metoda adauga in liste numele,creeaza variabila si o adauga in lista
     * Variabila se creeaza cu pattern-ul factory
     * @param nameVariable numele 
     * @param value valoarea 
     * @param type tip de date
     */
    public void add(String nameVariable, T value, String type) {
        this.name.add(nameVariable);
        VariablePart v = factory.getNod(type, value);
        this.variabile.add(v);

    }

}
