package com.saiyanstudio.weathergod;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.saiyanstudio.weathergod.fragments.AppPreferenceFragment;

/**
 * Created by root on 23/5/15.
 */
public class AppPreferenceActivity extends ActionBarActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    Toolbar toolbar;
    String theme,themeColor;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app_preference);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Weather God");
            toolbar.setTitleTextColor(0xFFFFFFFF);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getFragmentManager().beginTransaction().replace(R.id.content_frame,new AppPreferenceFragment()).commit();

        Bundle bundle = getIntent().getExtras();
        theme = bundle.getString("theme");
        themeColor = bundle.getString("themeColor");

        setThemeColor();

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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        themeColor = prefs.getString("AppThemeColor","blue");
        setThemeColor();
    }
}
