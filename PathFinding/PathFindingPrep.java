package PathFinding;

import Graph.Node;

import java.util.HashMap;

public class PathFindingPrep {

  private static HashMap<Integer, String> floorMap = new HashMap<Integer, String>();
  private static void buildReverseFloorMap(){
        floorMap.put(4, "4");
        floorMap.put(3, "3");
        floorMap.put(2, "2");
        floorMap.put(1, "1");
        floorMap.put(0, "G");
        floorMap.put(-1, "L1");
        floorMap.put(-2, "L2");
    }

    private static Node getClosestNode(HashMap<String, Node> nodeHashMap, double xCoord, double yCoord, String currentFloor, int endNodeTypeKey){
        String compareString = "Default Case";
        String restCompareString1 = "Default Case";
        String restCompareString2 = "Default Case";
        String exitCompareString = "Default Case";
        switch (endNodeTypeKey) {
            case 1:
                exitCompareString = "EXIT";
                break;
            case 2:
                restCompareString1 = "Bath";
                restCompareString2 = "Rest";
                break;
            case 3:
                compareString = "Cafe";
                break;
            case 4:
                compareString = "Information";
                break;
            case 5:
                compareString = "Gift Shop";
                break;
            case 6:
                compareString = "Parking";
                break;
            case 7:
                compareString = "Valet";
                break;
            case 8: // indicates hot keys is not being used
                compareString = "KEY";
                break;
            default:
                compareString = "error";
        }

        // make start node
        Node nodeNeighbor = new Node(null, 0, 0, null, null, null, null, null, null, 0, 0, 0, 0);
        double shortestDist = Double.MAX_VALUE;
        double checkDist;
        boolean onSameFloor;
        // find end node
        boolean condition1, condition2, condition3, condition4;

        for (String id : nodeHashMap.keySet()) {
            checkDist = Math.sqrt(Math.pow((nodeHashMap.get(id).getXCoord() - xCoord), 2) + Math.pow((nodeHashMap.get(id).getYCoord() - yCoord), 2));
            // make start node
            onSameFloor = nodeHashMap.get(id).getFloor().equals(currentFloor);
            // find end node
            condition1 = nodeHashMap.get(id).getLongName().contains(compareString);
            condition2 = nodeHashMap.get(id).getLongName().contains(restCompareString1);
            condition3 = nodeHashMap.get(id).getLongName().contains(restCompareString2);
            condition4 = nodeHashMap.get(id).getNodeType().contains(exitCompareString);

            // checks hotkey condition
            if (compareString.equals("KEY")){
                if (checkDist < shortestDist && onSameFloor) {
                    nodeNeighbor = nodeHashMap.get(id);
                    shortestDist = checkDist;
                }
            }
            // find end node
            else {
                if ((checkDist < shortestDist) && (condition1 || condition2 || condition3 || condition4)) {
                    nodeNeighbor = nodeHashMap.get(id);
                    shortestDist = checkDist;
                }
            }
        }

        return nodeNeighbor;
    }


    public static Node makeStartNode(HashMap<String, Node> nodeHashMap, double startX, double startY, int startFloor) {
        buildReverseFloorMap();
        String currentFloor = floorMap.get(startFloor);

        // key type 8 is for non-hot keys
        Node startNodeNeighbor = getClosestNode(nodeHashMap, startX, startY, currentFloor, 8);

        Node startNode = new Node("currentLocation", startX, startY, currentFloor, "Building", "kiosk", "startingKioskNode", "kioskNode", null, 0, 0, 0, 0);
        HashMap<String, Node> startNodeNeighbors = new HashMap<>();
        startNodeNeighbors.put(startNodeNeighbor.getNodeID(), startNodeNeighbor);
        startNode.setNeighbors(startNodeNeighbors);
        return startNode;
    }

    /**
     * finds the closest end node with the given hot key type
     * @param nodeHashMap
     * @param startX
     * @param startY
     * @param startFloor
     * @param endNodeTypeKey
     * @return
     */
    public static Node findEndNode(HashMap<String, Node> nodeHashMap, double startX, double startY, int startFloor, int endNodeTypeKey) {
        // Hotkeys: longName "Bath or Rest"
        //          nodeType "EXIT"
        //          longName "Cafe"
        //          longName "Parking"
        //          longName "Gift Shop"
        //          longName "Valet"
        buildReverseFloorMap();
        String currentFloor = floorMap.get(startFloor);
        // making sure the non-hot key is not checked
        // adds one so it is an error instead
        if(endNodeTypeKey == 8){
            endNodeTypeKey++;
        }
        return getClosestNode(nodeHashMap, startX, startY, currentFloor, endNodeTypeKey);
    }

    /**
     * finds the closest end node with click
     * @param nodeHashMap
     * @param endX
     * @param endY
     * @param endFloor
     * @return
     */
    public static Node clickEndNode(HashMap<String, Node> nodeHashMap, double endX, double endY, int endFloor) {
        System.out.println("You clicked an end node, " + endX + " " + endY + " " + endFloor);
        buildReverseFloorMap();
        String destFloor = floorMap.get(endFloor);

        // key type 8 is for non-hot keys
        return getClosestNode(nodeHashMap, endX, endY, destFloor, 8);
    }
}