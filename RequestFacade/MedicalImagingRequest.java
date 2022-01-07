package RequestFacade;

import Employee.Employee;
import Graph.Node;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.LinkedList;

public class MedicalImagingRequest extends Request implements IRequest {

    private String fName;
    private String lName;
    private String machineType;
    private String bodyPart;
    private boolean isEmergency;

    public MedicalImagingRequest(String reqID, String serviceType, String fName, String lName, String machineType, String bodyPart, boolean isEmergency, String description, Employee requester, Node location, Calendar timeRequested, Calendar scheduledTimeStart, Calendar scheduledTimeEnd, Calendar actualTimeStart, Calendar actualTimeEnd, boolean hasBeenCompleted, boolean cancelled) {
        super(reqID, serviceType, description, requester, location, timeRequested, scheduledTimeStart, scheduledTimeEnd, actualTimeStart, actualTimeEnd, hasBeenCompleted, cancelled);
        this.fName = fName;
        this.lName = lName;
        this.machineType = machineType;
        this.bodyPart = bodyPart;
        this.isEmergency = isEmergency;
    }


    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public boolean getIsEmergency() {
        return isEmergency;
    }

    public void setEmergency(boolean emergency) {
        isEmergency = emergency;
    }

    @Override
    public Request makeSafeRequest(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> imagingList, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) {

        //getting all other parameters needed to construct the BookRoomRequest
        String reqID = generateReqID();
        Calendar timeRequested = Calendar.getInstance();

        // actualTimeStart and actualTimeEnd are set to be equal to scheduledTimeStart and scheduledTimeEnd respectively for now
        MedicalImagingRequest r;
        if (IScheduler.isValidDate(scheduledTimeStart,scheduledTimeEnd)){
            r = new MedicalImagingRequest(reqID, type, imagingList.get(0), imagingList.get(1), imagingList.get(2), imagingList.get(3), isEmergency, description, requester, location, timeRequested, scheduledTimeStart, scheduledTimeEnd, scheduledTimeStart, scheduledTimeEnd, false, false);
            //adding request to requester's list of sent requests
            requester.addToSentRequests(r);

            Employee requestee = Employee.getRandAvailableEmp(allEmps, r, imagingList.get(2));

            //completing the rest of request procedures through method in abstract class
            if (!Request.requestProcedure(r, requester, requestee)) {
                return null;
            }

            if(r.getMachineType().equals("CT"))
                Request.addCTRequest(r);
            else if(r.getMachineType().equals("MRI"))
                Request.addMRIRequest(r);
            else if(r.getMachineType().equals("PET"))
                Request.addPETRequest(r);

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
        String a = "IMG" + i++;
        return a;
    }



}
