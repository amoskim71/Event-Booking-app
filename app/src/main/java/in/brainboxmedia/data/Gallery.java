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
public class Gallery implements Serializable {
    private String timestamp;
    private String url_large;
    private String url_medium;
    private String url_small;
    private String names;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl_large() {
        return url_large;
    }

    public void setUrl_large(String url_large) {
        this.url_large = url_large;
    }

    public String getUrl_medium() {
        return url_medium;
    }

    public void setUrl_medium(String url_medium) {
        this.url_medium = url_medium;
    }

    public String getUrl_small() {
        return url_small;
    }

    public void setUrl_small(String url_small) {
        this.url_small = url_small;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String name) {
        this.names = name;
    }
}
