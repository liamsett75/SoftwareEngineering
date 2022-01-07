package RequestFacade;

import Databases.ReligiousRequestDB;
import Employee.Employee;
import Graph.Node;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

public class ReligiousRequest extends Request implements IRequest {
    private String patientFName;
    private String patientLName;
    private String religion;
    private String religiousActivity;

    //Constructor
    public ReligiousRequest(String reqID, String serviceType, String religion, String religiousActivity, String description, Employee requester, String patientFName, String patientLName, Node location, Calendar timeRequested, Calendar scheduledTimeStart, Calendar scheduledTimeEnd, Calendar actualTimeStart, Calendar actualTimeEnd, boolean hasBeenCompleted, boolean cancelled) {
        super(reqID, serviceType, description, requester, location, timeRequested, scheduledTimeStart, scheduledTimeEnd, actualTimeStart, actualTimeEnd, hasBeenCompleted, cancelled);
        this.patientFName = patientFName;
        this.patientLName = patientLName;
        this.religion = religion;
        this.religiousActivity = religiousActivity;
    }

    //Getters
    public String getReligion() { return religion; }
    public String getReligiousActivity() { return religiousActivity; }
    public String getPatientFName() { return patientFName; }
    public String getPatientLName() { return patientLName; }

    //Setters
    public void setReligion(String religion) { this.religion = religion; }
    public void setReligiousActivity(String religiousActivity) { this.religiousActivity = religiousActivity; }
    public void setPatientFName(String fName){ this.patientFName = fName; }
    public void setPatientLName(String lName){ this.patientLName = lName; }

    //Other methods

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
     */
    @Override
    public Request makeSafeRequest(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> religionAndSpecs, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws IOException, MessagingException {

        //getting all other parameters needed to construct the BookRoomRequest
        String reqID = generateReqID();
        Calendar timeRequested = Calendar.getInstance();

        if (IScheduler.isValidDate(scheduledTimeStart, scheduledTimeEnd)) {

            // actualTimeStart and actualTimeEnd are set to be equal to scheduledTimeStart and scheduledTimeEnd respectively for now

            // public ReligiousRequest(String reqID, String serviceType, String religion, String religiousActivity, String description, Employee requester, String patientFName, String patientLName, Node location, Calendar timeRequested, Calendar scheduledTimeStart, Calendar scheduledTimeEnd, Calendar actualTimeStart, Calendar actualTimeEnd, boolean hasBeenCompleted, boolean cancelled) {
            ReligiousRequest r = new ReligiousRequest(reqID, type, religionAndSpecs.get(2), religionAndSpecs.get(3), description, requester, religionAndSpecs.get(0), religionAndSpecs.get(1), location, timeRequested, scheduledTimeStart, scheduledTimeEnd, scheduledTimeStart, scheduledTimeEnd, false, false);

//            System.out.println("INSIDE OF MAKE SAFE REQUEST");
            for(String s: religionAndSpecs)
            {
//                System.out.println(s);
            }

            Employee requestee = Employee.getRandAvailableEmp(allEmps, r, "REL"); /*religionAndSpecs.get(2) + " " + religionAndSpecs.get(3)*/

//            System.out.println("we got to before req proc");

            //completing the rest of request procedures through method in abstract class
            if (!Request.requestProcedure(r, requester, requestee)) {
                return null;
            }

//            System.out.println("we got to after req procedure");

            try {
                ReligiousRequestDB.addReligiousRequest(r);
//                System.out.println("request gets added");
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
        String a = "REL" + i++;
        return a;
    }

    /**
     * Retrieves activities available at the hospital for every kind of religion
     * @return hm
     */
    public static HashMap<String, LinkedList<String>> getActivitiesByReligion(){
        HashMap<String, LinkedList<String>> hm = new HashMap<>();
        LinkedList<String> muslimActivities = new LinkedList<>();
        muslimActivities.add("Prayer");
        muslimActivities.add("Holy Day Ritual");

        LinkedList<String> catholicActivities = new LinkedList<>();
        catholicActivities.add("Communion");
        catholicActivities.add("Sacrament");
        catholicActivities.add("Prayer");
        catholicActivities.add("Holy Day Ritual");

        LinkedList<String> protestantActivities = new LinkedList<>();
        protestantActivities.add("Communion");
        protestantActivities.add("Holy Day Ritual");

        LinkedList<String> jewishActivities = new LinkedList<>();
        jewishActivities.add("Shabbat Candles");
        jewishActivities.add("Holy day Ritual");

        hm.put("Muslim", muslimActivities);
        hm.put("Catholic", catholicActivities);
        hm.put("Protestant", protestantActivities);
        hm.put("Jewish", jewishActivities);

        return hm;
    }
}
