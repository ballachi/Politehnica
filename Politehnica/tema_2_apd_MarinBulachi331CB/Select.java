
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author marinbulachi
 */
public class Select implements Callable {

    private ArrayList<ArrayList<Object>> table;
    private ArrayList index;
    private String op;

    private ArrayList l;

    Select(ArrayList<ArrayList<Object>> table, ArrayList index, String op) {
        this.table = table;
        this.index = index;
        this.op = op;
    }

    @Override
    public Object call() throws Exception {
        ArrayList list = operations(table, op, index);
        return list;
    }

    public ArrayList operations(ArrayList<ArrayList<Object>> table, String op, ArrayList index) {
        l = new ArrayList();

        if (op.charAt(0) == 'm' && op.charAt(1) == 'a' && op.charAt(2) == 'x' && op.charAt(3) == '(') {
            String columnNamesOp = op.substring(4, op.length() - 1);

            int columNumber = -1;
            for (int i = 0; i < table.size(); i++) {
                String a = (String) (table.get(i).get(0));
                if (a.equals(columnNamesOp)) {
                    columNumber = i;
                    break;
                }
            }
            if (columNumber != -1) {
                int max = -100000;
                for (int i = 0; i < index.size(); i++) {
                    int ind = (int) index.get(i);
                    int n = (int) (table.get(columNumber).get(ind));
                    if (n > max) {
                        max = n;
                    }
                }
                l.add(max);
            }

        } else if (op.charAt(0) == 'm' && op.charAt(1) == 'i' && op.charAt(2) == 'n' && op.charAt(3) == '(') {
            String columnNamesOp = op.substring(4, op.length() - 1);
            int columNumber = -1;
            for (int i = 0; i < table.size(); i++) {
                String a = (String) (table.get(i).get(0));
                if (a.equals(columnNamesOp)) {
                    columNumber = i;
                    break;
                }
            }
            if (columNumber != -1) {
                int min = 100000;
                for (int i = 0; i < index.size(); i++) {
                    int ind = (int) index.get(i);
                    int n = (int) (table.get(columNumber).get(ind));
                    if (n < min) {
                        min = n;
                    }
                }
                l.add(min);
            }

        } else if (op.charAt(0) == 's' && op.charAt(1) == 'u' && op.charAt(2) == 'm' && op.charAt(3) == '(') {
            String columnNamesOp = op.substring(4, op.length() - 1);
            int columNumber = -1;
            for (int i = 0; i < table.size(); i++) {
                String a = (String) (table.get(i).get(0));
                if (a.equals(columnNamesOp)) {
                    columNumber = i;
                    break;
                }
            }
            if (columNumber != -1) {
                int sum = 0;
                for (int i = 0; i < index.size(); i++) {
                    int ind = (int) index.get(i);
                    int n = (int) (table.get(columNumber).get(ind));
                    sum += n;
                }
                l.add(sum);
            }

        } else if (op.charAt(0) == 'a' && op.charAt(1) == 'v' && op.charAt(2) == 'g' && op.charAt(3) == '(') {
            String columnNamesOp = op.substring(4, op.length() - 1);
            int columNumber = -1;
            for (int i = 0; i < table.size(); i++) {
                String a = (String) (table.get(i).get(0));
                if (a.equals(columnNamesOp)) {
                    columNumber = i;
                    break;
                }
            }
            if (columNumber != -1) {
                int sum = 0;
                for (int i = 0; i < index.size(); i++) {
                    int ind = (int) index.get(i);
                    int n = (int) (table.get(columNumber).get(ind));
                    sum += n;
                }
                int avg = sum / index.size();
                l.add(avg);
            }

        } else if (op.charAt(0) == 'c' && op.charAt(1) == 'o' && op.charAt(2) == 'u' && op.charAt(3) == 'n'
                && op.charAt(4) == 't' && op.charAt(5) == '(') {
            String columnNamesOp = op.substring(6, op.length() - 1);
            int columNumber = -1;
            for (int i = 0; i < table.size(); i++) {
                String a = (String) (table.get(i).get(0));
                if (a.equals(columnNamesOp)) {
                    columNumber = i;
                    break;
                }
            }
            if (columNumber != -1) {
                l.add(index.size());
            }

        } else {
            int columNumber = -1;
            for (int i = 0; i < table.size(); i++) {
                String a = (String) (table.get(i).get(0));
                if (a.equals(op)) {
                    columNumber = i;
                    break;
                }
            }
            for (int i = 0; i < index.size(); i++) {
                int ind = (int) index.get(i);
                l.add(table.get(columNumber).get(ind));
            }

        }
        return l;

    }

}
