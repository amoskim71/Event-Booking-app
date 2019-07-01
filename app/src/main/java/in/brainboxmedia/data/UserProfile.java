package in.brainboxmedia.data;

import android.support.annotation.Keep;

import com.google.firebase.database.IgnoreExtraProperties;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */
@IgnoreExtraProperties
@Keep
public class UserProfile {

    private String fname;
    private String lname;
    private String email;
    private String phone;
    private String gender;

    public UserProfile() {
    }

    public UserProfile(String fname, String lname, String email, String phone, String gender) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
