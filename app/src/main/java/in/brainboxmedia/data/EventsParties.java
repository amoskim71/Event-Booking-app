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
public class EventsParties {

    private String name;
    private String place;
    private String party_poster;
    private String desscription;
    private String time;
    private String date;
    private String available;
    private String latitude;
    private String longitude;

    public EventsParties() {

    }

    public EventsParties(String available, String name, String place, String party_poster, String desscription, String time, String date, String latitude, String longitude) {
        this.name = name;
        this.place = place;
        this.party_poster = party_poster;
        this.desscription = desscription;
        this.time = time;
        this.date = date;
        this.available = available;
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getParty_poster() {
        return party_poster;
    }

    public void setParty_poster(String party_poster) {
        this.party_poster = party_poster;
    }

    public String getDesscription() {
        return desscription;
    }

    public void setDesscription(String desscription) {
        this.desscription = desscription;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
