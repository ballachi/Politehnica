/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * @author Marin
 */
public class Serialize {

    private boolean intorcere;   //folosesc pentru a afla cind ma intorc din recursivitate
    private final ArrayList<Integer> idVazute=new ArrayList<Integer> ();
    
    /**
     * 
     * @param nod
     * @param nameFile Numele fisierului
     */
    public  void Serializare(Nod nod, String nameFile) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(nameFile));
            boolean cond = true;
            Nod aux = nod;

            String s = SerializareAux(nod, "", 0, null);
            writer.write(s);
        } catch (IOException e) {
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
            }
        }

        intorcere = false;

    }

    
    /**
     * Metoda face serializarea recursiv
     * 
     * @param aux  nodul
     * @param rs  un string care se va scrie in fisier
     * @param nivel pentru a stie cite tab-uri este necesar sa pun
     * @param nodPrecedent 
     * @return 
     */
    private String SerializareAux(Nod aux, String rs, int nivel, Nod nodPrecedent) {
        if (aux != null) {

            String tab = "";

            for (int i = 0; i < nivel; i++) {               
                tab += "\t\t";
                
            }
            if (!idVazute.contains(aux.getId())) {          //daca nodul nu a fost vazut 
                idVazute.add(aux.getId());
                nivel++;
                int tip = aux.getVersion();              //aflu de ce tip este
                rs += tab + ("<Object class=\"" + aux.getClass().getSimpleName() + "\" Version=\"" + tip + "\" id=\"" + aux.getId() + "\">\n");
                rs += tab + ("\t<Nume>" + aux.getName() + "</Nume>\n");
                switch (tip) {
                    case 1:
                        rs += tab + ("\t<LIST>\n");
                        break;
                    case 2:
                        rs += tab + ("\t<VECTOR>\n");
                        break;
                    case 3:
                        rs += tab + ("\t<SET>\n");
                        break;
                    default:
                        break;
                }
                //aux.setVazut();

                for (int i = 0; i < aux.size(); i++) {
                    rs = SerializareAux(aux.nextNod(), rs, nivel, aux);     //petru toate 
                }
                intorcere = true;

                switch (tip) {
                    case 1:
                        rs += tab + ("\t</LIST>\n");
                        break;
                    case 2:
                        rs += tab + ("\t</VECTOR>\n");
                        break;
                    case 3:
                        rs += tab + ("\t</SET>\n");
                        break;
                    default:
                        break;
                }
                rs += tab + ("</Object>\n");

            } else {
                if ((!intorcere || nodPrecedent.legat(aux)) && (!aux.equals(nodPrecedent) || nodPrecedent == null)) {
                    rs += tab + ("<Reference class=\"" + aux.getClass().getSimpleName() + "\" Version=\"" + aux.getVersion() + "\" id=\"" + aux.getId() + "\">\n");

                }
            }

        }
        return rs;

    }

}
