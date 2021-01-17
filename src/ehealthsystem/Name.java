package ehealthsystem;

import ehealthsystem.model.Patient;
import ehealthsystem.model.Doctor;

public class Name {

    public String setName(Patient patient){
        return patient.firstName + " " + patient.lastName;
    }

    public String setName(Doctor doctor){
        return doctor.firstName + " " + doctor.lastName;
    }
}
