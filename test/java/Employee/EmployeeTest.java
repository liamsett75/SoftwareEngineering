package Employee;

import _Initialize.RandomObject;
import _Initialize._Initialize;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Calendar;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EmployeeTest {

    private static Employee employee1;
    private static LinkedList<Employee> allEmployees = new LinkedList<Employee>(); // cleared after every test

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
     *
     */
    @Test
    public void isAvailable(){

    }

    /**
     * there are no employees
     */
    @Test
    public void getAllAvailableEmps() {
        assertEquals(0, Employee.getAllAvailableEmps(allEmployees, Calendar.getInstance(), Calendar.getInstance()).size());
    }


    /**
     * there is one busy employee
     */
    @Test
    public void getAllAvailableEmps1() {
        // to tese these properly you must ad a request to the employee

//        // given
//        Employee employee = (Employee) RandomObject.retrieveRandom("Employee");
//        employee.setBusy(true);
//        allEmployees.add(employee);
//
//        assertEquals(0, Employee.getAllAvailableEmps(allEmployees, Calendar.getInstance(), Calendar.getInstance()).size());
//        allEmployees.clear();
    }

    /**
     * there is one not busy employee
     */
    @Test
    public void getAllAvailableEmps2() {
//        // given
//        Employee employee = (Employee) RandomObject.retrieveRandom("Employee");
//        employee.setBusy(false);
//        allEmployees.add(employee);
//
//        assertEquals(1, Employee.getAllAvailableEmps(allEmployees, Calendar.getInstance(), Calendar.getInstance()).size());
//        allEmployees.clear();
    }

    /**
     * there is one busy and one not busy employee
     */
    @Test
    public void getAllAvailableEmps3() {
//        // given
//        Employee employee = (Employee) RandomObject.retrieveRandom("Employee");
//        employee.setBusy(false);
//        allEmployees.add(employee);
//
//        employee = (Employee) RandomObject.retrieveRandom("Employee");
//        employee.setBusy(true);
//        allEmployees.add(employee);
//
//        assertEquals(1, Employee.getAllAvailableEmps(allEmployees, Calendar.getInstance(), Calendar.getInstance()).size());
//        allEmployees.clear();
    }

    /**
     * tests the employee is the same one retrieved from the ID
     */
    @Test
    public void getEmpFromID() {
        // given
        Employee employee = (Employee) RandomObject.retrieveRandom("Employee");
        allEmployees.add(employee);
        // when
        String empID = employee.getEmpID();
        // then
        assertEquals(Employee.getEmpFromID(allEmployees, empID), employee);
        allEmployees.clear();
    }

    /**
     * tests the employee is the same one retrieved from the ID
     * the list has multiple employees so the main empID is changed
     * to an invalid ID
     */
    @Test
    public void getEmpFromID1() {
        // given
        Employee employee = (Employee) RandomObject.retrieveRandom("Employee");
        allEmployees.add(employee);
        employee = (Employee) RandomObject.retrieveRandom("Employee");
        allEmployees.add(employee);
        employee = (Employee) RandomObject.retrieveRandom("Employee");
        allEmployees.add(employee);
        // when
        String empID = employee.getEmpID().concat("objective");
        employee.setEmpID(empID);
        // then
        assertEquals(Employee.getEmpFromID(allEmployees, empID), employee);
        allEmployees.clear();
    }

    /**
     * tests a fake ID that is not in the system, one employee is in the system
     */
    @Test
    public void getEmpFromID2() {
        // given
        Employee employee = (Employee) RandomObject.retrieveRandom("Employee");
        allEmployees.add(employee);
        // when
        String empID = employee.getEmpID().concat("ID");
        // then
        assertNull(Employee.getEmpFromID(allEmployees, empID));
        allEmployees.clear();
    }

    /**
     * tests the employee is the same one retrieved from the first and last name
     */
    @Test
    public void getEmpFromName() {
        // given
        Employee employee = (Employee) RandomObject.retrieveRandom("Employee");
        allEmployees.add(employee);
        // when
        String firstName = employee.getFirstName();
        String lastName = employee.getLastName();
        // then
        assertEquals(Employee.getEmpFromName(allEmployees,firstName,lastName), employee);
        allEmployees.clear();
    }

    /**
     * tests the employee is the same one retrieved from the first and last name
     * the list has multiple employees so the main first and last name is changed
     * to an invalid first and last name
     */
    @Test
    public void getEmpFromName1() {
        // given
        Employee employee = (Employee) RandomObject.retrieveRandom("Employee");
        allEmployees.add(employee);
        employee = (Employee) RandomObject.retrieveRandom("Employee");
        allEmployees.add(employee);
        employee = (Employee) RandomObject.retrieveRandom("Employee");
        allEmployees.add(employee);
        // when
        String firstName = employee.getFirstName().concat("objective");
        String lastName = employee.getLastName().concat("objective");
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        // then
        assertEquals(Employee.getEmpFromName(allEmployees,firstName,lastName), employee);
        allEmployees.clear();
    }

    /**
     * tests a fake first and last name that is not in the system, one employee is in the system
     */
    @Test
    public void getEmpFromName2(){
        // given
        Employee employee = (Employee) RandomObject.retrieveRandom("Employee");
        allEmployees.add(employee);
        // when
        String firstName = employee.getFirstName().concat("objective");
        String lastName = employee.getLastName().concat("objective");
        // then
        assertNull(Employee.getEmpFromName(allEmployees,firstName,lastName));
    }

    /**
     * tests what happens when two employees have the same first and last name
     * first and last name should not be unique, finds the first employee where the name matches
     */
    @Test
    public void getEmpFromName3(){
        // given
        Employee employee1 = (Employee) RandomObject.retrieveRandom("Employee");
        String firstName = employee1.getFirstName();
        String lastName = employee1.getLastName();
        allEmployees.add(employee1);
        Employee employee2 = (Employee) RandomObject.retrieveRandom("Employee");
        allEmployees.add(employee2);
        // when
        employee2.setFirstName(firstName);
        employee2.setLastName(lastName);
        // then
        assertEquals(Employee.getEmpFromName(allEmployees,firstName,lastName), employee1);
        allEmployees.clear();
    }


//
//    @Test
//    public void eGetEmployeesByTypeAndSpec() {
//        LinkedList<Employee> emps = new LinkedList <Employee>();
//        emps.add(emp1);
//        assertEquals(Employee.getEmployeesByTypeAndSpec(allEmps,"Interpreter","Georgian"),emps);
//    }
///
//    /**
//     * cannot be tested directly, called by rm.makeSafeITReq
//     */
//    @Test
//    public void getRandAvailableEmp() throws GeneralSecurityException, IOException, MessagingException {
//        //Employee.getRandAvailableEmp(allEmps,, "Georgian");
//        RequestMaker rm = new RequestMaker();
//        LinkedList<String > itList = new LinkedList<>();
//        itList.add("IT");
//        rm.makeSafeITReq(allEmps, MainScreenController.getCurEmployee(),"IT",itList,"ITIssue",null,null,null);
//        // constructor is package private
//        //ITRequest itRequest = new ITRequest(allEmps, MainScreenController.getCurEmployee(),"IT",itList,"ITIssue",null,null,null);
//        //assertEquals(Employee.getRandAvailableEmp(allEmps, itRequest ,"Georgian"),emp1);
//    }


}