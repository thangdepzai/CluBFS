/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clubfs_mfo;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author thang
 */
public class Cluster implements Cloneable {

// list chua danh sach cac thanh pho
    private ArrayList<Integer> ListCity;
    // list chua danh sach canh cua Cluster
    private ArrayList<Edge> ListEdge;
// ham contructor

    public Cluster(ArrayList<Integer> ListCity) {
        this.ListCity = ListCity;
    }

    public Cluster(ArrayList<Integer> ListCity, ArrayList<Edge> ListEdge) {
        this.ListCity = ListCity;
        this.ListEdge = ListEdge;
    }

    // ham khơi tao ca the
    public void InitSubtree() {
        ListEdge = new ArrayList<Edge>();
        ArrayList<Integer> ListCity_clone = (ArrayList<Integer>) ListCity.clone();
        ArrayList<Integer> vertex = new ArrayList<Integer>();
        Random rand = new Random();
        int vitri = rand.nextInt(ListCity.size());
        int k = ListCity.get(vitri);
        vertex.add(k);
        ListCity_clone.remove(vitri);

        while (ListEdge.size() < ListCity.size() - 1) {
            vitri = rand.nextInt(ListCity_clone.size());
            int c1 = ListCity_clone.get(vitri);
            ListCity_clone.remove(vitri);
            vitri = rand.nextInt(vertex.size());
            int c2 = vertex.get(vitri);
            vertex.add(c1);
            ListEdge.add(new Edge(c1, c2));
        }

    }
// ham check xem danh sach co canh (m1,m2)

    public boolean checkHasEdgeInList(int m1, int m2) {
        for (Edge e : this.ListEdge) {
            if (e.CheckEdge(m1, m2)) {
                return true;
            }
        }
        return false;

    }

    // hma remove canh khoi list
    public void removeEdge(int m1, int m2) {
        for (int i = 0; i < this.ListEdge.size(); i++) {
            if (ListEdge.get(i).CheckEdge(m1, m2)) {

                this.ListEdge.remove(i);
                break;
            }

        }

    }

    public ArrayList<Integer> getListCity() {
        return ListCity;
    }

    public ArrayList<Edge> getListEdge() {
        return ListEdge;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Cluster cloneCluster = (Cluster) super.clone(); //To change body of generated methods, choose Tools | Templates.
        cloneCluster.ListEdge = (ArrayList<Edge>) cloneCluster.ListEdge.clone();
        return cloneCluster;
    }
    public boolean CheckNumberVertex(int vertex){
        int dem =0;
        for(int v:ListCity ){
            if(checkHasEdgeInList(vertex,v)){
                dem++;
            }
        }
        return (dem!=ListEdge.size());
    }
}
