import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
/**
 * Clasa in care pastrez variabile
 * @author Marin
 */
public class Variables {

    public static Variables var = new Variables();

    private Variables() {
    }
    
    
    public static String nameFile;
    private final HashMap<String, Integer> variable = new HashMap();
/**
 * Adauga variabilele in HashMap
 * @param name numele variabilei
 * @param value valoarea
 */
    public void add(String name, int value) {
        if (variable.get(name) == null) {
            variable.put(name, value);
        } else {
            variable.remove(name);
            variable.put(name, value);
        }
    }

/**
 *  returneaza valoarea variabilei dupa nume
 * in caz de nu exista sa arunca exceptie si se scrie in fisier Check failed , exit program
 * @param name
 * @return
 */
    public int get(String name) {
        int x = 0;
        try {
            x = variable.get(name);
        } catch (NullPointerException n) {
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(nameFile));
                writer.write("Check failed");

            } catch (IOException e) {
                System.out.println("Eroare");
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                }
            }

            System.exit(0);
        }
        return x;
    }

    public static Variables getInstance() {
        return var;
    }

    public static void nameFile(String nume) {
        nameFile = nume;
    }

}
