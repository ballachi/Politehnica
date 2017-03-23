/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 *
 * @author Marin
 */
public class Quadtree {

    Quadtree[] nodes;
    ArrayList<Quadtree> object;
    double y1, y2, x1, x2;
    static Quadtree VectAllOb[];
    static int size = 0;

    public Quadtree() {
    }

    public Quadtree(Quadtree ob, double x1, double y1, double x2, double y2) {

        object = new ArrayList<>();

        object.add(ob);
        nodes = new Quadtree[4];
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

    }

    Quadtree(double x1, double y1, double x2, double y2) {
        //object = new ArrayList<>();
        //Quadtree VectAllOb[]=new Quadtree[100000];
        nodes = new Quadtree[4];
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    void add(Quadtree ob, Quadtree root) {
        Quadtree parinte;
        double newy1;
        double newx1;
        double newy2;
        double newx2;

        //cadranul 1
        newy1 = (root.y1 + root.y2) / 2;
        newx1 = (root.x1 + root.x2) / 2;
        newy2 = root.y2;
        newx2 = root.x2;

        if (apartine(newx1, newy1, newx2, newy2, ob.getx1(), ob.gety1(), ob.getx2(), ob.gety2())) {

            if (root.nodes[0] == null) {
                root.nodes[0] = new Quadtree(ob, newx1, newy1, newx2, newy2);
            } else {
                if (root.nodes[0].object.size() > 0) {
                    int ok = 0;
                    int p = root.nodes[0].object.size();

                    for (int i = 0; i < p; i++) {
                        if (apartine(root.nodes[0].object.get(i).getx1(), root.nodes[0].object.get(i).gety1(), root.nodes[0].object.get(i).getx2(),
                                root.nodes[0].object.get(i).gety2(), ob.getx1(), ob.gety1(), ob.getx2(), ob.gety2())) {

                            ok++;
                        } else {
                            break;
                        }
                    }
                    if (ok == root.nodes[0].object.size()) {
                        root.nodes[0].object.add(ob);
                    } else {
                        int e = root.nodes[0].object.size();
                        for (int j = 0; j < e; j++) {
                            add(root.nodes[0].object.get(j), root.nodes[0]);
                        }
                        add(ob, root.nodes[0]);
                        for (int j = e - 1; j != -1; j--) {
                            root.nodes[0].object.remove(j);
                        }

                    }

                } else {
                    add(ob, root.nodes[0]);
                }
            }
        }

        //cadranul 2
        newy1 = (root.y1 + root.y2) / 2;
        newx1 = root.x1;
        newy2 = root.y2;
        newx2 = (root.x1 + root.x2) / 2;
        if (apartine(newx1, newy1, newx2, newy2, ob.getx1(), ob.gety1(), ob.getx2(), ob.gety2())) {  //daca se afla in cadran 2
            if (root.nodes[1] == null) {
                root.nodes[1] = new Quadtree(ob, newx1, newy1, newx2, newy2);
            } else {
                if (root.nodes[1].object.size() > 0) {
                    int ok = 0;
                    int p = root.nodes[1].object.size();
                    for (int i = 0; i < p; i++) {
                        if (apartine(root.nodes[1].object.get(i).getx1(), root.nodes[1].object.get(i).gety1(), root.nodes[1].object.get(i).getx2(),
                                root.nodes[1].object.get(i).gety2(), ob.getx1(), ob.gety1(), ob.getx2(), ob.gety2())) {
                            //root.nodes[1].object.add(ob);
                            ok++;
                        } else {
                            break;
                        }
                    }
                    if (ok == root.nodes[1].object.size()) {
                        root.nodes[1].object.add(ob);
                    } else {
                        int e = root.nodes[1].object.size();
                        add(ob, root.nodes[1]);

                        for (int j = 0; j < e; j++) {
                            add(root.nodes[1].object.get(j), root.nodes[1]);
                        }
                        for (int j = e - 1; j != -1; j--) {
                            root.nodes[1].object.remove(j);
                        }
                    }

                } else {

                    add(ob, root.nodes[1]);
                }

            }

        }

        //cadranul 3
        newy1 = root.y1;
        newx1 = root.x1;
        newy2 = (root.y1 + root.y2) / 2;
        newx2 = (root.x1 + root.x2) / 2;

        if (apartine(newx1, newy1, newx2, newy2, ob.getx1(), ob.gety1(), ob.getx2(), ob.gety2())) {

            if (root.nodes[2] == null) {
                root.nodes[2] = new Quadtree(ob, newx1, newy1, newx2, newy2);
            } else {

                if (root.nodes[2].object.size() > 0) {
                    int p = root.nodes[2].object.size();
                    int ok = 0;
                    for (int i = 0; i < p; i++) {

                        if (apartine(root.nodes[2].object.get(i).getx1(), root.nodes[2].object.get(i).gety1(), root.nodes[2].object.get(i).getx2(),
                                root.nodes[2].object.get(i).gety2(), ob.getx1(), ob.gety1(), ob.getx2(), ob.gety2())) {
                            ok++;

                        } else {
                            break;
                        }
                    }
                    if (ok == root.nodes[2].object.size()) {
                        root.nodes[2].object.add(ob);
                    } else {
                        int e = root.nodes[2].object.size();
                        add(ob, root.nodes[2]);

                        for (int j = 0; j < e; j++) {
                            add(root.nodes[2].object.get(j), root.nodes[2]);
                        }
                        for (int j = e - 1; j != -1; j--) {
                            root.nodes[2].object.remove(j);
                        }

                    }

                } else {
                    add(ob, root.nodes[2]);
                }
            }

        }

        newy1 = root.y1;
        newx1 = (root.x1 + root.x2) / 2;
        newy2 = (root.y1 + root.y2) / 2;
        newx2 = root.x2;

        if (apartine(newx1, newy1, newx2, newy2, ob.getx1(), ob.gety1(), ob.getx2(), ob.gety2())) {
            if (root.nodes[3] == null) {
                root.nodes[3] = new Quadtree(ob, newx1, newy1, newx2, newy2);
            } else {
                if (root.nodes[3].object.size() > 0) {
                    int ok = 0;
                    int p = root.nodes[3].object.size();
                    for (int i = 0; i < p; i++) {
                        if (apartine(root.nodes[3].object.get(i).getx1(), root.nodes[3].object.get(i).gety1(), root.nodes[3].object.get(i).getx2(),
                                root.nodes[3].object.get(i).gety2(), ob.getx1(), ob.gety1(), ob.getx2(), ob.gety2())) {
                            ok++;

                        } else {
                            break;
                        }
                    }
                    if (ok == root.nodes[3].object.size()) {
                        root.nodes[3].object.add(ob);
                    } else {
                        int e = root.nodes[3].object.size();
                        add(ob, root.nodes[3]);
                        for (int j = 0; j < e; j++) {
                            add(root.nodes[3].object.get(j), root.nodes[3]);
                        }
                        for (int j = e - 1; j != -1; j--) {
                            root.nodes[3].object.remove(j);
                        }

                    }

                } else {
                    add(ob, root.nodes[3]);
                }
            }

        }

    }

    double getx1() {
        return 0;
    }

    double getx2() {
        return 0;
    }

    double gety1() {
        return 0;
    }

    double gety2() {
        return 0;
    }

    String determina(Quadtree tata, double x, double y, String a) {
        if (tata != null) {
            for (int j = 0; j < 4 && a.equals(""); j++) {
                if (tata.nodes[j] != null) {

                    if (tata.nodes[j].x1 <= x && tata.nodes[j].y1 <= y && tata.nodes[j].x2 >= x && tata.nodes[j].y2 >= y && a.equals("")) {
                        a = determina(tata.nodes[j], x, y, a);
                        for (int i = 0; i < tata.nodes[j].object.size(); i++) {
                            if (tata.nodes[j].object.get(i).detAparitia(x, y)) {
                                if (tata.nodes[j].object.get(i).getId() != -1) {
                                    String b = tata.nodes[j].object.get(i).getId() + "";
                                    int c = a.indexOf("" + b);
                                    if (c < 0) {
                                        a += b + " ";
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        return a;
    }

//    Quadtree Stergere(Quadtree tata, int id, Quadtree ob) {
//        if (tata != null && ob == null) {
//            int size = tata.object.size();
//            if (size >= 0 && ob == null) {
//                for (int i = 0; i < size && ob == null; i++) {
//                    if (tata.object.get(i).getId() == id) {
//                        ob = tata.object.get(i);
//                        return ob;
//                    }
//                }
//            }
//            ob = Stergere(tata.nodes[0], id, ob);
//            ob = Stergere(tata.nodes[1], id, ob);
//            ob = Stergere(tata.nodes[2], id, ob);
//            ob = Stergere(tata.nodes[3], id, ob);
//
//        }
//        return ob;
//
//    }

    void StergeOptimizat(Quadtree a, Quadtree tata, int id) {

        if (tata != null) {
            for (int j = 0; j < 4; j++) {
                if (tata.nodes[j] != null) {
                    if (apartine(tata.nodes[j].x1, tata.nodes[j].y1, tata.nodes[j].x2, tata.nodes[j].y2, a.getx1(), a.gety1(), a.getx2(), a.gety2())) {
                        StergeOptimizat(a, tata.nodes[j], id);
                        int size = tata.nodes[j].object.size();
                        if (size >= 0) {
                            for (int i = 0; i < size; i++) {
                                if (tata.nodes[j].object.get(i).getId() == id) {
                                    tata.nodes[j].object.remove(i);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public int getId() {
        return 0;
    }

    boolean apartine(double x1, double y1, double x2, double y2, double ox1, double oy1, double ox2, double oy2) {

        return     ((ox2 >= x1 && oy2 >= y1 && ox2 <= x2 && oy2 <= y2)
                || (ox1 >= x1 && oy2 >= y1 && ox1 <= x2 && oy2 <= y2)
                || (ox1 >= x1 && oy1 >= y1 && ox1 <= x2 && oy1 <= y2)
                || (ox2 >= x1 && oy1 >= y1 && ox2 <= x2 && oy1 <= y2)
                || ((ox1 <= x1 && x1 <= x2 && x2 <= ox2 && y1 <= oy1 && oy1 <= oy2 && oy2 <= y2)
                || (x1 <= ox1 && ox1 <= ox2 && ox2 < x2 && oy1 <= y1 && y1 <= y2 && y2 <= oy2)))
                || (ox1 <= x1 && oy1 <= y1 && ox2 >= x1 && oy2 >= y1)
                || (ox1 <= x1 && oy1 <= y2 && ox2 >= x1 && oy2 >= y2)
                || (ox1 <= x2 && oy1 <= y2 && ox2 >= x2 && oy2 >= y2)
                || (ox1 <= x2 && oy1 <= y1 && ox2 >= x2 && oy2 >= y1);
    }

    boolean detAparitia(double x, double y) {

        return false;
    }

    String determinaux(Quadtree tata, double x1, double y1, double x2, double y2, String a) {
        if (tata != null) {
            for (int j = 0; j < 4; j++) {
                if (tata.nodes[j] != null) {
                    if (apartine(tata.nodes[j].x1, tata.nodes[j].y1, tata.nodes[j].x2, tata.nodes[j].y2, x1, y1, x2, y2)) {

                        a = determinaux(tata.nodes[j], x1, y1, x2, y2, a);

                        //Display(tata.nodes[j]);
                        for (int i = 0; i < tata.nodes[j].object.size(); i++) {

                            if (apartine(tata.nodes[j].object.get(i).getx1(), tata.nodes[j].object.get(i).gety1(),
                                    tata.nodes[j].object.get(i).getx2(), tata.nodes[j].object.get(i).gety2(), x1, y1, x2, y2)) {

                                if (tata.nodes[j].object.get(i).getId() != -1) {
                                    String b = "" + tata.nodes[j].object.get(i).getId();

                                    int c = a.indexOf(b + " ");
                                    int cc = a.indexOf(" " + b + " ");
                                    if (c < 0 || cc < 0) {
                                        a += b + " ";
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
        return a;
    }

    void setId(int id) {

    }

}
