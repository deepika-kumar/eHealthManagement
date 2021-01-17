package ehealthsystem.model;

public class Doctor {
    public String firstName;
    public String lastName;
    public String eMail;
    public String phone;
    public String gender;
    public String speciality;
    public String language;
    public String location;
    public String status;

    public Doctor() {

    }

    public Doctor(String firstName, String lastName, String eMail, String phone, String gender, String speciality, String language, String location, String status){
        this.firstName = new String(firstName);
        this.lastName = new String(lastName);
        this.eMail = new String(eMail);
        this.phone = new String(phone);
        this.gender = new String(gender);
        this.speciality = new String(speciality);
        this.language = new String(language);
        this.location = new String(location);
        this.status = new String(status);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
