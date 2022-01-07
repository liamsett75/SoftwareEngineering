package RequestFacade;

import Employee.Employee;
import Graph.Node;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

public class PrescriptionRequest extends Request implements IRequest {
    private String patientFName;
    private String patientLName;
    private String classification;
    private String prescriptionName;

    PrescriptionRequest(String reqID, String serviceType, String classification, String prescriptionName, String description, Employee requester, Node location, Calendar timeRequested, Calendar scheduledTimeStart, Calendar scheduledTimeEnd, Calendar actualTimeStart, Calendar actualTimeEnd, boolean hasBeenCompleted, boolean cancelled) {
        super(reqID, serviceType, description, requester, location, timeRequested, scheduledTimeStart, scheduledTimeEnd, actualTimeStart, actualTimeEnd, hasBeenCompleted, cancelled);
        this.patientFName = patientFName;
        this.patientLName = patientLName;
        this.classification = classification;
        this.prescriptionName = prescriptionName;
    }

    // getters
    public String getPatientFName() {
        return patientFName;
    }
    public String getPatientLName() {
        return patientLName;
    }
    public String getClassification() {
        return classification;
    }
    public String getPrescriptionName() {
        return prescriptionName;
    }

    // setters
    public void setPatientFName(String patientFName) {
        this.patientFName = patientFName;
    }
    public void setPatientLName(String patientLName) {
        this.patientLName = patientLName;
    }
    public void setClassification(String classification) {
        this.classification = classification;
    }
    public void setPrescriptionName() {
        this.prescriptionName = prescriptionName;
    }

    /**
     * Method will safely make a request checking that all conditions are met
     * and will add request to requester's list of sent requests
     *
     * @param allEmps
     * @param requester
     * @param type
     * @param classAndPresName
     * @param description
     * @param location
     * @param scheduledTimeStart
     * @param scheduledTimeEnd
     */
    @Override
    public Request makeSafeRequest(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> classAndPresName, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws IOException, MessagingException {

        //getting all other parameters needed to construct the PrescriptionRequest
        String reqID = generateReqID();
        Calendar timeRequested = Calendar.getInstance();

        if (IScheduler.isValidDate(scheduledTimeStart, scheduledTimeEnd)){
            // actualTimeStart and actualTimeEnd are set to be equal to scheduledTimeStart and scheduledTimeEnd respectively for now
            PrescriptionRequest r = new PrescriptionRequest(reqID, type, classAndPresName.get(2), classAndPresName.get(3), description, requester, location, timeRequested, scheduledTimeStart, scheduledTimeEnd, scheduledTimeStart, scheduledTimeEnd, false, false);

            Employee requestee = Employee.getRandAvailableEmp(allEmps, r, "PRE");

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
        String a = "PRE" + i++;
        return a;
    }

    /**
     * Retrieves prescriptions available for every kind of classification
     * @return
     */
    public static HashMap<String, LinkedList<String>> getPrescriptionByClassification() {
        HashMap<String, LinkedList<String>> hm = new HashMap<>();
        LinkedList<String> schedule1Drugs = new LinkedList<>();
        schedule1Drugs.add("Heroin");
        schedule1Drugs.add("Marijuana");
        schedule1Drugs.add("Methaqualone");
        schedule1Drugs.add("Peyote");

        LinkedList<String> schedule2Drugs = new LinkedList<>();
        schedule2Drugs.add("Oxycodone");
        schedule2Drugs.add("Fentanyl");
        schedule2Drugs.add("Dexedrine");
        schedule2Drugs.add("Adderall");
        schedule2Drugs.add("Ritalin");

        LinkedList<String> schedule3Drugs = new LinkedList<>();
        schedule3Drugs.add("Vicodin");
        schedule3Drugs.add("Tylenol with Codeine");
        schedule3Drugs.add("Ketamine");
        schedule3Drugs.add("Anabolic Steroids");
        schedule3Drugs.add("Testosterone");

        LinkedList<String> schedule4Drugs = new LinkedList<>();
        schedule4Drugs.add("Xanax");
        schedule4Drugs.add("Soma");
        schedule4Drugs.add("Darvon");
        schedule4Drugs.add("Valium");
        schedule4Drugs.add("Ativan");
        schedule4Drugs.add("Talwin");
        schedule4Drugs.add("Ambien");

        LinkedList<String> schedule5Drugs = new LinkedList<>();
        schedule5Drugs.add("Cough Medicine");
        schedule5Drugs.add("Lomotil");
        schedule5Drugs.add("Motofen");
        schedule5Drugs.add("Lyrica");
        schedule5Drugs.add("Parepectolin");

        hm.put("Schedule 1", schedule1Drugs);
        hm.put("Schedule 2", schedule2Drugs);
        hm.put("Schedule 3", schedule3Drugs);
        hm.put("Schedule 4", schedule4Drugs);
        hm.put("Schedule 5", schedule5Drugs);

        return hm;
    }
}
