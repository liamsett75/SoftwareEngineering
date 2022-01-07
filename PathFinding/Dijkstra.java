package PathFinding;

import Graph.Node;

public class Dijkstra extends AdvancedPathFindingTemplate implements Strategy {

    void calcCosts(Node i, Node current) {
         g = calcGCost(current, i);  //set g value of neighbors
         z = calcZCost(current, i);
         f = g + z;   //set f value of neighbors
    }

    void assignCostsToNode(Node i) {
        i.setGVal(g);
        i.setZVal(z);
        i.setFVal(f);
    }

    @Override
    public boolean equals(Object object){
        return object instanceof Dijkstra;
    }

    @Override
    public String toString() {
        return "Dijkstra";
    }
}
