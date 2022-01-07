package Employee;

import RequestFacade.IScheduler;
import RequestFacade.Request;
import RoomBooking.Booking;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Employee implements IScheduler {
    String firstName;
    String lastName;
    String empID;
    String empType; //for now can be interpreter or sanitation or admin
    String specifications;
    boolean isBusy;
    String phoneNo;
    String emailUserName;
    boolean emailPref;
    HashMap<String, Booking> requestedBookings; //map<bookingID, Booking>
    HashMap<String, Request>  sentRequests; //map<reqID, Request>
    HashMap<String, Request> receivedRequests;//map<reqID, Request>

    public Employee (String empID, String firstName, String lastName, String empType, String specifications, Boolean isBusy, String phoneNo, String emailUserName, boolean emailPref, HashMap<String, Booking> requestedBookings, HashMap<String, Request> sentRequests, HashMap<String, Request> receivedRequests){
        this.empID = empID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.empType = empType;
        this.specifications = specifications;
        this.isBusy = isBusy;
        this.phoneNo = phoneNo;
        this.emailUserName = emailUserName;
        this.emailPref = emailPref;
        this.requestedBookings = requestedBookings;
        this.sentRequests = sentRequests;
        this.receivedRequests = receivedRequests;
    }

    //other constructor that sets hashmaps to empty
    public Employee (String empID, String firstName, String lastName, String empType, String specifications, Boolean isBusy, String phoneNo, String emailUserName, boolean emailPref){
        this.empID = empID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.empType = empType;
        this.specifications = specifications;
        this.isBusy = isBusy;
        this.phoneNo = phoneNo;
        this.emailUserName = emailUserName;
        this.emailPref = emailPref;
        this.requestedBookings = new HashMap<String, Booking>(); //booking id, booking
        this.sentRequests = new HashMap<String, Request>();
        this.receivedRequests = new HashMap<String, Request>();
    }

    // default constructor
   // public Employee(String s, String brent, String rolfes, String admin, String none, boolean b, String s1, String brolfes){}

    //getters
    public String getEmpID() {
        return empID;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmpType() {
        return empType;
    }
    public String getSpecifications() {return specifications;}
    public boolean isBusy() {
        return isBusy;
    }


    public boolean isEmailPref() {
        return emailPref;
    }

    public void setEmailPref(boolean emailPref) {
        this.emailPref = emailPref;
    }

    public boolean getEmailPref()
    {
        return this.emailPref;
    }

    public String getPhoneNo(){ return phoneNo; }
    public String getEmailUserName(){ return emailUserName;}
    public HashMap<String, Booking> getRequestedBookings(){ return requestedBookings; }
    public HashMap<String,Request> getSentRequests() {
        return sentRequests;
    }
    public HashMap<String,Request> getReceivedRequests() { return receivedRequests; }
    public Boolean getBusy() {
        return isBusy;
    }


    //setters
    public void setEmpID(String empID) { this.empID = empID; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmpType(String empType) { this.empType = empType; }
    public void setSpecifications(String specifications) { this.specifications = specifications;}
    public void setBusy(boolean busy) { isBusy = busy; }
    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }
    public void setEmailUserName(String emailUserName) { this.emailUserName = emailUserName; }
    public void setRequestedBookings(HashMap<String, Booking> requestedBookings){ this.requestedBookings = requestedBookings; }
    public void setSentRequests(HashMap<String, Request> sentRequests) { this.sentRequests = sentRequests; }
    public void setReceivedRequests(HashMap<String, Request> receivedRequests) { this.receivedRequests = receivedRequests; }

    /**
     * Adds the given Booking to an Employee's hash map of bookings
     * @param b A single Booking
     */
    public void addToRequestedBookings(Booking b){
        this.requestedBookings.put(b.getBookingID(), b);
    }

    /**
     * Adds the given Request to an Employee's hash map of sent requests
     * @param r A single Request
     */
    public void addToSentRequests(Request r) {
        this.sentRequests.put(r.getReqID(), r);
//        System.out.println("number of requests " + sentRequests.size());
    }

    /**
     * Adds the given Request to an Employee's hash map of received requests
     * @param r A single Request
     */
    public void addToReceivedRequests(Request r){
        this.receivedRequests.put(r.getReqID(), r);
    }

    /**
     * Compares the attributes of the current Employee with the given Employee
     * @param employee A given Employee
     * @return true if all of the attributes of the current Employee are equal to
     *  those of the given Employee or false if at least one of the attributes of
     *  the given is Employee is not equal to the current Employee
     */
    public Boolean equals(Employee employee){
        return (this.getEmpID().equals(employee.getEmpID()) &&
                this.getFirstName().equals(employee.getFirstName()) &&
                this.getLastName().equals(employee.getLastName()) &&
                this.getEmpType().equals(employee.getEmpType()) &&
                this.getSpecifications().equals(employee.getSpecifications()) &&
                this.getBusy().equals(employee.getBusy()) &&
                this.getPhoneNo().equals(employee.getPhoneNo()) &&
                this.getEmailUserName().equals(employee.getEmailUserName()));
    }

    /**
     * Given a list of employees, will search through it to
     * find employees that are not busy
     * @param emp
     * @param allEmps
     * @return available
     */
    public static boolean isAvailableEmp (Employee emp, Calendar startDate, Calendar endDate, LinkedList<Employee> allEmps) {
        if(emp.getReceivedRequests().isEmpty()){
//            System.out.println("received requests in isavailable is empty");
            return true;
        } else {
            for (Request r : emp.getReceivedRequests().values()) {
                if (IScheduler.isAvailable(r.getScheduledTimeStart(), r.getScheduledTimeEnd(), startDate, endDate)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Given a list of employees, and a start and end time,
     * will search through list to find all available employees for those times
     * @param allEmps
     * @param start
     * @param end
     * @return
     */
    public static LinkedList<Employee> getAllAvailableEmps(LinkedList<Employee> allEmps, Calendar start, Calendar end){
        LinkedList<Employee> availableEmps = new LinkedList<Employee>();
        if (allEmps != null) {
            for (Employee e : allEmps) {
                if (isAvailableEmp(e, start, end, allEmps)) {
                    availableEmps.add(e);
                }
            }
            return availableEmps;
        } else {
            return null;
        }
    }

    public static Employee getRandAvailableEmp (LinkedList<Employee> allEmps, Request r, String specialization) {
//        System.out.println("rand emps called");
        // Filter employees that are of the requested type and specialization using getEmployeesByType method found in Employees class
        LinkedList<Employee> filteredServiceEmps = new LinkedList<Employee>();
        filteredServiceEmps.addAll(Employee.getEmployeesByTypeAndSpec(allEmps, r.getServiceType(), specialization));


        // Get random available employee and mark them busy
        filteredServiceEmps = Employee.getAllAvailableEmps(filteredServiceEmps, r.getScheduledTimeStart(), r.getScheduledTimeEnd());
        if (filteredServiceEmps.size()==0){
            return null;
        } else {
            Random rand = new Random();
            Employee chosenEmployee = filteredServiceEmps.get(rand.nextInt(filteredServiceEmps.size()));

            // Add request to their receivedRequests list
            chosenEmployee.addToReceivedRequests(r);
            //Request.addRequest(r); // adds the given request into a master list of requests

            //return chosen employee
            return chosenEmployee;
        }
    }


    /**
     * Given a list of employees and an employee ID, will
     * find and return employee corresponding to that ID
     * @param emps
     * @param ID
     * @return e or null
     */
    public static Employee getEmpFromID(List<Employee> emps, String ID){
        for (Employee e: emps){
            if (e.getEmpID().equals(ID)){
                return e;
            }
        }
        return null;
    }

    /**
     * Given a list of employees, a first and a last name, will
     * find and return employee corresponding to that name
     * @param allEmps
     * @param firstName
     * @param lastName
     * @return e or null
     */
    public static Employee getEmpFromName(List<Employee> allEmps, String firstName, String lastName){
        for (Employee e: allEmps){
            if (e.getFirstName().equals(firstName) && e.getLastName().equals(lastName)){
                return e;
            }
        }
        return null;
    }

    /**
     * THIS DOES NOT WORK
     * Retrieves a list of employees filtered by type of service request
     * @param allEmps A LinkedList with all of the employees from the database
     * @param serviceType A String which represents the type of service request
     * @return A LinkedList of Employees of a certain type of service request
     */
    public static LinkedList<Employee> getEmployeesByTypeAndSpec(LinkedList<Employee> allEmps, String serviceType, String specifications) {
        LinkedList<Employee> filtered = new LinkedList<Employee>();

        for (Employee e : allEmps) {
            if (e.getEmpType().equals(serviceType)&& e.getSpecifications().equals(specifications)){
//                System.out.println("emp type is: " + e.getEmpType() + "input type is: " + serviceType);
//                System.out.println("emp spec is: " + e.getSpecifications() + "input spec is: " + specifications);
                filtered.add(e);
            }
        }
        if (filtered.size()==0){
//            System.out.println("No employee of type: " + serviceType + " and spec " + specifications);

        }
        return filtered;
    }

    /**
     * Retrieves a list of employees filters by type of service request and specifications
     * @param filtered A LinkedList with the employees filtered by type
     * @param specifications A String which represents the specifications for a certain service request
     * @return A LinkedList of Employees of a certain type and specification
     */
    public static LinkedList<Employee> getEmpBySpecification(LinkedList<Employee> filtered, String specifications) {
        LinkedList<Employee> filteredSpec = new LinkedList<Employee>();
        for (Employee e : filtered) {
            if (e.specifications.equalsIgnoreCase(specifications)) {
                filteredSpec.add(e);
                return filteredSpec;
            }
        }
        System.out.println("Employee specifications do not match any of the requested.");
        return null;
    }

    /**
     * absorb() - absorbs the result set fields into the object itself
     * @param resultSet you must call resultSet.next() before passing it to this function or it will cause deadlock
     * @throws SQLException for any result set errors
     */
    public void absorb(ResultSet resultSet) throws SQLException{
        try {
            this.empID = resultSet.getString("empID");
            this.firstName = resultSet.getString("firstName");
            this.lastName = resultSet.getString("lastName");
            this.empType = resultSet.getString("empType");
            this.specifications = resultSet.getString("specifications");
            this.isBusy = resultSet.getString("isBusy").equals("T");
            this.phoneNo = resultSet.getString("phoneNo");
            this.emailUserName = resultSet.getString("emailUserName");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return isBusy == employee.isBusy &&
                emailPref == employee.emailPref &&
                firstName.equals(employee.firstName) &&
                lastName.equals(employee.lastName) &&
                empID.equals(employee.empID) &&
                empType.equals(employee.empType) &&
                specifications.equals(employee.specifications) &&
                phoneNo.equals(employee.phoneNo) &&
                emailUserName.equals(employee.emailUserName);
    }
}
