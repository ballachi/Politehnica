/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marin
 */
public class Circle extends Quadtree {

    double raza;
    double x, y;
    int id;

    public Circle(double raza, double x, double y, int id) {
        this.id = id;
        this.raza = raza;
        this.x = x;
        this.y = y;
    }

    public Circle() {

    }

    @Override
    public double getx1() {
        return x - raza;
    }

    @Override
    public double gety1() {
        return y - raza;
    }

    @Override
    public double getx2() {
        return x + raza;
    }

    @Override
    public double gety2() {
        return y + raza;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    boolean detAparitia(double x, double y) {
        boolean a = false;
        if ((x - this.x) * (x - this.x) + (y - this.y) * (y - this.y) <= raza * raza) {
            a = true;
        }
        return a;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
    

}
