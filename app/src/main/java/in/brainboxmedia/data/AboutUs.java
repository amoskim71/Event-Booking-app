package in.brainboxmedia.data;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */

import android.support.annotation.Keep;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
@Keep
public class AboutUs {

    private String facebook;
    private String google;
    private String twitter;
    private String about_us;
    private String cover;
    private String email;
    private String phone;
    private String website_link;

    public AboutUs() {

    }

    public AboutUs(String facebook, String google, String twitter, String about_us, String cover, String email, String phone, String website_link) {
        this.facebook = facebook;
        this.google = google;
        this.about_us = about_us;
        this.twitter = twitter;
        this.cover = cover;
        this.email = email;
        this.phone = phone;
        this.website_link = website_link;
    }

    public String getAbout_us() {
        return about_us;
    }

    public void setAbout_us(String about_us) {
        this.about_us = about_us;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public String getWebsite_link() {
        return website_link;
    }

    public void setWebsite_link(String website_link) {
        this.website_link = website_link;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getGoogle() {
        return google;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
}
