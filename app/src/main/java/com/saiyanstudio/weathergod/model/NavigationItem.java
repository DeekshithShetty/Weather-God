package com.saiyanstudio.weathergod.model;

import com.saiyanstudio.weathergod.R;

/**
 * Created by root on 27/5/15.
 */
public class NavigationItem {
    private String mIcon;
    private String itemName;

    public NavigationItem(String mIcon,String itemName){
        this.mIcon = mIcon;
        this.itemName = itemName;
    }

    public int getmIconId() {
        int iconId = R.drawable.refresh_icon;

        if (mIcon.equals("daily_forecast")) {
            iconId = R.drawable.week_icon;
        } else if (mIcon.equals("my_location")) {
            iconId = R.drawable.location_icon;
        } else if (mIcon.equals("about")) {
            iconId = R.drawable.about;
        } else if (mIcon.equals("contact_me")) {
            iconId = R.drawable.contact_me;
        } else if (mIcon.equals("settings")) {
            iconId = R.drawable.settings;
        }else if (mIcon.equals("rate_me")) {
            iconId = R.drawable.rate_me;
        }else if (mIcon.equals("gps")) {
            iconId = R.drawable.gps_icon;
        }

        return iconId;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
