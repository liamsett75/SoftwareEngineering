package Databases;

import Employee.Employee;

import java.sql.*;
import java.util.LinkedList;

import static Databases.DBConnection.getConnection;

/**
 * This class is used for creating and querying the Node table in the database
 */
public class EmployeeDB
{
    /**
     * Connects to the database and creates a tbale called Employee
     * Also it makes sure to drop the table if it already exists
     * before making it again
     */
    public static void createEmployeeTable() {

        try {
            String empTable = "CREATE TABLE Employee (empID VARCHAR(10) PRIMARY KEY , firstName VARCHAR(20), lastName varchar(20), empType Varchar(10), specifications VARCHAR(50), isBusy CHAR(1), phoneNo VARCHAR(10), emailUserName VARCHAR(20), emailPref CHAR(1), CONSTRAINT busyCheckEMP check (isBusy in ('T', 'F')), CONSTRAINT prefCheckEMP check (emailPref in ('Y', 'N')))";
            PreparedStatement pstmt = getConnection().prepareStatement(empTable);
            pstmt.executeUpdate();

//            System.out.println("created employee table pt 1");

        } catch (SQLException e) {
            try {
                String dropTable = "DROP TABLE Employee";
                PreparedStatement pstmt = getConnection().prepareStatement(dropTable);
                pstmt.executeUpdate();

                String empTable = "CREATE TABLE Employee (empID VARCHAR(10) PRIMARY KEY , firstName VARCHAR(20), lastName varchar(20), empType Varchar(10), specifications VARCHAR(50), isBusy CHAR(1), phoneNo VARCHAR(10), emailUserName VARCHAR(20), emailPref CHAR(1), CONSTRAINT busyCheckEMP check (isBusy in ('T', 'F')), CONSTRAINT prefCheckEMP check (emailPref in ('Y', 'N')))";
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
    public static void addEmployeeDB(Employee emp) throws SQLException{
        //Connection conn = getConnection();
        try {
            PreparedStatement empStatement = getConnection().prepareStatement("Insert into Employee values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
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


    public static void deleteEmployeeDB(Employee emp) {
        try {
            // deletes the nodeID n from the database
            Statement empDelete = getConnection().createStatement();
            String deleteStatement = String.format("DELETE FROM Employee WHERE empID='%s'", emp.getEmpID());
            empDelete.executeUpdate(deleteStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(emp.getEmpType().equals("IMG"))
        {
            ImagingEmployeeDB.deleteImagingEmployeeDB(emp);
        }

    }


    public static void editEmployeeDB(Employee oldEmp, Employee newEmp) {
        try {
            deleteEmployeeDB(oldEmp);
            addEmployeeDB(newEmp);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * returns a list of all of the empID of all employees
     * in the database
     * @return LinkedList of Strings of empID
     */
    public static LinkedList<String> getAllEmpID() {
        LinkedList<String> employeeIDs = new LinkedList<String>();
        String tempEmpID;

        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT empID FROM Employee";
            ResultSet rset = stmt.executeQuery(query);

            while (rset.next()) {
                tempEmpID = rset.getString("empID");
                employeeIDs.add(tempEmpID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeIDs;
    }

    /**
     * returns a list of the empIDs for only the
     * employees who are specifically of type admin
     * @return LinkedList<String> of empIDs
     */
    public static LinkedList<String> getAdminEmpID() {
        LinkedList<String> adminIDs = new LinkedList<String>();
        String tempEmpID;

        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT empID FROM Employee WHERE EMPTYPE = 'Admin'";
            ResultSet rset = stmt.executeQuery(query);

            while (rset.next()) {
                tempEmpID = rset.getString("empID");
                adminIDs.add(tempEmpID);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminIDs;
    }

    /**
     *Creates a Linked List of emplyees from the database
     * @return LinkedList<Employee> that contains employee objects
     * of all employees represented in the database
     */
    public static LinkedList<Employee> getDBEmployees() {
        LinkedList<Employee> tempList = new LinkedList<Employee>();
        Employee tempEmp;

        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM EMPLOYEE";
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
                tempIsBusy = false; // should need to be reset every loop
                tempPref = false;
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


    /**
     * Queries database for username of employee with given
     * empID and creates email from that
     * @param empID String of empID
     * @return String of email
     */
    public static String getEmailDB(String empID) {
        final String DOMAIN = "@wpi.edu";

        String totalEmail = "";
        String tempUserName = "";

        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM EMPLOYEE WHERE EMPID = '" + empID + "'";

            System.out.println(query);
            ResultSet rset = stmt.executeQuery(query);
            rset.next();
            tempUserName = rset.getString("emailUserName");
            totalEmail = tempUserName + DOMAIN;
            rset.close(); // should be closed
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return totalEmail;
    }

    /**
     * Queries database for employee with given id and
     * creates an employee object from the fields
     *
     * @param id String representing empID field
     * @return Employee object with given emID
     */
    public static Employee getEmployeeDB(String id) {
        Employee tempEmp = null;

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

        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM EMPLOYEE WHERE EMPID = " + "'" + id + "'";
            System.out.println(query);
            ResultSet rset = stmt.executeQuery(query);
            rset.next();

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
            rset.close(); // should be closed
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tempEmp;
    }

    public static String getNotifications(String empID)
    {
        String curEmailPref = "";

        try {

            Statement stmt = getConnection().createStatement();
//            System.out.println("EMP ID BEING USED FOR PROBLEM QUERY IS " + empID);
           String query = "SELECT EMAILPREF FROM EMPLOYEE WHERE EMPID = '" + empID + "'";
            ResultSet rset = stmt.executeQuery(query);
            rset.next();
            curEmailPref = rset.getString(1);

            rset.close(); // should be closed
        } catch (SQLException e) {
            System.out.println("Caught some sort of error in getNotiications");
//            e.printStackTrace();
        }
            return curEmailPref;
    }


    /**
     *
     * @param uName
     * @param password
     * @return valid
     */
    public static boolean checkUserNamePassword(String uName, String password) {

//        System.out.println("password inputted is " + password);
        boolean valid = false;
        String tempPassword = "";
        String updatedPassword = password.replace("%","");

//        System.out.println("updates password is " + updatedPassword);


        try {
            Statement stmt = getConnection().createStatement();
            String query = "SELECT * FROM EMPLOYEE WHERE EMAILUSERNAME = " + "'" + uName + "'";
            ResultSet rset = stmt.executeQuery(query);
            rset.next();

            tempPassword = rset.getString("empID");
            rset.close(); // should be closed
            if(updatedPassword.equals(tempPassword))
                valid = true;

        } catch (SQLException e) {
            //empty bc if username is not found it should also return false
            return false;
        }
//        System.out.println("check uname password is " + valid);
        return valid;
    }
}
