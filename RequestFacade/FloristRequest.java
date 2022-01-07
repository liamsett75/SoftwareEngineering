package RequestFacade;

import Employee.Employee;
import Graph.Node;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;

public class FloristRequest extends Request implements IRequest {
    private String flowerType;
    private String color;
    private String numOf;
    LinkedList<String> bouquet = new LinkedList<> ();

    //Constructor
    FloristRequest(String flowerType, String color, String numOf, String reqID, String serviceType, String description, Employee requester, Node location, Calendar timeRequested, boolean hasBeenCompleted, boolean cancelled) {
        super(reqID, serviceType, description, requester, location, timeRequested,null, null, null, null, hasBeenCompleted, cancelled);
        this.flowerType = flowerType;
        this.color = color;
        this.numOf = numOf;
    }

    //Getter
    public LinkedList<String> getBouquet() {
        return bouquet;
    }

    //Setter
    public void setBouquet() {
        this.bouquet = bouquet;
    }

    //Other methods


    /**
     * Method will safely make a request checking that all conditions are met
     * and will add request to requester's list of sent requests
     *
     * @param allEmps
     * @param requester
     * @param type
     * @param description
     * @param location
     * @param scheduledTimeStart
     * @param scheduledTimeEnd
     * @return
     */
    public Request makeSafeRequest(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> bouquet, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) {

        //getting all other parameters needed to construct the BookRoomRequest
        String reqID = generateReqID();
        Calendar timeRequested = Calendar.getInstance();

        String Type, Number, Color;

        if(bouquet == null) {
            Type = "";
            Number = "";
            Color = "";
        }
        else {
            Type = bouquet.get(0);
            Number = bouquet.get(1);
            Color = bouquet.get(2);
        }

        if (IScheduler.isValidDate(scheduledTimeStart,scheduledTimeEnd)){
            // actualTimeStart and actualTimeEnd are set to be equal to scheduledTimeStart and scheduledTimeEnd respectively for now
            Request r = new FloristRequest(Type, Color, Number, reqID, type, description, requester, location, timeRequested, false, false);

            Employee requestee = Employee.getRandAvailableEmp(allEmps, r, "FLO");

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
    private static int i = 0;
    @Override
    public String generateReqID() {
        String a = "FLORAL" + i++;
        return a;
    }

}
