/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

public class Graf {

    private final ArrayList<Integer> muchii;
    private final int nrNod;
    private final int k;
    private final int nrM;

    private final boolean matriceDeAdiacenta[][];

    public Graf(int nV, int nE, int k) {
        nrNod = nV;
        nrM = nE;
        this.k = k;
        muchii = new ArrayList<>();
        matriceDeAdiacenta = new boolean[nrNod + 1][nrNod + 1];
    }

    public void addM(int a, int b) {
        muchii.add(a);
        muchii.add(b);
        matriceDeAdiacenta[a][b] = true;
        matriceDeAdiacenta[b][a] = true;
    }

    public String Transformare() {
        String y[][] = new String[nrNod + 1][k + 1];            //nu folosesc indicele 0 
        String F = "";
        int count = 0;
        //imi creez nrNod*k variabile
        for (int i = 1; i <= nrNod; i++) {
            for (int j = 1; j <= k; j++) {
                count++;
                y[i][j] = "x" + count;
            }
        }

        //at_most_one
        //Pentru fiecare K fac clauze (~vi V ~vj) unde 1 <= i < j <= Numaru de noduri
        for (int i = 1; i <= k; i++) {
            F += "";
            for (int j = 1; j <= nrNod; j++) {
                for (int h = j + 1; h <= nrNod; h++) {
                    F += "(~" + y[j][i] + "V" + "~" + y[h][i] + ")";
                    if (h != nrNod + 1) {
                        F += "^";
                    }
                }
            }
        }

        int counter = 0;
        
        
        //at_least_one
        //Pentru fiecare perechi de muchii 
        //fac clauze (Xv1 V Xu1 ...Xvi V Xui ...Xvk V Xuk )
        for (int i = 1; i <= nrM; i++) {
            int m1, m2;
            m1 = muchii.get(counter);
            m2 = muchii.get(++counter);
            counter++;
            F += "(";
            for (int j = 1; j <= k; j++) {
                F += "" + y[m1][j] + "V" + y[m2][j] + "";
                if (j != k) {
                    F += "V";
                }
            }
            F += ")";
            if (i != nrM) {
                F += "^";
            }
        }
        return F;
    }

}
