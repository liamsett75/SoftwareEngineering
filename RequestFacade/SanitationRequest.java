package RequestFacade;

import Databases.SanitationRequestDB;
import Employee.Employee;
import Graph.Node;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;

 public class SanitationRequest extends Request implements IRequest {

      private String sanType;
      private boolean isBiohazard;

      //Constructor
      public SanitationRequest(String reqID, String serviceType, String sanType, boolean isBiohazard, String description, Employee requester, Node location, Calendar timeRequested, Calendar scheduledTimeStart, Calendar scheduledTimeEnd, Calendar actualTimeStart, Calendar actualTimeEnd, boolean hasBeenCompleted, boolean cancelled) {
        super(reqID, serviceType, description, requester, location, timeRequested, scheduledTimeStart, scheduledTimeEnd, actualTimeStart, actualTimeEnd, hasBeenCompleted, cancelled);
        this.sanType = sanType;
        this.isBiohazard = isBiohazard;
      }

     //Getter
     public String getSanType() {
        return sanType;
    }
     public boolean getIsBiohazard() { return isBiohazard; }

     //Setter
     public void setSanType(String sanType) {
         this.sanType = sanType;
     }
     public void setBiohazard(boolean biohazard) { isBiohazard = biohazard; }

     //Other methods

     /**
      * Method will safely make a request checking that all conditions are met
      * and will add request to requester's list of sent requests
      * @param allEmps
      * @param requester
      * @param type
      * @param sanType
      * @param location
      * @param scheduledTimeStart
      * @param scheduledTimeEnd
      */
     @Override
     public Request makeSafeRequest(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> sanType, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws IOException, MessagingException {

         //getting all other parameters needed to construct the BookRoomRequest
        // String reqID = generateReqID();

         boolean idAvailable;
         String reqID;


         do {
             reqID = generateReqID();
             idAvailable = SanitationRequestDB.availableSanitationReqID(reqID);
         } while (!idAvailable);

//         System.out.println("FINAL GOOD REQ ID IS " + reqID);

         Calendar timeRequested = Calendar.getInstance();

         boolean bhValue;
         if(sanType.get(1).equals("T"))
             bhValue = true;
         else
             bhValue = false;

         if (IScheduler.isValidDate(scheduledTimeStart, scheduledTimeEnd)){

             // actualTimeStart and actualTimeEnd are set to be equal to scheduledTimeStart and scheduledTimeEnd respectively for now
//             System.out.println("locatin field in sanitation request is " + location.getNodeID());
             SanitationRequest r = new SanitationRequest(reqID, type, sanType.getFirst(), bhValue, description, requester, location, timeRequested, scheduledTimeStart, scheduledTimeEnd, scheduledTimeStart, scheduledTimeEnd, false, false);

             Employee requestee = Employee.getRandAvailableEmp(allEmps, r, "SAN"); //sanType.getFirst()

             //completing the rest of request procedures through method in abstract class
             if (!Request.requestProcedure(r, requester, requestee)) {
                 return null;
             }

             try {
                 SanitationRequestDB.addSanitationRequest(r);
             }catch (SQLException e)
             {
                 e.printStackTrace();
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
        String a = "SAN" + i++;
        return a;
    }
}
