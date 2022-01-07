package PathFinding;

import _Initialize._Initialize;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class TextualDirectionsTest {
    @BeforeClass
    public static void setUp(){
        PathFindingTemplate.setStartNode(null);
        PathFindingTemplate.setEndNode(null);
        _Initialize.setUp();
    }

    @AfterClass
    public static void tearDown(){
        _Initialize.setUp();
    }

    // how t set language, mainScreenController.directionsController.textualLanguage == 1


    // Do I need to set a language

//    /**
//     * go up, nodes are on different floors
//     */
//    @Test
//    public void getDirection() {
//        double[][] coords = new double[2][3];
//        coords[0][0] = 1000;
//        coords[0][1] = 1000;
//        coords[0][2] = 1;
//        coords[1][0] = 1000;
//        coords[1][1] = 1000;
//        coords[1][2] = 2;
//        TextualDirections.setCoords(coords);
//        String directions = TextualDirections.getDirection();
//        assertTrue(directions.contains("go up"));
//    }
//
//    /**
//     * go down
//     */
//    @Test
//    public void getDiretions1(){
//        double[][] coords = new double[2][3];
//        coords[0][0] = 1000;
//        coords[0][1] = 1000;
//        coords[0][2] = 2;
//        coords[1][0] = 1000;
//        coords[1][1] = 1000;
//        coords[1][2] = 1;
//        TextualDirections.setCoords(coords);
//        String directions = TextualDirections.getDirection();
//        assertTrue(directions.contains("go down"));
//    }

//    /**
//     * slightly left
//     */
//    @Test
//    public void getDiretions2(){
//        double[][] coords = new double[3][3];
//        coords[0][0] = 2000;
//        coords[0][1] = 1000;
//        coords[0][2] = 1;
//        coords[1][0] = 2000;
//        coords[1][1] = 3000;
//        coords[1][2] = 1;
//        coords[2][0] = 1000;
//        coords[2][1] = 2000;
//        coords[2][2] = 1;
//        TextualDirections.setCoords(coords);
//        String directions = TextualDirections.getDirection();
//        assertTrue(directions.contains("slightly left"));
//    }

//    /**
//     * slightly right
//     */
//    @Test
//    public void getDiretions3(){
//
//    }

//    /**
//     * left
//     */
//    @Test
//    public void getDiretions4(){
////        double[][] coords = new double[3][3];
////        coords[0][0] = 2000;
////        coords[0][1] = 1000;
////        coords[0][2] = 1;
////        coords[1][0] = 2000;
////        coords[1][1] = 2000;
////        coords[1][2] = 1;
////        coords[2][0] = 1000;
////        coords[2][1] = 2000;
////        coords[2][2] = 1;
////        TextualDirections.setCoords(coords);
////        String directions = TextualDirections.getDirection();
////        assertTrue(directions.contains("turn left"));
//    }
//
//    /**
//     * right
//     */
//    @Test
//    public void getDiretions5(){
//        double[][] coords = new double[3][3];
//        coords[0][0] = 2000;
//        coords[0][1] = 2000;
//        coords[0][2] = 1;
//        coords[1][0] = 2000;
//        coords[1][1] = 1000;
//        coords[1][2] = 1;
//        coords[2][0] = 3000;
//        coords[2][1] = 1000;
//        coords[2][2] = 1;
//        TextualDirections.setCoords(coords);
//        String directions = TextualDirections.getDirection();
//        System.out.println(directions);
//        assertTrue(directions.contains("turn right"));
//    }
//
//    /**
//     * sharp left
//     */
//    @Test
//    public void getDiretions6(){
//        double[][] coords = new double[3][3];
//        coords[0][0] = 2000;
//        coords[0][1] = 2000;
//        coords[0][2] = 1;
//        coords[1][0] = 2000;
//        coords[1][1] = 1000;
//        coords[1][2] = 1;
//        coords[2][0] = 1000;
//        coords[2][1] = 2000;
//        coords[2][2] = 1;
//        TextualDirections.setCoords(coords);
//        String directions = TextualDirections.getDirection();
//        assertTrue(directions.contains("sharp left"));
//    }
//
//    /**
//     * sharp right
//     */
//    @Test
//    public void getDiretions7(){
//        double[][] coords = new double[3][3];
//        coords[0][0] = 2000;
//        coords[0][1] = 2000;
//        coords[0][2] = 1;
//        coords[1][0] = 1000;
//        coords[1][1] = 1000;
//        coords[1][2] = 1;
//        coords[2][0] = 3000;
//        coords[2][1] = 1000;
//        coords[2][2] = 1;
//        TextualDirections.setCoords(coords);
//        String directions = TextualDirections.getDirection();
//        System.out.println(directions);
//        assertTrue(directions.contains("sharp right"));
 //   }
//
//    /**
//     * combined
//     */
//    @Test
//    public void getDiretions8(){
//
//    }

}