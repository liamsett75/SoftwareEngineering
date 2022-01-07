package Databases;

//import imaging.Employee.Employee;
//import imaging.ImagingRequest;
//import imaging.RequestFacade.MedicalImagingInfo;
//import imaging.RequestFacade.MedicalImagingRequest;
//import imaging.RequestFacade.RequestMaker;
//import imaging.graph.Node;

import Employee.Employee;
import Graph.Node;
import RequestFacade.Request;
import RequestFacade.SanitationRequest;
import UIControllers.SanitationController;

import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

import static Databases.DBConnection.getConnection;
//
//import static imaging.Databases.DBConnection.getConnection;
//import static imaging.RequestFacade.MedicalImagingRequest.stringToCalendar;

public class SanitationRequestDB {

    private static HashMap<String, Node> bhHashMap = new HashMap<String, Node>();

    public HashMap<String, Node> getBhHashMap() {
        return bhHashMap;
    }

    public void setBhHashMap(HashMap<String, Node> bhHashMap) {
        this.bhHashMap = bhHashMap;
    }

    public static void createSanitationRequestTable() {
        try {

            Statement statement = getConnection().createStatement();
            DatabaseMetaData databaseMetadata = getConnection().getMetaData();
            ResultSet rset = databaseMetadata.getTables(null, null, "SANITATIONREQUEST", null);

            if (rset.next()) {
                String nodeTable = "DELETE FROM SANITATIONREQUEST";
                PreparedStatement pstmt = getConnection().prepareStatement(nodeTable);
                pstmt.executeUpdate();

            } else {


                String nodeTable = "CREATE TABLE SANITATIONREQUEST (reqID varchar(10) Primary Key, serviceType varchar(20), nodeID varchar(15), timeReqString varchar(50), " +
                        "hasBeenCompleted char(1), cancelled char(1), empCompleted varchar(15), description varchar(200)," +
                        "sanType VARCHAR(50), biohazard char(1),"+
                        "CONSTRAINT completedCheckSanReuests check (hasBeenCompleted in ('T', 'F')), CONSTRAINT cancelledCheckSanRequests check (cancelled in ('T', 'F')), " +
                        "CONSTRAINT biohazardCheckSanRequests check (biohazard in ('T', 'F')))";

                PreparedStatement pstmt = getConnection().prepareStatement(nodeTable);
                pstmt.executeUpdate();

//                System.out.println("San request has been made");
            }
            rset.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean availableSanitationReqID(String id) {
        Request tempRequest = null;

        int numAffected = -1;

        boolean isAvailable = true;
        LinkedList<SanitationRequest> sanRequests = getAllSanitationRequestsHashMap();

        for(Request r: sanRequests)
        {
            if(r.getReqID().equals(id))
                isAvailable = false;
        }
        return isAvailable;
    }

    public static void addSanitationRequest(SanitationRequest r) throws SQLException {

        try {
            // creates a prepared statement to made the node
            PreparedStatement requestStatement = getConnection().prepareStatement("Insert into SANITATIONREQUEST values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"); //13

            String completedStr = "F";
            if (r.getHasBeenCompleted())
                completedStr = "T";

            String cancelledStr = "F";
            if (r.getCancelled())
                cancelledStr = "T";

            String biohazardStr = "F";
            if(r.getIsBiohazard())
                biohazardStr = "T";

            requestStatement.setString(1, r.getReqID());
            requestStatement.setString(2, r.getServiceType());
            requestStatement.setString(3, r.getLocation().getNodeID());
            requestStatement.setString(4, r.getTimeRequested().getTime().toString());
            requestStatement.setString(5, completedStr);
            requestStatement.setString(6, cancelledStr);
            requestStatement.setString(7, r.getCompletedByWho().getEmpID());
            requestStatement.setString(8, r.getDescription());
            requestStatement.setString(9, r.getSanType());
            requestStatement.setString(10, biohazardStr);

            int i = requestStatement.executeUpdate();

        } catch (SQLException e) {
        }
    }

    //deletes request from db with given rq id
    public static void deleteSanitationRequestDB(String id) throws SQLException {

        try {
            Statement reqDelete = getConnection().createStatement();
            String deleteStatement = String.format("DELETE FROM SANITATIONREQUEST WHERE reqID='%s'", id);
            reqDelete.executeUpdate(deleteStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static SanitationRequest getSanitationRequest(String id) {

        SanitationRequest tempRequest = null;

        String tempReqID, tempServiceType, tempDescription, tempSanType;
        Node tempLocation;
        Calendar tempTimeRequested;
        boolean tempHasBeenCompleted, tempCancelled, tempBiohazard;
        Employee tempCompletedByWho;

        String tempLocationID, tempTimeRequestedString, tempHasBeenCompletedString, tempCancelledString, tempCompletedByWhoID, tempBiohazardString;

        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM SANITATIONREQUEST WHERE REQID = " + "'" + id + "'";
            ResultSet rset = stmt.executeQuery(query);

            rset.next();

                  /*
            CREATE TABLE SANITATIONREQUEST (reqID varchar(10) Primary Key, serviceType varchar(20), nodeID varchar(15), timeReqString varchar(50), " +
                        "hasBeenCompleted char(1), cancelled char(1), empCompleted varchar(15), description varchar(200)," +
                        "sanType VARCHAR(50), biohazard char(1)"+
                        "CONSTRAINT completedCheckSanReuests check (hasBeenCompleted in ('T', 'F')), CONSTRAINT cancelledCheckSanRequests check (cancelled in ('T', 'F')), " +
                        "CONSTRAINT biohazardCheckSanRequests check (biohazard in ('T', 'F')))";
             */

            tempReqID = rset.getString("reqID");
            tempServiceType = rset.getString("serviceType");
            tempDescription = rset.getString("description");
            tempLocationID = rset.getString("nodeID");
            tempTimeRequestedString = rset.getString("timeReqString");
            tempHasBeenCompletedString = rset.getString("hasBeenCompleted");
            tempCancelledString = rset.getString("cancelled");
            tempCompletedByWhoID = rset.getString("empCompleted");
            tempBiohazardString = rset.getString("biohazard");
            tempSanType = rset.getString("sanType");

            //now you need to query for those above things/translate them to make the object

            tempLocation = NodeDB.getNodeDB(tempLocationID);
            tempCompletedByWho = EmployeeDB.getEmployeeDB(tempCompletedByWhoID);

            if (tempHasBeenCompletedString.equals("T"))
                tempHasBeenCompleted = true;
            else
                tempHasBeenCompleted = false;

            if (tempCancelledString.equals("T"))
                tempCancelled = true;
            else
                tempCancelled = false;

            if (tempBiohazardString.equals("T"))
                tempBiohazard = true;
            else
                tempBiohazard = false;

            tempTimeRequested = Request.stringToCalendar(tempTimeRequestedString);


            tempRequest = new SanitationRequest(tempReqID, tempServiceType, tempSanType, tempBiohazard, tempDescription, null, tempLocation, tempTimeRequested, null, null, null, null, tempHasBeenCompleted, tempCancelled);

            tempRequest.setCompletedByWho(tempCompletedByWho);

            rset.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tempRequest;
    }

    public static LinkedList<SanitationRequest> getAllSanitationRequests() {

        LinkedList<SanitationRequest> requestLL = new LinkedList<SanitationRequest>();

        String tempReqID, tempServiceType, tempDescription, tempSanType;
        Node tempLocation;
        Calendar tempTimeRequested;
        boolean tempHasBeenCompleted, tempCancelled, tempBiohazard;
        Employee tempCompletedByWho;

        String tempLocationID, tempTimeRequestedString, tempHasBeenCompletedString, tempCancelledString, tempBiohazardString, tempCompletedByWhoID;

        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM SANITATIONREQUEST";
            //System.out.println(query);
            ResultSet rset = stmt.executeQuery(query);

            SanitationRequest tempRequest;

            while (rset.next()) {


                tempReqID = rset.getString("reqID");
                tempServiceType = rset.getString("serviceType");
                tempDescription = rset.getString("description");
                tempLocationID = rset.getString("nodeID");
                tempTimeRequestedString = rset.getString("timeReqString");
                tempHasBeenCompletedString = rset.getString("hasBeenCompleted");
                tempCancelledString = rset.getString("cancelled");
                tempCompletedByWhoID = rset.getString("empCompleted");
                tempBiohazardString = rset.getString("biohazard");
                tempSanType = rset.getString("sanType");

                //now you need to query for those above things/translate them to make the object

//                System.out.println("Temp location ID is " + tempLocationID);

                tempLocation = NodeDB.getNodeDB(tempLocationID);

//                System.out.println("node id of tempLocation is " + tempLocation.getNodeID());

                tempCompletedByWho = EmployeeDB.getEmployeeDB(tempCompletedByWhoID);

                if (tempHasBeenCompletedString.equals("T"))
                    tempHasBeenCompleted = true;
                else
                    tempHasBeenCompleted = false;

                if (tempCancelledString.equals("T"))
                    tempCancelled = true;
                else
                    tempCancelled = false;

                if (tempBiohazardString.equals("T"))
                    tempBiohazard = true;
                else
                    tempBiohazard = false;

                tempTimeRequested = Request.stringToCalendar(tempTimeRequestedString);

                Employee e0 = EmployeeDB.getEmployeeDB("staff");
                tempRequest = new SanitationRequest(tempReqID, tempServiceType, tempSanType, tempBiohazard, tempDescription, e0, tempLocation, tempTimeRequested, null, null, null, null, tempHasBeenCompleted, tempCancelled);

                tempRequest.setCompletedByWho(tempCompletedByWho);

                requestLL.add(tempRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requestLL;
    }


    public static void setSanitationRequestCompletedDB(String reqID, boolean val)
    {
        SanitationRequest newReq = getSanitationRequestHashMap(reqID);
        newReq.setHasBeenCompleted(val);

//        System.out.println("set new val");

        try {
//            System.out.println("in try block");
            deleteSanitationRequestDB(reqID);
//            System.out.println("Request deleted");
            addSanitationRequest(newReq);
//            System.out.println("updated request added");
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
//        System.out.println("finishd");
    }

    public static LinkedList<SanitationRequest> getAllSanitationRequestsHashMap() {

        bhHashMap = SanitationController.getBhNodeHM();

        LinkedList<SanitationRequest> requestLL = new LinkedList<SanitationRequest>();

        String tempReqID, tempServiceType, tempDescription, tempSanType;
        Node tempLocation;
        Calendar tempTimeRequested;
        boolean tempHasBeenCompleted, tempCancelled, tempBiohazard;
        Employee tempCompletedByWho;

        String tempLocationID, tempTimeRequestedString, tempHasBeenCompletedString, tempCancelledString, tempBiohazardString, tempCompletedByWhoID;

        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM SANITATIONREQUEST";
            //System.out.println(query);
            ResultSet rset = stmt.executeQuery(query);

            SanitationRequest tempRequest;

            while (rset.next()) {


                tempReqID = rset.getString("reqID");
                tempServiceType = rset.getString("serviceType");
                tempDescription = rset.getString("description");
                tempTimeRequestedString = rset.getString("timeReqString");
                tempHasBeenCompletedString = rset.getString("hasBeenCompleted");
                tempCancelledString = rset.getString("cancelled");
                tempCompletedByWhoID = rset.getString("empCompleted");
                tempBiohazardString = rset.getString("biohazard");
                tempSanType = rset.getString("sanType");

                //now you need to query for those above things/translate them to make the object

                tempCompletedByWho = EmployeeDB.getEmployeeDB(tempCompletedByWhoID);


                if (tempHasBeenCompletedString.equals("T"))
                    tempHasBeenCompleted = true;
                else
                    tempHasBeenCompleted = false;

                if (tempCancelledString.equals("T"))
                    tempCancelled = true;
                else
                    tempCancelled = false;

                if (tempBiohazardString.equals("T"))
                    tempBiohazard = true;
                else
                    tempBiohazard = false;

                //-------------------------------

                //System.out.println("Temp location ID is " + tempLocationID);

                //this is the line that causes issues bc the node is deleted
                //tempLocation = NodeDB.getNodeDB(tempLocationID);

                //System.out.println("node id of tempLocation is " + tempLocation.getNodeID());

                if(tempBiohazard && !tempHasBeenCompleted) //if req is a biohazard and hos not been completed
                {
                    tempLocation = bhHashMap.get(tempReqID);
                    tempLocationID = tempLocation.getNodeID();

                }else //if req is not a biohazard or has been completed
                {
                    tempLocationID = rset.getString("nodeID");
                    tempLocation = NodeDB.getNodeDB(tempLocationID);
                }


                //------------------------------

                tempTimeRequested = Request.stringToCalendar(tempTimeRequestedString);

                Employee e0 = EmployeeDB.getEmployeeDB("staff");
                tempRequest = new SanitationRequest(tempReqID, tempServiceType, tempSanType, tempBiohazard, tempDescription, e0, tempLocation, tempTimeRequested, null, null, null, null, tempHasBeenCompleted, tempCancelled);

                tempRequest.setCompletedByWho(tempCompletedByWho);

                requestLL.add(tempRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requestLL;
    }

    public static SanitationRequest getSanitationRequestHashMap(String id) {

        bhHashMap = SanitationController.getBhNodeHM();

        SanitationRequest tempRequest = null;

        String tempReqID, tempServiceType, tempDescription, tempSanType;
        Node tempLocation;
        Calendar tempTimeRequested;
        boolean tempHasBeenCompleted, tempCancelled, tempBiohazard;
        Employee tempCompletedByWho;

        String tempLocationID, tempTimeRequestedString, tempHasBeenCompletedString, tempCancelledString, tempCompletedByWhoID, tempBiohazardString;

        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM SANITATIONREQUEST WHERE REQID = " + "'" + id + "'";
            ResultSet rset = stmt.executeQuery(query);

            rset.next();

                  /*
            CREATE TABLE SANITATIONREQUEST (reqID varchar(10) Primary Key, serviceType varchar(20), nodeID varchar(15), timeReqString varchar(50), " +
                        "hasBeenCompleted char(1), cancelled char(1), empCompleted varchar(15), description varchar(200)," +
                        "sanType VARCHAR(50), biohazard char(1)"+
                        "CONSTRAINT completedCheckSanReuests check (hasBeenCompleted in ('T', 'F')), CONSTRAINT cancelledCheckSanRequests check (cancelled in ('T', 'F')), " +
                        "CONSTRAINT biohazardCheckSanRequests check (biohazard in ('T', 'F')))";
             */

            tempReqID = rset.getString("reqID");
            tempServiceType = rset.getString("serviceType");
            tempDescription = rset.getString("description");
            //tempLocationID = rset.getString("nodeID");
            tempTimeRequestedString = rset.getString("timeReqString");
            tempHasBeenCompletedString = rset.getString("hasBeenCompleted");
            tempCancelledString = rset.getString("cancelled");
            tempCompletedByWhoID = rset.getString("empCompleted");
            tempBiohazardString = rset.getString("biohazard");
            tempSanType = rset.getString("sanType");

            //now you need to query for those above things/translate them to make the object

            tempCompletedByWho = EmployeeDB.getEmployeeDB(tempCompletedByWhoID);

            if (tempHasBeenCompletedString.equals("T"))
                tempHasBeenCompleted = true;
            else
                tempHasBeenCompleted = false;

            if (tempCancelledString.equals("T"))
                tempCancelled = true;
            else
                tempCancelled = false;

            if (tempBiohazardString.equals("T"))
                tempBiohazard = true;
            else
                tempBiohazard = false;

            tempTimeRequested = Request.stringToCalendar(tempTimeRequestedString);

            //tempLocation = NodeDB.getNodeDB(tempLocationID);

            if(tempBiohazard) //if req is a biohazard
            {
//                System.out.println("This is a biohazard node");
                //System.out.println("Temp request is is " + );
                tempLocation = bhHashMap.get(tempReqID);
                tempLocationID = tempLocation.getNodeID();
//                System.out.println("Location node to be set to biohazard is " + tempLocationID);

                //HashMap<String, Node> tempHashMap = SanitationController.getBhNodeHM();
                //tempHashMap.remove(tempReqID);
                //SanitationController.setBhNodeHM(tempHashMap);
//                System.out.println("--------------------Now bh node is removed from hash map");

            }else //if req is not a biohazard
            {
//                System.out.println("This is NOT a biohazard node");
                tempLocationID = rset.getString("nodeID");
                tempLocation = NodeDB.getNodeDB(tempLocationID);
//                System.out.println("Location node to be set to not biohazard is " + tempLocationID);
            }


            tempRequest = new SanitationRequest(tempReqID, tempServiceType, tempSanType, tempBiohazard, tempDescription, null, tempLocation, tempTimeRequested, null, null, null, null, tempHasBeenCompleted, tempCancelled);

            tempRequest.setCompletedByWho(tempCompletedByWho);

            rset.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tempRequest;
    }

}

