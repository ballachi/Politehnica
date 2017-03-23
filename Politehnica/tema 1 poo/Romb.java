/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Polygon;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

/**
 *
 * @author Marin
 */
public class Romb extends Quadtree {

    double x1, x2, y1, y2, x3, y3, x4, y4;
    int id;

    public Romb() {
    }

    public Romb(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, int id) {

        this.id = id;


        this.x1 = x3;
        this.x2 = x2;
        this.y1 = y3;
        this.y2 = y2;
        this.x3 = x1;
        this.y3 = y1;
        this.x4 = x4;
        this.y4 = y4;
    }

    @Override
    public double getx1() {
        return x2;
    }

    @Override
    public double gety1() {
        return y3;
    }

    @Override
    public double getx2() {
        return x4;
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


                double A = Math.abs (x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));
		double A1 = Math.abs (x1 * (y2 - y) + x2 * (y - y1) + x * (y1 - y2));
		double A2 = Math.abs (x1 * (y - y3) + x * (y3 - y1) + x3 * (y1 - y));
		double A3 = Math.abs (x * (y2 - y3) + x2 * (y3 - y) + x3 * (y - y2));
		if(A==A1 + A2 + A3){
                    return true;
                }
                
                
                A = Math.abs (x1 * (y3 - y4) + x3 * (y4 - y1) + x4 * (y1 - y3));
		A1 = Math.abs (x1 * (y3 - y) + x3 * (y - y1) + x * (y1 - y3));
		A2 = Math.abs (x1 * (y - y4) + x * (y4 - y1) + x4 * (y1 - y));
		A3 = Math.abs (x * (y3 - y4) + x3 * (y4 - y) + x4 * (y - y3));
                
                return (A==A1 + A2 + A3);


    }
    
    @Override
        public void setId(int id) {
        this.id = id;
    }
        
        

}
