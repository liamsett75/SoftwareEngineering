package Databases;

import Employee.Employee;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static Databases.DBConnection.getConnection;

public class ReligiousEmployeeDB {

    /**
     * Connects to the database and creates a table called Employee
     * Also it makes sure to drop the table if it already exists
     * before making it again
     */
    public static void createReligiousEmployeeTable() {


        try {
            //String empTable = "CREATE TABLE SanitationEmployee (empID VARCHAR(10) PRIMARY KEY, firstName VARCHAR(20), lastName varchar(20), specifications VARCHAR(50), isBusy CHAR(1), phoneNo VARCHAR(10), emailUserName VARCHAR(20), CONSTRAINT busyCheck check (isBusy in ('T', 'F')))";

            String bsTable = "CREATE TABLE ReligiousEmployee (empID VARCHAR(10) PRIMARY KEY, firstName VARCHAR(20), lastName varchar(20),  empType Varchar(10), specifications VARCHAR(50), isBusy CHAR(1), phoneNo VARCHAR(10), emailUserName VARCHAR(20), CONSTRAINT busyCheck2 check (isBusy in ('T', 'F')))";

            PreparedStatement pstmt = getConnection().prepareStatement(bsTable);
//            System.out.println("makes prepares stateent");
            pstmt.executeUpdate();
//            System.out.println("ReligiousEmployee  table created pt1");

        } catch (SQLException e) {
            try {
                String dropTable = "DROP TABLE ReligiousEmployee ";
                PreparedStatement pstmt = getConnection().prepareStatement(dropTable);
                pstmt.executeUpdate();

                String empTable = "CREATE TABLE ReligiousEmployee  (empID VARCHAR(10) PRIMARY KEY, firstName VARCHAR(20), lastName varchar(20),  empType Varchar(10), specifications VARCHAR(50), isBusy CHAR(1), phoneNo VARCHAR(10), emailUserName VARCHAR(20), CONSTRAINT busyCheck2 check (isBusy in ('T', 'F')))";
                PreparedStatement pstmt1 = getConnection().prepareStatement(empTable);
                pstmt1.executeUpdate();

//                System.out.println("ReligiousEmployee  table created pt 2");

            } catch (SQLException e1) {
//                System.out.println("ReligiousEmployee  table not created");
                e1.printStackTrace();
            }
        }
    }


    /**
     * adds emp object to the database
     * @param emp the Employee object to add to DB
     * @throws java.sql.SQLException problems sending node data to the database
     */
    public static void addReligiousEmployeeDB(Employee emp) throws SQLException{

        try {
            PreparedStatement empStatement = getConnection().prepareStatement("Insert into ReligiousEmployee  values (?, ?, ?, ?, ?, ?, ?, ?)");

            String busyTemp = "F";
            if(emp.getBusy())
                busyTemp = "T";

            empStatement.setString(1, emp.getEmpID());
            empStatement.setString(2, emp.getFirstName());
            empStatement.setString(3, emp.getLastName());
            empStatement.setString(4, emp.getEmpType());
            empStatement.setString(5, emp.getSpecifications());
            empStatement.setString(6, busyTemp);
            empStatement.setString(7, emp.getPhoneNo());
            empStatement.setString(8, emp.getEmailUserName());

            // inserts the node into the database
            empStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
