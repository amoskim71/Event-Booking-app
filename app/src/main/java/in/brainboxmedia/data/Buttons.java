package in.brainboxmedia.data;

import android.support.annotation.Keep;
/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */
@Keep
public class Buttons {
    private String mButtonName;
    private int mButtonBackground;

    public Buttons() {

    }

    public Buttons(String buttonName, int buttonBackground) {
        mButtonBackground = buttonBackground;
        mButtonName = buttonName;
    }

    public String getButtonName() {
        return mButtonName;
    }

    public void setButtonName(String buttonName) {
        mButtonName = buttonName;
    }

    public int getButtonBackground() {
        return mButtonBackground;
    }

    public void setButtonBackground(int buttonBackground) {
        mButtonBackground = buttonBackground;
    }
}