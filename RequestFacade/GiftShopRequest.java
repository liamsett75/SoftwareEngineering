package RequestFacade;

import Employee.Employee;
import Graph.Node;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

public class GiftShopRequest extends Request implements IRequest {

    private HashMap<String, Double> shopCatalog = new HashMap<>(); // Hashmap serves as a drop down list, you select an item and it auto populates you item name and cost. You can then select a time and room ID, which can be done by clicking a node on the map
    private String item;
    private double giftPrice;
    private String roomID;
    private double deliverTime;

    GiftShopRequest(String reqID, String serviceType, String sanType, String description, Employee requester, Node location, Calendar timeRequested, Calendar scheduledTimeStart, Calendar scheduledTimeEnd, Calendar actualTimeStart, Calendar actualTimeEnd, boolean hasBeenCompleted, boolean cancelled, HashMap<String, Double> shopCatalog, String item, double giftPrice, String roomID, double deliverTime) {
        super(reqID, serviceType, description, requester, location, timeRequested, scheduledTimeStart, scheduledTimeEnd, actualTimeStart, actualTimeEnd, hasBeenCompleted, cancelled);
        this.shopCatalog = shopCatalog;
        this.item = item;
        this.roomID = roomID;
        this.deliverTime = deliverTime;
        this.giftPrice = giftPrice;
    }

    /**
     *
     * @param allEmps
     * @param requester
     * @param type
     * @param sanType
     * @param description
     * @param location
     * @param scheduledTimeStart
     * @param scheduledTimeEnd
     * @return
     */
    @Override
    public Request makeSafeRequest(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> sanType, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws IOException, MessagingException {

        //getting all other parameters needed to construct the Gift Shop Request
        String reqID = generateReqID();
        Calendar timeRequested = Calendar.getInstance();

        if(IScheduler.isValidDate(scheduledTimeStart, scheduledTimeEnd)){
            // actualTimeStart and actualTimeEnd are set to be equal to scheduledTimeStart and scheduledTimeEnd respectively for now
            GiftShopRequest r = new GiftShopRequest(reqID, type, sanType.getFirst(), description, requester, location, timeRequested, scheduledTimeStart, scheduledTimeEnd, scheduledTimeStart, scheduledTimeEnd, false, false, shopCatalog, item, giftPrice, roomID, deliverTime);

            Employee requestee = Employee.getRandAvailableEmp(allEmps, r, "GIF");

            //completing the rest of request procedures through method in abstract class
            if (!Request.requestProcedure(r, requester, requestee)) {
                return null;
            }

            return r;
        }
        return null;
    }

    public void populateCatalog() {
        shopCatalog.put("Flowers", 70.00);
        shopCatalog.put("Balloons", 20.00);
        shopCatalog.put("Water Bottle", 12.95);
        shopCatalog.put("Fleece", 78.95);
    }

    /**
     * Method will generate ID of request
     */
    static private int i = 0;
    @Override
    public String generateReqID() {
        String a = "GIFT" + i++;
        return a;
    }
}