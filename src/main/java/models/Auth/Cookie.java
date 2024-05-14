package models.Auth;

import models.Users.Admin;
import models.Users.Doctor;
import models.Users.Patient;

public class Cookie {
    public static Patient identityPatient;
    public static Admin identityAdmin;
    public static Doctor identityDoctor;

    public static void setCookie(Patient userInfo) {
        identityPatient = userInfo;
    }

    public static void setCookie(Admin userInfo) {
        identityAdmin = userInfo;
    }

    public static void setCookie(Doctor userInfo) {
        identityDoctor = userInfo;
    }
}
