package RequestFacade;

import Employee.Employee;
import Graph.Node;
import javafx.scene.effect.InnerShadow;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.LinkedList;

public class ITRequest extends Request implements IRequest{
    String deviceNumber;

    //Constructor
    ITRequest(String reqID, String serviceType, String deviceNumber, String description, Employee requester, Node location, Calendar timeRequested, Calendar scheduledTimeStart, Calendar scheduledTimeEnd, Calendar actualTimeStart, Calendar actualTimeEnd, boolean hasBeenCompleted, boolean cancelled) {
        super(reqID, serviceType, description, requester, location, timeRequested, scheduledTimeStart, scheduledTimeEnd, actualTimeStart, actualTimeEnd, hasBeenCompleted, cancelled);
        this.deviceNumber = deviceNumber;

    }

    //Getter
    public String getITIssue() {
        return deviceNumber;
    }

    //Setter
    public void setITIssue(String ITIssue) {
        this.deviceNumber =ITIssue;
    }

    //Other methods

    /**
     * Method will safely make a request checking that all conditions are met
     * and will add request to requester's list of sent requests
     * @param allEmps
     * @param requester
     * @param type
     * @param ITType
     * @param location
     * @param scheduledTimeStart
     * @param scheduledTimeEnd
     * @throws IOException
     * @throws MessagingException
     * @return
     */
    @Override
    public Request makeSafeRequest(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> ITType, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws IOException, MessagingException {
        //getting all other parameters needed to construct the BookRoomRequest
        String reqID = generateReqID();
        Calendar timeRequested = Calendar.getInstance();

        if(IScheduler.isValidDate(scheduledTimeStart,scheduledTimeEnd)){
            // actualTimeStart and actualTimeEnd are set to be equal to scheduledTimeStart and scheduledTimeEnd respectively for now
            Request r = new ITRequest(reqID, type, ITType.getFirst(), description, requester, location, timeRequested, scheduledTimeStart, scheduledTimeEnd, scheduledTimeStart, scheduledTimeEnd, false, false);

            Employee requestee = Employee.getRandAvailableEmp(allEmps, r, "IT");

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
        String a = "IT" + i++;
        return a;
    }
}
