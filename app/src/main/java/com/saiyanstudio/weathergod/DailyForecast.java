package com.saiyanstudio.weathergod;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.saiyanstudio.weathergod.fragments.AlertDialogFragment;
import com.saiyanstudio.weathergod.model.WeatherData;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DailyForecast extends ActionBarActivity {
    public static final String TAG = "WEATHER GOD";
    double latitude = 0.0;
    double longitude = 0.0;

    private WeatherData hourlyWeatherData;
    private String jsonData;

    private ProgressBar mProgressBar;
    private TextView mProgrsslabel;
    private RelativeLayout relativeLayout;
    private List<WeatherData> weatherList2 = new ArrayList<WeatherData>();

    LinearLayout myLinear;
    LayoutInflater vi;
    View child;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    int apiCounter;
    Random random = new Random();
    String apiKey;

    TextView temparatureLabel2;
    ImageView iconImageView2;
    TextView localTimeTag2;
    TextView summaryLabel2;
    TextView humidityValue2;
    TextView precipValue2;

    LineChart lineChart;
    YAxis yAxis;
    XAxis xAxis;
    Legend legend;
    int lineColor;

    String theme,themeColor;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Daily Forecast");
            toolbar.setTitleTextColor(0xFFFFFFFF);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        apiCounter = prefs.getInt("ApiCounter",random.nextInt(10) + 1);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgrsslabel = (TextView) findViewById(R.id.progressLabel);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        Bundle bundle = getIntent().getExtras();
        theme = bundle.getString("theme");
        themeColor = bundle.getString("themeColor");

        lineChart = (LineChart) findViewById(R.id.lineChart);
        yAxis = lineChart.getAxisLeft();
        xAxis = lineChart.getXAxis();
        legend = lineChart.getLegend();

        setTheme();
        setThemeColor();

        latitude = bundle.getDouble("latitude");
        longitude = bundle.getDouble("longitude");


        getForecast(latitude, longitude);
    }

    private void findApiKey(){
        apiCounter = random.nextInt(10) + 1;
        switch (apiCounter){
            case 1:
                apiKey = "a5a1715d578e4ac675509c78ea3c4e57";
                break;
            case 2:
                apiKey = "dd25d9477807f19439b5741380f93297";
                break;
            case 3:
                apiKey = "6c2722ab961c038415a536d82a42688c";
                break;
            case 4:
                apiKey = "4ec3021a20aed80c2a271b296eae3cde";
                break;
            case 5:
                apiKey = "1e913a54edfc6b06065b97cd1aba9169";
                break;
            case 6:
                apiKey = "b30d3c5d082f88b568028387f4f97111";
                break;
            case 7:
                apiKey = "692bd48c5203971b50359f4fc3db0635";
                break;
            case 8:
                apiKey = "926d3d866ebb797f0bae4670dcc5e156";
                break;
            case 9:
                apiKey = "edd4a8915a306cbd159d048ddd8eadc4";
                break;
            case 10:
                apiKey = "f2d81cf2f4f0eaab26dcf19c7f6eb6a7";
                break;
        }
    }

    private void getForecast(double latitude, double longitude) {
        findApiKey();
        String forecastURL = ("https://api.forecast.io/forecast/" + apiKey + "/" + latitude + "," + longitude);

        if (isNetworkAvailable()) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastURL)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {

                @Override
                public void onFailure(Request request, IOException e) {
                    errorAlert();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        jsonData = response.body().string();
                        Log.v(TAG, jsonData);

                        if (response.isSuccessful()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        createHourlyForecastList();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    createChart();
                                    mProgrsslabel.setVisibility(View.INVISIBLE);
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                        } else {

                            errorAlert();

                        }

                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        } else {
            Toast.makeText(this, "Network Unavailable", Toast.LENGTH_SHORT).show();
            mProgrsslabel.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;

        if (networkInfo != null && networkInfo.isConnected()) {

            isAvailable = true;
        }
        return isAvailable;
    }

    private WeatherData getHourlyWeatherDetails(String jsonData, int position) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON---" + timezone);

        JSONObject daily = forecast.getJSONObject("daily");
        JSONObject currently = daily.getJSONArray("data").getJSONObject(position);
        Log.i(TAG, currently.toString());

        WeatherData currentWeatherData = new WeatherData();
        currentWeatherData.setHumidity(currently.getDouble("humidity"));
        currentWeatherData.setWindSpeed(currently.getDouble("windSpeed"));
        currentWeatherData.setPressure(currently.getDouble("pressure"));
        currentWeatherData.setTime(currently.getLong("time"));
        currentWeatherData.setIcon(currently.getString("icon"));
        currentWeatherData.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeatherData.setSummary(currently.getString("summary"));
        currentWeatherData.setTemperature(currently.getDouble("temperatureMin"));
        //currentWeatherData.setTemperatureLow(currently.getDouble("temperatureMin"));
        currentWeatherData.setTemperatureHigh(currently.getDouble("temperatureMax"));
        currentWeatherData.setTimeZone(timezone);

        return currentWeatherData;
    }


    private void errorAlert() {
        AlertDialogFragment dialog = new AlertDialogFragment();

        dialog.setDialogTitle("Error : ");
        dialog.setDialogMessage("Oops there was an error");
        dialog.setDialogButtonText("OK");

        dialog.show(getFragmentManager(), "error_dialog");
    }

    private void createChart(){
        //MPAndroidChart
        lineChart.clear();
        lineChart.clearAnimation();

        Log.i(TAG, "Testing 0000000");
        int count = 0;
        ArrayList<Entry> entries = new ArrayList<>();
        for(WeatherData w : weatherList2){
            Log.i(TAG,"Testing 0000000");
            entries.add(new Entry(w.getTemperatureHigh(), count));
            count++;
        }
        Log.i(TAG, "Testing 1111111 : entries.size() " + entries.size());
        int count2 = 0;
        ArrayList<Entry> entries2 = new ArrayList<>();
        for(WeatherData w : weatherList2){
            entries2.add(new Entry(w.getTemperature(), count2));
            count2++;
        }
        Log.i(TAG, "Testing 1111111 : entries2.size() " + entries2.size());
        ArrayList<String> labels = new ArrayList<String>();
        for(WeatherData w : weatherList2){
            String week = w.getDayOfTheWeek().substring(0,3);
            labels.add(week);
        }
        Log.i(TAG, "Testing 1111111 : labels.size() " + labels.size());
        LineDataSet dataset = new LineDataSet(entries, "Temp High");
        dataset.setFillAlpha(65);
        dataset.setFillColor(lineColor);
        dataset.setColor(lineColor);
        if(theme.compareTo("dark") == 0)
            dataset.setCircleColor(Color.BLACK);
        else
            dataset.setCircleColor(Color.WHITE);
        dataset.setCircleColorHole(lineColor);
        dataset.setLineWidth(4f);
        dataset.setCircleSize(9f);
        dataset.setDrawValues(false);

        Log.i(TAG, "Testing dataset 1 passed");
        LineDataSet dataset2 = new LineDataSet(entries2, "Temp Low");
        dataset2.setFillAlpha(65);
        dataset2.setFillColor(0xFFC6C6C6);
        dataset2.setColor(0xFFC6C6C6);
        if(theme.compareTo("dark") == 0)
            dataset2.setCircleColor(Color.BLACK);
        else
            dataset2.setCircleColor(Color.WHITE);
        dataset2.setCircleColorHole(0xFFC6C6C6);
        dataset2.setLineWidth(4f);
        dataset2.setCircleSize(9f);
        dataset2.setDrawValues(false);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription("");

        Log.i(TAG, "Testing dataset 2 passed");
        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataset);
        dataSets.add(dataset2);

        Log.i(TAG, "Adding to datasets passed");
        LineData data = new LineData(labels, dataSets);
        lineChart.setData(data);
        Log.i(TAG, "Setting data to line chart passed");
        lineChart.setClickable(false);
        lineChart.setNoDataTextDescription("");
        //chart.setNoDataTextDescription("No data available for chart");
        lineChart.setHorizontalScrollBarEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription("");
        lineChart.setTouchEnabled(false);
        lineChart.enableScroll();
        Log.i(TAG, "Almost done");
        lineChart.getLegend().setForm(Legend.LegendForm.CIRCLE);
        lineChart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        lineChart.animateY(2500);
        Log.i(TAG, "congrats u finished");
    }

    private void createHourlyForecastList() throws JSONException {
        myLinear = (LinearLayout) findViewById(R.id.myLinear);
        myLinear.removeAllViewsInLayout();

        for(int i = 0; i < 7; i++) {
            hourlyWeatherData = getHourlyWeatherDetails(jsonData, i);
            Log.i(TAG, hourlyWeatherData.getHumidity() + "");
            weatherList2.add(i, hourlyWeatherData);

            //changes
            vi = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            child = vi.inflate(R.layout.daily_forecast_list_item, null);

            temparatureLabel2 = (TextView)child.findViewById(R.id.temperatureLabel);
            iconImageView2 = (ImageView)child.findViewById(R.id.iconImageView);
            localTimeTag2 = (TextView)child.findViewById(R.id.localTimeTag);
            summaryLabel2 = (TextView)child.findViewById(R.id.summaryLabel);
            humidityValue2 = (TextView)child.findViewById(R.id.humidityValue);
            precipValue2 = (TextView)child.findViewById(R.id.precipValue);

            Drawable drawable2 = getResources().getDrawable(hourlyWeatherData.getIconId());

            temparatureLabel2.setText(hourlyWeatherData.getTemperature() + "");
            if(i == 0)
                localTimeTag2.setText("Today");
            else
                localTimeTag2.setText(hourlyWeatherData.getDayOfTheWeek());
            humidityValue2.setText(hourlyWeatherData.getHumidity() + "");
            precipValue2.setText(hourlyWeatherData.getPrecipChance() + "%");
            summaryLabel2.setText(hourlyWeatherData.getSummary());
            iconImageView2.setImageDrawable(drawable2);

            setThemeForChild();
            setThemeColorForChild();

            myLinear.addView(child, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setThemeColor() {

        if(themeColor.compareTo("orange") == 0){
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_orange);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_orange_dark));
                }
            }
            lineColor = 0xFFFA6800;
        }else if(themeColor.compareTo("blue") == 0){
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_blue);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_blue_dark));
                }
            }
            lineColor = 0xFF1BA1E2;
        }else if(themeColor.compareTo("green") == 0){
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_green);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_green_dark));
                }
            }
            lineColor = 0xFF42BD41;
        }else if(themeColor.compareTo("purple") == 0){
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_purple);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_purple_dark));
                }
            }
            lineColor = 0xFF8E44A8;
        }else if(themeColor.compareTo("pink") == 0){
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_pink);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_pink_dark));
                }
            }
            lineColor = 0xFFE61443;
        }else if(themeColor.compareTo("indigo") == 0){
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_indigo);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_indigo_dark));
                }
            }
            lineColor = 0xFF3F51B5;
        }else if(themeColor.compareTo("yellow") == 0){
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_yellow);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_yellow_dark));
                }
            }
            lineColor = 0xFFF1C40f;
        }else if(themeColor.compareTo("red") == 0){
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_red);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_red_dark));
                }
            }
            lineColor = 0xFFD32F2F;
        }else if(themeColor.compareTo("grey") == 0){
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_grey);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_grey_dark));
                }
            }
            lineColor = 0xFF607D8B;
        }

        createChart();
    }

    private void setTheme() {

        if(theme.compareTo("dark") == 0){
            setThemeToDark();
        }else{
            setThemeToLight();
        }
    }

    private void setThemeToDark() {
        relativeLayout.setBackgroundResource(R.color.dark_background_color);
        //listView2.setBackgroundResource(R.color.dark_background_color);
        mProgrsslabel.setTextColor(getResources().getColorStateList(R.color.dark_bright_text_color));

        yAxis.setTextColor(0xFFC6C6C6);
        xAxis.setTextColor(0xFFC6C6C6);
        legend.setTextColor(Color.WHITE);
        lineChart.setBackgroundColor(Color.BLACK);
    }

    private void setThemeToLight() {
        relativeLayout.setBackgroundResource(R.color.light_background_color);
        //listView2.setBackgroundResource(R.color.light_background_color);
        mProgrsslabel.setTextColor(getResources().getColorStateList(R.color.light_bright_text_color));

        yAxis.setTextColor(0xFF444444);
        xAxis.setTextColor(0xFF444444);
        legend.setTextColor(Color.BLACK);
        lineChart.setBackgroundColor(Color.WHITE);

    }

    private void setThemeColorForChild(){
        Resources res = getResources();
        int newColor = res.getColor(R.color.theme_orange);

        if(themeColor.compareTo("orange") == 0){
            newColor = res.getColor(R.color.theme_orange);
            summaryLabel2.setTextColor(getResources().getColorStateList(R.color.theme_orange));
        }else if(themeColor.compareTo("blue") == 0){
            newColor = res.getColor(R.color.theme_blue);
            summaryLabel2.setTextColor(getResources().getColorStateList(R.color.theme_blue));
        }else if(themeColor.compareTo("green") == 0){
            newColor = res.getColor(R.color.theme_green);
            summaryLabel2.setTextColor(getResources().getColorStateList(R.color.theme_green));
        }else if(themeColor.compareTo("purple") == 0){
            newColor = res.getColor(R.color.theme_purple);
            summaryLabel2.setTextColor(getResources().getColorStateList(R.color.theme_purple));
        }else if(themeColor.compareTo("pink") == 0){
            newColor = res.getColor(R.color.theme_pink);
            summaryLabel2.setTextColor(getResources().getColorStateList(R.color.theme_pink));
        }else if(themeColor.compareTo("indigo") == 0){
            newColor = res.getColor(R.color.theme_indigo);
            summaryLabel2.setTextColor(getResources().getColorStateList(R.color.theme_indigo));
        }else if(themeColor.compareTo("yellow") == 0){
            newColor = res.getColor(R.color.theme_yellow);
            summaryLabel2.setTextColor(getResources().getColorStateList(R.color.theme_yellow));
        }else if(themeColor.compareTo("red") == 0){
            newColor = res.getColor(R.color.theme_red);
            summaryLabel2.setTextColor(getResources().getColorStateList(R.color.theme_red));
        }else if(themeColor.compareTo("grey") == 0){
            newColor = res.getColor(R.color.theme_grey);
            summaryLabel2.setTextColor(getResources().getColorStateList(R.color.theme_grey));
        }
        iconImageView2.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
    }

    private void setThemeForChild(){
        if(theme.compareTo("dark") == 0){
            setThemeToDarkForChild();
        }else{
            setThemeToLightForChild();
        }
    }

    private void setThemeToDarkForChild(){
        LinearLayout myLinear = (LinearLayout) findViewById(R.id.myLinear);
        myLinear.setBackgroundColor(Color.BLACK);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.setBackgroundColor(Color.BLACK);
        myLinear.setBackgroundColor(Color.BLACK);
        RelativeLayout relativeLayout2 = (RelativeLayout)child.findViewById(R.id.relativeLayout);
        relativeLayout2.setBackgroundColor(Color.BLACK);
        //relativeLayout.setBackgroundResource(R.color.dark_background_color);
        temparatureLabel2.setTextColor(getResources().getColorStateList(R.color.dark_bright_text_color));
        localTimeTag2.setTextColor(getResources().getColorStateList(R.color.dark_medium_text_color));
        TextView hum2 = (TextView)child.findViewById(R.id.humidityLabel);
        hum2.setTextColor(getResources().getColorStateList(R.color.dark_medium_text_color));
        TextView rain2 = (TextView)child.findViewById(R.id.precipLabel);
        rain2.setTextColor(getResources().getColorStateList(R.color.dark_medium_text_color));
        humidityValue2.setTextColor(getResources().getColorStateList(R.color.dark_bright_text_color));
        precipValue2.setTextColor(getResources().getColorStateList(R.color.dark_bright_text_color));
    }

    private void setThemeToLightForChild(){
        LinearLayout myLinear = (LinearLayout) findViewById(R.id.myLinear);
        myLinear.setBackgroundColor(Color.WHITE);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.setBackgroundColor(Color.WHITE);
        myLinear.setBackgroundColor(Color.WHITE);
        RelativeLayout relativeLayout2 = (RelativeLayout) child.findViewById(R.id.relativeLayout);
        relativeLayout2.setBackgroundColor(Color.WHITE);
        //relativeLayout.setBackgroundResource(R.color.light_background_color);
        temparatureLabel2.setTextColor(getResources().getColorStateList(R.color.light_bright_text_color));
        localTimeTag2.setTextColor(getResources().getColorStateList(R.color.light_medium_text_color));
        TextView hum2 = (TextView)child.findViewById(R.id.humidityLabel);
        hum2.setTextColor(getResources().getColorStateList(R.color.light_medium_text_color));
        TextView rain2 = (TextView)child.findViewById(R.id.precipLabel);
        rain2.setTextColor(getResources().getColorStateList(R.color.light_medium_text_color));
        humidityValue2.setTextColor(getResources().getColorStateList(R.color.light_bright_text_color));
    }
}
