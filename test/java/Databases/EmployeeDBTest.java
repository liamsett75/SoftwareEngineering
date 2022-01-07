package Databases;

import Employee.Employee;
import _Initialize.RandomObject;
import _Initialize._Initialize;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.LinkedList;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class EmployeeDBTest {
    @BeforeClass
     public static void setUp(){
        _Initialize.setClearEmployeeFlag(true);
        _Initialize.setUp();
     }

     @AfterClass
     public static void tearDown(){
        _Initialize.setUp();
     }

    /**
     * table should be created every test to test the class methods
     */
    @Before
    public void CreateEmployeeTable() { EmployeeDB.createEmployeeTable(); }

    /**
     * adds employees to the db and an manual list
     * @param toAdd
     * @param testList
     */
    private void addEmployeesLoop(int toAdd, LinkedList<Employee> testList){
        LinkedList<String> empIDs = new LinkedList<>(); // has to check employee ID's are not the same but still fit in the DB
        int employeesAdded = 0;
        do{
            Employee employee = (Employee) RandomObject.retrieveRandom("Employee");

            // if ID matches a previous one try again
            if(empIDs.contains(employee.getEmpID())){
                continue;
            }
            empIDs.add(employee.getEmpID()); // keeps track of the empIDs added

            // adds to the db
            try{
                EmployeeDB.addEmployeeDB(employee);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // adds to the manual list
            testList.add(employee);

            employeesAdded++;
        } while (employeesAdded < toAdd); // 4 is arbitrary
    }


    /**
     * tests getting all employees from the database
     * order is not assumed, each list is compared using containsAll
     */
    @Test
    public void getDBEmployees() {
        // given
        LinkedList<Employee> testList = new LinkedList<Employee>(); // manuel list, order does not matter
        addEmployeesLoop( 4, testList); // adds 4 employees with different IDs
        // when
        LinkedList<Employee> tempList = EmployeeDB.getDBEmployees();
        // then
        assertTrue(testList.containsAll(tempList));
    }

    /**
     * tests adding an employee
     * checks that the employee is there in the database and matches the employee sent
     */
    @Test
    public void addEmployeeDB() {
        // given
        LinkedList<Employee> testList = new LinkedList<>();
        addEmployeesLoop(1, testList);
        // when
        LinkedList<Employee> tempList = EmployeeDB.getDBEmployees();
        // then
        assertTrue(testList.containsAll(tempList));
    }

    /**
     * tests getting all the empIDs
     * order is not assumed, each list is compared using containsAll
     */
    @Test
    public void getAllEmpID() {
        // given
        LinkedList<Employee> testList = new LinkedList<>();
        addEmployeesLoop(4, testList);
        // adds all the IDs to a new list
        LinkedList<String> tempEmpIDList = new LinkedList<>();
        for(Employee employee: testList){
            tempEmpIDList.add(employee.getEmpID());
        }
        // when
        LinkedList<String> testEmpIDList = EmployeeDB.getAllEmpID();
        // then
        assertTrue(tempEmpIDList.containsAll(testEmpIDList));
    }

    /**
     * tests getting all the admin empIDs
     * order is not assumed, each list is compared using containsAll
     */
    @Test
    public void getAdminEmpID() {
        // given
        LinkedList<Employee> testList = new LinkedList<>();
        addEmployeesLoop(4, testList);
        // store the IDs if the employee is an admin
        LinkedList<String> tempEmpIDList = new LinkedList<>();
        for(Employee employee: testList){
            if(employee.getEmpType().equals("Admin")){
                tempEmpIDList.add(employee.getEmpID());
            }
        }
        // when
        LinkedList<String> testEmpIDList = EmployeeDB.getAdminEmpID();
        // then
        assertTrue(tempEmpIDList.containsAll(testEmpIDList));
    }

    /**
     * tests getting an employee's email from db
     */
    @Test
    public void getEmailDB(){
        final String DOMAIN = "@wpi.edu";
        // given
        LinkedList<Employee> testList = new LinkedList<>();
        addEmployeesLoop(3, testList);
        String empID = testList.get(1).getEmpID();
        String tempEmail = testList.get(1).getEmailUserName() + DOMAIN;
        // when
        String testEmail = EmployeeDB.getEmailDB(empID);
        // then
        assertEquals(tempEmail, testEmail);
    }

    /**
     * tests getting an employee's email from an invalid empID
     */
    @Test
    public void getEmailDB1(){

    }

    /**
     * tests getting employee from the db with empID
     */
    @Test
    public void getEmployeeDB(){

    }

    /**
     * tests getting employee from the db from an invalid empID
     */
    @Test
    public void getEmployeeDB1(){

    }

    /**
     * tests getting email preference from the db with empID
     */
    @Test
    public void getNotifications(){

    }

    /**
     * tests getting email preference from the db from an invalid empID
     */
    @Test
    public void getNotifications1(){

    }

    /**
     * checks the username and password for the user ID
     */
    @Test
    public void checkUserNamePassword(){
        // username: email usename
        // password: empID with no %
    }

    /**
     * checks the username and password for the user ID
     * wrong password
     */
    @Test
    public void checkUserNamePassword1(){
        // username: email usename
        // password: empID with no %
    }

    /**
     * checks the username and password for the user ID
     * wrong email
     */
    @Test
    public void checkUserNamePassword2(){
        // username: email usename
        // password: empID with no %
    }

    /**
     * checks the username and password for the user ID
     * both are wrong
     */
    @Test
    public void checkUserNamePassword3(){
        // username: email usename
        // password: empID with no %
    }
}