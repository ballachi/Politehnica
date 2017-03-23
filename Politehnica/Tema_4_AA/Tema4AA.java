/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Marin
 */
public class Tema4AA {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        BufferedReader newReader = null;
        String[] vect;
        int nV, nE, k;
        String line;
        BufferedWriter writer = null;
        
        try {
            FileInputStream file = new FileInputStream("test.in");
            newReader = new BufferedReader(new InputStreamReader(file));
            
            writer = new BufferedWriter(new FileWriter("./test.out"));

            line = newReader.readLine();
            vect = line.split(" ");
            nV = Integer.parseInt(vect[0]);   //Citesc prima linie
            nE = Integer.parseInt(vect[1]);
            k = Integer.parseInt(vect[2]);
            Graf g=new Graf(nV,nE,k);
            while ((line = newReader.readLine()) != null) {   //Citesc urmatoarele linii
                vect = line.split(" ");
                g.addM(Integer.parseInt(vect[0]),Integer.parseInt(vect[1]));  //adaug muchii la graf
            }
            writer.write(g.Transformare());      //apelez Transformarea
            

        } catch (IOException e) {
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if(newReader!=null){
                    newReader.close();
                }
            } catch (IOException e) {
            }
        }
        
    }
}
