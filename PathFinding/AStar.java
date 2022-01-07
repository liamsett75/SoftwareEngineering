package PathFinding;

import Graph.Node;

public class AStar extends AdvancedPathFindingTemplate implements Strategy{

    void calcCosts(Node i, Node current) {
        g = calcGCost(current, i);  //set g value of neighbors
        h = calcHCost(i, endNode);  //set h value of neighbors
        z = calcZCost(current, i);
        f = g + h + z;   //set f value of neighbors
    }

    void assignCostsToNode(Node i) {
        i.setGVal(g);
        i.setHVal(h);
        i.setZVal(z);
        i.setFVal(f);
    }

    @Override
    public boolean equals(Object object){
        return object instanceof AStar;
    }

    @Override
    public String toString() {
        return "AStar";
    }
}
