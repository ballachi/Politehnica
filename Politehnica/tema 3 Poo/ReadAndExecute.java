
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Marin
 */
public class ReadAndExecute {

    /**
     * Metoda care primeste ca parametru numele fisierului 
     * Citeste linie cu line
     * Adauga variablilele in variabile
     * Scrie fisierul "arbore.out"
     * @param nameFile 
     */
    static void readExecute(String nameFile) {
        variabile nodes=variabile.getInstance();
        nodes.add("", "", "operatie");
        String line;
        String[] vect;
        BufferedReader Reader = null;
        BufferedWriter writer = null;
        try {
            FileInputStream file = new FileInputStream(nameFile);
            Reader = new BufferedReader(new InputStreamReader(file));
            writer = new BufferedWriter(new FileWriter("arbore.out"));
            while ((line = Reader.readLine()) != null) {                        //citesc o linie din fisier
                vect = line.split("\\s+");
                switch (vect[0]) {
                    case "int":
                        nodes.add( vect[1] , Integer.parseInt(vect[3].split(";")[0]) ,"int");
                        break;
                    case "double":
                        nodes.add( vect[1] , Double.parseDouble(vect[3].split(";")[0]),"double" );
                        break;
                    case "string":
                        
                        String vect2[]=line.split("\"");
                        nodes.add( vect[1] , vect2[1] , "string");
                        break;
                    case "eval":
                        writer.write(Eval.eval(line)+"\n");
                        break;
                    default:
                        break;
                }

            }
        } catch (IOException e) {
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }

                if (Reader != null) {
                    Reader.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
