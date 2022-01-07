package PathFinding;

import Graph.Node;

import java.util.HashMap;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class StrategyRunner {
    // start, transported to each context object
    protected HashMap<String, Node> nodeHashMap;
    protected String startNodeID;
    protected String endNodeID;
    protected int conditions;

    // finnish
    private static double[][] coords = new double[20][3];
    //private static boolean updatedCoords = false;

    // these need to be in multiple classes at once, with the current set up
    private static Semaphore semaphoreStrategy = new Semaphore(1);
    private static Semaphore semaphoreRun = new Semaphore(0);
    private static CyclicBarrier strategyGate = new CyclicBarrier(4);

    public static Semaphore getSemaphoreStrategy() {
        return semaphoreStrategy;
    }

    public static void setSemaphoreStrategy(Semaphore semaphore) {
        StrategyRunner.semaphoreStrategy = semaphore;
    }

    public static Semaphore getSemaphoreRun() {
        return semaphoreRun;
    }

    public static void setSemaphoreRun(Semaphore semaphoreRun) {
        StrategyRunner.semaphoreRun = semaphoreRun;
    }

    public double[][] getCoords() {
        return coords;
    }

    public static void setCoords(double[][] coords) {
        StrategyRunner.coords = coords;
    }

    public static CyclicBarrier getStrategyGate() {
        return strategyGate;
    }

    public StrategyRunner() {};

    public double[][] run(HashMap<String, Node> nodeHashMap, String startNodeID, String endNodeID, int conditions) {
        // hard coded, the number of strategies used, please update
        int numberOfStrategies = 3;

        // thread preparation, contextAStar implements runable
        Context contextAStar = new Context(new AStar(), nodeHashMap, startNodeID, endNodeID, conditions);
        Thread threadAStar = new Thread(contextAStar);
        // thread preparation, contextBreadthFirst implements runable
        Context contextBreadthFirst = new Context(new BreadthFirst(), nodeHashMap, startNodeID, endNodeID, conditions);
        Thread threadBreadthFirst = new Thread(contextBreadthFirst);
        // thread preparation, contextDepthFirst implements runable
        Context contextDepthFirst = new Context(new DepthFirst(), nodeHashMap, startNodeID, endNodeID, conditions);
        Thread threadDepthFirst = new Thread(contextDepthFirst);
        // new strategies prep here

        try {
            // the +1 accounts for the default case
            System.out.println("choosing strategies");
            for (int strategy = 0; strategy < numberOfStrategies + 1; strategy++) {
                switch (strategy) {
                    case 0:
                        // thread execution
                        threadAStar.start();
                        break;
                    case 1:
                        // thread execution
                        threadBreadthFirst.start();
                        break;
                    case 2:
                        // thread execution
                        threadDepthFirst.start();
                        break;
                        // each new strategy is a new case, update numberOfStrategies
                    default:
                        System.out.println("distribution complete");
                        strategyGate.await();
                        System.out.println("started execution");
                        semaphoreRun.acquire();// holds main thread till at least one algorithm is completed

                        System.out.println("Main thread released");
                        break;
                }
            }

        } catch (InterruptedException | BrokenBarrierException e) {
            // thrown by strateyGate.await();
            e.printStackTrace();
        }

        // calculate time here or after coords are returned
        try{
            threadAStar.join();
            threadBreadthFirst.join();
            threadDepthFirst.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return coords;
    }

}
