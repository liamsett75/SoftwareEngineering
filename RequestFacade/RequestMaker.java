package RequestFacade;

import Employee.Employee;
import Graph.Node;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.LinkedList;


public class RequestMaker {
    private AudioVisualRequest audioVisualRequest = new AudioVisualRequest(null, null, null, null, null, null, null, null, null, null, null, null, null, false, false);
    private GiftShopRequest giftShopRequest = new GiftShopRequest(null, null, null, null, null, null, null, null, null, null, null, false, false, null, null, 0, null, 0);
    private InternalTransportRequest internalTransportRequest = new InternalTransportRequest(null, null, null, null, null, null, null, null, null, null, null, false, false);
    private InterpreterRequest interpreterRequest = new InterpreterRequest(null, null, null, null, null, null, null, null, null, null, null, false, false);
    private PatientInfoRequest patientInfoRequest = new PatientInfoRequest(null, null, null, null, null, null, null, null, null, null, false, false, null, null);
    private PrescriptionRequest prescriptionRequest = new PrescriptionRequest(null, null, null, null, null, null, null, null, null, null, null, null, false, false);
    private ReligiousRequest religiousRequest = new ReligiousRequest(null, null, null, null, null, null, null, null, null, null, null, null, null, null, false, false);
    private SanitationRequest sanitationRequest = new SanitationRequest(null, null, null, false,null, null, null, null, null, null, null, null, false, false);
    private CodeRequest codeRequest = new CodeRequest(null, null, null, null, null, null, null, false, false);
    private ITRequest itRequest = new ITRequest(null, null, null, null, null, null, null, null, null, null, null, false, false);
    private FloristRequest floristRequest = new FloristRequest(null, null, null, null, null, null, null, null, null, false, false);
    private ExternalTransportationRequest externalTransportRequest = new ExternalTransportationRequest(null, null, null, null, null, null, null, null, null, null, null, null, false, false);
    private MedicalImagingRequest imagingRequest= new MedicalImagingRequest(null, null, null, null, null, null, false, null, null, null, null, null, null, null, null, false, false);

    /**
     *
     * @param allEmps
     * @param requester
     * @param type
     * @param deviceSpecifications
     * @param description
     * @param location
     * @param scheduledTimeStart
     * @param scheduledTimeEnd
     * @return
     */

    public Request makeSafeAVReq(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> deviceSpecifications, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws GeneralSecurityException, IOException, MessagingException {
        return audioVisualRequest.makeSafeRequest(allEmps, requester, type, deviceSpecifications, description, location, scheduledTimeStart, scheduledTimeEnd);
    }

    /**
     *
     * @param allEmps
     * @param requester
     * @param type
     * @param giftType
     * @param description
     * @param location
     * @param scheduledTimeStart
     * @param scheduledTimeEnd
     * @return
     */
    public Request makeSafeGifReq(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> giftType, String description,  Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws IOException, MessagingException {
        return giftShopRequest.makeSafeRequest(allEmps, requester, type, giftType, description, location, scheduledTimeStart, scheduledTimeEnd);
    }

    /**
     *
     * @param allEmps
     * @param requester
     * @param type
     * @param tranType
     * @param description
     * @param location
     * @param scheduledTimeStart
     * @param scheduledTimeEnd
     * @return
     */
    public Request  makeSafeIntTranReq(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> tranType, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws IOException, MessagingException {
        return internalTransportRequest.makeSafeRequest(allEmps, requester, type, tranType, description, location, scheduledTimeStart, scheduledTimeEnd);
    }

    /**
     *
     * @param allEmps
     * @param requester
     * @param type
     * @param tranType
     * @param description
     * @param location
     * @param scheduledTimeStart
     * @param scheduledTimeEnd
     * @return
     */
    public Request  makeSafeExtTranReq(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> tranType, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws IOException, MessagingException {
        return externalTransportRequest.makeSafeRequest(allEmps, requester, type, tranType, description, location, scheduledTimeStart, scheduledTimeEnd);
    }

    public Request makeSafeIntReq(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> lang, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws IOException, MessagingException {
        return interpreterRequest.makeSafeRequest(allEmps, requester, type, lang, description, location, scheduledTimeStart, scheduledTimeEnd);
    }

    /**
     *
     * @param allEmps
     * @param requester
     * @param type
     * @param rxType
     * @param description
     * @param location
     * @param scheduledTimeStart
     * @param scheduledTimeEnd
     * @return
     */
      public Request makeSafePreReq(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> rxType, String description,  Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws IOException, MessagingException {
          return prescriptionRequest.makeSafeRequest(allEmps, requester, type, rxType, description, location, scheduledTimeStart, scheduledTimeEnd);
      }

    /**
     *
     * @param allEmps
     * @param requester
     * @param type
     * @param attributes
     * @param description
     * @param location
     * @param scheduledTimeStart
     * @param scheduledTimeEnd
     * @return
     */
    public Request makeSafePatInfoReq(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> attributes, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws IOException, MessagingException {
        return patientInfoRequest.makeSafeRequest(allEmps, requester, type, attributes, description, location, scheduledTimeStart, scheduledTimeEnd);
    }

    /**
     *
     * @param allEmps
     * @param requester
     * @param type
     * @param religion
     * @param description
     * @param location
     * @param scheduledTimeStart
     * @param scheduledTimeEnd
     * @return
     */
    public Request makeSafeRelReq(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> religion, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws IOException, MessagingException {
       //

       //public Request makeSafeRequest(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> religionAndSpecs, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws IOException, MessagingException {
        return religiousRequest.makeSafeRequest(allEmps, requester, type, religion, description, location, scheduledTimeStart, scheduledTimeEnd);


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
    public Request makeSafeSanReq(LinkedList<Employee> allEmps, Employee requester, String type, boolean isBiohazard, LinkedList<String> sanType, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws GeneralSecurityException, IOException, MessagingException {
//        System.out.println("sanitation type is" + sanType);
//        System.out.println("biohazard value is " + isBiohazard);

        if(isBiohazard)
            sanType.add("T");
        else
            sanType.add("F");

        //makeSafeSanReq(LinkedList<Employee> allEmps, Employee requester, String type, boolean isBiohazard, LinkedList<String> sanType, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd)
         return sanitationRequest.makeSafeRequest(allEmps, requester, type, sanType, description, location, scheduledTimeStart, scheduledTimeEnd);
    }

    /**
     *
     * @param allEmps
     * @param requester
     * @param type
     * @param ITType
     * @param description
     * @param location
     * @param scheduledTimeStart
     * @param scheduledTimeEnd
     * @return
     */
    public Request makeSafeITReq(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> ITType, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws IOException, MessagingException {
        return itRequest.makeSafeRequest(allEmps, requester, type, ITType, description, location, scheduledTimeStart, scheduledTimeEnd);
    }

    /**
     *
     * @param allEmps
     * @param requester
     * @param type
     * @param extras
     * @param description
     * @param location
     * @return
     */
    public Request makeSafeCodeReq(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> extras, String description, Node location) throws IOException, MessagingException {
        return codeRequest.makeSafeRequest(allEmps, requester, type, extras, description, location, null, null);
    }

    /**
     *
     * @param allEmps
     * @param requester
     * @param type
     * @param bouquet
     * @param description
     * @param location
     * @param scheduledTimeStart
     * @param scheduledTimeEnd
     * @return
     */
    public Request makeSafeFlowerReq(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> bouquet, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) {
        return floristRequest.makeSafeRequest(allEmps, requester, type, bouquet, description, location, scheduledTimeStart, scheduledTimeEnd);
    }

    public Request makeSafeImagingReq(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> imagingList, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) {
        return imagingRequest.makeSafeRequest(allEmps, requester, type, imagingList, description, location, scheduledTimeStart, scheduledTimeEnd);
    }


}




