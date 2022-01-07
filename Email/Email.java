/**
 * Class responsible for making pre made emails
 */

package Email;

import Databases.EmployeeDB;
import Employee.Employee;
import UIControllers.MainScreenController;
import com.google.api.services.gmail.Gmail;
import Values.AppValues;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;

import static Email.EmailSetup.getService;
import static Email.SendEmail.sendMessage;

public class Email {
    private static MimeMessage message = null;
    private static Gmail service = null;
    private static String curEmpID = null;
    private static Message.RecipientType TO = javax.mail.Message.RecipientType.TO;

    private static boolean falseSetup = (message == null) || (service == null) || (curEmpID == null);

    public Email() throws GeneralSecurityException, IOException, MessagingException {
        message = EmailSetup.messageSetup();
        service = getService();
        falseSetup = (message == null) || (service == null) || (curEmpID == null);

        System.out.println("false set up is " + falseSetup);
    }

    public static void setCurEmpID() {
        System.out.println("Email setCurEmpID: " + MainScreenController.getCurEmployee().getEmpID());
        curEmpID = AppValues.getInstance().curEmp.getEmpID();
        falseSetup = (message == null) || (service == null) || (curEmpID == null);

        System.out.println("false set up is " + falseSetup);
    }


    /**
     * Sends A* Directions to current user
     *
     * @param path A* Path in String format
     * @param startName Start Node name
     * @param endName End Node name
     * @throws MessagingException
     * @throws IOException
     */
    public static void sendDirections(String path, String startName, String endName) {
        System.out.println("Employee Notifications: " + EmployeeDB.getNotifications(curEmpID));

        if(EmployeeDB.getNotifications(curEmpID).equals("Y") && !falseSetup) {
            try {
                setEmailAddress(EmployeeDB.getEmailDB(curEmpID));
                message.setSubject("Directions from " + startName + " to " + endName);

                String bodyText = "Hello, \n\nThis is an automated message giving you the path from " + startName + " to " + endName + "\n\n" + path;

                bodyText += EmailSetup.getSignature();

                message.setContent(bodyText, "text/plain");

                sendMessage(service, "aquaamaroks@gmail.com", message);
            }
            catch (MessagingException | IOException e) {
//                System.out.println("Error");
            }
        }
        else {
//            System.out.println("No email sent due to user preferences");
        }
    }


    /**
     * Sends a confirmation email to the person who requested the service
     *
     * @param requestedService in String format
     * @param location where the request is needed
     * @throws MessagingException
     * @throws IOException
     */
    public static void sendServiceRequester(String requestedService, String location){
        if(EmployeeDB.getNotifications(curEmpID).equals("Y") && !falseSetup) {
            try {
                setEmailAddress(EmployeeDB.getEmailDB(curEmpID));

                message.setSubject("Your Service Request");

                String bodyText = "Hello,\n\nYou have requested " + requestedService + " to " + location + " at " + EmailSetup.getTime();

                bodyText += EmailSetup.getSignature();

                message.setContent(bodyText, "text/plain");

                sendMessage(service, "aquaamaroks@gmail.com", message);
            }
            catch (IOException | MessagingException e) {
//                System.out.println("Error");
            }

        }
        else {
//            System.out.println("No email sent due to user preferences");
        }

    }

    /**
     * Sends an email to the employee assigned to fulfilling the request
     *
     * @param destination where the request is needed
     * @param serviceType type of service in String format
     * @param emp the employee fulfilling the request
     * @throws MessagingException
     * @throws IOException
     */
    public static void sendServiceRequestee(String destination, String serviceType, Employee emp) {
        setCurEmpID();
        System.out.println("CUR EMP IS IS " +curEmpID );
        //should i have chnages this from cur emp
        if(EmployeeDB.getNotifications(curEmpID).equals("Y") && !falseSetup) {
            try {
                setEmailAddress(EmployeeDB.getEmailDB(emp.getEmpID()));

                message.setSubject("Service Requested");

                String bodyText = "Hello, \n\nYour service has been requested at " + destination + " for " + serviceType;

                bodyText += EmailSetup.getSignature();

                message.setContent(bodyText, "text/plain");

                sendMessage(service, "aquaamaroks@gmail.com", message);
            }
            catch (IOException | MessagingException e) {
//                System.out.println("Error");
            }
        }
        else {
//            System.out.println("No email sent due to user preferences");
        }
    }

    /**
     * Clears the currently stored email addresses and applies the one email address
     *
     * @param emailAddress
     * @throws MessagingException
     */
    private static void setEmailAddress(String emailAddress) throws MessagingException {
        message.setRecipients(TO, (Address[]) null);
        message.addRecipients(TO, emailAddress);
    }

}
