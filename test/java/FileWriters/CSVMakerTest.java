package FileWriters;

import Graph.Edge;
import Graph.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class CSVMakerTest {

    private List<String> readFile(String filename)
    {
        List<String> records = new ArrayList<String>();
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null)
            {
                records.add(line);
            }
            reader.close();
            return records;
        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
            return null;
        }
    }


    String name = "CSV File is created Bruhh";
   private CSVMaker maker = new CSVMaker();
   LinkedList<Node> list = new LinkedList<>();
   LinkedList<Edge> list1 = new LinkedList<>();
   File file  = new File("CSVMakerTest.csv");
   FileWriter fw = new FileWriter("CSVMakerTest.csv");
   File file1 = new File ("testEdges1.csv");

    Node node1 = new Node("123",10,50,"1","2","hall","hall","hall");
    Node node2 = new Node("1234",0,0,"2","2","lab","Lab","lab");
    Edge edge1 = new Edge("AHALL00202_AHALL00302","AHALL00302","AHALL00202");
    Edge edge2 = new Edge("AHALL00502_ADEPT00102","ADEPT00102","AHALL00502");

    public CSVMakerTest() throws IOException {
    }

    @Before
     public void addNodes(){
         list.add(node1);
         list.add(node2);
         list1.add(edge1);
         list1.add(edge2);
     }

    @Test
    public void makeNodesFile() throws IOException {
        maker.makeNodesFile(list,"File created brother");




    }

    @Test
    public void makeEdgesFile() {
        maker.makeEdgesFile(list1,"Edges CVS created mate");


    }
}