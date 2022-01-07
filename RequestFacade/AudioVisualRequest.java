package RequestFacade;

import Employee.Employee;
import Graph.Node;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;

public class AudioVisualRequest extends Request implements IRequest {
    // fields, look at others for some ideas
    private String deviceType;
    private String deviceName;
    private String deviceBrand;
    private Integer deviceID;
    //Constructor
    public AudioVisualRequest(String reqID, String serviceType, String deviceType, String deviceName, String deviceBrand, String description, Employee requester, Node location, Calendar timeRequested, Calendar scheduledTimeStart, Calendar scheduledTimeEnd, Calendar actualTimeStart, Calendar actualTimeEnd, boolean hasBeenCompleted, boolean cancelled) {
        super(reqID, serviceType, description, requester, location, timeRequested, scheduledTimeStart, scheduledTimeEnd, actualTimeStart, actualTimeEnd, hasBeenCompleted, cancelled);
        this.deviceType = deviceType;
        this.deviceName = deviceName;
        this.deviceBrand = deviceBrand;
        this.deviceID = i;
    }

    // RequestMaker has to initialize each class to use it
    public AudioVisualRequest(){}
    public String getDeviceType() {
        return deviceType;
    }
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
    public String getDeviceName() {
        return deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public String getDeviceBrand() {
        return deviceBrand;
    }
    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }
    public Integer getDeviceID() {
        return deviceID;
    }
    public void setDeviceID(Integer deviceID) {
        this.deviceID = deviceID;
    }


    /**
     * Method will safely make a request checking that all conditions are met
     * and will add request to requester's list of sent requests
     * @param allEmps
     * @param requester
     * @param type
     * @param specifications
     * @param description
     * @param location
     * @param scheduledTimeStart
     * @param scheduledTimeEnd
     * @return true to show creating the request was successful
     */
    @Override
    public Request makeSafeRequest(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> specifications, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws IOException, MessagingException {
        //getting all other parameters needed to construct the BookRoomRequest
        String reqID = generateReqID();
        Calendar timeRequested = Calendar.getInstance();

        // deviceSpecifications is ordered in: deviceType, deviceName, deviceBrand
        // actualTimeStart and actualTimeEnd are set to be equal to scheduledTimeStart and scheduledTimeEnd respectively for now
        Request r;
        if(IScheduler.isValidDate(scheduledTimeStart,scheduledTimeEnd)){

            r = new AudioVisualRequest(reqID, type, specifications.get(0), specifications.get(1), specifications.get(2), description, requester, location, timeRequested, scheduledTimeStart, scheduledTimeEnd, null, null, false, false);

            Employee requestee = Employee.getRandAvailableEmp(allEmps, r, "AV");

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
        String ID = "AVR" + i++;
        return ID;
    }


}
