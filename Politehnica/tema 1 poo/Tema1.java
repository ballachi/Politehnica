/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Marin
 */
public class Tema1 {

    public static void main(String[] args) throws FileNotFoundException {

        Scanner s = new Scanner(new File("quadtree.in"));
        int operatie;
        int fig,id;
        String line;

        String rs = "";
        Quadtree[] idu = new Quadtree[99999];
        //Rectangle b3 = new Rectangle(0, 0, 0, 0, 0);
        Quadtree a = new Quadtree(s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble());
                BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("quadtree.out"));
           

        while (s.hasNextInt()) {

            operatie = s.nextInt();
            if (operatie == 11) {
                //add
                fig = s.nextInt();
                if (fig == 1) {
                    //Dreptunghi
                    id = s.nextInt();

                    Rectangle r = new Rectangle(s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble(), id);
                    idu[id] = r;
                    a.add(r, a);

                } else if (fig == 2) {
                    //Triunghi
                    double cord[] = new double[6];
                    id = s.nextInt();

                    Triangle t = new Triangle(s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble(), id);
                    a.add(t, a);
                    idu[id] = t;
                } else if (fig == 3) {
                    //Cerc
                    id = s.nextInt();

                    Circle c = new Circle(s.nextDouble(), s.nextDouble(), s.nextDouble(), id);
                    idu[id] = c;
                    a.add(c, a);
                } else if (fig == 4) {
                    //romb
                    id = s.nextInt();
                    Romb r = new Romb(s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble(), id);
                    idu[id] = r;
                    a.add(r, a);
                }
            } else if (operatie == 22) {
                //stergereddd

                //id = s.nextInt();
                idu[s.nextInt()].setId(-1);
                //System.out.println(""+aux.getId());
               // a.StergeOptimizat(idu[id], a, id);
            } else if (operatie == 33) {
                //coleziune cu .
                line = "";
                 //s.nextDouble()
                   //       s.nextDouble()
                line += a.determina(a, s.nextDouble(), s.nextDouble(), "") + "\n";
                if (line.equals("\n")) {
                     writer.write("NIL" + "\n");
                } else {
                    line = sortline(line);
                    line = line.trim();
                    writer.write(line + "\n");
                }
            } else if (operatie == 44) {
                //coleziune cu figura
                fig = s.nextInt();

                if (fig == 1) {
                    //Dreptunghi
                    double cord[] = new double[4];

                  Rectangle r = new Rectangle(s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble(), 0);
                    line="";
                    line = a.determinaux(a, r.getx1(), r.gety1(), r.getx2(), r.gety2(), " ") + "\n";
                    if (line.equals(" \n")) {
                        writer.write("NIL" + "\n");
                    } else {
                        line = sortline(line);
                        line = line.trim();
                        writer.write(line + "\n");
                    }
                } else if (fig == 2) {
                    //Triunghi
    
                    Triangle t = new Triangle(s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble(), 0);
                    line = a.determinaux(a, t.getx1(), t.gety1(), t.getx2(), t.gety2(), " ") + "\n";
                    if (line.equals(" \n")) {
                        writer.write("NIL" + "\n");
                    } else {
                        line = sortline(line);
                        line = line.trim();
                        writer.write(line + "\n");
                    }
                } else if (fig == 3) {
                    //Cerc
 

                    Circle c = new Circle(s.nextDouble(), s.nextDouble(), s.nextDouble(), 0);
                    line = a.determinaux(a, c.getx1(), c.gety1(), c.getx2(), c.gety2(), " ") + "\n";
                    if (line.equals(" \n")) {
                        writer.write("NIL" + "\n");
                    } else {
                        line = sortline(line);
                        line = line.trim();
                        writer.write(line + "\n");
                    }
                } else if (fig == 4) {
                    //romb
      
                    Romb r = new Romb(s.nextDouble(), s.nextDouble(), s.nextDouble(),s.nextDouble(), s.nextDouble(), s.nextDouble(),s.nextDouble(), s.nextDouble(), 0);
                    //System.out.println("BLEA suka--------");
                    line = a.determinaux(a, r.getx1(), r.gety1(), r.getx2(), r.gety2(), " ") + "\n";
                    if (line.equals(" \n")) {
                        writer.write("NIL" + "\n");
                    } else {
                        line = sortline(line);
                        line = line.trim();
                        writer.write(line + "\n");
                    }
                }
            }

        }

        // a.Dis

        } catch (IOException e) {
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
            }
        }


    }

    static String sortline(String line) {
        ArrayList<Integer> vector= new ArrayList<>();

        String rs = "";
        Scanner in = new Scanner(line);
        int integer = 0;
        while (in.hasNextInt()) {
            vector.add(in.nextInt());
        }
        int vec[] = new int[vector.size()];
        int i = 0;
        while (vector.size() > i) {
            vec[i] = vector.get(i);
            i++;

        }

        Arrays.sort(vec);

        i = 0;
        while (vector.size() > i) {
            rs += vec[i] + " ";
            i++;

        }

        return rs; //pot sa mai optimizez
    }

}
