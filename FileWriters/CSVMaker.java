package FileWriters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import Graph.Edge;
import Graph.Node;


/* This class is responsible for creating the csv files from
the updated databases and therefore overwriting the existing files
for nodes csv and edges csv
 */
public class CSVMaker {

    private static final String FILE_HEADER = "nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName";
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    /**
     * creates csv file using information from nodes in linked list
     * @param nodeList Linked List that contains all of the nodes in the databse
     * @param name String of what the new csv file should be made
     */
    public static void makeNodesFile(LinkedList<Node> nodeList, String name)
    {
        final String FILE_HEADER = "nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName";

        FileWriter fw = null;

        String fileName = System.getProperty("user.dir") + File.separator + name;

        try {
            fw = new FileWriter(fileName);

            //Write the CSV file header
            fw.append(FILE_HEADER.toString());

            //Add a new line separator after the header
            fw.append(NEW_LINE_SEPARATOR);

            //Write a new student object list to the CSV file
            for (Node n : nodeList) {
                fw.append(n.getNodeID());
                fw.append(COMMA_DELIMITER);
                fw.append(String.valueOf(n.getXCoord()));
                fw.append(COMMA_DELIMITER);
                fw.append(String.valueOf(n.getYCoord()));
                fw.append(COMMA_DELIMITER);
                fw.append(String.valueOf(n.getFloor()));
                fw.append(COMMA_DELIMITER);
                fw.append(n.getBuilding());
                fw.append(COMMA_DELIMITER);
                fw.append(n.getNodeType());
                fw.append(COMMA_DELIMITER);
                fw.append(n.getLongName());
                fw.append(COMMA_DELIMITER);
                fw.append(n.getShortName());
                fw.append(NEW_LINE_SEPARATOR);
            }

//            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
//            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            // if the file is created the fileWriter should be destroyed
            // file writer cannot be flushed if it is null
            try {
                assert fw != null;
                fw.flush();
                fw.close();
            } catch (IOException | NullPointerException e) {
//                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
    }

    /**
     * creates csv file using information from edges in linked list
     * @param edgeList Linked List that contains all of the edges in the databse
     * @param name String of what the new csv file should be made
     */
    public static void makeEdgesFile(LinkedList<Edge> edgeList, String name)
    {
        final String FILE_HEADER = "edgeID,startNode,endNode";

        FileWriter fw = null;

        String fileName = System.getProperty("user.dir") + File.separator + name;

        try {
            fw = new FileWriter(fileName);

            //Write the CSV file header
            fw.append(FILE_HEADER.toString());

            //Add a new line separator after the header
            fw.append(NEW_LINE_SEPARATOR);

            //Write a new student object list to the CSV file
            for (Edge ed : edgeList) {
                fw.append(ed.getEdgeID());
                fw.append(COMMA_DELIMITER);
                fw.append(ed.getStartNodeID());
                fw.append(COMMA_DELIMITER);
                fw.append(ed.getEndNodeID());
                fw.append(NEW_LINE_SEPARATOR);
            }

//            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
//            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            // if the file is created the fileWriter should be destroyed
            // file writer cannot be flushed if it is null
            try {
                assert fw != null;
                fw.flush();
                fw.close();
            } catch (IOException | NullPointerException e) {
//                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
    }
}







