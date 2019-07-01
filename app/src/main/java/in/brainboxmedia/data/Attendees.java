package in.brainboxmedia.data;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */

import android.support.annotation.Keep;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
@Keep
public class Attendees implements Serializable {

    private String amountPaid;
    private String eventId; // product name in payment params
    private String detail;
    private String merchantData;
    private String names;
    private String eventName;
    private String dates;
    private String times;
    private String venues;

    public Attendees() {
    }

    public Attendees(String amountPaid, String eventId, String details, String merchantData, String name, String eventName, String date, String time, String venue) {

        this.amountPaid = amountPaid;
        this.eventId = eventId;
        this.detail = details;
        this.merchantData = merchantData;
        this.names = name;
        this.eventName = eventName;
        this.dates = date;
        this.times = time;
        this.venues = venue;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getMerchantData() {
        return merchantData;
    }

    public void setMerchantData(String merchantData) {
        this.merchantData = merchantData;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String name) {
        this.names = name;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String date) {
        this.dates = date;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String time) {
        this.times = time;
    }

    public String getVenues() {
        return venues;
    }

    public void setVenues(String venue) {
        this.venues = venue;
    }

}
