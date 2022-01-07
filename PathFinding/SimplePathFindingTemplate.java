package PathFinding;

import Graph.Node;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class SimplePathFindingTemplate extends PathFindingTemplate {


    // primitive methods
    abstract void visit(ArrayList<Node> nodesToVisit, Node i);

    /**
     * Calculates the path using depth first or breadth first
     * @param nodeHashMap List of nodes related by their IDs
     * @param startNodeID The starting node
     * @param endNodeID The destination node
     * @return An double [][] of Nodes
     */
    public final double[][] getPath(HashMap<String, Node> nodeHashMap, String startNodeID, String endNodeID, int conditions) {
        // template set up
        buildFloorMap();
        configureConditions(conditions);

        Node current;
        startNode = nodeHashMap.get(startNodeID);
        endNode = nodeHashMap.get(endNodeID);
        startNode.setParent(null);

        ArrayList<Node> parentList = new ArrayList<>(); // should be local
        ArrayList<Node> nodesToVisit = new ArrayList<>();
        ArrayList<Node> visitedNodes = new ArrayList<>();

        nodesToVisit.add(startNode);
        current = nodesToVisit.get(0);
        visitedNodes.add(current);

        nodesToVisit.add(startNode);
        while(!nodesToVisit.isEmpty() && !current.equals(endNode)) {
            nodesToVisit.remove(0);
            HashMap<String, Node> neighbors = current.getNeighbors();
            for (Node i : neighbors.values()) {

                checkConditions(i, current);
              
                if (!visitedNodes.contains(i) && !noStairCondition && !noElevCondition) {
                    visit(nodesToVisit, i); // makes the algorithm breadth or depth
                    visitedNodes.add(i);
                    i.setParent(current);
                }
            }
            try { // Attempts to get the current Node, if it fails that means the path is not possible
                current = nodesToVisit.get(0);
            }
            catch (IndexOutOfBoundsException e){ // Catches the case of the path not being possible and handles it accordingly
               return youCantGoThere();
            }
        }

        addToParents(current, parentList);

        int length = parentList.size();
        double finalPathCoords[][] = new double[length][3];

        int i = 0;

        for (Node j: parentList) {
            finalPathCoords[i][0] = j.getXCoord();
            finalPathCoords[i][1] = j.getYCoord();
            finalPathCoords[i][2] = floorMap.get(j.getFloor());
            i++;
        }

        this.startNode = startNode;
        this.endNode = endNode;

        return finalPathCoords;
    }
}
