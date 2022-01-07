package RequestFacade;

import Employee.Employee;
import Graph.Node;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static Email.Email.sendServiceRequestee;
import static Email.Email.sendServiceRequester;

public abstract class Request {
    private String reqID;
    private String serviceType;
    private Employee requester; // the employee who requested the service
    private Node location;
    private Calendar timeRequested;
    private Calendar scheduledTimeStart;
    private Calendar scheduledTimeEnd;
    private Calendar actualTimeStart; // the time at which the request was started
    private Calendar actualTimeEnd; // the time at which the request was completed
    private boolean hasBeenCompleted;
    private boolean cancelled;
    private Employee completedByWho;
    private String description;

    static LinkedList<Request> allRequests = new LinkedList<Request>();


    private static LinkedList<MedicalImagingRequest> ctRequests = new LinkedList<MedicalImagingRequest>();
    private static LinkedList<MedicalImagingRequest> mriRequests = new LinkedList<MedicalImagingRequest>();
    private static LinkedList<MedicalImagingRequest> petRequests = new LinkedList<MedicalImagingRequest>();


    //Constructor
    Request(String reqID, String serviceType, String description, Employee requester, Node location, Calendar timeRequested, Calendar scheduledTimeStart, Calendar scheduledTimeEnd, Calendar actualTimeStart, Calendar actualTimeEnd, boolean hasBeenCompleted, boolean cancelled) {
        this.reqID = reqID;
        this.serviceType = serviceType;
        this.description = description;
        this.requester = requester;
        this.location = location;
        this.timeRequested = timeRequested;
        this.scheduledTimeStart = scheduledTimeStart;
        this.scheduledTimeEnd = scheduledTimeEnd;
        this.actualTimeStart = actualTimeStart;
        this.actualTimeEnd = actualTimeEnd;
        this.hasBeenCompleted = hasBeenCompleted;
        this.cancelled = cancelled;
        completedByWho = null;
    }

    protected Request() {
    }

    //Getters
    public String getReqID() {
        return this.reqID;
    }
    public String getServiceType() {
        return this.serviceType;
    }
    public String getDescription() { return this.description; }
    public Employee getRequester() { return this.requester; }
    public Node getLocation() { return this.location; }
    public Calendar getTimeRequested() { return timeRequested; }
    public Calendar getScheduledTimeStart() { return scheduledTimeStart; }
    public Calendar getScheduledTimeEnd() { return scheduledTimeEnd; }
    public Calendar getActualTimeStart() { return actualTimeStart; }
    public Calendar getActualTimeEnd() { return actualTimeEnd; }
    public boolean getHasBeenCompleted() { return this.hasBeenCompleted; }
    public boolean getCancelled() { return this.cancelled; }
    public Employee getCompletedByWho() { return this.completedByWho; }


    public static LinkedList<Request> getAllRequests() {
//        System.out.println("getter list size is " + allRequests.size());
        return allRequests;
    }

    public static LinkedList<MedicalImagingRequest> getCtRequests() {
        return ctRequests;
    }

    public static LinkedList<MedicalImagingRequest> getMriRequests() {
        return mriRequests;
    }

    public static LinkedList<MedicalImagingRequest> getPetRequests() {
        return petRequests;
    }

    //Setters
    public void setReqID(String reqID) { this.reqID = reqID; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    public void setDescription(String description) { this.description = description; }
    public void setRequester(Employee requester) { this.requester = requester; }
    public void setLocation(Node location) { this.location = location; }
    public void setTimeRequested(Calendar timeRequested) { this.timeRequested = timeRequested; }
    public void setScheduledTimeStart(Calendar scheduledTimeStart) { this.scheduledTimeStart = scheduledTimeStart; }
    public void setScheduledTimeEnd(Calendar scheduledTimeEnd) { this.scheduledTimeEnd = scheduledTimeEnd; }
    public void setActualTimeStart(Calendar actualTimeStart) { this.actualTimeStart = actualTimeStart; }
    public void setActualTimeEnd(Calendar actualTimeEnd) { this.actualTimeEnd = actualTimeEnd; }
    public void setHasBeenCompleted(boolean hasBeenCompleted) { this.hasBeenCompleted = hasBeenCompleted; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
    public void setCompletedByWho(Employee emp) { this.completedByWho = emp; }

    public static void setAllRequests() { allRequests = allRequests; }


    //Adder
    public static void addRequest(Request r) {
        allRequests.add(r);
        //System.out.println("size of all requests is " + allRequests.size());

    }

    public static void addCTRequest(MedicalImagingRequest r) {
        ctRequests.add(r);
//        System.out.println("size of ct list is " + ctRequests.size());
    }

    public static void addMRIRequest(MedicalImagingRequest r) {
        mriRequests.add(r);
//        System.out.println("size of mri list is " + mriRequests.size());
    }

    public static void addPETRequest(MedicalImagingRequest r) {
        petRequests.add(r);
//        System.out.println("size of pet requests is " + petRequests.size());
//        System.out.println("size of all requests is " + allRequests.size());
    }

    /**
     * Calculates the time it took for a task to be completed from the time it
     * was started to the time it was completed in milliseconds
     *
     * @param timeStart The time the request was begun represented as a Date
     * @param timeEnd   The time the request was completed represented as a Date
     * @param timeUnit  The unit in which the difference is calculated in
     * @return The difference in time in milliseconds
     */
    long calculateElapsedTime(Date timeStart, Date timeEnd, TimeUnit timeUnit) {
        long diffInMS = timeEnd.getTime() - timeStart.getTime();
        return timeUnit.convert(diffInMS, TimeUnit.MILLISECONDS);
    }

    /**
     * Turns information of object into a printable string
     *
     * @return a+b+c+d
     */
//    public static String toString(Request r) {
//        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
//
//        String a = "Booking Request " + r.reqID + " of type " + r.serviceType + ".\n";
//        String b = "Requested by " + r.requester.getFirstName() + " " + r.requester.getLastName() + " at " + r.location.getLongName() + ".\n";
//        String c = "Time requested: " + r.timeRequested.getTime() + ". ";
//        String d = "Scheduled start: " + r.scheduledTimeStart.getTime() + " Scheduled end: " + r.scheduledTimeEnd.getTime() + ".\n";
//        String e = "Completed?: " + r.hasBeenCompleted + ".\n";
//
//        System.out.println(a + b + c + d + e);
//        return a + b + c + d + e;
//
//    }


    /**
     * Template to do all procedures required after a request is created
     * @param r
     * @param requester
     * @param requestee
     * @throws IOException
     * @throws MessagingException
     */
    static boolean requestProcedure (Request r, Employee requester, Employee requestee) {

        if (requestee == null){
            return false;

        } else {

            //filling requestedByWho field in request
            r.setCompletedByWho(requestee);

//            System.out.println("r is " + r.getReqID());
//            System.out.println("requester is " + requester.getEmpID());
//            System.out.println("requestee is " + requestee.getEmpID());

            //getting random available employee to fulfill the request and sending email to both requester and requestee
            sendReqEmail(r, requester, requestee);

            //adding request to list of all requests
            allRequests.add(r);

            //adding request to requester's list of sent requests
            requester.addToSentRequests(r);
        }
        return true;
    }


    /**
     * Method will get a random available employee to fulfill a request,
     * then send an email to both the requester and requestee
     * @param r
     * @param requestee
     * @param requester
     * @throws IOException
     * @throws MessagingException
     */
    static void sendReqEmail(Request r, Employee requester, Employee requestee) {

        //getting location for email
        String location;

        if (r.getLocation()==null){
            location = "n/a";
        }
        else {
            location = r.getLocation().getLongName();
        }

        //if requestee is not null then find available random employee and send them an email that they're requested
        if (requestee == null) {
            sendServiceRequester(r.getServiceType(), location);
        } else {
           sendServiceRequestee(location, r.getServiceType(), requestee);
        }
    }


    /**
     * Will safely cancel request
     */
    void cancelSafeRequest() {
        this.setCancelled(true);
        //removing cancelled request from list of received requests of the Employee assigned to do the task
        this.getCompletedByWho().getReceivedRequests().remove(this.description);

        // TODO: send and email saying that it was cancelled
    }

    public static Calendar stringToCalendar(String str) {

        Calendar cal = null;

        try {
            cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            cal.setTime(sdf.parse(str));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cal;

    }
}

