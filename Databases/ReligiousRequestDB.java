package Databases;

//import imaging.Employee.Employee;
//import imaging.ImagingRequest;
//import imaging.RequestFacade.MedicalImagingInfo;
//import imaging.RequestFacade.MedicalImagingRequest;
//import imaging.RequestFacade.RequestMaker;
//import imaging.graph.Node;

import Employee.Employee;
import Graph.Node;
import RequestFacade.ReligiousRequest;
import RequestFacade.Request;
import UIControllers.MainScreenController;

import java.sql.*;
import java.util.Calendar;
import java.util.LinkedList;

import static Databases.DBConnection.getConnection;
//
//import static imaging.Databases.DBConnection.getConnection;
//import static imaging.RequestFacade.MedicalImagingRequest.stringToCalendar;

public class ReligiousRequestDB {

    public static void createReligiousRequestTable() {
        try {

            Statement statement = getConnection().createStatement();
            DatabaseMetaData databaseMetadata = getConnection().getMetaData();
            ResultSet rset = databaseMetadata.getTables(null, null, "RELIGIOUSREQUEST", null);

            if (rset.next()) {
                String nodeTable = "DELETE FROM RELIGIOUSREQUEST";
                PreparedStatement pstmt = getConnection().prepareStatement(nodeTable);
                pstmt.executeUpdate();

//                System.out.println("deleted existing stuff from rel req table");

            } else {

                String requestTable = "CREATE TABLE ReligiousRequest (reqID Varchar(10) Primary Key, serviceType Varchar(20), religion Varchar(20)," +
                        "religiousActivity Varchar(20), description Varchar(200), empID Varchar(10), patientFName Varchar(20)," +
                        "patientLName Varchar(20), nodeID Varchar(10), timeRequested VARCHAR(40), hasBeenCompleted char(1), cancelled char(1)," +
                        "CONSTRAINT completedCheckRel2 check (hasBeenCompleted in ('T', 'F')), CONSTRAINT cancelledCheckRel2 check (cancelled in ('T', 'F')))";

                PreparedStatement pstmt = getConnection().prepareStatement(requestTable);
                pstmt.executeUpdate();

//                System.out.println("Rel request has been made");
            }
            rset.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean availableReligiousReqID(String id) {
        Request tempRequest = null;

        int numAffected = -1;

        boolean isAvailable = true;
        LinkedList<ReligiousRequest> relRequests = getAllReligiousRequests();

        for(Request r: relRequests)
        {
            if(r.getReqID().equals(id))
                isAvailable = false;
        }
        return isAvailable;
    }

    public static void addReligiousRequest(ReligiousRequest r) throws SQLException {

        try {
            // creates a prepared statement to made the node
            PreparedStatement requestStatement = getConnection().prepareStatement("Insert into RELIGIOUSREQUEST values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            String completedStr = "F";
            if (r.getHasBeenCompleted())
                completedStr = "T";

            String cancelledStr = "F";
            if (r.getCancelled())
                cancelledStr = "T";


            /*
                  String requestTable = "CREATE TABLE ReligiousRequest (reqID Varchar(10) Primary Key, serviceType Varchar(20), religion Varchar(20)," +
                    "religiousActivity Varchar(20), description Varchar(200), empID Varchar(10), patientFName Varchar(20)," +
                    "patientLName Varchar(20), nodeID Varchar(10), timeRequested VARCHAR(40),
                     hasBeenCompleted BOOLEAN, cancelled BOOLEAN)";
             */

            requestStatement.setString(1, r.getReqID());
            requestStatement.setString(2, r.getServiceType());
            requestStatement.setString(3, r.getReligion());
            requestStatement.setString(4, r.getReligiousActivity());
            requestStatement.setString(5, r.getDescription());
            requestStatement.setString(6, r.getCompletedByWho().getEmpID());
            requestStatement.setString(7, r.getPatientFName());
            requestStatement.setString(8, r.getPatientLName());
            requestStatement.setString(9, r.getLocation().getNodeID());
            requestStatement.setString(10, r.getTimeRequested().getTime().toString());
            requestStatement.setString(11, completedStr);
            requestStatement.setString(12, cancelledStr);

//            System.out.println("REq IS is " + r.getReqID());

            int i = requestStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //deletes request from db with given rq id
    public static void deleteReligiousRequestDB(String id) throws SQLException {

        try {
            Statement reqDelete = getConnection().createStatement();
            String deleteStatement = String.format("DELETE FROM RELIGIOUSREQUEST WHERE reqID='%s'", id);
            reqDelete.executeUpdate(deleteStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ReligiousRequest getReligiousRequest(String id) {

        ReligiousRequest tempRequest = null;

        String tempReqID, tempServiceType, tempDescription, tempPatientFName, tempPatientLName, tempReligion, tempReligiousActivity;
        Node tempLocation;
        Calendar tempTimeRequested;
        boolean tempHasBeenCompleted, tempCancelled;
        Employee tempCompletedByWho;

        String tempLocationID, tempTimeRequestedString, tempHasBeenCompletedString, tempCancelledString, tempCompletedByWhoID;

        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM RELIGIOUSREQUEST WHERE REQID = " + "'" + id + "'";
            ResultSet rset = stmt.executeQuery(query);

            rset.next();

              /*
                  String requestTable = "CREATE TABLE ReligiousRequest (reqID Varchar(10) Primary Key, serviceType Varchar(20), religion Varchar(20)," +
                    "religiousActivity Varchar(20), description Varchar(200), empID Varchar(10), patientFName Varchar(20)," +
                    "patientLName Varchar(20), nodeID Varchar(10), timeRequested VARCHAR(40),
                     hasBeenCompleted BOOLEAN, cancelled BOOLEAN)";
             */

            tempReqID = rset.getString("reqID");
            tempServiceType = rset.getString("serviceType");
            tempDescription = rset.getString("description");
            tempLocationID = rset.getString("nodeID");
            tempTimeRequestedString = rset.getString("timeRequested");
            tempHasBeenCompletedString = rset.getString("hasBeenCompleted");
            tempCancelledString = rset.getString("cancelled");
            tempCompletedByWhoID = rset.getString("empID");
            tempPatientFName = rset.getString("patientFName");
            tempPatientLName = rset.getString("patientLName");
            tempReligion = rset.getString("religion");
            tempReligiousActivity = rset.getString("religiousActivity");



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

            tempTimeRequested = Request.stringToCalendar(tempTimeRequestedString);


            //ReligiousRequest(String reqID, String serviceType, String religion, String religiousActivity, String description, Employee requester, String patientFName, String patientLName, Node location, Calendar timeRequested, Calendar scheduledTimeStart, Calendar scheduledTimeEnd, Calendar actualTimeStart, Calendar actualTimeEnd, boolean hasBeenCompleted, boolean cancelled) {
            //

            // public ReligiousRequest(String reqID, String serviceType, String religion, String religiousActivity, String description, Employee requester, String patientFName, String patientLName, Node location, Calendar timeRequested, Calendar scheduledTimeStart, Calendar scheduledTimeEnd, Calendar actualTimeStart, Calendar actualTimeEnd, boolean hasBeenCompleted, boolean cancelled) {
            tempRequest = new ReligiousRequest(tempReqID, tempServiceType, tempReligion, tempReligiousActivity, tempDescription, MainScreenController.getCurEmployee(), tempPatientFName, tempPatientLName, tempLocation, tempTimeRequested, null, null, null, null, tempHasBeenCompleted, tempCancelled);

            tempRequest.setCompletedByWho(tempCompletedByWho);

            rset.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tempRequest;
    }

    public static LinkedList<ReligiousRequest> getAllReligiousRequests() {

        LinkedList<ReligiousRequest> requestLL = new LinkedList<ReligiousRequest>();


        String tempReqID, tempServiceType, tempDescription, tempPatientFName, tempPatientLName, tempReligion, tempReligiousActivity;
        Node tempLocation;
        Calendar tempTimeRequested;
        boolean tempHasBeenCompleted, tempCancelled;
        Employee tempCompletedByWho;

        String tempLocationID, tempTimeRequestedString, tempHasBeenCompletedString, tempCancelledString, tempCompletedByWhoID;


        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM RELIGIOUSREQUEST";
            //System.out.println(query);
            ResultSet rset = stmt.executeQuery(query);

            ReligiousRequest tempRequest;

            while (rset.next()) {

                tempReqID = rset.getString("reqID");
                tempServiceType = rset.getString("serviceType");
                tempDescription = rset.getString("description");
                tempLocationID = rset.getString("nodeID");
                tempTimeRequestedString = rset.getString("timeRequested");
                tempHasBeenCompletedString = rset.getString("hasBeenCompleted");
                tempCancelledString = rset.getString("cancelled");
                tempCompletedByWhoID = rset.getString("empID");
                tempPatientFName = rset.getString("patientFName");
                tempPatientLName = rset.getString("patientLName");
                tempReligion = rset.getString("religion");
                tempReligiousActivity = rset.getString("religiousActivity");

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

                tempTimeRequested = Request.stringToCalendar(tempTimeRequestedString);

                //ReligiousRequest(String reqID, String serviceType, String religion, String religiousActivity, String description, Employee requester, String patientFName, String patientLName, Node location, Calendar timeRequested, Calendar scheduledTimeStart, Calendar scheduledTimeEnd, Calendar actualTimeStart, Calendar actualTimeEnd, boolean hasBeenCompleted, boolean cancelled) {
                //

                // public ReligiousRequest(String reqID, String serviceType, String religion, String religiousActivity, String description, Employee requester, String patientFName, String patientLName, Node location, Calendar timeRequested, Calendar scheduledTimeStart, Calendar scheduledTimeEnd, Calendar actualTimeStart, Calendar actualTimeEnd, boolean hasBeenCompleted, boolean cancelled) {
                tempRequest = new ReligiousRequest(tempReqID, tempServiceType, tempReligion, tempReligiousActivity, tempDescription, MainScreenController.getCurEmployee(), tempPatientFName, tempPatientLName, tempLocation, tempTimeRequested, null, null, null, null, tempHasBeenCompleted, tempCancelled);

                tempRequest.setCompletedByWho(tempCompletedByWho);

                requestLL.add(tempRequest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return requestLL;
    }


    public static void setReligiousRequestCompletedDB(String reqID, boolean val)
    {
        ReligiousRequest newReq = getReligiousRequest(reqID);
        newReq.setHasBeenCompleted(val);

//        System.out.println("set new val");

        try {
//            System.out.println("in try block");
            deleteReligiousRequestDB(reqID);
//            System.out.println("Request deleted");
            addReligiousRequest(newReq);
//            System.out.println("updated request added");
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
//        System.out.println("finishd");
    }

}

