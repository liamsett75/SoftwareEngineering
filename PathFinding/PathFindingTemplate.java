package PathFinding;

import Graph.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public abstract class PathFindingTemplate {
    // set up
    protected static Node startNode = null;
    protected static Node endNode = null;
    protected static HashMap<String, Integer> floorMap = new HashMap<>();
    protected static boolean stairAllowed;
    protected static boolean elevAllowed;
    // can change
    protected boolean noStairCondition;
    protected boolean noElevCondition;
    //protected LinkedList<Node> parentList = new LinkedList<>();
    static Node getStartNode() {
        return startNode;
    }
    static Node getEndNode() {
        return endNode;
    }

    public static void setStartNode(Node startNode) {
        PathFindingTemplate.startNode = startNode;
    }

    public static void setEndNode(Node endNode) {
        PathFindingTemplate.endNode = endNode;
    }

    /**
     * builds floor map for the class
     */
    public static final void buildFloorMap(){
        floorMap.put("4", 4);
        floorMap.put("3", 3);
        floorMap.put("2", 2);
        floorMap.put("1", 1);
        floorMap.put("G", 0);
        floorMap.put("L1", -1);
        floorMap.put("L2", -2);
    }

    /**
     * configures the conditions
     */
    public static final void configureConditions(int conditions){
        stairAllowed = !(conditions % 2 == 0); // Returns False if we can't use stairs
        elevAllowed = !(conditions % 3 == 0); // Returns False if we can't use elevs
    }

    /**
     * you can't go there
     */
    public static final double[][] youCantGoThere(){
        System.out.println("You can't go there");
        double finalPathCoords[][] = new double[2][3];
        finalPathCoords[0][0] = startNode.getXCoord();
        finalPathCoords[0][1] = startNode.getYCoord();
        finalPathCoords[0][2] = floorMap.get(startNode.getFloor());
        finalPathCoords[1][0] = startNode.getXCoord();
        finalPathCoords[1][1] = startNode.getYCoord();
        finalPathCoords[1][2] = floorMap.get(startNode.getFloor());
        return finalPathCoords;
    }

    /**
     * checks no stair condition
     * checks no elevator condition
     */
    public final void checkConditions(Node i, Node current){
        noStairCondition = (i.getNodeID().contains("STAI") && current.getNodeID().contains("STAI")) && !stairAllowed;
        noElevCondition = (i.getNodeID().contains("ELEV") && current.getNodeID().contains("ELEV")) && !elevAllowed;
    }

    /**
     *
     */
    public final void addToParents(Node current, ArrayList<Node> parentList){
        parentList.add(current);
        while (current.getParent() != null) {  // get parent of final node
            parentList.add(current.getParent());
            current = current.getParent();
        }

        Collections.reverse(parentList);
    }

    public static HashMap<String, Integer> getFloorMap() {
        return floorMap;
    }
}
