/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clubfs_mfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author thang
 */
public class Individual {

    private int skillFactor;
    private Map<Integer, Integer> trace = new HashMap<Integer, Integer>();

    public void setSkillFactor(int skillFactor) {
        this.skillFactor = skillFactor;
    }

    public void setScalarFitness(double scalarFitness) {
        this.scalarFitness = scalarFitness;
    }
    Random rand = new Random();
    private double scalarFitness;
    Double[] fitness = new Double[2];
    private ArrayList<Cluster> gene = new ArrayList<Cluster>();

    public void InitGene() {
        ArrayList<Integer> VertexConnect = new ArrayList<>();
        int i = 0;
        for (List<Integer> c : CluBFS_MFO.ListClusterCity) {
            int k = c.get(rand.nextInt(c.size()));
            if (c.contains(CluBFS_MFO.SOURCE_VERTEX)) {
                trace.put(i, CluBFS_MFO.SOURCE_VERTEX);
            } else {
                trace.put(i, k);
            }
            i++;
            VertexConnect.add(k);

            Cluster clt = new Cluster((ArrayList<Integer>) c);
            clt.InitSubtree();
            gene.add(clt);

        }

        Cluster clt = new Cluster(VertexConnect);
        clt.InitSubtree();
        gene.add(clt);

    }

    public Map<Integer, Integer> getTrace() {
        return trace;
    }

    public void setTrace(Map<Integer, Integer> trace) {
        this.trace = trace;
    }

    public int getSkillFactor() {
        return skillFactor;
    }

    public double getScalarFitness() {
        return scalarFitness;
    }

    public Double[] getFitness() {
        return fitness;
    }

    public void setFitness() {
        double weight = 0;
        int[] truoc = FindPathBFS();
        for (int i = 1; i < CluBFS_MFO.NUM_CITY + 1; i++) {
            if (i != CluBFS_MFO.SOURCE_VERTEX) {
                weight += C(CluBFS_MFO.SOURCE_VERTEX, i, truoc);
            }
        }
        this.fitness[0] = weight;
        weight = 0;
        for (int i : CluBFS_MFO.ListClusterCity.get(CluBFS_MFO.mark)) {
            if (CluBFS_MFO.ListClusterCity.get(CluBFS_MFO.mark).contains(truoc[i - 1])) {
                weight += CluBFS_MFO.distances[i - 1][truoc[i - 1] - 1];

            }
        }
        this.fitness[1] = weight;
    }

    public ArrayList<Cluster> getGene() {
        return gene;
    }

    public ArrayList<Integer> DsKe(int v) {
        ArrayList<Integer> L1 = new ArrayList<>();
        for (int i = 0; i < CluBFS_MFO.NUMBER_OF_CLUSTERS; i++) {
            if (CluBFS_MFO.ListClusterCity.get(i).contains(v)) {
                for (Edge j : gene.get(i).getListEdge()) {
                    if (j.getX() == v) {
                        L1.add(j.getY());
                    } else if (j.getY() == v) {
                        L1.add(j.getX());
                    }
                }
                break;
            }
        }
        if (gene.get(CluBFS_MFO.NUMBER_OF_CLUSTERS).getListCity().contains(v)) {
            for (Edge j : gene.get(CluBFS_MFO.NUMBER_OF_CLUSTERS).getListEdge()) {
                if (j.getX() == v) {
                    L1.add(j.getY());
                } else if (j.getY() == v) {
                    L1.add(j.getX());
                }
            }
        }
        return L1;

    }
    public int[] FindPathBFS(int start) {
        ArrayList<Integer> queue = new ArrayList<>();
        int[] truoc = new int[CluBFS_MFO.NUM_CITY];
        int[] chuaXet = new int[CluBFS_MFO.NUM_CITY];
        Arrays.fill(truoc, 0);
        Arrays.fill(chuaXet, 1);
        queue.add(start);
        chuaXet[start - 1] = 1;

        while (queue.size() != 0) {
            int p = queue.remove(0);
            ArrayList<Integer> Ke = DsKe(p);
            for (int u : Ke) {
                if (chuaXet[u - 1] == 1) {
                    truoc[u - 1] = p;
                    queue.add(u);
                    chuaXet[u - 1] = 0;
                }
            }
        }
        return truoc;
    }
    public int[] FindPathBFS() {
        ArrayList<Integer> queue = new ArrayList<>();
        int[] truoc = new int[CluBFS_MFO.NUM_CITY];
        int[] chuaXet = new int[CluBFS_MFO.NUM_CITY];
        Arrays.fill(truoc, 0);
        Arrays.fill(chuaXet, 1);
        queue.add(CluBFS_MFO.SOURCE_VERTEX);
        chuaXet[CluBFS_MFO.SOURCE_VERTEX - 1] = 1;

        while (queue.size() != 0) {
            int p = queue.remove(0);
            ArrayList<Integer> Ke = DsKe(p);
            for (int u : Ke) {
                if (chuaXet[u - 1] == 1) {
                    truoc[u - 1] = p;
                    queue.add(u);
                    chuaXet[u - 1] = 0;
                }
            }
        }
        return truoc;
    }

    public double C(int start, int finish, int[] truoc) {
        double weight = 0;
        int j = finish;
        while (j != start) {
            weight += CluBFS_MFO.distances[j - 1][truoc[j - 1] - 1];
            j = truoc[j - 1];
        }

        return weight;

    }

  
    

}
