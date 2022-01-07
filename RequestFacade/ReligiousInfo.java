package RequestFacade;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

public class ReligiousInfo extends RecursiveTreeObject<ReligiousInfo> {

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

    private StringProperty patientFName;
    private StringProperty patientLName;
    private StringProperty religion;
    private StringProperty religiousActivity;

    public ReligiousInfo(String reqID, String serviceType, String sanType, String description, String requesterID, String locationName, String timeRequested, String scheduledTimeStart, String scheduledTimeEnd, String actualTimeStart, String actualTimeEnd, boolean hasBeenCompleted, boolean cancelled, String patientFName, String patientLName, String religion, String religiousActivity) {
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
        this.completedByWhoID = new SimpleStringProperty("");

        this.patientFName = new SimpleStringProperty(patientFName);
        this.patientLName = new SimpleStringProperty(patientLName);
        this.religion = new SimpleStringProperty(religion);
        this.religiousActivity = new SimpleStringProperty(religiousActivity);

    }

    public ReligiousInfo() {
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


        this.patientFName = new SimpleStringProperty("");
        this.patientLName = new SimpleStringProperty("");
        this.religion = new SimpleStringProperty("");
        this.religiousActivity = new SimpleStringProperty("");

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

    public ObservableValue<String> getPatientFName() {
        return patientFName;
    }

    public void setPatientFName(String patientFName) {
        this.patientFName.set(patientFName);
    }

    public ObservableValue<String> getPatientLName() {
       return patientLName;
    }

    public void setPatientLName(String patientLName) {
        this.patientLName.set(patientLName);
    }

    public ObservableValue<String> getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion.set(religion);
    }

    public ObservableValue<String> getReligiousActivity() {
        return religiousActivity;
    }

    public void setReligiousActivity(String religiousActivity) {
        this.religiousActivity.set(religiousActivity);
    }

    public static ReligiousInfo requestToInfo(ReligiousRequest rr)
    {
        ReligiousInfo ri = new ReligiousInfo();

        ri.setReqID(rr.getReqID());
        ri.setServiceType(rr.getServiceType());
        ri.setRequesterID(rr.getRequester().getEmpID());
        ri.setLocationName(rr.getLocation().getShortName());
        ri.setTimeRequested(rr.getTimeRequested().getTime().toString());

        if(rr.getScheduledTimeStart() != null)
            ri.setScheduledTimeStart(rr.getScheduledTimeStart().getTime().toString());

        else
            ri.setScheduledTimeStart("");

        if(rr.getScheduledTimeEnd() != null)
            ri.setScheduledTimeEnd(rr.getScheduledTimeEnd().getTime().toString());
        else
            ri.setScheduledTimeStart("");


        if(rr.getActualTimeStart() != null)
            ri.setActualTimeStart(rr.getActualTimeStart().getTime().toString());
        else
            ri.setScheduledTimeStart("");


        if(rr.getActualTimeEnd() != null)
            ri.setActualTimeEnd(rr.getActualTimeEnd().getTime().toString());
        else
            ri.setScheduledTimeStart("");


        ri.setHasBeenCompleted(rr.getHasBeenCompleted());
        ri.setCancelled(rr.getCancelled());

        if(rr.getCompletedByWho() != null)
            ri.setCompletedByWhoID(rr.getCompletedByWho().getFirstName() + " " + rr.getCompletedByWho().getLastName());
        else
            ri.setCompletedByWhoID("");

        ri.setPatientFName(rr.getPatientFName());
        ri.setPatientLName(rr.getPatientLName());
        ri.setReligion(rr.getReligion());
        ri.setReligiousActivity(rr.getReligiousActivity());


        ri.setDescription(rr.getDescription());

        return ri;

    }


}
