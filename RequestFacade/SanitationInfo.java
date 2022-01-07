package RequestFacade;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

public class SanitationInfo extends RecursiveTreeObject<SanitationInfo> {

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
    private StringProperty sanType;
    private boolean biohazard;

    public SanitationInfo(String reqID, String serviceType, String sanType, String description, String requesterID, String locationName, String timeRequested, String scheduledTimeStart, String scheduledTimeEnd, String actualTimeStart, String actualTimeEnd, boolean hasBeenCompleted, boolean cancelled, boolean biohazard) {
        this.reqID = new SimpleStringProperty(reqID);
        this.serviceType = new SimpleStringProperty(serviceType);
        this.sanType = new SimpleStringProperty(sanType);
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
        this.biohazard = biohazard;
        completedByWhoID = new SimpleStringProperty("");
    }

    public SanitationInfo() {
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
        this.biohazard = false;
        completedByWhoID = new SimpleStringProperty("");
        sanType = new SimpleStringProperty("");
    }


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

    public boolean isBiohazard() {
        return biohazard;
    }

    public void setBiohazard(boolean biohazard) {
        this.biohazard = biohazard;
    }

    public ObservableValue<String> getSanType() {
        return sanType;
    }

    public void setSanType(String sanType) {
        this.sanType = new SimpleStringProperty(sanType);
    }

    public static SanitationInfo requestToInfo(SanitationRequest sr)
    {
        SanitationInfo si = new SanitationInfo();

        si.setReqID(sr.getReqID());
        si.setServiceType(sr.getServiceType());
        si.setRequesterID(sr.getRequester().getEmpID());
        si.setLocationName(sr.getLocation().getShortName());
        si.setTimeRequested(sr.getTimeRequested().getTime().toString());
        si.setSanType(sr.getSanType());

        if(sr.getScheduledTimeStart() != null)
            si.setScheduledTimeStart(sr.getScheduledTimeStart().getTime().toString());

        else
            si.setScheduledTimeStart("");

        if(sr.getScheduledTimeEnd() != null)
            si.setScheduledTimeEnd(sr.getScheduledTimeEnd().getTime().toString());
        else
            si.setScheduledTimeStart("");


        if(sr.getActualTimeStart() != null)
            si.setActualTimeStart(sr.getActualTimeStart().getTime().toString());
        else
            si.setScheduledTimeStart("");


        if(sr.getActualTimeEnd() != null)
            si.setActualTimeEnd(sr.getActualTimeEnd().getTime().toString());
        else
            si.setScheduledTimeStart("");


        si.setHasBeenCompleted(sr.getHasBeenCompleted());
        si.setCancelled(sr.getCancelled());

        if(sr.getCompletedByWho() != null)
            si.setCompletedByWhoID(sr.getCompletedByWho().getFirstName() + " " + sr.getCompletedByWho().getLastName());
        else
            si.setCompletedByWhoID("");

        si.setDescription(sr.getDescription());
        boolean tempBH = sr.getIsBiohazard();
//        System.out.println("biohazard value of req id " + sr.getReqID() + " is " + tempBH);
        si.setBiohazard(tempBH);
//        System.out.println("after being set bh val is " + si.isBiohazard());

        return si;

    }


}
