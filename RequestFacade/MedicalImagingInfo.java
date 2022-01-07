package RequestFacade;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

import java.util.Calendar;

public class MedicalImagingInfo extends RecursiveTreeObject<MedicalImagingInfo> implements Comparable<MedicalImagingInfo> {

    private StringProperty fName;
    private StringProperty lName;
    private StringProperty machineType;
    private StringProperty bodyPart;
    private StringProperty timeRequested;
    private StringProperty description;
    //make actual time here
    private boolean emergency;
    private Calendar timeRequestedCalender;
    private String value;

    public MedicalImagingInfo(String fName, String lName, String machineType, String bodyPart, String timeRequested, String description, boolean emergency, Calendar timeRequestedCalender) {
        this.fName = new SimpleStringProperty(fName);
        this.lName = new SimpleStringProperty(lName);
        this.machineType = new SimpleStringProperty(machineType);
        this.bodyPart = new SimpleStringProperty(bodyPart);
        this.timeRequested = new SimpleStringProperty(timeRequested);
        this.description = new SimpleStringProperty(description);
        this.timeRequestedCalender = timeRequestedCalender;
        this.emergency = emergency;
        setValue();

//        System.out.println("finsied set value");
    }

    public MedicalImagingInfo() {
        this.fName = new SimpleStringProperty("");
        this.lName = new SimpleStringProperty("");
        this.machineType = new SimpleStringProperty("");
        this.bodyPart = new SimpleStringProperty("");
        this.timeRequested = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
        this.timeRequestedCalender = null;
        value = "";
    }

    public ObservableValue<String> getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = new SimpleStringProperty(fName);
    }

    public ObservableValue<String> getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = new SimpleStringProperty(lName);
    }

    public ObservableValue<String> getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = new SimpleStringProperty(machineType);
    }

    public ObservableValue<String> getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = new SimpleStringProperty(bodyPart);
    }

    public ObservableValue<String> getTimeRequested() {
        return timeRequested;
    }

    public void setTimeRequested(String timeRequested) {
        this.timeRequested = new SimpleStringProperty(timeRequested);;
    }

    public ObservableValue<String> getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = new SimpleStringProperty(description);
    }

    public String getValue() {
        return value;
    }

    public void setValue() {
        String emergencyVal, bodyVal, timeVal;
        String total = "";

        if(this.getEmergency())
            emergencyVal = "a";
        else
            emergencyVal = "b";


//        System.out.println("body part is " + this.getBodyPart().toString());

//        System.out.println("string value of body part is " + this.getBodyPart().getValue());

        if(this.getBodyPart().getValue().equals("Head"))
            bodyVal = "c";
        else if(this.getBodyPart().getValue().equals("Torso"))
            bodyVal = "d";
        else if(this.getBodyPart().getValue().equals("Leg"))
            bodyVal = "e";
        else if(this.getBodyPart().getValue().equals("Arm"))
            bodyVal = "f";
        else
            bodyVal = "z";

        total = emergencyVal + bodyVal;

        this.value = total;

//        System.out.println("set value to " + total);

    }

    public boolean getEmergency() {
        return emergency;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }

    public Calendar getTimeRequestedCalender() {
        return timeRequestedCalender;
    }

    public void setTimeRequestedCalender(Calendar timeRequestedCalender) {
        this.timeRequestedCalender = timeRequestedCalender;
    }


    public static MedicalImagingInfo medicalImagingToInfo(MedicalImagingRequest r)
    {
        MedicalImagingInfo mi = new MedicalImagingInfo();

        mi.setfName(r.getfName());
        mi.setlName(r.getlName());
        mi.setMachineType(r.getMachineType());
        mi.setBodyPart(r.getBodyPart());
        mi.setTimeRequested(r.getTimeRequested().getTime().toString());
        mi.setDescription(r.getDescription());
        mi.setEmergency(r.getIsEmergency());
        mi.setTimeRequestedCalender(r.getTimeRequested());
       mi.setValue();

        return mi;
    }

    @Override
    public int compareTo(MedicalImagingInfo m) {
        return this.getValue().compareTo(m.getValue());
    }

}
