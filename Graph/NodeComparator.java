package Graph;
import java.util.Comparator;

/**
 * Comparator method needed for sorting Nodes into the priority queue in A*
 */

public class NodeComparator implements Comparator<Node> {
    @Override
    public int compare(Node a, Node b) {
        //ordering in ascending order to get lowest first
        if (a.getFVal() > b.getFVal()){
            return 1;
        }
        else if (a.getFVal() < b.getFVal()){
            return -1;
        }
        return 0;
    }
}

