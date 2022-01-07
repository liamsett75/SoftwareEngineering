package PathFinding;

import Graph.Node;

import java.util.ArrayList;

public class DepthFirst extends SimplePathFindingTemplate implements Strategy{

    void visit(ArrayList<Node> nodesToVisit, Node i) {
        nodesToVisit.add(0, i); // Component that makes the algorithm depth first, goes deeps by investigating a Nodes neighbors
    }

    @Override
    public boolean equals(Object object){
        return object instanceof DepthFirst;
    }

    @Override
    public String toString() {
        return "Depth First";
    }
}
