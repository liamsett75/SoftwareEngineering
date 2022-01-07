package RequestFacade;

import Employee.*;
import Graph.Node;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Calendar;

import java.util.LinkedList;

public interface IRequest {
    Request makeSafeRequest(LinkedList<Employee> allEmps, Employee requester, String type, LinkedList<String> extraSpecifications, String description, Node location, Calendar scheduledTimeStart, Calendar scheduledTimeEnd) throws GeneralSecurityException, IOException, MessagingException;// {}
    String generateReqID();
    static void sendReqEmail(LinkedList<Employee> allEmps, Request r) {}
}
