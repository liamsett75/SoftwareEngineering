package PathFinding;

import Graph.Node;

import java.util.HashMap;

public interface Strategy {
     double[][] getPath(HashMap<String, Node> nodeHashMap, String startNodeID, String endNodeID, int conditions);
     static Node getStartNode(Node startNode) {
          return startNode;
     }

     static Node getEndNode(Node endNode) {
          return endNode;
     }

}
