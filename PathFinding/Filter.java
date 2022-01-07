package PathFinding;

import Graph.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Filter {
    private static LinkedList<Node> nodes;
    private static String filterOne;
    private static String filterTwo;
    private static String floor;

    /**
     * Set the Linked List of nodes to use for the filtering
     *
     * @param nodes LinkedList of Nodes
     */
    public static void setLinkedNodes(LinkedList<Node> nodes) {
        Filter.nodes = nodes;
    }

    /**
     * Set what you want to filter (Single)
     *
     * @param filter Node Type
     */
    public static void setSingleFilter(String filter) {
        Filter.filterOne = filter;
    }

    /**
     * Set what you want to filter (Double)
     *
     * @param filterOne Node Type
     * @param filterTwo Node Type
     */
    public static void setDualFilter(String filterOne, String filterTwo) {
        Filter.filterOne = filterOne;
        Filter.filterTwo = filterTwo;
    }

    /**
     * Set the filter floor
     *
     * @param floor Floor
     */
    public static void setFloorFilter(String floor) {
        Filter.floor = floor;
    }

    /**
     * Get filtered floor
     *
     * @return List of Nodes
     */
    public static List<Node> getFloorFilter() {
        if(floor.equalsIgnoreCase("All")) {
            filterOne = "HALL";
            return Filter.nodes.stream().filter(Filter::isSingleExclusive).collect(Collectors.toList());
        }
        else {
            return Filter.nodes.stream().filter(Filter::isFloor).collect(Collectors.toList());
        }
    }

    /**
     * Get what was filtered (Single)
     *
     * @return List of Nodes
     */
    public static List<Node> getSingleFilter() {
        return Filter.nodes.stream().filter(Filter::isSingle).collect(Collectors.toList());
    }

    /**
     * Get what was filtered (Double)
     *
     * @return List of Nodes
     */
    public static List<Node> getDualFilter() {
        return Filter.nodes.stream().filter(Filter::isDouble).collect(Collectors.toList());
    }

    /**
     * Get what was exclusively filtered (Single)
     *
     * @return List of Nodes
     */
    public static List<Node> getSingleExclusiveFilter() {
        return Filter.nodes.stream().filter(Filter::isSingleExclusive).collect(Collectors.toList());
    }

    /**
     * Get what was exclusively filtered (Double)
     *
     * @return List of Nodes
     */
    public static List<Node> getDualExclusiveFilter() {
        return Filter.nodes.stream().filter(Filter::isDualExclusive).collect(Collectors.toList());
    }

    /**
     * Get whatever rooms are Bookable
     *
     * @return List of Nodes
     */
    public static List<Node> getBookableFilter() {
        return Filter.nodes.stream().filter(Filter::isBookable).collect(Collectors.toList());
    }

    /**
     * Get the Long Names of what was filtered
     *
     * @return List of Strings
     */
    public static List<String> toLongName(List<Node> nodes) {
        return nodes.stream().map(Node::getLongName).collect(Collectors.toList());
    }

    private static boolean isBookable(Node node) {
        return node.getNodeType().equalsIgnoreCase("CONF") || node.getNodeType().equalsIgnoreCase("LABS");
    }

    private static boolean isSingle(Node node) {
        return node.getNodeType().equalsIgnoreCase(filterOne);
    }

    private static boolean isDouble(Node node) {
        return node.getNodeType().equalsIgnoreCase(filterOne) || node.getNodeType().equalsIgnoreCase(filterTwo);
    }

    private static boolean isSingleExclusive(Node node) {
        return !node.getNodeType().equalsIgnoreCase(filterOne);
    }

    private static boolean isDualExclusive(Node node) {
        return !node.getNodeType().equalsIgnoreCase(filterOne) || !node.getNodeType().equalsIgnoreCase(filterTwo);
    }

    private static boolean isFloor(Node node) {
        return node.getFloor().equalsIgnoreCase(floor) && !node.getNodeType().equalsIgnoreCase("HALL");
    }
}

