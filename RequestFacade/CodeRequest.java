package RequestFacade;

import Employee.Employee;
import Graph.Node;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

public class CodeRequest extends Request implements IRequest {
    private String codeType;
    private String codeDescription;

    CodeRequest(String reqID, String serviceType, String secTypeDescription, LinkedList<String> extras, Employee requester, Node location, Calendar timeRequested, boolean hasBeenCompleted, boolean cancelled) {
        super(reqID, serviceType, secTypeDescription, requester, location, timeRequested, null, null, null, null, hasBeenCompleted, cancelled);
//        System.out.println("extras is: " + extras);
        if (extras != null){
            this.codeType = extras.getFirst();
            this.codeDescription = extras.getLast();
        }
    }

    /**
     * Method will safely make a request checking that all conditions are met
     * and will add request to requester's list of sent requests
     * @param allEmps
     * @param requester
     * @param type
     * @param description
     * @param location
     * @param scheduledTimeStart
     * @param scheduledTimeEnd
     * @return
     */
    public Request makeSafeRequest(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> extraSpecifications, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws IOException, MessagingException {
        //getting all other parameters needed to construct the BookRoomRequest


        String reqID = generateReqID();

        String serviceType = type;
        Calendar timeRequested = Calendar.getInstance();

        Request r = new CodeRequest(reqID, serviceType, description, extraSpecifications, requester, location, timeRequested,false, false);

        //adding request to requester's list of sent requests
        requester.addToSentRequests(r);

        // codeType + "\n" + codeDescription
        Employee requestee = Employee.getRandAvailableEmp(allEmps, r, "COD");

        if (!Request.requestProcedure(r, requester, requestee)) {
            return null;
        }

        return r;
    }

    /**
     * Method will generate ID of request
     */
    static private int i = 0;

    public String generateReqID() {
        String a = "SEC" + i++;
        return a;
    }

    /**
     *
     * @return hm
     */
    public static HashMap<String, String> getActivitiesByCode() {
        HashMap<String, String> hm = new HashMap<>();

        hm.put("Code Red", "Fire");
        hm.put("Code Blue", "Adult Medical Emergency");
        hm.put("Code White", "Pediatric Medical Emergency");
        hm.put("Code Pink", "Infant Abduction");
        hm.put("Code Purple", "Child Abduction");
        hm.put("Code Yellow", "Bomb Threat");
        hm.put("Code Grey", "Combative Person");
        hm.put("Code Silver", "Person with a weapon and/or Active Shooter and/or Hostage Situation");
        hm.put("Code Orange", "Hazardous Material Spill/Release");
        hm.put("Code Green", "Patient Elopement");
        hm.put("Code Triage Internal", "Internal Disaster");
        hm.put("Code Triage External", "External Disaster");

        return hm;
    }
}
