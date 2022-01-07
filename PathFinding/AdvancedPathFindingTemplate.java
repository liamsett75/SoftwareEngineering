package PathFinding;

import Graph.Node;
import Graph.NodeComparator;

import java.util.*;

public abstract class AdvancedPathFindingTemplate extends PathFindingTemplate {
    // cost variables
    protected double g;
    protected double h;
    protected double z;
    protected double f;

    // primitive methods
    abstract void calcCosts(Node i, Node current);
    abstract void assignCostsToNode(Node i);

    /**
     * Calculates the distance between the current node and its neighbor
     * @param current The current node
     * @param neighbor The neighboring node
     * @return A double which represents the cost
     */
    public final double calcGCost(Node current, Node neighbor) {
        double current_neighbor_dist = Math.sqrt(Math.pow((current.getXCoord() - neighbor.getXCoord()), 2) +
                Math.pow((current.getYCoord() - neighbor.getYCoord()), 2));
        double g = current.getGVal()+ current_neighbor_dist;
        return g;
    }

    /**
     * Calculates the distance between the neighboring node and the goal node
     * @param neighbor The neighboring node
     * @param goal The goal node
     * @return A double which represents the distance between the neighbor and goal nodes
     */
    public final double calcHCost(Node neighbor, Node goal) {
        double h = Math.sqrt(Math.pow((neighbor.getXCoord() - goal.getXCoord()),2) +
                Math.pow((neighbor.getYCoord() - goal.getYCoord()),2));
        return h;
    }

    /**
     * Calculates the floor distance between the current node and the neighboring node
     * @param current The current node
     * @param neighbor The neighboring node
     * @return A double which represents the distance between the neighbor and goal nodes
     */
    public final double calcZCost(Node current, Node neighbor) {
        String currentFloor = current.getFloor();
        String neighborFloor = neighbor.getFloor();

        double z = 318.3 * Math.abs((floorMap.get(currentFloor) - floorMap.get(neighborFloor)));
        return z;
    }

    /**
     * Calculates the best route using A*, Dijkstra's, or Best First
     * @param nodeHashMap List of nodes related by their IDs
     * @param startNodeID The starting node
     * @param endNodeID The destination node
     * @return An double [][] of Nodes
     */
    public final double[][] getPath(HashMap<String, Node> nodeHashMap, String startNodeID, String endNodeID, int conditions) {
        // template set up
        buildFloorMap();
        configureConditions(conditions);

        startNode = new Node(null, 0, 0, null, null, null, null, null, null, 0, 0, 0, 0);
        endNode = new Node(null, 0, 0, null, null, null, null, null, null, 0, 0, 0, 0);

        startNode = nodeHashMap.get(startNodeID);
        endNode = nodeHashMap.get(endNodeID);

        startNode.setParent(null);

        Comparator<Node> fCompare = new NodeComparator();
        PriorityQueue<Node> sortedNodes = new PriorityQueue<Node>(fCompare);  // stores nodes by the smallest f value
        ArrayList<Node> parentList = new ArrayList<Node>(); // should be local
        ArrayList<Node> visited = new ArrayList<Node>();

        double parentCoordArray[][];
        double totalTime;

        sortedNodes.add(startNode);
        Node current = sortedNodes.peek();

        // If the priority list is not empty, take the smallest node and add to visited
        while((sortedNodes.size() != 0) && !current.getNodeID().equals(endNodeID)) {
            current = sortedNodes.poll();
            visited.add(current);

            HashMap<String,Node> neighbors = current.getNeighbors();

            for (Node i : neighbors.values()) { //iterate through its neighbors

                calcCosts(i, current); // primitive method

                checkConditions(i, current);

                if (!visited.contains(i) && !sortedNodes.contains(i) && !noStairCondition && !noElevCondition){
                    assignCostsToNode(i); // primitive method

                    sortedNodes.add(i);  // add i to priority queue
                    i.setParent(current);
                }
                else if (g < i.getGVal() && sortedNodes.contains(i)) {  // if lower, then update current i
                    i.setGVal(g);
                }
            }
        }
        if(sortedNodes.size() == 0) {
            return youCantGoThere();
        }

        addToParents(current, parentList);

        String finalPath = "";

        LinkedList<Double> parentCoords = new LinkedList<>();
        int length = parentList.size();
        double finalPathCoords[][] = new double[length][3];
        int i = 0;
        for (Node j: parentList) {
            finalPathCoords[i][0] = j.getXCoord();
            finalPathCoords[i][1] = j.getYCoord();
            finalPathCoords[i][2] = floorMap.get(j.getFloor());
            finalPath += j.getNodeID() + " ";
            i++;
        }

        this.startNode = startNode;
        this.endNode = endNode;

        return finalPathCoords;
    }

}
