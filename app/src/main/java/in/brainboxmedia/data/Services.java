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
public class Services {
    private String corporate;
    private String list_services;
    private String manage;
    private String offers;
    private String service;

    public String getCorporate() {
        return corporate;
    }

    public void setCorporate(String corporate) {
        this.corporate = corporate;
    }

    public String getlist_services() {
        return list_services;
    }

    public void setlist_services(String list_services) {
        this.list_services = list_services;
    }

    public String getManage() {
        return manage;
    }

    public void setManage(String manage) {
        this.manage = manage;
    }

    public String getOffers() {
        return offers;
    }

    public void setOffers(String offers) {
        this.offers = offers;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
