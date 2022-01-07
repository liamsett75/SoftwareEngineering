package PathFinding;

import Graph.Node;

public class BestFirst extends AdvancedPathFindingTemplate implements Strategy{

    void calcCosts(Node i, Node current) {
        h = calcHCost(i, endNode);  //set h value of neighbors
        z = calcZCost(current, i);
        f = h + z;   //set f value of neighbors
    }

    void assignCostsToNode(Node i) {
        i.setHVal(h);
        i.setZVal(z);
        i.setFVal(f);
    }

    @Override
    public boolean equals(Object object){
        return object instanceof BestFirst;
    }

    @Override
    public String toString() {
        return "Best First";
    }
}
