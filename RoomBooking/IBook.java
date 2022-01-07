package RoomBooking;

import Employee.Employee;
import Graph.Node;

import java.util.Calendar;

public interface IBook {
    boolean makeSafeBooking(Employee requester, Node location, String description, Calendar scheduledTimeStart, Calendar scheduledTimeEnd);

    String generateID();

    //void updateBooking();
}
