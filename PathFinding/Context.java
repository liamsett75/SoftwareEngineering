package PathFinding;

import Graph.Node;

import java.util.HashMap;
import java.util.concurrent.BrokenBarrierException;

public class Context implements Runnable {

    // informational
    private Node startNode;
    private Node endNode;
    private Strategy strategy;
    // preparation
    protected HashMap<String, Node> nodeHashMap;
    protected String startNodeID;
    protected String endNodeID;
    protected int conditions;

    private double[][] tempCoords;

    private double totalTime;
    private double totalDist;

    public Context (Strategy strategy, HashMap<String, Node> nodeHashMap, String startNodeID, String endNodeID, int conditions) {
        this.strategy = strategy;
        this.nodeHashMap = nodeHashMap;
        this.startNodeID = startNodeID;
        this.endNodeID = endNodeID;
        this.conditions = conditions;
    }

    // threading function
    @Override
    public void run() {

        try{
            StrategyRunner.getStrategyGate().await(); // wait here till the main thread calls it
            // runs the class strategy
            tempCoords = strategy.getPath(nodeHashMap, startNodeID, endNodeID,conditions);
            System.out.println("got coords from " + this.toString());

            StrategyRunner.getSemaphoreStrategy().acquire();
            if(StrategyRunner.getSemaphoreRun().availablePermits()==0){
                System.out.println(this.toString() + "got to critical region");
                StrategyRunner.getSemaphoreRun().release(2); // released the main semaphore, allows the main thread in Strategy Runner to continue
                // sets the coords
                StrategyRunner.setCoords(tempCoords);
                // calculates total time based on coordinates
                totalTime = calcTotalTime(tempCoords);
            }
            StrategyRunner.getSemaphoreStrategy().release();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    // only runs one strategy, will complete later
    public double[][] runOne(){
        System.out.println("Getting a path");
        tempCoords = strategy.getPath(nodeHashMap, startNodeID, endNodeID, conditions);
        // calculates total time based on coordinates
        totalTime = calcTotalTime(tempCoords);
        totalDist = calcTotalDist(tempCoords);
        return tempCoords;
    }

    public static double calcTotalTime(double[][] nodeCoords) {
        return calcTotalDist(nodeCoords) * 2.875 / 10 * .2179;
    }

    public static double calcTotalDist(double[][] nodeCoords){
        double totalDist = 0;
        double dist = 0;
        for (int i = 0; i < nodeCoords.length - 1; i++) {
            dist = Math.sqrt(Math.pow((nodeCoords[i][0] - nodeCoords[i+1][0]), 2) +
                    Math.pow((nodeCoords[i][1] - nodeCoords[i+1][1]), 2));
            totalDist += dist;
        }
        return totalDist;
    }

    @Override
    public String toString() {
        return String.valueOf(strategy.toString());
    }

    public double getTotalTime() {
        return totalTime;
    }
    public double getTotalDist() {return totalDist;}

    public Strategy getStrategy() {
        return strategy;
    }


}
