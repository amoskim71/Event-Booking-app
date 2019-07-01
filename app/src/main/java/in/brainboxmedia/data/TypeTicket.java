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
public class TypeTicket {

    private String price;
    private String detail;
    private String header;
    private int numberOfTicket;
    private String headerNumberAndPrice;

    public TypeTicket() {

    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getheader() {
        return header;
    }

    public void setheader(String header) {
        this.header = header;
    }

    public int getNumberOfTicket() {
        return numberOfTicket;
    }

    public void setNumberOfTicket(int number) {
        this.numberOfTicket = number;
    }

    public String getHeaderNumberAndPrice() {
        return headerNumberAndPrice;
    }

    public void setHeaderNumberAndPrice(String headerNumberAndPrice) {
        this.headerNumberAndPrice = headerNumberAndPrice;
    }
}
