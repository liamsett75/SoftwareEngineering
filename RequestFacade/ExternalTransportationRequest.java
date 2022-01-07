package RequestFacade;

import Employee.Employee;
import Graph.Node;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;

public class ExternalTransportationRequest extends Request implements IRequest {

    private String specifications;
    private String dest;

    //Constructor
    ExternalTransportationRequest (String reqID, String serviceType, String sanType, String description, Employee requester, Node loc, String dest, Calendar timeRequested, Calendar scheduledTimeStart, Calendar scheduledTimeEnd, Calendar actualTimeStart, Calendar actualTimeEnd, boolean hasBeenCompleted, boolean cancelled) {
        super(reqID, serviceType, description, requester, loc, timeRequested, scheduledTimeStart, scheduledTimeEnd, actualTimeStart, actualTimeEnd, hasBeenCompleted, cancelled);
        this.specifications = sanType;
        this.dest = dest;
    }

    //Getter
    public String getSanType() {
        return specifications;
    }

    //Setter
    public void setSanType(String sanType) {
        this.specifications = sanType;
    }

    //Other methods

    /**
     * Method will safely make a request checking that all conditions are met
     * and will add request to requester's list of sent requests
     *
     * @param allEmps
     * @param requester
     * @param type
     * @param specifications
     * @param location
     * @param scheduledTimeStart
     * @param scheduledTimeEnd
     */
    @Override
    public Request makeSafeRequest(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> specifications, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws IOException, MessagingException {
        //getting all other parameters needed to construct the BookRoomRequest
        String reqID = generateReqID();
        Calendar timeRequested = Calendar.getInstance();

        // actualTimeStart and actualTimeEnd are set to be equal to scheduledTimeStart and scheduledTimeEnd respectively for now
        ExternalTransportationRequest r;
        if(IScheduler.isValidDate(scheduledTimeStart,scheduledTimeEnd)){
            r = new ExternalTransportationRequest(reqID, type, specifications.getFirst(), description, requester, null, specifications.get(1), timeRequested, scheduledTimeStart, scheduledTimeEnd, scheduledTimeStart, scheduledTimeEnd, false, false);
            //adding request to requester's list of sent requests
            requester.addToSentRequests(r);

            Employee requestee = Employee.getRandAvailableEmp(allEmps, r, "TEXT");

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
        String a = "EXT" + i++;
        return a;
    }


}
