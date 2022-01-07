package RequestFacade;

import Employee.Employee;
import Graph.Node;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;

public class PatientInfoRequest extends Request implements IRequest {
    private String fName;
    private String lName;

    //Constructor
    PatientInfoRequest(String reqID, String serviceType, String description, Employee requester, Node location, Calendar timeRequested, Calendar scheduledTimeStart, Calendar scheduledTimeEnd, Calendar actualTimeStart, Calendar actualTimeEnd, boolean hasBeenCompleted, boolean cancelled, String fName, String lName) {
        super(reqID, serviceType, description, requester, location, timeRequested, scheduledTimeStart, scheduledTimeEnd, actualTimeStart, actualTimeEnd, hasBeenCompleted, cancelled);
        this.fName = fName;
        this.lName = lName;
    }

    //Getters
    public String getFName() {
        return fName;
    }
    public String getLName() {
        return lName;
    }

    //Setters
    public void setFName(String fName) {
        this.fName = fName;
    }
    public void setLName(String lName) {
        this.lName = lName;
    }

    //Other methods

    /**
     * Method will safely make a request checking that all conditions are met
     * and will add request to requester's list of sent requests
     * @param allEmps
     * @param requester
     * @param type
     * @param name
     * @param location
     * @param scheduledTimeStart
     * @param scheduledTimeEnd
     */
    @Override
    public Request makeSafeRequest(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> name, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws IOException, MessagingException {

        //getting all other parameters needed to construct the BookRoomRequest
        String reqID = generateReqID();
        Calendar timeRequested = Calendar.getInstance();

        if (IScheduler.isValidDate(scheduledTimeStart, scheduledTimeEnd)){
            // actualTimeStart and actualTimeEnd are set to be equal to scheduledTimeStart and scheduledTimeEnd respectively for now
            PatientInfoRequest r = new PatientInfoRequest(reqID, type, description, requester, location, timeRequested, scheduledTimeStart, scheduledTimeEnd, scheduledTimeStart, scheduledTimeEnd, false, false, name.get(0), name.get(1));

            Employee requestee = Employee.getRandAvailableEmp(allEmps, r, "PAT");

            //completing the rest of request procedures through method in abstract class
            if (!Request.requestProcedure(r, requester, requestee)) {
                return null;
            }
            return r;
        }
        return null;
    }

    /**
     * Method will generate ID of request
     */
    static private int i = 0;
    @Override
    public String generateReqID() {
        String a = "PATINFO" + i++;
        return a;
    }

}
