package ehealthsystem;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Hyperlink;

public class Appointment {
    private SimpleStringProperty doctorEmail;
    private SimpleStringProperty patientEmail;
    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty mobile;
    private SimpleStringProperty reason;
    private SimpleStringProperty appointmentDay;
    private SimpleStringProperty appointmentTime;
    private SimpleStringProperty status;
    private Hyperlink hyperlink;

    public Appointment(){

    }

    public Appointment(String firstName, String lastName, String patientEmail, String mobile, String reason, String appointmentDay, String appointmentTime,String status) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.patientEmail = new SimpleStringProperty(patientEmail);
        this.mobile = new SimpleStringProperty(mobile);
        this.reason = new SimpleStringProperty(reason);
        this.appointmentDay = new SimpleStringProperty(appointmentDay);
        this.appointmentTime = new SimpleStringProperty(appointmentTime);
        this.hyperlink = new Hyperlink(status);
    }

    public String getDoctorEmail() {
        return doctorEmail.get();
    }


    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail.set(doctorEmail);
    }

    public String getPatientEmail() {
        return patientEmail.get();
    }


    public void setPatientEmail(String patientEmail) {
        this.patientEmail.set(patientEmail);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getMobile() {
        return mobile.get();
    }

    public void setMobile(String mobile) {
        this.mobile.set(mobile);
    }

    public String getReason() {
        return reason.get();
    }

    public void setReason(String reason) {
        this.reason.set(reason);
    }

    public String getAppointmentDay() {
        return appointmentDay.get();
    }

    public void setAppointmentDay(String appointmentDay) {
        this.appointmentDay.set(appointmentDay);
    }

    public String getAppointmentTime() {
        return appointmentTime.get();
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime.set(appointmentTime);
    }

    public Hyperlink getHyperlink() {
        return hyperlink;
    }

    public void setHyperlink(String status) {
        this.hyperlink = new Hyperlink(status);
    }
}
