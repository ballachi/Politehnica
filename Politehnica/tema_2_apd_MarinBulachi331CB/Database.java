/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author  marinbulachi
 */
public class Database extends Thread implements MyDatabase {

    Map<String, ArrayList<ArrayList<Object>>> hmap = new HashMap<String, ArrayList<ArrayList<Object>>>();
     Map<String, Semaphore> hmapSemaphore = new HashMap<String, Semaphore>();
    Map<String, Semaphore> hmapSemaphoreRead = new HashMap<String, Semaphore>();
    Map<String, Integer> readCount = new HashMap<String, Integer>();
    CyclicBarrier barrier;
    Thread[] myThreads;
    int numWorkerThreads;
    int auxNumWorkerThreads;
    ExecutorService executor;

    @Override
    public void initDb(int numWorkerThreads) {
        this.numWorkerThreads = numWorkerThreads;
        executor = Executors.newFixedThreadPool(numWorkerThreads);
    }

    @Override
    public void stopDb() {
        executor.shutdownNow();
        
    }
    @Override
    synchronized public void createTable(String tableName, String[] columnNames, String[] columnTypes) {
        int size = columnNames.length;
        ArrayList<ArrayList<Object>> table = new ArrayList<ArrayList<Object>>();
        for (int i = 0; i < size; i++) {
            ArrayList l = new ArrayList();
            l.add(columnNames[i]);
            l.add(columnTypes[i]);
            table.add(i, l);
        }
        hmapSemaphore.put(tableName, new Semaphore(1));
        hmapSemaphoreRead.put(tableName, new Semaphore(1));
        readCount.put(tableName, 0);
        hmap.put(tableName, table);
    }

    @Override
    public void insert(String tableName, ArrayList<Object> values) {
        try {
            hmapSemaphore.get(tableName).acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i < values.size(); i++) {
            hmap.get(tableName).get(i).add(values.get(i));
        }

        hmapSemaphore.get(tableName).release();
    }

    @Override
    public void startTransaction(String tableName) {
        executor.shutdownNow();
        auxNumWorkerThreads = numWorkerThreads;
        numWorkerThreads = 1;
        executor = Executors.newFixedThreadPool(numWorkerThreads);
    }

    @Override
    public void endTransaction(String tableName) {
        executor.shutdownNow();
        numWorkerThreads = auxNumWorkerThreads;
        executor = Executors.newFixedThreadPool(numWorkerThreads);
    }

    @Override
    public void update(String tableName, ArrayList<Object> values, String condition) {
        try {
            hmapSemaphore.get(tableName).acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

        ArrayList<ArrayList<Object>> table = hmap.get(tableName);
        ArrayList index = new ArrayList();
        index = condition(table, condition);
        for (int i = 0; i < index.size(); i++) {
            int index2 = (int) index.get(i);
            for (int j = 0; j < table.size(); j++) {
                table.get(j).set(index2, values.get(j));
            }
        }

        hmap.remove(tableName);
        hmap.put(tableName, table);

        hmapSemaphore.get(tableName).release();
    }

    @Override
    public ArrayList<ArrayList<Object>> select(String tableName, String[] operations, String condition) {
        //primul cititor pune bloc
        try {
            hmapSemaphoreRead.get(tableName).acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        int x = readCount.get(tableName);
        x++;
        readCount.remove(tableName);
        readCount.put(tableName, x);
        if (x == 1) {
            try {

                hmapSemaphore.get(tableName).acquire();

            } catch (InterruptedException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        hmapSemaphoreRead.get(tableName).release();

        // citirea    
        ArrayList<ArrayList<Object>> table = hmap.get(tableName);
        ArrayList<ArrayList<Object>> sel = new ArrayList<ArrayList<Object>>();

        ArrayList index = condition(table, condition);
        sel = (operations(table, operations, index));
        //doar ultimul poate sa scoata blocul  
        try {
            hmapSemaphoreRead.get(tableName).acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }

        int y = readCount.get(tableName);
        y--;
        readCount.remove(tableName);
        readCount.put(tableName, y);
        hmapSemaphoreRead.get(tableName).release();
        if (y == 0) {
            hmapSemaphore.get(tableName).release();
        }
        return sel;

    }

    public ArrayList<ArrayList<Object>> operations(ArrayList<ArrayList<Object>> table, String[] op, ArrayList index) {

        ArrayList<ArrayList<Object>> l = new ArrayList();
        ArrayList aux = new ArrayList();
        int size = op.length;
        for (int i = 0; i < size; i++) {
            Select p = new Select(table, index, op[i]);
            Future future = executor.submit(p);
            aux.add(future);
        }

        for (int i = 0; i < size; i++) {
            try {
                l.add((ArrayList<Object>) (((Future) aux.get(i)).get()));
            } catch (InterruptedException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return l;
    }

    public ArrayList condition(ArrayList<ArrayList<Object>> table, String condition) {

        ArrayList index = new ArrayList();
        Scanner scanner = new Scanner(condition);
        String columnNames = scanner.next();
        String op = scanner.next();
        String value = scanner.next();

        int columNumber = -1;
        for (int i = 0; i < table.size(); i++) {
            String a = (String) (table.get(i).get(0));
            if (a.equals(columnNames)) {
                columNumber = i;
                break;
            }
        }
        if (columNumber == -1) {
            for (int i = 2; i < table.get(0).size(); i++) {
                index.add(i);
            }
            return index;
        }

        ArrayList coloana = table.get(columNumber);

        int nuberLine = coloana.size();
        String type = (String) coloana.get(1);

        if (type.equals("int")) {
            int num;
            try {
                num = Integer.parseInt(value);
            } catch (NumberFormatException a) {
                return index;
            }
            if (op.equals("==")) {
                for (int i = 2; i < nuberLine; i++) {
                    if (num == ((int) coloana.get(i))) {
                        index.add(i);
                    }
                }
            } else if (op.equals(">")) {
                for (int i = 2; i < nuberLine; i++) {
                    if (((int) coloana.get(i)) > num) {
                        index.add(i);
                    }
                }
            } else if (op.equals("<")) {
                for (int i = 2; i < nuberLine; i++) {
                    if (((int) coloana.get(i)) < num) {
                        index.add(i);
                    }
                }
            }
        } else if (type.equals("string")) {
            if (op.equals("==")) {
                for (int i = 2; i < nuberLine; i++) {
                    if (((String) coloana.get(i)).equals(value)) {
                        index.add(i);
                    }
                }
            } else {
                for (int i = 2; i < table.get(0).size(); i++) {
                    index.add(i);
                }
            }
        } else if (type.equals("bool")) {
            boolean val;
            val = value.equals("true");
            if (op.equals("==")) {
                for (int i = 2; i < nuberLine; i++) {
                    if (((boolean) coloana.get(i)) == val) {
                        index.add(i);
                    }
                }
            } else {
                for (int i = 2; i < table.get(0).size(); i++) {
                    index.add(i);
                }
            }
        }

        return index;
    }

}
