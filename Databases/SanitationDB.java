package Databases;

import Employee.Employee;
import Graph.Node;
import RequestFacade.SanitationRequest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import static Databases.DBConnection.getConnection;

public class SanitationDB {

    /*String reqID,
    String serviceType,
    String sanType,
    String description,
    Employee requester,
    Node location,
    Calendar timeRequested,
    Calendar scheduledTimeStart,
    Calendar scheduledTimeEnd,
    Calendar actualTimeStart,
    Calendar actualTimeEnd,
    boolean hasBeenCompleted,
    boolean cancelled)
        super(reqID, s*/

    public static void createSanitationReqTable() {

        //   SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        // tries to create the table, throws an exception if the table already exists
        try {
            String sanitationReqTable = "CREATE TABLE SanitationReq (reqID VARCHAR(10) PRIMARY KEY, serviceType VARCHAR(20), sanType VARCHAR(50), " +
                    "description VARCHAR(100), requesterID VARCHAR(10), locaionID VARCHAR(10), timeReq VARCHAR(19), scheduledTimeStart VARCHAR(19), scheduledTimeEnd VARCHAR(19), " +
                    " actualTimeStart VARCHAR(19), actualTimeEnd VARCHAR(19), hasBeenCompleted CHAR(1), cancelled CHAR(1) " +
                    " CONSTRAINT completedChecl check (hasBeenCompleted in ('T', 'F')),  CONSTRAINT cancelledChack check (cancelled in ('T', 'F')))";


            PreparedStatement pstmt1 = getConnection().prepareStatement(sanitationReqTable);
            pstmt1.executeUpdate();
        } catch (SQLException e) {
            try {
                String dropTable = "DROP TABLE SanitationReq";
                PreparedStatement pstmt = getConnection().prepareStatement(dropTable);
                pstmt.executeUpdate();

                String sanitationReqTable = "CREATE TABLE SanitationReq (reqID VARCHAR(10) PRIMARY KEY, serviceType VARCHAR(20), sanType VARCHAR(50), " +
                        "description VARCHAR(100), requesterID VARCHAR(10), locaionID VARCHAR(10), timeReq VARCHAR(19), scheduledTimeStart VARCHAR(19), scheduledTimeEnd VARCHAR(19), " +
                        " actualTimeStart VARCHAR(19), actualTimeEnd VARCHAR(19), hasBeenCompleted CHAR(1), cancelled CHAR(1) " +
                        " CONSTRAINT completedChecl check (hasBeenCompleted in ('T', 'F')),  CONSTRAINT cancelledChack check (cancelled in ('T', 'F')))";

                PreparedStatement pstmt1 = getConnection().prepareStatement(sanitationReqTable);
                pstmt1.executeUpdate();

            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void addSanitationReq(SanitationRequest r) throws java.sql.SQLException {
        String completedChar = "F";
        String cancelledChar = "F";

        try {
            // creates a prepared statement to made the node
            PreparedStatement sanitationStatement = getConnection().prepareStatement("Insert into Node values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            sanitationStatement.setString(1, r.getReqID());
            sanitationStatement.setString(2, r.getServiceType());
            sanitationStatement.setString(3, r.getSanType()); //need to add this method to sanitation request
            sanitationStatement.setString(4, r.getDescription()); //need to add this method to request abs
            sanitationStatement.setString(5, r.getRequester().getEmpID());
            sanitationStatement.setString(6, r.getLocation().getNodeID());
            sanitationStatement.setString(7, r.getTimeRequested().toString());
            sanitationStatement.setString(8, r.getScheduledTimeStart().toString());
            sanitationStatement.setString(9, r.getScheduledTimeEnd().toString());
            sanitationStatement.setString(10, r.getActualTimeStart().toString());
            sanitationStatement.setString(11, r.getActualTimeEnd().toString());

            if (r.getHasBeenCompleted())
                completedChar = "T";

            if (r.getCancelled())
                cancelledChar = "T";

            sanitationStatement.setString(12, completedChar);
            sanitationStatement.setString(13, cancelledChar);
            // inserts the node into the database
            int i = sanitationStatement.executeUpdate();
            // System.out.println("UPDATED DB:" + i);
            //System.out.println("node added");

        } catch (SQLException e) {
            e.printStackTrace();
            //throw new java.sql.SQLException("NodeID nodeID:%s already exists", r.getNodeID());
        }


    }

    public static SanitationRequest getSanitationReqDB(String id) {
        SanitationRequest tempSR = null;

        /*String reqID, String serviceType, String sanType, String description, Employee requester, Node
        location, Calendar timeRequested, Calendar scheduledTimeStart, Calendar scheduledTimeEnd, Calendar actualTimeStart,
        Calendar C, boolean hasBeenCompleted, boolean cancelled) {
        */

        String tempReqID;
        String tempServiceType;
        String tempSanType;
        String tempDescription;
        Employee tempRequester;
        Node tempLocation;
        Calendar tempTimeRequested;
        Calendar tempScheduledTimeStart;
        Calendar tempScheduledTimeEnd;
        Calendar tempActualTimeStart;
        Calendar tempActualTimeEnd;
        boolean tempHasBeenCompleted;
        boolean tempCancelled;

            try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM SanitationReq WHERE reqID = " + "'" + id + "'";
//            System.out.println(query);
            ResultSet rset = stmt.executeQuery(query);
            rset.next();

            /*"CREATE TABLE SanitationReq (reqID VARCHAR(10) PRIMARY KEY, serviceType VARCHAR(20),
            sanType VARCHAR(50), " +
             "description VARCHAR(100), requesterID VARCHAR(10), locaionID VARCHAR(10), timeReq VARCHAR(19),
             scheduledTimeStart VARCHAR(19), scheduledTimeEnd VARCHAR(19), " +
                        " actualTimeStart VARCHAR(19), actualTimeEnd VARCHAR(19),
                         hasBeenCompleted CHAR(1), cancelled CHAR(1) " +
                        " CONSTRAINT completedChecl check (hasBeenCompleted in ('T', 'F')),
                         CONSTRAINT cancelledChack check (cancelled in ('T', 'F')))";
*/

            tempReqID = rset.getString("reqID");
            tempServiceType = rset.getString("serviceType");
            tempSanType = rset.getString("sanType");
            tempDescription = rset.getString("description");



            //Complicated ones

            tempRequester = null;
            tempLocation = null;
            tempTimeRequested = null;
            tempScheduledTimeStart = null;
            tempScheduledTimeEnd = null;
            tempActualTimeStart = null;
            tempActualTimeEnd = null;

//            tempNodeID = rset.getString("nodeID");
//            tempXCoord = rset.getDouble("xCoord");
//            tempYCoord = rset.getDouble("yCoord");
//            tempFloor = rset.getString("floor");
//            tempBuilding = rset.getString("building");
//            tempNodeType = rset.getString("nodeType");
//            tempLongName = rset.getString("longName");
//            tempShortName = rset.getString("shortName");

            // tempSR = new SanitationRequest(tempNodeID, tempXCoord, tempYCoord, tempFloor, tempBuilding, tempNodeType, tempLongName, tempShortName);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tempSR;
    }
}
