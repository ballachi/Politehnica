
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

    static void readExecute(String nameFile) {
        Graf g = null;
        Nod a, b, c;
        CurrentId id = CurrentId.getInstanceId();
        String line;
        String[] vect;
        BufferedReader Reader = null;
        BufferedWriter writer = null;
        try {
            FileInputStream file = new FileInputStream(nameFile);
            Reader = new BufferedReader(new InputStreamReader(file));

            while ((line = Reader.readLine()) != null) {                        //citesc o linie din fisier
                vect = line.split("\\s+");
                if (vect[0].equals("Settings")) {                               //la Settings creez un graf nou 
                    g = new Graf();                                                 //setez id de la 0
                    id.set(0);
                    g.setAllIteratie(Integer.parseInt(vect[1]), Integer.parseInt(vect[2]), Integer.parseInt(vect[3]));

                } else if (vect[0].equals("Add") && g != null) {                             //daca instructiunea este add
                    switch (vect[1]) {
                        case "NodA": {
                            //creez nodul si adaug toate legaturile
                            a = new NodA(vect[2], id.newGetId());
                            g.add(a);
                            int size = vect.length;
                            for (int i = 3; i < size; i++) {
                                g.addLegatura(vect[i], a);

                            }
                            break;
                        }
                        case "NodB": {
                            b = new NodB(vect[2], id.newGetId());
                            g.add(b);
                            int size = vect.length;
                            for (int i = 3; i < size; i++) {
                                g.addLegatura(vect[i], b);
                            }
                            break;
                        }
                        case "NodC": {
                            c = new NodC(vect[2], id.newGetId());
                            g.add(c);
                            int size = vect.length;
                            for (int i = 3; i < size; i++) {
                                g.addLegatura(vect[i], c);
                            }
                            break;
                        }
                        default:
                            break;
                    }
                } else if (vect[0].equals("AddM") && g != null) {
                    g.addLegatura(vect[1], vect[2]);

                } else if (vect[0].equals("Del") && g != null) {
                    g.stergeNod(vect[2]);

                } else if (vect[0].equals("DelM") && g != null) {
                    g.stergeMuchie(vect[1], vect[2]);

                } else if (vect[0].equals("Serialize") && g != null) {
                    Serialize s = new Serialize();
                    g.sort();
                    s.Serializare(g.getNode(vect[1]), vect[2]);

                } else if (vect[0].equals("Deserialize")) {
                    BufferedReader newReader;
                    FileInputStream file2 = new FileInputStream(vect[1]);
                    writer = new BufferedWriter(new FileWriter("Deserialize_" + vect[1] + "_CAST.log"));
                    newReader = new BufferedReader(new InputStreamReader(file2));
                    Deserialize.Deserializare(g, newReader, writer, null);
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
