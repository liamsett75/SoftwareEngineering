package PathFinding;

import java.util.ArrayList;

import static Email.Email.sendDirections;

public class TextualDirections {
    private static double[][] coords;

    // directions change coords
    // recorded when the directions change
    private static ArrayList<double[]> directionsChangeCoords;

    public TextualDirections() {}

    /**
     * Creates a String of directions for a single path for any algorithm
     *
     * @return A String of directions
     */
    public static String getDirection() {
        directionsChangeCoords = new ArrayList<>();
        String finalDirections = "\n";
        double floorDiff, angle, currentAngle, dist, previousAngle, walkingDist,
                straightThreshold, slightLeftThreshold, slightRightThreshold, leftTurnThreshold,
                rightTurnThreshold, sharpLeftThreshold, sharpRightThreshold;

        previousAngle = getAngle(coords[0][0], coords[0][1], coords[1][0], coords[1][1]);
        walkingDist = 0;

        straightThreshold = 30;
        slightLeftThreshold = 60;
        slightRightThreshold = -60;
        leftTurnThreshold = 105;
        rightTurnThreshold = -105;
        sharpLeftThreshold = 150;
        sharpRightThreshold = -150;

        for (int i = 0; i < coords.length - 1; i++) {
            floorDiff = getFloorDiff(coords[i][2], coords[i + 1][2]);
            if (floorDiff > 0) {
                directionsChangeCoords.add(coords[i+1]);
                finalDirections += "Next go up " + (int) Math.abs(floorDiff) + " floor. \n";
            }
            else if (floorDiff < 0) {
                directionsChangeCoords.add(coords[i+1]);
                finalDirections += "Next go down " + (int) Math.abs(floorDiff) + " floor. \n";
            }
            else {
                dist = getDistance(coords[i][0], coords[i][1], coords[i + 1][0], coords[i + 1][1]);
                currentAngle = getAngle(coords[i][0], coords[i][1], coords[i + 1][0], coords[i + 1][1]);
                angle = previousAngle - currentAngle;
                if(angle > 180) {
                    angle -= 360;
                }
                if(angle < -180) {
                    angle += 360;
                }
//                System.out.println("Angle: " + angle + " Previous Angle: " + previousAngle + " Current Angle: " + currentAngle);

                if (Math.abs(angle) < straightThreshold) {
                    walkingDist += dist;
                }
                else if (straightThreshold <= angle && angle < slightLeftThreshold) { // Slight Left
                    directionsChangeCoords.add(coords[i]);
                    finalDirections += "Walk straight " + (int) Math.ceil(walkingDist) + " feet. \nNext turn";
                    walkingDist = dist;
                    finalDirections += " slightly left and ";
                }
                else if (straightThreshold >= angle && angle > slightRightThreshold) { // Slight right
                    directionsChangeCoords.add(coords[i]);
                    finalDirections += "Walk straight " + (int) Math.ceil(walkingDist) + " feet. \nNext turn";
                    walkingDist = dist;
                    finalDirections += " slightly right and ";
                }
                else if (slightLeftThreshold <= angle && angle < leftTurnThreshold) { // Left
                    directionsChangeCoords.add(coords[i]);
                    finalDirections += "Walk straight " + (int) Math.ceil(walkingDist) + " feet. \nNext turn";
                    walkingDist = dist;
                    finalDirections += " left and ";
                }
                else if (slightRightThreshold >= angle && angle > rightTurnThreshold) { // Right
                    directionsChangeCoords.add(coords[i]);
                    finalDirections += "Walk straight " + (int) Math.ceil(walkingDist) + " feet. \nNext turn";
                    walkingDist = dist;
                    finalDirections += " right and ";
                }
                else if (leftTurnThreshold <= angle && angle < sharpLeftThreshold) { // Sharp Left
                    directionsChangeCoords.add(coords[i]);
                    finalDirections += "Walk straight " + (int) Math.ceil(walkingDist) + " feet. \nNext turn";
                    walkingDist = dist;
                    finalDirections += " sharp left and ";
                }
                else if (rightTurnThreshold >= angle && angle > sharpRightThreshold) { // Sharp Right
                    directionsChangeCoords.add(coords[i]);
                    finalDirections += "Walk straight " + (int) Math.ceil(walkingDist) + " feet. \nNext turn";
                    walkingDist = dist;
                    finalDirections += " sharp left and ";
                }
                previousAngle = currentAngle;
            }
        }
        directionsChangeCoords.add(coords[coords.length-1]); // the end
        finalDirections += "Walk straight " + (int) Math.ceil(walkingDist) + " feet.";

        try {
            sendDirections(finalDirections, PathFindingTemplate.getStartNode().getShortName(), PathFindingTemplate.getEndNode().getShortName());
        } catch (NullPointerException e) {
            System.out.println("Node not set properly");
        }
        System.out.println("Final directions are: " + finalDirections);

        String translate = Translate.translateDirections(finalDirections);

        return translate; // Switch to translate in the future
    }

    /**
     * Gets the difference between the two floors
     *
     * @param startFloor The starting floor
     * @param endFloor   The ending floor
     * @return A double that represents the difference between the end and start floors
     */
    private static double getFloorDiff(double startFloor, double endFloor) {
        return endFloor - startFloor;
    }

    /**
     * Calculates the distance between the start and end coordinates
     *
     * @param startX The starting X position
     * @param startY The starting Y position
     * @param endX   The ending X position
     * @param endY   The ending Y position
     * @return A double which represents the distance between the start and end coordinates
     */
    private static double getDistance(double startX, double startY, double endX, double endY) {
        return (Math.sqrt(Math.pow((endX - startX), 2) + Math.pow(endY - startY, 2))) * 3 / 10;
    }

    /**
     * Calculates the angle in degrees
     *
     * @param startX The starting X position
     * @param startY The starting Y position
     * @param endX   The ending X position
     * @param endY   The ending Y position
     * @return A double which represents the angle in degrees
     */
    private static double getAngle(double startX, double startY, double endX, double endY) {
        double angleRad = Math.atan2((endY - startY), (endX - startX));
        double angleDeg = angleRad * (180 / Math.PI);
        return angleDeg;
    }

    // setter
    public static void setCoords(double[][] coords) {
        TextualDirections.coords = coords;
    }

    public static double[][] getCoords() {
        return coords;
    }

    public static ArrayList<double[]> getDirectionsChangeCoords() {
        return directionsChangeCoords;
    }
}
