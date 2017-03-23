/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 *
 * @author Marin
 */
public class Triangle extends Quadtree {

    double x1, x2, y1, y2, x3, y3;
    int id;

    public Triangle(double x1, double y1, double x2, double y2, double x3, double y3, int id) {
        


        
        this.id = id;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
    }

    public Triangle() {
    }

    @Override
    public double getx1() {
        return x2;
    }

    @Override
    public double gety1() {
        return y2;
    }

    @Override
    public double getx2() {
        return x3;
    }

    @Override
    public double gety2() {
        return y1;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    boolean detAparitia(double x, double y) {
      
        
        double A = abs(x2 * y3 + x3 * y1 + y2 * x1 - x1 * y3 - x3 * y2 - y1 * x2) / 2;  //aria mare      
        double A1 = abs(x2 * y + x * y1 + y2 * x1 - x1 * y - x * y2 - y1 * x2) / 2;  //prima arie  //fara x3 y3
        double A2 = abs(x2 * y3 + x3 * y + y2 * x - x * y3 - x3 * y2 - y * x2) / 2;  // fara 1
        double A3 = abs(x * y3 + x3 * y1 + y * x1 - x1 * y3 - x3 * y - y1 * x) / 2;  //fara 2
        
        return A == A1 + A2 + A3;
    }
    @Override
    public void setId(int id) {
        this.id = id;
    }
    
    
}
