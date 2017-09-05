/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clubfs_mfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import static java.util.Collections.list;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thang
 */
public class CluBFS_MFO {

    static int NUM_CITY = 0;
    static int NUMBER_OF_CLUSTERS = 0;
    static int SOURCE_VERTEX = 0;
    static List<List<Integer>> ListClusterCity = new ArrayList<List<Integer>>();
    public static int sizePopulation = 100;
     static City[] cities;
     static Double[][] distances;
     static Random rand = new Random();
     static int max =-1;
     static int mark;
    public static void main(String[] args) {

        BufferedReader br = null;
        try {
            String sCurrentLine = null;
            br = new BufferedReader(new FileReader("3burma14.clt"));
            // read lines 1..4
            for (int j = 0; j < 4; j++) {
                sCurrentLine = br.readLine();
            }
            String[] str = sCurrentLine.split(": ");
            NUM_CITY = Integer.parseInt(str[1]);
            //******************************************
//             System.out.println(NUM_CITY);
            //******************************************
            sCurrentLine = br.readLine();
            str = sCurrentLine.split(": ");
            NUMBER_OF_CLUSTERS = Integer.parseInt(str[1]);
            //******************************************
//             System.out.println(NUMBER_OF_CLUSTERS);
            //******************************************
            for (int j = 0; j < 4; j++) {
                sCurrentLine = br.readLine();
            }
           cities = new City[NUM_CITY];
             distances = new Double[NUM_CITY][NUM_CITY];
            for (int j = 0; j < NUM_CITY; j++) {
                sCurrentLine = br.readLine();
                ///   System.out.println(sCurrentLine);
                str = sCurrentLine.split(" "); // split by space
                ArrayList<String> data = new ArrayList<>();
                for (int i = 0; i < str.length; i++) {
                    if (!str[i].isEmpty()) {
                        data.add(str[i]);
                    }
                }
                cities[j] = new City(Integer.parseInt(data.get(0)), Double.parseDouble(data.get(1)), Double.parseDouble(data.get(2)));
                for (int i = 0; i <= j; i++) {
                    if (i == j) {
                        distances[j][i] = 0.0;
                    } else {
                        distances[j][i] = distances[i][j] = Math.sqrt(Math.pow((cities[j].getX() - cities[i].getX()), 2)
                                + Math.pow((cities[j].getY() - cities[i].getY()), 2));
                    }
                }

            }

            for (int j = 0; j < 2; j++) {
                sCurrentLine = br.readLine();
            }
            str = sCurrentLine.split(": ");
            SOURCE_VERTEX = Integer.parseInt(str[1]);
            for (int j = 0; j < NUMBER_OF_CLUSTERS; j++) {
                sCurrentLine = br.readLine();
                str = sCurrentLine.split(" ");
                List<Integer> L = new ArrayList<>();
                for (int k = 1; k < str.length - 1; k++) {
                    L.add(Integer.parseInt(str[k])+1);
                }
                ListClusterCity.add(L);
                if(L.size()>max){
                    max = L.size();
                    mark =j;
                    
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Before MFO");
        Population pop = new Population();
        pop.initPopulation();
        for (int i = 0; i < 1000; i++) {
            System.out.println("Lần "+(i+1));
            pop = run(pop);
        }
         System.out.println("After MFO");
         System.out.println(pop.getPopulation().get(0).getFitness()[0]);
   
        

    }
      public static Population run(Population pop)  {
        Population temp_pop = new Population();
        temp_pop.population = (ArrayList<Individual>) pop.getPopulation().clone();
         
        
        Population new_pop = new Population(); 
        for (int i = 0; i < sizePopulation / 2; i++) {
            // ramdomly choose parents to crossover or mutation
            int parentIndex1, parentIndex2;
            parentIndex1 = rand.nextInt(sizePopulation);
            do {
                parentIndex2 = rand.nextInt(sizePopulation);
            } while (parentIndex1 == parentIndex2);

            if (parentIndex1 >= sizePopulation || parentIndex2 >= sizePopulation) {
                System.out.println("loi index");
            }
            ArrayList<Individual> childs = new ArrayList<Individual>();

            // crossover/mutation
            childs = pop.crossOver(pop.getPopulation().get(parentIndex1), pop.getPopulation().get(parentIndex2));
            // add to temp_pop
            temp_pop.getPopulation().add(childs.get(0));
            temp_pop.getPopulation().add(childs.get(1));
        }
           
        temp_pop.calculateScalarFitness(temp_pop.getPopulation(), temp_pop.getPopulation().size());
        // calculate scalarFitness and skillFactor of temp_pop
        // sort temp_pop by scalarFitness
         
        Collections.sort(temp_pop.getPopulation(), new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                if (o1.getScalarFitness() < o2.getScalarFitness()) {
                    return 1;
                } else if (o1.getScalarFitness() > o2.getScalarFitness()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
         
        for (int i = 0; i < sizePopulation; i++) {
            new_pop.getPopulation().add(temp_pop.getPopulation().get(i));
        }
        pop = null;
        return new_pop;
    }

    
}
