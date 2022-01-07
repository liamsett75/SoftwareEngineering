package RequestFacade;

import Employee.Employee;
import Graph.Node;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;

class InternalTransportRequest extends Request implements IRequest {
    private String transportType;

    //Constructor
    InternalTransportRequest(String reqID, String serviceType, String transportType, String description, Employee requester, Node location, Calendar timeRequested, Calendar scheduledTimeStart, Calendar scheduledTimeEnd, Calendar actualTimeStart, Calendar actualTimeEnd, boolean hasBeenCompleted, boolean cancelled) {
        super(reqID, serviceType, description, requester, location, timeRequested, scheduledTimeStart, scheduledTimeEnd, actualTimeStart, actualTimeEnd, hasBeenCompleted, cancelled);
        this.transportType = transportType;
    }

    //Getter
    public String getTransportType() { return transportType; }

    //Setter
    public void setTransportType(String transportType) { this.transportType = transportType; }

    //Other methods

    /**
     * Method will safely make a request checking that all conditions are met
     * and will add request to requester's list of sent requests
     * @param allEmps
     * @param requester
     * @param type
     * @param transportType
     * @param location
     * @param scheduledTimeStart
     * @param scheduledTimeEnd
     */
    public Request makeSafeRequest(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> transportType, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws IOException, MessagingException {

        //getting all other parameters needed to construct the BookRoomRequest
        String reqID = generateReqID();
        Calendar timeRequested = Calendar.getInstance();

        if (IScheduler.isValidDate(scheduledTimeStart, scheduledTimeEnd)){

            // actualTimeStart and actualTimeEnd are set to be equal to scheduledTimeStart and scheduledTimeEnd respectively for now
            InternalTransportRequest r = new InternalTransportRequest(reqID, type, transportType.getFirst(), description, requester, location, timeRequested, scheduledTimeStart, scheduledTimeEnd, scheduledTimeStart, scheduledTimeEnd, false, false);

            Employee requestee = Employee.getRandAvailableEmp(allEmps, r, "TINT");

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
    public String generateReqID() {
        String a = "ITRANS" + i++;
        return a;
    }


}
