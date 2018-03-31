package com.saiyanstudio.weathergod;

import android.content.ActivityNotFoundException;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.saiyanstudio.weathergod.adapter.NavigationListAdapter;
import com.saiyanstudio.weathergod.fragments.AlertDialogFragment;
import com.saiyanstudio.weathergod.model.NavigationItem;
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
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity implements android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener{

    public static final String TAG = "WEATHER GOD: " +  MainActivity.class.getSimpleName();
    double latitude;
    double longitude;
    String locationAddress;

    private WeatherData mCurrentWeather;
    private WeatherData hourlyWeatherData;
    String jsonData;

    TextView locationLabel;
    TextView mTemperatureLabel;
    TextView mHumidityValue;
    TextView mWindSpeed;
    TextView mPressure;
    TextView mPrecipValue;
    TextView mSummaryLabel;
    ImageView mIconImageView;
    TextView mLocalTime;
    LinearLayout linearLayoutMain;
    ScrollView scrollView;

    ImageView imageWeatherGod;
    TextView textWeatherGod;

    LinearLayout myLinear;
    LayoutInflater vi;
    View child;


    TextView temparatureLabel2;
    ImageView iconImageView2;
    TextView localTimeTag2;
    TextView summaryLabel2;
    TextView humidityValue2;
    TextView precipValue2;

    SwipeRefreshLayout swipeLayout;
    com.github.mikephil.charting.charts.BarChart chart;
    YAxis yAxis;
    XAxis xAxis;
    Legend legend;

    private List<WeatherData> weatherList = new ArrayList<WeatherData>();

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context context;
    String theme;
    String themeColor;
    int barColor;
    Boolean autoRefreshState;
    String refreshFreq;
    int apiCounter;
    Random random = new Random();

    String apiKey;

    private Handler handler;
    private long refreshTime;
    private MenuItem refreshItem;

    IntentFilter intentFilter;
    GPSTracker gps;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private List<NavigationItem> navigationList = new ArrayList<>();
    private NavigationListAdapter navigationDrawerAdapter;
    private String[] leftSliderData = {"Daily Forecast", "My Locations", "About", "Contact Me","Settings"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting and loading data from shared Preferences
        context = getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        theme = prefs.getString("AppTheme", "dark");
        themeColor = prefs.getString("AppThemeColor", "orange");
        autoRefreshState = prefs.getBoolean("AutoRefreshState", false);
        refreshFreq = prefs.getString("RefreshFreq", "30");
        apiCounter = prefs.getInt("ApiCounter",random.nextInt(10) + 1);

        setContentView(R.layout.activity_main);

        //setting the toolbar
        nitView();
        if (toolbar != null) {
            toolbar.setTitle("Weather God");
            toolbar.setTitleTextColor(0xFFFFFFFF);
            setSupportActionBar(toolbar);
        }
        initDrawer();

        //locating the views
        locationLabel = (TextView) findViewById(R.id.locationLabel);
        mTemperatureLabel = (TextView) findViewById(R.id.temperatureLabel);
        mHumidityValue = (TextView) findViewById(R.id.humidityValue);
        mWindSpeed = (TextView) findViewById(R.id.windSpeedValue);
        mPressure = (TextView) findViewById(R.id.pressureValue);
        mPrecipValue = (TextView) findViewById(R.id.precipValue);
        mSummaryLabel = (TextView) findViewById(R.id.summaryLabel);
        mIconImageView = (ImageView) findViewById(R.id.iconImageView);
        mLocalTime = (TextView) findViewById(R.id.localTimeTag);
        linearLayoutMain = (LinearLayout) findViewById(R.id.linearLayout_main);
        scrollView = (ScrollView) findViewById(R.id.scrollView);


        //initializing the chart
        chart = (com.github.mikephil.charting.charts.BarChart)findViewById(R.id.chart1);
        yAxis = chart.getAxisLeft();
        xAxis = chart.getXAxis();
        legend = chart.getLegend();

        gps = new GPSTracker(this);

        //setRefresh();
        setTheme();
        setThemeColor();

        //setting the swipe layout
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_red_light);

        // checking whether gps is enabled or not
        if(gps.canGetLocation() && isNetworkAvailable()){           //gps and network is enabled
            latitude = gps.getLatitude(); // returns latitude
            longitude = gps.getLongitude(); // returns longitude
            Toast.makeText(getApplicationContext(), "GPS is enabled", Toast.LENGTH_SHORT).show();
            findAddressFromLocation();

        }else if(isNetworkAvailable()){                            //network is enabled
            //gpsTurnedOffAlert();
            Toast.makeText(getApplicationContext(),"GPS is turned off",Toast.LENGTH_LONG).show();
            locationAddress = prefs.getString("MyLocation","New York");
            findLocationFromAddress();

        }else{                                                     //no network and gps
            locationLabel.setText(prefs.getString("MyLocation","New York"));
            mTemperatureLabel.setText(prefs.getString("MyTemp","30"));
            Toast.makeText(getApplicationContext(),"Network Unavailable",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        autoRefreshState = prefs.getBoolean("AutoRefreshState", false);
        refreshFreq = prefs.getString("RefreshFreq", "30");
        setRefresh();

        //registering reciever for location change event
        if(gps.canGetLocation()) {
            startService(new Intent(getBaseContext(), GPSTracker.class));
            intentFilter = new IntentFilter();
            intentFilter.addAction("LOCATION_CHANGED_ACTION");
            registerReceiver(intentReciever, intentFilter);
            Log.i(TAG, "Test : Registered reciever");
        }
    }

    private BroadcastReceiver intentReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "On brodcast recieve ");
            //Toast.makeText(MainActivity.this, "Brodcast Recieved", Toast.LENGTH_LONG).show();
            latitude = intent.getExtras().getDouble("latitude");
            longitude = intent.getExtras().getDouble("longitude");
            if(isNetworkAvailable())
                findLocationFromAddress();
        }
    };

    @Override public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getForecast(latitude, longitude);
                swipeLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
            }
        }, 3000);
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
        apiKey = "a5a1715d578e4ac675509c78ea3c4e57";
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
                            mCurrentWeather = getCurrentDetails(jsonData);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        completeRefresh();
                                        updateDisplay();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            errorAlert();
                        }

                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }

                }
            });
        } else {
            Toast.makeText(this,"Network Unavialable", Toast.LENGTH_SHORT).show();
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


    private void updateDisplay() throws JSONException {
        weatherList.clear();
        Log.i(TAG, "Weather List Size : " + weatherList.size() + "");

        //dynamically create layouts for hourly list
        createHourlyForecastList();

        locationLabel.setText(locationAddress + "");

        Drawable drawable = getResources().getDrawable(mCurrentWeather.getIconId());

        mTemperatureLabel.setText(mCurrentWeather.getTemperature() + "");
        mLocalTime.setText(mCurrentWeather.getFormattedTime());
        mHumidityValue.setText(mCurrentWeather.getHumidity() + "");
        mWindSpeed.setText(mCurrentWeather.getWindSpeed() + "");
        mPressure.setText(mCurrentWeather.getPressure() + "");
        mPrecipValue.setText(mCurrentWeather.getPrecipChance() + "%");
        mSummaryLabel.setText(mCurrentWeather.getSummary());
        mIconImageView.setImageDrawable(drawable);

        imageWeatherGod.setImageDrawable(drawable);
        textWeatherGod.setText(locationAddress);

        createChart();

    }

    private void createHourlyForecastList() throws JSONException {
        myLinear = (LinearLayout) findViewById(R.id.myLinear);
        myLinear.removeAllViewsInLayout();

        for(int i = 0; i < 7; i++) {
            hourlyWeatherData = getHourlyWeatherDetails(jsonData, i);
            Log.i(TAG, hourlyWeatherData.getHumidity() + "");
            weatherList.add(i, hourlyWeatherData);

            //changes
            vi = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            child = vi.inflate(R.layout.hourly_forecast_list_item, null);

            temparatureLabel2 = (TextView)child.findViewById(R.id.temperatureLabel);
            iconImageView2 = (ImageView)child.findViewById(R.id.iconImageView);
            localTimeTag2 = (TextView)child.findViewById(R.id.localTimeTag);
            summaryLabel2 = (TextView)child.findViewById(R.id.summaryLabel);
            humidityValue2 = (TextView)child.findViewById(R.id.humidityValue);
            precipValue2 = (TextView)child.findViewById(R.id.precipValue);

            Drawable drawable2 = getResources().getDrawable(hourlyWeatherData.getIconId());

            temparatureLabel2.setText(hourlyWeatherData.getTemperature() + "");
            localTimeTag2.setText(hourlyWeatherData.getFormattedTime());
            humidityValue2.setText(hourlyWeatherData.getHumidity() + "");
            precipValue2.setText(hourlyWeatherData.getPrecipChance() + "%");
            summaryLabel2.setText(hourlyWeatherData.getSummary());
            iconImageView2.setImageDrawable(drawable2);

            imageWeatherGod = (ImageView) findViewById(R.id.imageWeatherGod);
            textWeatherGod = (TextView) findViewById(R.id.textWeatherGod);

            setThemeForChild();
            setThemeColorForChild();

            myLinear.addView(child, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    private WeatherData getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        //Log.i(TAG, "From JSON: " + timezone);

        JSONObject currently = forecast.getJSONObject("currently");

        WeatherData currentWeatherData = new WeatherData();
        currentWeatherData.setHumidity(currently.getDouble("humidity"));
        currentWeatherData.setWindSpeed(currently.getDouble("windSpeed"));
        currentWeatherData.setPressure(currently.getDouble("pressure"));
        currentWeatherData.setTime(currently.getLong("time"));
        currentWeatherData.setIcon(currently.getString("icon"));
        currentWeatherData.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeatherData.setSummary(currently.getString("summary"));
        currentWeatherData.setTemperature(currently.getDouble("temperature"));
        currentWeatherData.setTimeZone(timezone);

        return currentWeatherData;
    }

    private WeatherData getHourlyWeatherDetails(String jsonData, int position) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        //Log.i(TAG, "From JSON: : : " + timezone);

        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONObject currently = hourly.getJSONArray("data").getJSONObject(position);
        //Log.i(TAG, currently.toString());

        WeatherData currentWeatherData = new WeatherData();
        currentWeatherData.setHumidity(currently.getDouble("humidity"));
        currentWeatherData.setWindSpeed(currently.getDouble("windSpeed"));
        currentWeatherData.setPressure(currently.getDouble("pressure"));
        currentWeatherData.setTime(currently.getLong("time"));
        currentWeatherData.setIcon(currently.getString("icon"));
        currentWeatherData.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeatherData.setSummary(currently.getString("summary"));
        currentWeatherData.setTemperature(currently.getDouble("temperature"));
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

    private void gpsTurnedOffAlert() {
        AlertDialogFragment dialog = new AlertDialogFragment();

        dialog.setDialogTitle("GPS ");
        dialog.setDialogMessage("GPS is turned off");
        dialog.setDialogButtonText("OK");

        dialog.show(getFragmentManager(), "error_dialog");
    }

    private void popUpTextBox(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("GPS isn't on");
        alert.setMessage("Enter the location ");
        alert.setCancelable(false);

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                locationAddress = input.getText().toString();
                findLocationFromAddress();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Set to default location", Toast.LENGTH_SHORT).show();
                locationAddress = "New York";
                findLocationFromAddress();
            }
        });

        alert.show();
    }

    private void findAddressFromLocation(){
        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(addresses.size() > 0) {
                locationAddress = addresses.get(0).getFeatureName();
                Log.i(TAG,"Location address : " + locationAddress);
                //Toast.makeText(getApplicationContext(), locationAddress, Toast.LENGTH_SHORT).show();
                getForecast(latitude, longitude);
            }else{
                Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"No Internet Connection", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void findLocationFromAddress(){
        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(locationAddress, 1);
            if (addresses.size() > 0) {
                String country = addresses.get(0).getCountryName();
                latitude = addresses.get(0).getLatitude();
                longitude = addresses.get(0).getLongitude();

                //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude + "\nCountry: " + country, Toast.LENGTH_LONG).show();
                getForecast(latitude, longitude);
            }else{
                Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"No Internet Connection", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1) {
            if (resultCode == RESULT_OK) {
                locationAddress = data.getData().toString();
                findLocationFromAddress();
                //Log.i(TAG, "WORKING...");
            }
        }else if (requestCode == 2){
            if (resultCode == RESULT_CANCELED) {

                if(isNetworkAvailable() || gps.canGetLocation()){
                    setRefresh();
                    setTheme();
                    setThemeColor();
                    try {

                        createHourlyForecastList();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    refreshListView(theme, themeColor);
                    setRefresh();
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this,AppPreferenceActivity.class);
            intent.putExtra("theme",theme);
            intent.putExtra("themeColor",themeColor);
            startActivityForResult(intent, 2);
            //startActivity(intent);
        }else  if(id == R.id.refresh_button){
            refreshItem = item;
            refresh();
            return true;
         }

        return super.onOptionsItemSelected(item);
    }

    public void refresh() {
     /* Attach a rotating ImageView to the refresh item as an ActionView */
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView iv = (ImageView) inflater.inflate(R.layout.refresh_action_view, null);

        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.refresh_rotate);
        rotation.setRepeatCount(Animation.INFINITE);
        iv.startAnimation(rotation);

        refreshItem.setActionView(iv);
        getForecast(latitude, longitude);
    }

    //after completing the refreshing
    public void completeRefresh() {
        if (refreshItem != null && refreshItem.getActionView() != null) {
            refreshItem.getActionView().clearAnimation();
            refreshItem.setActionView(null);
            Toast.makeText(MainActivity.this,"Refreshed",Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshListView(String str1,String str2) {
        weatherList.clear();
        Log.i(TAG,"Weather List Size : " +  weatherList.size() + "");
        for(int i = 0; i < 7; i++) {
            try {
                hourlyWeatherData = getHourlyWeatherDetails(jsonData, i);
                //Log.i(TAG, hourlyWeatherData.getHumidity() + "");
                 weatherList.add(i, hourlyWeatherData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setRefresh(){

        if(handler != null)
            handler.removeCallbacks(myRunnable);

        if(autoRefreshState) {
            handler = new Handler();
            refreshFreq = prefs.getString("RefreshFreq", "30");

            if(refreshFreq.compareTo("15") == 0){
                refreshTime = 120000;

            }else if(refreshFreq.compareTo("30") == 0){
                refreshTime = 1800000;

            }else if(refreshFreq.compareTo("60") == 0){
                refreshTime = 3600000;

            }
            MainActivity.this.handler.postDelayed(myRunnable,refreshTime);
        }
    }

    private final Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            getForecast(latitude,longitude);
            Toast.makeText(MainActivity.this,"Refreshed",Toast.LENGTH_SHORT).show();
            MainActivity.this.handler.postDelayed(myRunnable,refreshTime);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if(isNetworkAvailable()){
            editor = prefs.edit();
            editor.putString("MyLocation", locationAddress);
            editor.putString("MyTemp", mTemperatureLabel.getText().toString());
            editor.apply();
        }

        if(gps.canGetLocation()) {
            unregisterReceiver(intentReciever);
        }
    }

    private void gpsTime(){
        if(gps.canGetLocation() && isNetworkAvailable()){           //gps and network is enabled
            latitude = gps.getLatitude(); // returns latitude
            longitude = gps.getLongitude(); // returns longitude
            Toast.makeText(getApplicationContext(), "GPS is enabled", Toast.LENGTH_LONG).show();
            findAddressFromLocation();
        }else{
            Toast.makeText(getApplicationContext(), "GPS is disabled", Toast.LENGTH_LONG).show();
        }
    }

    private void createChart(){

        //MPAndroidChart
        chart.clear();
        chart.clearAnimation();

        int count = 0;
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(WeatherData w : weatherList){

            entries.add(new BarEntry(w.getTemperature(), count));
            count++;
        }
        BarDataSet dataset = new BarDataSet(entries, "Temperature");

        ArrayList<String> labels = new ArrayList<String>();
        for(WeatherData w : weatherList){
            labels.add(w.getFormattedTime());
        }

        //yAxis.setAxisMinValue(20);
        //yAxis.setAxisMaxValue(40);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        if(theme == "dark"){
            legend.setTextColor(0xFFFFFFFF);
            dataset.setValueTextColor(Color.WHITE);
        }else{
            legend.setTextColor(0xFF000000);
            dataset.setValueTextColor(Color.BLACK);
        }
        legend.setFormSize(6f); // set the size of the legend forms/shapes
        legend.setTextSize(12f);

        BarData data = new BarData(labels, dataset);
        chart.setData(data);
        dataset.setColor(barColor);
        chart.setClickable(false);
        chart.setNoDataTextDescription(" ");
        //chart.setNoDataTextDescription("No data available for chart");
        chart.setHorizontalScrollBarEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setTouchEnabled(false);
        chart.enableScroll();
        chart.setDescription(" ");
        chart.getLegend().setEnabled(false);
        chart.animateY(2500);

    }

    private void setThemeColor() {
        themeColor = prefs.getString("AppThemeColor", "orange");
        Resources res = this.getResources();
        int newColor = res.getColor(R.color.theme_orange);

        if(themeColor.compareTo("orange") == 0){
            newColor = res.getColor(R.color.theme_orange);
            mSummaryLabel.setTextColor(this.getResources().getColorStateList(R.color.theme_orange));
            barColor = 0xFFFA6800;
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_orange);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_orange_dark));
                }
            }
        }else if(themeColor.compareTo("blue") == 0){
            newColor = res.getColor(R.color.theme_blue);
            mSummaryLabel.setTextColor(this.getResources().getColorStateList(R.color.theme_blue));
            barColor = 0xFF1BA1E2;
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_blue);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_blue_dark));
                }
            }
        }else if(themeColor.compareTo("green") == 0){
            newColor = res.getColor(R.color.theme_green);
            mSummaryLabel.setTextColor(this.getResources().getColorStateList(R.color.theme_green));
            barColor = 0xFF42BD41;
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_green);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_green_dark));
                }
            }
        }else if(themeColor.compareTo("purple") == 0){
            newColor = res.getColor(R.color.theme_purple);
            mSummaryLabel.setTextColor(this.getResources().getColorStateList(R.color.theme_purple));
            barColor = 0xFF8E44A8;
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_purple);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_purple_dark));
                }
            }
        }else if(themeColor.compareTo("pink") == 0){
            newColor = res.getColor(R.color.theme_pink);
            mSummaryLabel.setTextColor(this.getResources().getColorStateList(R.color.theme_pink));
            barColor = 0xFFE61443;
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_pink);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_pink_dark));
                }
            }
        }else if(themeColor.compareTo("indigo") == 0){
            newColor = res.getColor(R.color.theme_indigo);
            mSummaryLabel.setTextColor(this.getResources().getColorStateList(R.color.theme_indigo));
            barColor = 0xFF3F51B5;
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_indigo);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_indigo_dark));
                }
            }
        }else if(themeColor.compareTo("yellow") == 0){
            newColor = res.getColor(R.color.theme_yellow);
            mSummaryLabel.setTextColor(this.getResources().getColorStateList(R.color.theme_yellow));
            barColor = 0xFFF1C40f;
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_yellow);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_yellow_dark));
                }
            }
        }else if(themeColor.compareTo("red") == 0){
            newColor = res.getColor(R.color.theme_red);
            mSummaryLabel.setTextColor(this.getResources().getColorStateList(R.color.theme_red));
            barColor = 0xFFD32F2F;
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_red);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_red_dark));
                }
            }
        }else if(themeColor.compareTo("grey") == 0){
            newColor = res.getColor(R.color.theme_grey);
            mSummaryLabel.setTextColor(this.getResources().getColorStateList(R.color.theme_grey));
            barColor = 0xFF607D8B;
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_grey);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_grey_dark));
                }
            }
        }
        mIconImageView.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);

        if(isNetworkAvailable() || gps.canGetLocation())
            createChart();
    }

    private void setTheme() {
        theme = prefs.getString("AppTheme", "dark");
        if(theme.compareTo("dark") == 0){
            setThemeToDark();
        }else{
            setThemeToLight();
        }
    }

    private void setThemeToDark() {
        linearLayoutMain.setBackgroundResource(R.color.dark_background_color);
        mTemperatureLabel.setTextColor(getResources().getColorStateList(R.color.dark_bright_text_color));
        TextView degreLabel = (TextView) findViewById(R.id.degreeImageView);
        degreLabel.setTextColor(getResources().getColorStateList(R.color.dark_bright_text_color));
        TextView degreeGradeLabel = (TextView) findViewById(R.id.degreeGradeLabel);
        degreeGradeLabel.setTextColor(getResources().getColorStateList(R.color.dark_bright_text_color));
        locationLabel.setTextColor(getResources().getColorStateList(R.color.dark_bright_text_color));
        mLocalTime.setTextColor(getResources().getColorStateList(R.color.dark_medium_text_color));
        TextView hum = (TextView) findViewById(R.id.humidityLabel);
        hum.setTextColor(getResources().getColorStateList(R.color.dark_medium_text_color));
        TextView rain = (TextView) findViewById(R.id.precipLabel);
        rain.setTextColor(getResources().getColorStateList(R.color.dark_medium_text_color));
        TextView wind = (TextView) findViewById(R.id.windSpeedLabel);
        wind.setTextColor(getResources().getColorStateList(R.color.dark_medium_text_color));
        TextView press = (TextView) findViewById(R.id.pressureLabel);
        press.setTextColor(getResources().getColorStateList(R.color.dark_medium_text_color));
        mHumidityValue.setTextColor(getResources().getColorStateList(R.color.dark_bright_text_color));
        mPrecipValue.setTextColor(getResources().getColorStateList(R.color.dark_bright_text_color));
        mWindSpeed.setTextColor(getResources().getColorStateList(R.color.dark_bright_text_color));
        mPressure.setTextColor(getResources().getColorStateList(R.color.dark_bright_text_color));

        TextView chartLabel = (TextView) findViewById(R.id.chartLabel);
        chartLabel.setTextColor(getResources().getColorStateList(R.color.dark_bright_text_color));
        TextView hourWeatherLabel = (TextView) findViewById(R.id.hourWeatherLabel);
        hourWeatherLabel.setTextColor(getResources().getColorStateList(R.color.dark_bright_text_color));

        if(isNetworkAvailable() || gps.canGetLocation()){

            yAxis.setTextColor(0xFFC6C6C6);
            xAxis.setTextColor(0xFFC6C6C6);
            legend.setTextColor(Color.WHITE);
            chart.setBackgroundColor(Color.BLACK);
        }
    }

    private void setThemeToLight() {
        linearLayoutMain.setBackgroundResource(R.color.light_background_color);
        mTemperatureLabel.setTextColor(getResources().getColorStateList(R.color.light_bright_text_color));
        TextView degreLabel = (TextView) findViewById(R.id.degreeImageView);
        degreLabel.setTextColor(getResources().getColorStateList(R.color.light_bright_text_color));
        TextView degreeGradeLabel = (TextView) findViewById(R.id.degreeGradeLabel);
        degreeGradeLabel.setTextColor(getResources().getColorStateList(R.color.light_bright_text_color));

        locationLabel.setTextColor(getResources().getColorStateList(R.color.light_bright_text_color));
        mLocalTime.setTextColor(getResources().getColorStateList(R.color.light_medium_text_color));
        TextView hum = (TextView) findViewById(R.id.humidityLabel);
        hum.setTextColor(getResources().getColorStateList(R.color.light_medium_text_color));
        TextView rain = (TextView) findViewById(R.id.precipLabel);
        rain.setTextColor(getResources().getColorStateList(R.color.light_medium_text_color));
        TextView wind = (TextView) findViewById(R.id.windSpeedLabel);
        wind.setTextColor(getResources().getColorStateList(R.color.light_medium_text_color));
        TextView press = (TextView) findViewById(R.id.pressureLabel);
        press.setTextColor(getResources().getColorStateList(R.color.light_medium_text_color));
        mHumidityValue.setTextColor(getResources().getColorStateList(R.color.light_bright_text_color));
        mPrecipValue.setTextColor(getResources().getColorStateList(R.color.light_bright_text_color));
        mWindSpeed.setTextColor(getResources().getColorStateList(R.color.light_bright_text_color));
        mPressure.setTextColor(getResources().getColorStateList(R.color.light_bright_text_color));

        TextView chartLabel = (TextView) findViewById(R.id.chartLabel);
        chartLabel.setTextColor(getResources().getColorStateList(R.color.light_bright_text_color));
        TextView hourWeatherLabel = (TextView) findViewById(R.id.hourWeatherLabel);
        hourWeatherLabel.setTextColor(getResources().getColorStateList(R.color.light_bright_text_color));

        if(isNetworkAvailable() || gps.canGetLocation()){

            yAxis.setTextColor(0xFF444444);
            xAxis.setTextColor(0xFF444444);
            legend.setTextColor(Color.BLACK);
            chart.setBackgroundColor(Color.WHITE);
        }
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
        theme = prefs.getString("AppTheme", "dark");
        if(theme.compareTo("dark") == 0){
            setThemeToDarkForChild();
        }else{
            setThemeToLightForChild();
        }
    }

    private void setThemeToDarkForChild(){
        RelativeLayout relativeLayout2 = (RelativeLayout)child.findViewById(R.id.relativeLayout);
        relativeLayout2.setBackgroundColor(Color.BLACK);
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

    private void nitView() {
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        navigationList.add(new NavigationItem("daily_forecast","Daily Forecast"));
        navigationList.add(new NavigationItem("my_location","My Locations"));
        navigationList.add(new NavigationItem("gps","GPS"));
        navigationList.add(new NavigationItem("rate_me","Rate the app"));
        navigationList.add(new NavigationItem("about","About"));
        navigationList.add(new NavigationItem("settings","Settings"));

        navigationDrawerAdapter=new NavigationListAdapter( MainActivity.this,navigationList);
        leftDrawerList.setAdapter(navigationDrawerAdapter);

        leftDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        drawerLayout.closeDrawers();
                        Intent intent = new Intent(MainActivity.this,DailyForecast.class);
                        intent.putExtra("latitude", latitude);
                        intent.putExtra("longitude",longitude);
                        intent.putExtra("theme", theme);
                        intent.putExtra("themeColor", themeColor);
                        startActivity(intent);
                        break;
                    case 1:
                        drawerLayout.closeDrawers();
                        Intent intent2 = new Intent(MainActivity.this,MyLocationsActivity.class);
                        intent2.putExtra("theme", theme);
                        intent2.putExtra("themeColor", themeColor);
                        startActivityForResult(intent2, 1);
                        break;
                    case 2:
                        drawerLayout.closeDrawers();
                        gpsTime();
                        break;
                    case 3:
                        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                        }
                        break;
                    case 4:
                        drawerLayout.closeDrawers();
                        Intent intent4 = new Intent(MainActivity.this,AboutActivity.class);
                        intent4.putExtra("themeColor", themeColor);
                        startActivity(intent4);
                        break;
                    case 5:
                        drawerLayout.closeDrawers();
                        Intent intent5 = new Intent(MainActivity.this,AppPreferenceActivity.class);
                        intent5.putExtra("theme",theme);
                        intent5.putExtra("themeColor", themeColor);
                        startActivityForResult(intent5, 2);
                        break;

                }

            }
        });
    }

    private void initDrawer() {

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.drawerOpen,R.string.drawerClose) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                toolbar.setTitle("Weather God");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                toolbar.setTitle("Weather God");
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
