package com.saiyanstudio.weathergod.model;

import com.saiyanstudio.weathergod.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Deekshith on 2015/05/07.
 */
public class WeatherData {
    private String mIcon;
    private long mTime;
    private double mTemperature;
    private double mTemperatureLow;
    private double mTemperatureHigh;
    private double mHumidity;
    private double mWindSpeed;
    private double mPressure;
    private double mPrecipChance;
    private String mSummary;
    private String mTimeZone;

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public int getIconId() {
        int iconId = R.drawable.clear_day;

        if (mIcon.equals("clear-day")) {
            iconId = R.drawable.clear_day;
        } else if (mIcon.equals("clear-night")) {
            iconId = R.drawable.clear_night;
        } else if (mIcon.equals("rain")) {
            iconId = R.drawable.rain;
        } else if (mIcon.equals("snow")) {
            iconId = R.drawable.snow;
        } else if (mIcon.equals("sleet")) {
            iconId = R.drawable.sleet;
        } else if (mIcon.equals("wind")) {
            iconId = R.drawable.wind;
        } else if (mIcon.equals("fog")) {
            iconId = R.drawable.fog;
        } else if (mIcon.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        } else if (mIcon.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        } else if (mIcon.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }

        return iconId;
    }

    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date dateTime = new Date(getTime() * 1000);
        String timeString = formatter.format(dateTime);

        return timeString;
    }

    public String getDayOfTheWeek(){
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(mTimeZone));
        Date dateTime = new Date(mTime * 1000);
        return formatter.format(dateTime);
    }

    public int getTemperature() {

        // return (int) Math.round(mTemperature);
        return (int) Math.round(((mTemperature - 32) * 5 / 9));
    }

    public void setTemperature(double temperature) {

        mTemperature = temperature;

    }
    public int getTemperatureLow() {

        // return (int) Math.round(mTemperature);
        return (int) Math.round((( mTemperature - 32) * 5 / 9));
    }

    public void setTemperatureLow(double temperatureLow) {

        mTemperatureLow = temperatureLow;

    }
    public int getTemperatureHigh() {

        // return (int) Math.round(mTemperature);
        return (int) Math.round(((mTemperatureHigh - 32) * 5 / 9));
    }

    public void setTemperatureHigh(double temperatureHigh) {

        mTemperatureHigh = temperatureHigh;

    }

    public double getHumidity() {

        return mHumidity;
    }

    public void setHumidity(double humidity) {

        mHumidity = humidity;
    }

    public double getWindSpeed() {

        return mWindSpeed;
    }

    public void setWindSpeed(double windSpeed) {

        mWindSpeed = windSpeed;
    }

    public double getPressure() {

        return mPressure;
    }

    public void setPressure(double pressure) {

        mPressure = pressure;
    }

    public int getPrecipChance() {
        double precipPercentage = mPrecipChance * 1000;
        if((int) Math.round(precipPercentage) > 100)
            return 100;
        else
            return (int) Math.round(precipPercentage);
    }

    public void setPrecipChance(double precipChance) {

        mPrecipChance = precipChance;
    }

    public String getSummary() {

        return mSummary;
    }

    public void setSummary(String summary) {

        mSummary = summary;
    }
}