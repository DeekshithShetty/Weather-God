package com.saiyanstudio.weathergod;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.saiyanstudio.weathergod.adapter.LocationListAdapter;
import com.saiyanstudio.weathergod.handler.LocationDatabaseHandler;
import com.saiyanstudio.weathergod.model.Location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MyLocationsActivity extends ActionBarActivity {

    public static final String TAG = "WEATHER GOD: " +  MyLocationsActivity.class.getSimpleName();
    private String newLocation;
    private String country;
    private LocationDatabaseHandler db;

    private RelativeLayout relativeLayout;
    private ListView listView;
    private List<Location> locationList = new ArrayList<>();
    private LocationListAdapter adapter;
    private String theme,themeColor;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_locations);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("My Locations");
            toolbar.setTitleTextColor(0xFFFFFFFF);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = new LocationDatabaseHandler(this);
        Log.d(TAG,"Retrieving locations....");
        locationList = db.getAllLocations();
        Log.d(TAG,"Locations retrieved successfully....");


        Bundle bundle = getIntent().getExtras();
        theme = bundle.getString("theme");
        themeColor = bundle.getString("themeColor");

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        //list view
        listView = (ListView) findViewById(R.id.layout_lv);

        setTheme();
        setThemeColor();

        adapter = new LocationListAdapter(this,locationList,theme,themeColor);
        Log.v(TAG, "Testing : " + adapter.getCount());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), "You clicked: " + locationList.get(position).getLocationName(), Toast.LENGTH_SHORT).show();
                Intent data = new Intent();
                data.setData(Uri.parse(locationList.get(position).getLocationName()));
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    private void popUpTextBox(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("New Location");
        alert.setMessage("Enter the location to be added");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newLocation = input.getText().toString();
                findLocationFromAddress();
                Location lc = new Location(newLocation,country);
                locationList.add(0, lc);
                db.addLocation(new Location(newLocation, country));
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), newLocation + " added", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        });

        alert.show();
    }

    private void findLocationFromAddress(){
        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(newLocation, 1);
            if (addresses.size() > 0) {
                country = addresses.get(0).getCountryName();
            }
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"Internet Connection Lost", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_locations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                    finish();
                    break;
            case R.id.add_btn:
                popUpTextBox();
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
        }else if(themeColor.compareTo("blue") == 0){
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_blue);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_blue_dark));
                }
            }
        }else if(themeColor.compareTo("green") == 0){
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_green);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_green_dark));
                }
            }
        }else if(themeColor.compareTo("purple") == 0){
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_purple);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_purple_dark));
                }
            }
        }else if(themeColor.compareTo("pink") == 0){
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_pink);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_pink_dark));
                }
            }
        }else if(themeColor.compareTo("indigo") == 0){
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_indigo);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_indigo_dark));
                }
            }
        }else if(themeColor.compareTo("yellow") == 0){
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_yellow);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_yellow_dark));
                }
            }
        }else if(themeColor.compareTo("red") == 0){
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_red);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_red_dark));
                }
            }
        }else if(themeColor.compareTo("grey") == 0){
            if (toolbar != null) {
                toolbar.setBackgroundResource(R.color.theme_grey);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.theme_grey_dark));
                }
            }
        }

    }

    private void setTheme() {
        if(theme.compareTo("dark") == 0) {
            setThemeToDark();
        }else{
            setThemeToLight();
        }
    }

    private void setThemeToDark(){
        relativeLayout.setBackgroundResource(R.color.dark_background_color);
        listView.setBackgroundResource(R.color.dark_background_color);
    }

    private void setThemeToLight(){
        relativeLayout.setBackgroundResource(R.color.light_background_color);
        listView.setBackgroundResource(R.color.light_background_color);
    }
}
