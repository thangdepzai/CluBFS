/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clubfs_mfo;

/**
 *
 * @author thang
 */
public class Edge {

    private int x;
    private int y;

    public Edge(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
// hma kiem tra canh do co phai la (m1,m2)
    public boolean CheckEdge(int m1, int m2) {
        if ((this.x == m1 && this.y == m2) || (this.x == m2 && this.y == m1)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

   
    

}
