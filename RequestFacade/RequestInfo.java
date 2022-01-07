package RequestFacade;

//Class that turns all request fields into strings and booleans for input in database

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

public class RequestInfo extends RecursiveTreeObject<RequestInfo> {

    /*private String reqID;
    private String serviceType;
    private Employee requester; // the employee who requested the service
    private Node location;
    private Calendar timeRequested;
    private Calendar scheduledTimeStart;
    private Calendar scheduledTimeEnd;
    private Calendar actualTimeStart; // the time at which the request was started
    private Calendar actualTimeEnd; // the time at which the request was completed
    private boolean hasBeenCompleted;
    private boolean cancelled;
    private Employee completedByWho;
    private String description;
     */

    private StringProperty reqID;
    private StringProperty serviceType;
    private StringProperty requesterID; // the employee who requested the service
    private StringProperty locationName;
    private StringProperty timeRequested;
    private StringProperty scheduledTimeStart;
    private StringProperty scheduledTimeEnd;
    private StringProperty actualTimeStart; // the time at which the request was started
    private StringProperty actualTimeEnd; // the time at which the request was completed
    private boolean hasBeenCompleted;
    private boolean cancelled;
    private StringProperty completedByWhoID;
    private StringProperty description;

    public RequestInfo(String reqID, String serviceType, String description, String requesterID, String locationName, String timeRequested, String scheduledTimeStart, String scheduledTimeEnd, String actualTimeStart, String actualTimeEnd, boolean hasBeenCompleted, boolean cancelled) {
        this.reqID = new SimpleStringProperty(reqID);
        this.serviceType = new SimpleStringProperty(serviceType);
        this.description = new SimpleStringProperty(description);
        this.requesterID = new SimpleStringProperty(requesterID);
        this.locationName = new SimpleStringProperty(locationName);
        this.timeRequested = new SimpleStringProperty(timeRequested);
        this.scheduledTimeStart = new SimpleStringProperty(scheduledTimeStart);
        this.scheduledTimeEnd = new SimpleStringProperty(scheduledTimeEnd);
        this.actualTimeStart = new SimpleStringProperty(actualTimeStart);
        this.actualTimeEnd = new SimpleStringProperty(actualTimeEnd);
        this.hasBeenCompleted = hasBeenCompleted;
        this.cancelled = cancelled;
        completedByWhoID = new SimpleStringProperty("");
    }

    public RequestInfo() {
        this.reqID = new SimpleStringProperty("");
        this.serviceType = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
        this.requesterID = new SimpleStringProperty("");
        this.locationName = new SimpleStringProperty("");
        this.timeRequested = new SimpleStringProperty("");
        this.scheduledTimeStart = new SimpleStringProperty("");
        this.scheduledTimeEnd = new SimpleStringProperty("");
        this.actualTimeStart = new SimpleStringProperty("");
        this.actualTimeEnd = new SimpleStringProperty("");
        this.hasBeenCompleted = false;
        this.cancelled = false;
        completedByWhoID = new SimpleStringProperty("");
    }

    //--------------------------------------------------------

    public ObservableValue<String> getReqID() {
        return reqID;
    }

    public void setReqID(String reqID) {
        this.reqID = new SimpleStringProperty(reqID);
    }

    public ObservableValue<String> getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = new SimpleStringProperty(serviceType);
    }

    public ObservableValue<String> getRequesterID() {
        return requesterID;
    }

    public void setRequesterID(String requesterID) {
        this.requesterID = new SimpleStringProperty(requesterID);
    }

    public ObservableValue<String> getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = new SimpleStringProperty(locationName);
    }

    public ObservableValue<String> getTimeRequested() {
        return timeRequested;
    }

    public void setTimeRequested(String timeRequested) {
        this.timeRequested = new SimpleStringProperty(timeRequested);
    }

    public ObservableValue<String> getScheduledTimeStart() {
        return scheduledTimeStart;
    }

    public void setScheduledTimeStart(String scheduledTimeStart) {
        this.scheduledTimeStart = new SimpleStringProperty(scheduledTimeStart);
    }

    public ObservableValue<String> getScheduledTimeEnd() {
        return scheduledTimeEnd;
    }

    public void setScheduledTimeEnd(String scheduledTimeEnd) {
        this.scheduledTimeEnd = new SimpleStringProperty(scheduledTimeEnd);
    }

    public ObservableValue<String> getActualTimeStart() {
        return actualTimeStart;
    }

    public void setActualTimeStart(String actualTimeStart) {
        this.actualTimeStart = new SimpleStringProperty(actualTimeStart);
    }

    public ObservableValue<String> getActualTimeEnd() {
        return actualTimeEnd;
    }

    public void setActualTimeEnd(String actualTimeEnd) {
        this.actualTimeEnd = new SimpleStringProperty(actualTimeEnd);
    }

    public boolean isHasBeenCompleted() {
        return hasBeenCompleted;
    }

    public void setHasBeenCompleted(boolean hasBeenCompleted) {
        this.hasBeenCompleted = hasBeenCompleted;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public ObservableValue<String> getCompletedByWhoID() {
        return completedByWhoID;
    }

    public void setCompletedByWhoID(String completedByWhoID) {
        this.completedByWhoID = new SimpleStringProperty(completedByWhoID);
    }

    public ObservableValue<String> getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = new SimpleStringProperty(description);
    }

 //--------------------------------------------------------------------------


    /**
     *
     * @param r
     * @return ri
     */
    public static RequestInfo requestToInfo(Request r)
    {
        RequestInfo ri = new RequestInfo();

        ri.setReqID(r.getReqID());
        ri.setServiceType(r.getServiceType());
        ri.setRequesterID(r.getRequester().getEmpID());
        ri.setLocationName(r.getLocation().getShortName());
        ri.setTimeRequested(r.getTimeRequested().getTime().toString());

        if(r.getScheduledTimeStart() != null)
            ri.setScheduledTimeStart(r.getScheduledTimeStart().getTime().toString());
        else
            ri.setScheduledTimeStart("");

        if(r.getScheduledTimeEnd() != null)
            ri.setScheduledTimeEnd(r.getScheduledTimeEnd().getTime().toString());
        else
            ri.setScheduledTimeStart("");


        if(r.getActualTimeStart() != null)
            ri.setActualTimeStart(r.getActualTimeStart().getTime().toString());
        else
            ri.setScheduledTimeStart("");


        if(r.getActualTimeEnd() != null)
            ri.setActualTimeEnd(r.getActualTimeEnd().getTime().toString());
        else
            ri.setScheduledTimeStart("");


        ri.setHasBeenCompleted(r.getHasBeenCompleted());
        ri.setCancelled(r.getCancelled());

        if(r.getCompletedByWho() != null)
            ri.setCompletedByWhoID(r.getCompletedByWho().getFirstName() + " " + r.getCompletedByWho().getLastName());
        else
            ri.setCompletedByWhoID("");

        ri.setDescription(r.getDescription());

        return ri;
    }



}
