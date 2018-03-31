package com.saiyanstudio.weathergod;

import android.content.Intent;
import android.net.MailTo;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


public class AboutActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private String themeColor;

    private Button contactBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("About");
            toolbar.setTitleTextColor(0xFFFFFFFF);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();
        themeColor = bundle.getString("themeColor","orange");
        setThemeColor();

        contactBtn = (Button) findViewById(R.id.contactBtn);
        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"feedback.saiyanstudio@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT,"Weather God Feedback");
                //i.putExtra(Intent.EXTRA_TEXT,"Body");
                try{
                    startActivity(Intent.createChooser(i,"Send Email .."));
                }catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(AboutActivity.this,"No email clients installed",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
