/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marin
 */
public class Rectangle extends Quadtree {

    double width1, height1, width2, height2;
    int id;

    public Rectangle(double width1, double height1, double width2, double height2, int id) {
        this.id = id;
        this.width1 = width1;
        this.height1 = height1;
        this.width2 = width2;
        this.height2 = height2;
    }

    @Override
    public double getx1() {
        return width1;
    }

    @Override
    public double gety1() {
        return height1;
    }

    @Override
    public double getx2() {
        return width2;
    }

    @Override
    public double gety2() {
        return height2;
    }

    public int getId() {
        return id;
    }

    @Override
    boolean detAparitia(double x, double y) {
        boolean r = false;
        if (gety1() <= y && getx1() <= x && gety2() >= y && getx2() >= x) {
            r = true;
        }
        return r;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
