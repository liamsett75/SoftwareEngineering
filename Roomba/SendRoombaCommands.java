package Roomba;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class SendRoombaCommands {
    public static double[][] coords;

    public void connect(String inputString) {
        try {
            String sentence = inputString;
            char[] myArr = sentence.toCharArray();
            for (int i = 0; i < sentence.length(); i++) {
                String modifiedSentence;
                Socket clientSocket = new Socket("130.215.209.183", 9999);
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                outToServer.writeBytes(Character.toString(myArr[i]));
                modifiedSentence = inFromServer.readLine();
                clientSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void connect(double[][] inputArray) {
//        try {
//            Socket clientSocket = new Socket("130.215.8.207", 9999);
//            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
//            outToServer.writeBytes("S"); // Current issue with all the code is here
//            clientSocket.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public String coordsToString(double[][] directions) {
        String commandString = "";
        for(int i = 0; i < directions.length; i++) {
            //if(directions[i][])
            if(directions[i][0] == 0 && directions[i][1] == 0) {

            }
            else {
                commandString += ",";
                if (directions[i][0] == 0) {
                    commandString += "S";
                }
                if (directions[i][0] == 1) {
                    commandString += "L";
                }
                if (directions[i][0] == -1) {
                    commandString += "R";
                }
                commandString += (int) Math.abs(Math.ceil(directions[i][1]));
                commandString += ",-";
            }
        }

        return commandString;
    }

    public double[][] makeInstructions(double[][] inputCoords) {
//        System.out.println("Coords");
        for(int q = 0; q < inputCoords.length; q++) {
//            System.out.print(inputCoords[q][0] + " ");
//            System.out.print(inputCoords[q][1] + " ");
//            System.out.println(inputCoords[q][2]);
        }
//        System.out.println("Length " + inputCoords.length);
        double[][] roombaDirections = new double[inputCoords.length * 2][2];
        double floorDiff, angle, currentAngle, dist, previousAngle, walkingDist,
                straightThreshold, slightLeftThreshold, slightRightThreshold, leftTurnThreshold,
                rightTurnThreshold, sharpLeftThreshold, sharpRightThreshold;
        int count;

        previousAngle = getAngle(inputCoords[0][0], inputCoords[0][1], inputCoords[1][0], inputCoords[1][1]);
        walkingDist = 0;

        straightThreshold = 30;
        slightLeftThreshold = 60;
        slightRightThreshold = -60;
        leftTurnThreshold = 105;
        rightTurnThreshold = -105;
        sharpLeftThreshold = 150;
        sharpRightThreshold = -150;
        count = 0;
//        System.out.println("COORDS HERE");
        for (int i = 0; i < inputCoords.length - 1; i++) {
//            System.out.println(inputCoords[i][0] + " " + inputCoords[i][1]);
            floorDiff = getFloorDiff(inputCoords[i][2], inputCoords[i + 1][2]);
            if (floorDiff > 0) {
                roombaDirections[0][0] = 3;
            } else if (floorDiff < 0) {
                roombaDirections[0][0] = 4;
            } else {
                dist = getDistance(inputCoords[i][0], inputCoords[i][1], inputCoords[i + 1][0], inputCoords[i + 1][1]);
                currentAngle = getAngle(inputCoords[i][0], inputCoords[i][1], inputCoords[i + 1][0], inputCoords[i + 1][1]);
                angle = previousAngle - currentAngle;
//                System.out.println("i: " + i + " dist: " + dist + " angle: " + angle);
                if (angle > 180) {
                    angle -= 360;
                }
                if (angle < -180) {
                    angle += 360;
                }
                if (Math.abs(angle) < straightThreshold) {
                    walkingDist += dist;
                } else if (straightThreshold <= angle) { // Slight Left
                    count++;
                    roombaDirections[count][0] = 0;
                    roombaDirections[count][1] = walkingDist;
                    count++;
                    roombaDirections[count][0] = 1;
                    roombaDirections[count][1] = angle;
                    walkingDist = dist;
                } else if (straightThreshold >= angle) { // Slight right
                    count++;
                    roombaDirections[count][0] = 0;
                    roombaDirections[count][1] = walkingDist;
                    count++;
                    roombaDirections[count][0] = -1;
                    roombaDirections[count][1] = angle;
                    walkingDist = dist;
                }
//                System.out.println("Walk: " + walkingDist);
                previousAngle = currentAngle;
            }
        }
//        System.out.println("Exit for");
        count++;
        System.out.println(count);
        roombaDirections[count][0] = 0;
        roombaDirections[count][1] = walkingDist;
//        System.out.println("ROOMBA DIRECTIONS");
        for (int j = 0; j < roombaDirections.length; j++) {
//            System.out.print(roombaDirections[j][0] + " ");
//            System.out.println(roombaDirections[j][1]);
        }
        return roombaDirections;
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
        SendRoombaCommands.coords = coords;
    }
}
