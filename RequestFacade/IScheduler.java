package RequestFacade;

import java.time.LocalDateTime;
import java.util.Calendar;

import static java.util.TimeZone.getTimeZone;

public interface IScheduler {

    /**
     *
     * @param scheduledStartDate
     * @param scheduledEndDate
     * @param requestedStartDate
     * @param requestedEndDate
     * @return
     */
    static boolean isAvailable(Calendar scheduledStartDate, Calendar scheduledEndDate, Calendar requestedStartDate, Calendar requestedEndDate) {
        //this function assumes that scheduling times have been checked for validity before being input in this function

        if (requestedStartDate==null || requestedEndDate == null) return true;
//        System.out.println("scheduled start time: " + scheduledStartDate.getTime().toString());
//        System.out.println("scheduled end time: " + scheduledEndDate.getTime().toString());
//        System.out.println("entry start time: " + requestedStartDate.getTime().toString());
//        System.out.println("entry end time: " + requestedEndDate.getTime().toString());
//        System.out.println("-------------------------IScheduler.isAvailable is running-------------------- ");
        //creating condition variables for room to be unavailable
        boolean condition1, condition2;

        //room will be available if:
        //room is requested up until the time room was already booked for
        condition1 = requestedEndDate.before(scheduledStartDate) || requestedEndDate.equals(scheduledStartDate);

//        System.out.println("request before already scheduled request?: " + condition1);
        //room is requested starting from time when room finishes being booked
        condition2 = requestedStartDate.after(scheduledEndDate) || requestedStartDate.equals(scheduledEndDate);
//        System.out.println("request after already scheduled request?: " + condition2);
//        System.out.println("room/employee already booked?: " + (condition1||condition2));
//        System.out.println("--------------------------IScheduler.isAvailable stopped running----------------");
        if(condition1==false && condition2 == false){
//            System.out.println("both conditions false -- IScheduler -- isAvailable");
        }
        return (condition1 || condition2);
    }


    static boolean isValidDate(Calendar startDate, Calendar endDate){

        //if (startDate!= null) { System.out.println("start date is: " + startDate.getTime()); }
        //if (endDate!= null) { System.out.println("end date is: " + endDate.getTime()); }
//        System.out.println("--------------------------------IScheduler.isValidDate running-------------------");
        if (startDate != null && endDate != null){

            //don't want to make a request or booking on a date that's already passed
            //if (startDate.before(Calendar.getInstance(getTimeZone("America/New_York")))) return false;

            //checking all other forms of valid dates
            boolean b =  startDate.before(endDate);
//            System.out.println("start date before end date?: " + b);
            boolean c = startDate.equals(Calendar.getInstance()) || startDate.after(Calendar.getInstance(getTimeZone("America/New_York")));
//            System.out.println("start and right now are the saem or start after rn??: " + c);
            boolean e = b && c;
//            System.out.println("date is valid?: " + e);
//            System.out.println("--------------------IScheduler.isValidDate stopped running---------------------");
//            System.out.println();
            return e;
        }
//        System.out.println("date is valid");
//        System.out.println("--------------------IScheduler.isValidDate stopped running---------------------");
//        System.out.println();
        return true;
    }

    static boolean isAvailableLDT(LocalDateTime scheduledSDT, LocalDateTime scheduledEDT, LocalDateTime requestedSDT, LocalDateTime requestedEDT) {
        //this function assumes that scheduling times have been checked for validity before being input in this function

//        System.out.println("scheduled start time: " + scheduledStartDate.getTime().toString());
//        System.out.println("scheduled end time: " + scheduledEndDate.getTime().toString());
//        System.out.println("entry start time: " + requestedStartDate.getTime().toString());
//        System.out.println("entry end time: " + requestedEndDate.getTime().toString());
//        System.out.println("-------------------------IScheduler.isAvailable is running-------------------- ");
        //creating condition variables for room to be unavailable
        boolean condition1, condition2;

        //room will be available if:
        //room is requested up until the time room was already booked for
        condition1 = requestedEDT.compareTo(scheduledSDT) < 0 || requestedEDT.equals(scheduledSDT);

//        System.out.println("request before already scheduled request?: " + condition1);
        //room is requested starting from time when room finishes being booked
        condition2 = requestedSDT.compareTo(scheduledEDT) > 0 || requestedSDT.equals(scheduledEDT);
//        System.out.println("request after already scheduled request?: " + condition2);
//        System.out.println("room/employee already booked?: " + (condition1||condition2));
//        System.out.println("--------------------------IScheduler.isAvailable stopped running----------------");
        if (condition1 == false && condition2 == false) {
            System.out.println("both conditions false -- IScheduler -- isAvailableLDT");
        }
        return (condition1 || condition2);
    }

}
