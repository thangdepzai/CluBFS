/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clubfs_mfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thang
 */
public class Population {

    ArrayList<Individual> population = new ArrayList<>();

    static Random rand = new Random(5);
    // cac array phuc vụ cho hàm dijkstra_heap
    double[] d = new double[CluBFS_MFO.NUM_CITY];
    int[] p = new int[CluBFS_MFO.NUM_CITY];
    int[] K = new int[CluBFS_MFO.NUM_CITY];
    // hàm get population
    public ArrayList<Individual> getPopulation() {
        return population;
    }
// khoi tao quan the
    public void initPopulation() {
        for (int i = 0; i < CluBFS_MFO.sizePopulation; i++) {
            Individual indiv = new Individual();
            indiv.InitGene();

            indiv.setFitness();
            population.add(indiv);
        }
      
        calculateScalarFitness(population, CluBFS_MFO.sizePopulation);
      
    }
// lai ghep va dot bien
    public ArrayList<Individual> crossOver(Individual indiv1, Individual indiv2) {
        ArrayList<Individual> child = new ArrayList<Individual>();
        if (indiv1.getSkillFactor() == indiv2.getSkillFactor()) {
            //   System.out.println("lai ghep");
           
            child = Cross2(indiv1, indiv2);
            child.get(0).setFitness();
            child.get(1).setFitness();
            
            return child;
        } else {
           
            child.add(mutation(indiv1));
            child.add(mutation(indiv2));

            return child;
        }
    }

    public static Individual mutation(Individual indiv) {
        Individual ind = new Individual();
        // clone individual
        for (int i = 0; i < CluBFS_MFO.NUMBER_OF_CLUSTERS + 1; i++) {

            try {
                Cluster clt = (Cluster) indiv.getGene().get(i).clone();
                ind.getGene().add(clt);

            } catch (CloneNotSupportedException e) {
                e.printStackTrace();

            }
        }
        
        Map<Integer, Integer> trace_copy = new HashMap<Integer, Integer>();
        for (int i : indiv.getTrace().keySet()) {
            trace_copy.put(i, indiv.getTrace().get(i));
        }
        ind.setTrace(trace_copy);
        
       
        
        int m1, m2, k = 0;
        // chon cluster co it nhat 1 canh
        do {
            k = rand.nextInt(CluBFS_MFO.NUMBER_OF_CLUSTERS);
        } while (indiv.getGene().get(k).getListEdge().size() < 2);
        // chon ngau nhien 2 dinh trong cluster
        do{
        m1 = indiv.getGene().get(k).getListCity().get(rand.nextInt(indiv.getGene().get(k).getListCity().size()));
        }while(!indiv.getGene().get(k).CheckNumberVertex(m1));
        int[] truoc = indiv.FindPathBFS(m1);
       
    
        do {
            m2 = indiv.getGene().get(k).getListCity().get(rand.nextInt(indiv.getGene().get(k).getListCity().size()));
        } while (m1 == m2 || indiv.getGene().get(k).checkHasEdgeInList(m1, m2) || !indiv.getGene().get(k).getListCity().contains(truoc[m2 - 1]));
       
        ind.getGene().get(k).getListEdge().add(new Edge(m1, m2));
        ind.getGene().get(k).removeEdge(m2, truoc[m2 - 1]);
        ind.setFitness();
       
        return ind;

    }
// danh sach dinh ke voi dinh v
    public ArrayList<Integer> DsKe(int v, Cluster clt1, Cluster clt2) {
        ArrayList<Integer> L1 = new ArrayList<>();
        for (Edge e : clt1.getListEdge()) {
            if (e.getX() == v && !L1.contains(e.getY())) {
                L1.add(e.getY());
            } else if (e.getY() == v && !L1.contains(e.getX())) {
                L1.add(e.getX());
            }
        }
        for (Edge e : clt2.getListEdge()) {
            if (e.getX() == v && !L1.contains(e.getY())) {
                L1.add(e.getY());
            } else if (e.getY() == v && !L1.contains(e.getX())) {
                L1.add(e.getX());
            }
        }
        return L1;
    }

//----------------------------heap---------------------
    public int parent(int i) {
        return (i + 1) / 2 - 1;
    }

    public int lChild(int i) {
        int x = i + 1;
        int y = x * 2;
        return y - 1;
    }

    public int rChild(int i) {
        int x = i + 1;
        int y = x * 2 + 1;
        return y - 1;
    }

    public Cluster Dijkstra_Heap(Cluster clt1, Cluster clt2, int start) {

        for (int u : clt1.getListCity()) {
            d[u - 1] = Double.MAX_VALUE;
            p[u - 1] = 0;
            K[u - 1] = 0;
        }
        d[start - 1] = 0;
        ArrayList<Double> Q = new ArrayList<>();
        for (int i = 0; i < clt1.getListCity().size(); i++) {
            Q.add(0.0);
        }

        int j = 0;
        Map<Integer, Integer> pos = new HashMap<Integer, Integer>();
        for (int i : clt1.getListCity()) {
            Q.set(j, d[i - 1]);
            pos.put(j, i);
            j++;
        }
        Build_Min_Heap(Q, pos);
        while (Q.size() != 0) {
            int u = Extract_Min(Q, pos);
            K[u - 1] = 1;
            ArrayList<Integer> Keof_u = DsKe(u, clt1, clt2);
            for (int v : Keof_u) {
                if (K[v - 1] == 0 && d[v - 1] > d[u - 1] + CluBFS_MFO.distances[u - 1][v - 1]) {
                    d[v - 1] = d[u - 1] + CluBFS_MFO.distances[u - 1][v - 1];
                    p[v - 1] = u;
                    int m = 0;
                    for (int i : pos.keySet()) {
                        if (pos.get(i) == v) {
                            m = i;
                            break;
                        }
                    }
                    Decrease_Key(Q, m, d[v - 1], pos);
                }
            }

        }

        ArrayList<Edge> NewListEdge = new ArrayList<Edge>();
        ArrayList<Integer> T = (ArrayList<Integer>) clt1.getListCity().clone();
        T.remove(T.indexOf(start));
        for (int i : T) {
            NewListEdge.add(new Edge(p[i - 1], i));
        }

        return new Cluster(clt1.getListCity(), NewListEdge);
    }

    public Cluster Prim_Heap(Cluster clt1, Cluster clt2, int start) {

        for (int u : clt1.getListCity()) {
            d[u - 1] = Double.MAX_VALUE;
            p[u - 1] = 0;
            K[u - 1] = 0;
        }
        d[start - 1] = 0;
        ArrayList<Double> Q = new ArrayList<>();
        for (int i = 0; i < clt1.getListCity().size(); i++) {
            Q.add(0.0);
        }

        int j = 0;
        Map<Integer, Integer> pos = new HashMap<Integer, Integer>();
        for (int i : clt1.getListCity()) {
            Q.set(j, d[i - 1]);
            pos.put(j, i);
            j++;
        }
        Build_Min_Heap(Q, pos);
        while (Q.size() != 0) {
            int u = Extract_Min(Q, pos);
            K[u - 1] = 1;
            ArrayList<Integer> Keof_u = DsKe(u, clt1, clt2);
            if (!Keof_u.isEmpty()) {
                for (int v : Keof_u) {
                    if (K[v - 1] == 0 && d[v - 1] > CluBFS_MFO.distances[u - 1][v - 1]) {
                        d[v - 1] = CluBFS_MFO.distances[u - 1][v - 1];
                        p[v - 1] = u;
                        int m = 0;
                        for (int i : pos.keySet()) {
                            if (pos.get(i) == v) {
                                m = i;
                                break;
                            }
                        }
                        Decrease_Key(Q, m, d[v - 1], pos);
                    }
                }
            }
        }

        ArrayList<Edge> NewListEdge = new ArrayList<Edge>();
        ArrayList<Integer> T = (ArrayList<Integer>) clt1.getListCity().clone();
        T.remove(T.indexOf(start));
        for (int i : T) {
            NewListEdge.add(new Edge(p[i - 1], i));
        }

        return new Cluster(clt1.getListCity(), NewListEdge);
    }

    public ArrayList<Individual> Cross1(Individual indiv1, Individual indiv2) {
        Individual ind1 = new Individual();
        Individual ind2 = new Individual();
        Arrays.fill(d, 0);
        Arrays.fill(K, 0);
        Arrays.fill(p, 0);
        for (int i = 0; i < CluBFS_MFO.NUMBER_OF_CLUSTERS; i++) {
            ind1.getGene().add(Prim_Heap(indiv1.getGene().get(i), indiv2.getGene().get(i), indiv1.getTrace().get(i)));
            ind2.getGene().add(Prim_Heap(indiv1.getGene().get(i), indiv2.getGene().get(i), indiv2.getTrace().get(i)));
        }
        try {
            ind1.getGene().add((Cluster) indiv1.getGene().get(CluBFS_MFO.NUMBER_OF_CLUSTERS).clone());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Population.class.getName()).log(Level.SEVERE, null, ex);
        }

        Map<Integer, Integer> trace_copy1 = new HashMap<Integer, Integer>();
        for (int i : indiv1.getTrace().keySet()) {
            trace_copy1.put(i, indiv1.getTrace().get(i));
        }
        ind1.setTrace(trace_copy1);
        try {
            ind2.getGene().add((Cluster) indiv2.getGene().get(CluBFS_MFO.NUMBER_OF_CLUSTERS).clone());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Population.class.getName()).log(Level.SEVERE, null, ex);
        }
        Map<Integer, Integer> trace_copy = new HashMap<Integer, Integer>();
        for (int i : indiv2.getTrace().keySet()) {
            trace_copy.put(i, indiv2.getTrace().get(i));
        }
        ind2.setTrace(trace_copy);
        ArrayList<Individual> Child = new ArrayList<>();
        Child.add(ind1);
        Child.add(ind2);
        return Child;

    }

    public ArrayList<Individual> Cross2(Individual indiv1, Individual indiv2) {
        Individual ind1 = new Individual();
        Individual ind2 = new Individual();
        Arrays.fill(d, 0);
        Arrays.fill(K, 0);
        Arrays.fill(p, 0);
        for (int i = 0; i < CluBFS_MFO.NUMBER_OF_CLUSTERS; i++) {
            ind1.getGene().add(Dijkstra_Heap(indiv1.getGene().get(i), indiv2.getGene().get(i), indiv1.getTrace().get(i)));
            ind2.getGene().add(Dijkstra_Heap(indiv1.getGene().get(i), indiv2.getGene().get(i), indiv2.getTrace().get(i)));
        }
        try {
            ind1.getGene().add((Cluster) indiv1.getGene().get(CluBFS_MFO.NUMBER_OF_CLUSTERS).clone());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Population.class.getName()).log(Level.SEVERE, null, ex);
        }

        Map<Integer, Integer> trace_copy1 = new HashMap<Integer, Integer>();
        for (int i : indiv1.getTrace().keySet()) {
            trace_copy1.put(i, indiv1.getTrace().get(i));
        }
        ind1.setTrace(trace_copy1);
        try {
            ind2.getGene().add((Cluster) indiv2.getGene().get(CluBFS_MFO.NUMBER_OF_CLUSTERS).clone());
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Population.class.getName()).log(Level.SEVERE, null, ex);
        }
        Map<Integer, Integer> trace_copy = new HashMap<Integer, Integer>();
        for (int i : indiv2.getTrace().keySet()) {
            trace_copy.put(i, indiv2.getTrace().get(i));
        }
        ind2.setTrace(trace_copy);
        ArrayList<Individual> Child = new ArrayList<>();
        Child.add(ind1);
        Child.add(ind2);
        return Child;

    }

    private void Build_Min_Heap(ArrayList<Double> Q, Map<Integer, Integer> pos) {
        int n = Q.size();
        for (int i = n / 2 - 1; i > -1; i--) {
            Min_Heapify(Q, i, n, pos);
        }
    }

    private void Min_Heapify(ArrayList<Double> A, int i, int n, Map<Integer, Integer> pos) {
        int l = lChild(i);
        int r = rChild(i);
        int minimum;
        if (l < n && A.get(l) < A.get(i)) {
            minimum = l;
        } else {
            minimum = i;
        }

        if (r < n && A.get(r) < A.get(minimum)) {
            minimum = r;
        }

        if (minimum != i) {

            double tmp = A.get(minimum);
            A.set(minimum, A.get(i));
            A.set(i, tmp);
            int tmp1 = pos.get(i);
            pos.put(i, pos.get(minimum));
            pos.put(minimum, tmp1);
            Min_Heapify(A, minimum, n, pos);
        }

    }

    private int Extract_Min(ArrayList<Double> Q, Map<Integer, Integer> pos) {
        int n = Q.size();

        int k = pos.get(0);
        Q.set(0, Q.get(n - 1));
        int tmp = pos.get(n - 1);
        pos.put(n - 1, pos.get(0));
        pos.put(0, tmp);
        Q.remove(n - 1);
        Min_Heapify(Q, 0, n - 1, pos);
        return k;
    }

    private void Decrease_Key(ArrayList<Double> Q, int m, double d, Map<Integer, Integer> pos) {
        Q.set(m, d);
        while (m > 0) {
            if (Q.get(parent(m)) > Q.get(m)) {
                int c = parent(m);
                double tmp = Q.get(c);
                Q.set(c, Q.get(m));
                Q.set(m, tmp);
                int tmp1 = pos.get(m);
                pos.put(m, pos.get(c));
                pos.put(c, tmp1);
                m = c;

            } else {
                break;
            }
        }

    }
// hma tinh scalar
    public void calculateScalarFitness(ArrayList<Individual> pop, int popLength) {

        double[] scalarFitness = new double[popLength];
        int[] skillFactor = new int[popLength];

        ArrayList<Individual> temp_pop = (ArrayList<Individual>) pop.clone();


        for (int i = 0; i < popLength; i++) {
            scalarFitness[i] = 0;
            temp_pop.get(i).setScalarFitness(0);

        }

        for (int i = 0; i < 2; i++) {
            int j = i;
            Collections.sort(temp_pop, new Comparator<Individual>() {
                @Override

                public int compare(Individual o1, Individual o2) {
                    if (o1.getFitness()[j] > o2.getFitness()[j]) {
                        return 1;
                    } else if (o1.getFitness()[j] < o2.getFitness()[j]) {
                        return -1;
                    } else {
                        return 0;
                    }

                }
            });
            for (int k = 0; k < popLength; k++) {
                Individual ind = pop.get(k);
                if (1.0 / (temp_pop.indexOf(ind) + 1) > scalarFitness[k]) {
                    scalarFitness[k] = (1.0 / (temp_pop.indexOf(ind) + 1));
                    skillFactor[k] = i + 1;
                }
            }
        }
        for (int k = 0; k < popLength; k++) {

            pop.get(k).setScalarFitness(scalarFitness[k]);
            pop.get(k).setSkillFactor(skillFactor[k]);
        }
        skillFactor = null;
        scalarFitness = null;

    }

}
