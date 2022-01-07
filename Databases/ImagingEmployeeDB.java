package Databases;

import Employee.Employee;

import java.sql.*;
import java.util.LinkedList;

import static Databases.DBConnection.getConnection;

public class ImagingEmployeeDB
{
    /**
     * Connects to the database and creates a tbale called Employee
     * Also it makes sure to drop the table if it already exists
     * before making it again
     */
    public static void createImagingEmployeeTable() {

        try {
            String empTable = "CREATE TABLE ImagingEmployee (empID VARCHAR(10) PRIMARY KEY , firstName VARCHAR(20), lastName varchar(20), empType Varchar(10), specifications VARCHAR(50), isBusy CHAR(1), phoneNo VARCHAR(10), emailUserName VARCHAR(20), emailPref CHAR(1), CONSTRAINT busyCheckIMG check (isBusy in ('T', 'F')), CONSTRAINT prefCheck3 check (emailPref in ('Y', 'N')))";
            PreparedStatement pstmt = getConnection().prepareStatement(empTable);
            pstmt.executeUpdate();

//            System.out.println("created employee table pt 1");

        } catch (SQLException e) {

            try {
                String dropTable = "DROP TABLE ImagingEmployee";
                PreparedStatement pstmt = getConnection().prepareStatement(dropTable);
                pstmt.executeUpdate();

                String empTable = "CREATE TABLE ImagingEmployee (empID VARCHAR(10) PRIMARY KEY , firstName VARCHAR(20), lastName varchar(20), empType Varchar(10), specifications VARCHAR(50), isBusy CHAR(1), phoneNo VARCHAR(10), emailUserName VARCHAR(20), emailPref CHAR(1), CONSTRAINT busyCheckIMG check (isBusy in ('T', 'F')), CONSTRAINT prefCheck3 check (emailPref in ('Y', 'N')))";
                PreparedStatement pstmt1 = getConnection().prepareStatement(empTable);
                pstmt1.executeUpdate();

//                System.out.println("created employee table pt2");

            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * adds emp object to the database
     * @param emp the Employee object to add to DB
     * @throws java.sql.SQLException problems sending node data to the database
     */
    public static void addImagingEmployeeDB(Employee emp) throws SQLException{
        Connection conn = getConnection();
        try {
            PreparedStatement empStatement = getConnection().prepareStatement("Insert into ImagingEmployee values (?, ?, ?, ?, ?, ?, ?, ?, ?)");

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

            String prefTemp = "N";
            if(emp.getEmailPref())
                prefTemp = "Y";

            empStatement.setString(9, prefTemp);

            // inserts the node into the database
            empStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void deleteImagingEmployeeDB(Employee emp) {
        try {
            // deletes the nodeID n from the database
            Statement empDelete = getConnection().createStatement();
            String deleteStatement = String.format("DELETE FROM ImagingEmployee WHERE empID='%s'", emp.getEmpID());
            empDelete.executeUpdate(deleteStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *Creates a Linked List of emplyees from the database
     * @return LinkedList<Employee> that contains employee objects
     * of all employees represented in the database
     */
    public static LinkedList<Employee> getDBImagingEmployees() {
        LinkedList<Employee> tempList = new LinkedList<Employee>();
        Employee tempEmp;

        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM IMAGINGEMPLOYEE";
            ResultSet rset = stmt.executeQuery(query);

            String tempFirstName;
            String tempLastName;
            String tempEmpID;
            String tempEmpType; //for now can be interpreter or sanitation or admin
            boolean tempIsBusy = false;
            String tempIsBusyString;
            String tempPhoneNo;
            String tempEmailUserName;
            boolean tempPref = false;
            String tempPrefString;
            String tempSpecifications;

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
                tempPrefString = rset.getString("emailPref");
                if(tempPrefString.equals("Y"))
                    tempPref = true;
                tempSpecifications = rset.getString("specifications");

                tempEmp = new Employee(tempEmpID, tempFirstName, tempLastName, tempEmpType, tempSpecifications, tempIsBusy, tempPhoneNo, tempEmailUserName, tempPref);
                tempList.add(tempEmp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tempList;
    }



}
