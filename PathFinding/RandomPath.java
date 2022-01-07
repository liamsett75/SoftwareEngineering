package PathFinding;

import Databases.NodeDB;
import Graph.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class RandomPath extends PathFindingTemplate implements Strategy {
    Node startNode;
    Node endNode;

    @Override
    public double[][] getPath(HashMap<String, Node> nodeHashMap, String startNodeID, String endNodeID, int conditions) {
        buildFloorMap();

        startNode = nodeHashMap.get(startNodeID);
        endNode = nodeHashMap.get(endNodeID);
        startNode.setParent(null);
        LinkedList<Node> allNodes = NodeDB.getDBNodes();
        double finalPathCoords[][];

        ArrayList<Node> parentList = new ArrayList<>(); // should be local
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        Node current;
        do{

            int bound = random.nextInt(allNodes.size());
            current = allNodes.get(bound);

            addToParents(current, parentList);
            int length = parentList.size();
            finalPathCoords = new double[length][3];

            int i = 0;

            for (Node j: parentList) {
                finalPathCoords[i][0] = j.getXCoord();
                finalPathCoords[i][1] = j.getYCoord();
                finalPathCoords[i][2] = floorMap.get(j.getFloor());
                i++;
            }
        } while (!current.getNodeID().equals(endNode.getNodeID()));

        return finalPathCoords;
    }
}
