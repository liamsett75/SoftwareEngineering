package RequestFacade;

import Employee.Employee;
import Graph.Node;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;

 class InterpreterRequest extends Request implements IRequest {
     private String lang;

    //Constructor
    InterpreterRequest(String reqID, String serviceType, String lang, String description, Employee requester, Node location, Calendar timeRequested, Calendar scheduledTimeStart, Calendar scheduledTimeEnd, Calendar actualTimeStart, Calendar actualTimeEnd, boolean hasBeenCompleted, boolean cancelled) {
        super(reqID, serviceType, description, requester, location, timeRequested, scheduledTimeStart, scheduledTimeEnd, actualTimeStart, actualTimeEnd, hasBeenCompleted, cancelled);
        this.lang = lang;
    }

     //Getter
     public String getLang() { return lang; }
     //Setter
     public void setLang(String lang) { this.lang = lang; }

     //Other methods

     /**
      * Method will safely make a request checking that all conditions are met
      * and will add request to requester's list of sent requests
      * @param allEmps
      * @param requester
      * @param type
      * @param lang
      * @param description
      * @param location
      * @param scheduledTimeStart
      * @param scheduledTimeEnd
      */
     @Override
     public Request makeSafeRequest(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> lang, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws IOException, MessagingException {

        //getting all other parameters needed to construct the BookRoomRequest
        String reqID = generateReqID();
        Calendar timeRequested = Calendar.getInstance();

        if (IScheduler.isValidDate(scheduledTimeStart,scheduledTimeEnd)){
            //actualTimeStart and actualTimeEnd are set to be equal to scheduledTimeStart and scheduledTimeEnd respectively for now
            InterpreterRequest r = new InterpreterRequest(reqID, type, lang.getFirst(), description, requester, location, timeRequested, scheduledTimeStart, scheduledTimeEnd, scheduledTimeStart, scheduledTimeEnd, false, false);

            Employee requestee = Employee.getRandAvailableEmp(allEmps, r, "INT");
            r.setCompletedByWho(requestee);

            //completing the rest of request procedures through method in abstract class
            if (!Request.requestProcedure(r, requester, requestee)) {
                return null;
            }
            return r;
        }
        return null;
     }

     /**
      * @return a
      * Method will generate ID of request
      */
    static private int i = 0;
     @Override
     public String generateReqID() {
        String a = "INT" + i++;
        return a;
    }

}
