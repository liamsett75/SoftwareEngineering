package Databases;

import Employee.Employee;

import java.sql.*;
import java.util.LinkedList;

import static Databases.DBConnection.getConnection;

public class SanitationEmployeeDB {

    /**
     * Connects to the database and creates a tbale called Employee
     * Also it makes sure to drop the table if it already exists
     * before making it again
     */
    public static void createSanitationEmployeeTable() {

        try {

            Statement statement = getConnection().createStatement();
            DatabaseMetaData databaseMetadata = getConnection().getMetaData();
            ResultSet rset = databaseMetadata.getTables(null, null, "SANITATIONEMPLOYEE", null);

            if (rset.next()) {
                String nodeTable = "DELETE FROM SANITATIONEMPLOYEE";
                PreparedStatement pstmt = getConnection().prepareStatement(nodeTable);
                pstmt.executeUpdate();

            } else {


                String sanEmpTable = "CREATE TABLE SanitationEmployee (empID VARCHAR(10) PRIMARY KEY, firstName VARCHAR(20), lastName varchar(20),  empType Varchar(20), specifications VARCHAR(50), isBusy CHAR(1), phoneNo VARCHAR(10), emailUserName VARCHAR(20), emailPref CHAR(1), CONSTRAINT busyCheckSan check (isBusy in ('T', 'F')), CONSTRAINT emailCheckSan check (emailPref in ('Y','N')))";
                PreparedStatement pstmt = getConnection().prepareStatement(sanEmpTable);
                pstmt.executeUpdate();
//                System.out.println("NEW SAN EMP HAS BEEN MADE");
//                System.out.println("San request has been made");
            }
            rset.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

//
//        try {
//            //String empTable = "CREATE TABLE SanitationEmployee (empID VARCHAR(10) PRIMARY KEY, firstName VARCHAR(20), lastName varchar(20), specifications VARCHAR(50), isBusy CHAR(1), phoneNo VARCHAR(10), emailUserName VARCHAR(20), CONSTRAINT busyCheck check (isBusy in ('T', 'F')))";
//
//            String bsTable = "CREATE TABLE SanitationEmployee (empID VARCHAR(10) PRIMARY KEY, firstName VARCHAR(20), lastName varchar(20),  empType Varchar(20), specifications VARCHAR(50), isBusy CHAR(1), phoneNo VARCHAR(10), emailUserName VARCHAR(20), emailPef CHAR(1), CONSTRAINT busyCheckSan check (isBusy in ('T', 'F')), CONSTRAINT emailCheckSan check (emailPref in ('T','F')))";
//
//            PreparedStatement pstmt = getConnection().prepareStatement(bsTable);
////            System.out.println("makes prepares stateent");
//            pstmt.executeUpdate();
////            System.out.println("sanitation table created pt1");
//
//        } catch (SQLException e) {
//            try {
//                String dropTable = "DROP TABLE SanitationEmployee";
//                PreparedStatement pstmt = getConnection().prepareStatement(dropTable);
//                pstmt.executeUpdate();
//
//                String empTable = "CREATE TABLE SanitationEmployee (empID VARCHAR(10) PRIMARY KEY, firstName VARCHAR(20), lastName varchar(20),  empType Varchar(20), specifications VARCHAR(50), isBusy CHAR(1), phoneNo VARCHAR(10), emailUserName VARCHAR(20), emailPef CHAR(1), CONSTRAINT busyCheckSan check (isBusy in ('T', 'F')), CONSTRAINT emailCheckSan check (emailPref in ('T','F')))";
//                PreparedStatement pstmt1 = getConnection().prepareStatement(empTable);
//                pstmt1.executeUpdate();
//
////                System.out.println("sanitation table created pt 2");
//
//            } catch (SQLException e1) {
////                System.out.println("sanitation table not created");
//                e1.printStackTrace();
//            }
//        }
    }


    /**
     * adds emp object to the database
     * @param emp the Employee object to add to DB
     * @throws java.sql.SQLException problems sending node data to the database
     */
    public static void addSanitationEmployeeDB(Employee emp) throws SQLException{

        try {
            PreparedStatement empStatement = getConnection().prepareStatement("Insert into SanitationEmployee values (?, ?, ?, ?, ?, ?, ?, ?, ?)");

            String busyTemp = "F";
            if(emp.getBusy())
                busyTemp = "T";

            String emailTemp = "N";
            if(emp.getEmailPref())
                emailTemp = "Y";

            empStatement.setString(1, emp.getEmpID());
            empStatement.setString(2, emp.getFirstName());
            empStatement.setString(3, emp.getLastName());
            empStatement.setString(4, emp.getEmpType());
            empStatement.setString(5, emp.getSpecifications());
            empStatement.setString(6, busyTemp);
            empStatement.setString(7, emp.getPhoneNo());
            empStatement.setString(8, emp.getEmailUserName());
            empStatement.setString(9, emailTemp);

            // inserts the node into the database
            empStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static LinkedList<Employee> getDBSanitationEmployees() {
        LinkedList<Employee> tempList = new LinkedList<Employee>();
        Employee tempEmp;

        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM SANITATIONEMPLOYEE";
            ResultSet rset = stmt.executeQuery(query);

            String tempFirstName;
            String tempLastName;
            String tempEmpID;
            String tempEmpType; //for now can be interpreter or sanitation or admin
            boolean tempIsBusy = false;
            String tempIsBusyString;
            String tempPhoneNo;
            String tempEmailUserName;
            String tempSpecifications;
            String tempPref;
            boolean tempPrefBool = true;

            while (rset.next()) {
                tempFirstName = rset.getString("firstName");
                tempLastName = rset.getString("lastName");
                tempEmpID = rset.getString("empID");
                tempEmpType = rset.getString("empType");
                tempIsBusyString = rset.getString("isBusy");
                if (tempIsBusyString.equals("T"))
                    tempIsBusy = true;

                tempPhoneNo = rset.getString("phoneNo");
                tempEmailUserName = rset.getString("emailUserName");
                tempSpecifications = rset.getString("specifications");
                tempPref = rset.getString("emailPref");

                if (tempPref==null) {
//                    System.out.println("temp pref is null");
                } else if (tempPref.equalsIgnoreCase("n")){
                    tempPrefBool = false;
                } else {
                    tempPrefBool = true;
                }

                tempEmp = new Employee(tempEmpID, tempFirstName, tempLastName, tempEmpType, tempSpecifications, tempIsBusy, tempPhoneNo, tempEmailUserName, tempPrefBool);
                tempList.add(tempEmp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tempList;
    }

}
