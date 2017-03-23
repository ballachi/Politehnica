

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 *
 * @author Marin
 */
public class Deserialize {

    /**
     * Metoda este recursiva
     * @param g graful
     * @param newReader
     * @param writer
     * @param nod
     * @throws IOException 
     */
    public static void Deserializare(Graf g, BufferedReader newReader, BufferedWriter writer, Nod nod) throws IOException {
        String line;
        String[] vect;
        Nod a;
        try {
            while ((line = newReader.readLine()) != null) {    //citesc o linie
                line = line.replace("\t", "");                  //sterg tab-urile
                vect = line.split("\\s+");                      
                if (vect[0].equals("</Object>")) {
                    break;
                }
                if (vect[0].equals("<Object")) {        //inserez in graf
                    switch (vect[1]) {
                        case "class=\"NodA\"": {
                            int version = Integer.parseInt(vect[2].split("\"")[1]);
                            vect = vect[3].split("\"");
                            int id = Integer.parseInt(vect[1]); 
                            line = newReader.readLine();            //mai citesc o linie
                            line.trim();
                            vect = line.split(">");
                            vect = vect[1].split("<");
                            a = new NodA(vect[0], id);              //creez un nod nou 
                            g.add(a);                              //aduag in graf
                            if (g.isErorA()) {
                                writer.write("Fail cast NodA " + vect[0] + " from Version=\"" + version + "\" to Version=\"" + g.getIteratieA() + "\"\n");
                            } else {
                                writer.write("Ok cast NodA " + vect[0] + " from Version=\"" + version + "\" to Version=\"" + g.getIteratieA() + "\"\n");
                            }
                            Deserializare(g, newReader, writer, a);
                            break;
                        }
                        case "class=\"NodB\"": {
                            int version = Integer.parseInt(vect[2].split("\"")[1]);
                            vect = vect[3].split("\"");
                            int id = Integer.parseInt(vect[1]);
                            line = newReader.readLine();
                            line.trim();
                            vect = line.split(">");
                            vect = vect[1].split("<");
                            a = new NodB(vect[0], id);
                            g.add(a);
                            if (g.isErorB()) {
                                writer.write("Fail cast NodB " + vect[0] + " from Version=\"" + version + "\" to Version=\"" + g.getIteratieB() + "\"\n");
                            } else {
                                writer.write("Ok cast NodB " + vect[0] + " from Version=\"" + version + "\" to Version=\"" + g.getIteratieB() + "\"\n");
                            }
                            Deserializare(g, newReader, writer, a);
                            break;
                        }
                        case "class=\"NodC\"": {
                            int version = Integer.parseInt(vect[2].split("\"")[1]);
                            vect = vect[3].split("\"");
                            int id = Integer.parseInt(vect[1]);
                            line = newReader.readLine();
                            line.trim();
                            vect = line.split(">");
                            vect = vect[1].split("<");
                            a = new NodC(vect[0], id);
                            g.add(a);
                            int size = vect.length;
                            if (g.isErorC()) {
                                writer.write("Fail cast NodC " + vect[0] + " from Version=\"" + version + "\" to Version=\"" + g.getIteratieC() + "\"\n");
                            } else {
                                writer.write("Ok cast NodC " + vect[0] + " from Version=\"" + version + "\" to Version=\"" + g.getIteratieC() + "\"\n");

                            }
                            Deserializare(g, newReader, writer, a);
                            break;
                        }
                        default:
                            break;
                    }
                } else if (vect[0].equals("<Reference")) {   //adaug legatura
                    vect = vect[3].split("\"");
                    Nod aux = g.getNodeId(Integer.parseInt(vect[1]));

                    if (nod != null && !nod.equals(aux)) {
                        aux.addLegaturi(nod);
                        nod.addLegaturi(aux);
                    }

                }

            }
        } catch (IOException e) {
        } finally {
        }
    }

}
