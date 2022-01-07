package PathFinding;

import Graph.Node;

import java.util.ArrayList;

public class BreadthFirst extends SimplePathFindingTemplate implements Strategy{

    void visit(ArrayList<Node> nodesToVisit, Node i) {
        nodesToVisit.add(i); // Adds the node to the back of the list, makes it breadth first as it just continually expands outward
    }

    @Override
    public boolean equals(Object object){
        return object instanceof BreadthFirst;
    }

    @Override
    public String toString() {
        return "Breadth First";
    }
}