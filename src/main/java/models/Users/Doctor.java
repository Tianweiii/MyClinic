package models.Users;

public class Doctor extends User {
    final private String specialization;

    public Doctor(String ID, String username, String password, String dateOfBirth, String gender, String role, String specialization) {
        super(ID, username, password, dateOfBirth, gender, role);
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + specialization;
    }

    public String getSpecialization() {
        return specialization;
    }
}
