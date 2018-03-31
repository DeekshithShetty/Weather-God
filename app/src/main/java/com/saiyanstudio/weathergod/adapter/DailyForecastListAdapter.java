package com.saiyanstudio.weathergod.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saiyanstudio.weathergod.model.WeatherData;
import com.saiyanstudio.weathergod.R;

import java.util.List;

/**
 * Created by root on 20/5/15.
 */
public class DailyForecastListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<WeatherData> weatherData;
    private String theme,themeColor;

    TextView temparatureLabel;
    ImageView iconImageView;
    TextView localTimeTag;
    TextView summaryLabel;
    TextView humidityValue;
    TextView precipValue;

    public DailyForecastListAdapter(Activity activity,List<WeatherData> currentWeatherData,String theme,String themeColor){
        this.activity = activity;
        this.weatherData = currentWeatherData;
        this.theme = theme;
        this.themeColor = themeColor;
    }

    public void setThemeString(String theme){
        this.theme = theme;
    }

    @Override
    public int getCount() {
        return weatherData.size();
    }

    @Override
    public Object getItem(int position) {
        return weatherData.get(position);
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
            convertView = inflater.inflate(R.layout.daily_forecast_list_item,null);

        temparatureLabel = (TextView) convertView.findViewById(R.id.temperatureLabel);
        iconImageView = (ImageView) convertView.findViewById(R.id.iconImageView);
        localTimeTag = (TextView) convertView.findViewById(R.id.localTimeTag);
        summaryLabel = (TextView) convertView.findViewById(R.id.summaryLabel);
        humidityValue = (TextView) convertView.findViewById(R.id.humidityValue);
        precipValue = (TextView) convertView.findViewById(R.id.precipValue);

        if(theme.compareTo("dark") == 0){
            setThemeToDark(convertView);
        }else{
            setThemeToLight(convertView);
        }

        setThemeColor();

        WeatherData cwd = weatherData.get(position);

        Drawable drawable = activity.getResources().getDrawable(cwd.getIconId());

        temparatureLabel.setText(cwd.getTemperature() + "");
        if(position == 0)
            localTimeTag.setText("Today");
        else if(position == 1)
            localTimeTag.setText("Tomorrow");
        else
            localTimeTag.setText(cwd.getDayOfTheWeek());
        humidityValue.setText(cwd.getHumidity() + "");
        precipValue.setText(cwd.getPrecipChance() + "%");
        summaryLabel.setText(cwd.getSummary());
        iconImageView.setImageDrawable(drawable);

        return convertView;
    }

    private void setThemeColor() {
        Resources res = activity.getResources();
        int newColor = res.getColor(R.color.theme_orange);

        if(themeColor.compareTo("orange") == 0){
            newColor = res.getColor(R.color.theme_orange);
            summaryLabel.setTextColor(activity.getResources().getColorStateList(R.color.theme_orange));
        }else if(themeColor.compareTo("blue") == 0){
            newColor = res.getColor(R.color.theme_blue);
            summaryLabel.setTextColor(activity.getResources().getColorStateList(R.color.theme_blue));
        }else if(themeColor.compareTo("green") == 0){
            newColor = res.getColor(R.color.theme_green);
            summaryLabel.setTextColor(activity.getResources().getColorStateList(R.color.theme_green));
        }else if(themeColor.compareTo("purple") == 0){
            newColor = res.getColor(R.color.theme_purple);
            summaryLabel.setTextColor(activity.getResources().getColorStateList(R.color.theme_purple));
        }else if(themeColor.compareTo("pink") == 0){
            newColor = res.getColor(R.color.theme_pink);
            summaryLabel.setTextColor(activity.getResources().getColorStateList(R.color.theme_pink));
        }else if(themeColor.compareTo("indigo") == 0){
            newColor = res.getColor(R.color.theme_indigo);
            summaryLabel.setTextColor(activity.getResources().getColorStateList(R.color.theme_indigo));
        }else if(themeColor.compareTo("yellow") == 0){
            newColor = res.getColor(R.color.theme_yellow);
            summaryLabel.setTextColor(activity.getResources().getColorStateList(R.color.theme_yellow));
        }else if(themeColor.compareTo("red") == 0){
            newColor = res.getColor(R.color.theme_red);
            summaryLabel.setTextColor(activity.getResources().getColorStateList(R.color.theme_red));
        }else if(themeColor.compareTo("grey") == 0){
            newColor = res.getColor(R.color.theme_grey);
            summaryLabel.setTextColor(activity.getResources().getColorStateList(R.color.theme_grey));
        }
        iconImageView.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
    }

    private void setThemeToDark(View convertView){
        RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relativeLayout);
        relativeLayout.setBackgroundColor(Color.BLACK);
        //relativeLayout.setBackgroundResource(R.color.dark_background_color);
        temparatureLabel.setTextColor(convertView.getResources().getColorStateList(R.color.dark_bright_text_color));
        localTimeTag.setTextColor(convertView.getResources().getColorStateList(R.color.dark_medium_text_color));
        TextView hum = (TextView) convertView.findViewById(R.id.humidityLabel);
        hum.setTextColor(convertView.getResources().getColorStateList(R.color.dark_medium_text_color));
        TextView rain = (TextView) convertView.findViewById(R.id.precipLabel);
        rain.setTextColor(convertView.getResources().getColorStateList(R.color.dark_medium_text_color));
        humidityValue.setTextColor(convertView.getResources().getColorStateList(R.color.dark_bright_text_color));
        precipValue.setTextColor(convertView.getResources().getColorStateList(R.color.dark_bright_text_color));
    }

    private void setThemeToLight(View convertView){
        RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relativeLayout);
        relativeLayout.setBackgroundColor(Color.WHITE);
        //relativeLayout.setBackgroundResource(R.color.light_background_color);
        temparatureLabel.setTextColor(convertView.getResources().getColorStateList(R.color.light_bright_text_color));
        localTimeTag.setTextColor(convertView.getResources().getColorStateList(R.color.light_medium_text_color));
        TextView hum = (TextView) convertView.findViewById(R.id.humidityLabel);
        hum.setTextColor(convertView.getResources().getColorStateList(R.color.light_medium_text_color));
        TextView rain = (TextView) convertView.findViewById(R.id.precipLabel);
        rain.setTextColor(convertView.getResources().getColorStateList(R.color.light_medium_text_color));
        humidityValue.setTextColor(convertView.getResources().getColorStateList(R.color.light_bright_text_color));
        precipValue.setTextColor(convertView.getResources().getColorStateList(R.color.light_bright_text_color));
    }
}
