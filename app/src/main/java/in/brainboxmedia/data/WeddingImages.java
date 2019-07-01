package in.brainboxmedia.data;

import android.support.annotation.Keep;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */
@IgnoreExtraProperties
@Keep
public class WeddingImages implements Serializable {

    private String bottom;
    private String id;
    private String middle_description;
    private String top;
    private String url;

    public String getBottom() {
        return bottom;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMiddle_description() {
        return middle_description;
    }

    public void setMiddle_description(String middle_description) {
        this.middle_description = middle_description;
    }
}
