package com.saiyanstudio.weathergod.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saiyanstudio.weathergod.R;
import com.saiyanstudio.weathergod.model.Location;

import java.util.List;

/**
 * Created by root on 23/5/15.
 */
public class LocationListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Location> locationData;
    private String theme,themeColor;

    RelativeLayout relativeLayout;
    TextView locationLabel;
    TextView countryLabel;

    public LocationListAdapter(Activity activity,List<Location> locationData,String theme,String themeColor){
        this.activity = activity;
        this.locationData = locationData;
        this.theme = theme;
        this.themeColor = themeColor;
    }

    @Override
    public int getCount() {
        return locationData.size();
    }

    @Override
    public Object getItem(int position) {
        return locationData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
            convertView = inflater.inflate(R.layout.location_list_item,null);

        locationLabel = (TextView) convertView.findViewById(R.id.locationLabel);
        countryLabel = (TextView) convertView.findViewById(R.id.countryLabel);

        if(theme.compareTo("dark") == 0){
            setThemeToDark(convertView);
        }else{
            setThemeToLight(convertView);
        }

        setThemeColor();

        Location loc = locationData.get(position);

        locationLabel.setText(loc.getLocationName() + "");
        countryLabel.setText(loc.getCountryName() + "");

        return convertView;
    }

    private void setThemeColor() {
        Resources res = activity.getResources();

        if(themeColor.compareTo("orange") == 0){
            countryLabel.setTextColor(res.getColorStateList(R.color.theme_orange));
        }else if(themeColor.compareTo("blue") == 0){
            countryLabel.setTextColor(res.getColorStateList(R.color.theme_blue));

        }else if(themeColor.compareTo("green") == 0){
            countryLabel.setTextColor(res.getColorStateList(R.color.theme_green));

        }else if(themeColor.compareTo("purple") == 0){
            countryLabel.setTextColor(res.getColorStateList(R.color.theme_purple));

        }else if(themeColor.compareTo("pink") == 0){
            countryLabel.setTextColor(res.getColorStateList(R.color.theme_pink));

        }else if(themeColor.compareTo("indigo") == 0){
            countryLabel.setTextColor(res.getColorStateList(R.color.theme_indigo));

        }else if(themeColor.compareTo("yellow") == 0){
            countryLabel.setTextColor(res.getColorStateList(R.color.theme_yellow));

        }else if(themeColor.compareTo("red") == 0){
            countryLabel.setTextColor(res.getColorStateList(R.color.theme_red));

        }else if(themeColor.compareTo("grey") == 0){
            countryLabel.setTextColor(res.getColorStateList(R.color.theme_grey));

        }

    }

    private void setThemeToDark(View convertView){
        RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relativeLayout);
        relativeLayout.setBackgroundColor(Color.BLACK);

        locationLabel.setTextColor(convertView.getResources().getColorStateList(R.color.dark_bright_text_color));
    }

    private void setThemeToLight(View convertView){
        relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relativeLayout);
        relativeLayout.setBackgroundColor(Color.WHITE);
        locationLabel.setTextColor(convertView.getResources().getColorStateList(R.color.light_bright_text_color));

    }
}
