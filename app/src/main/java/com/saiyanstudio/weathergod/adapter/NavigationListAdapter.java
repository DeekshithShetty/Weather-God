package com.saiyanstudio.weathergod.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.saiyanstudio.weathergod.R;
import com.saiyanstudio.weathergod.model.NavigationItem;

import java.util.List;

/**
 * Created by root on 27/5/15.
 */
public class NavigationListAdapter  extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<NavigationItem> navigationItems;

    ImageView nav_icon;
    TextView nav_listTitle;

    public NavigationListAdapter(Activity activity,List<NavigationItem> navigationItems){
        this.activity = activity;
        this.navigationItems = navigationItems;
    }

    @Override
    public int getCount() {
        return navigationItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navigationItems.get(position);
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
            convertView = inflater.inflate(R.layout.navigation_drawer_list_item,null);

        nav_icon = (ImageView) convertView.findViewById(R.id.nav_icon);
        nav_listTitle = (TextView) convertView.findViewById(R.id.nav_listTitle);

        NavigationItem nav = navigationItems.get(position);

        nav_listTitle.setText(nav.getItemName() + "");

        Drawable drawable = activity.getResources().getDrawable(nav.getmIconId());
        nav_icon.setImageDrawable(drawable);
        int newColor = activity.getResources().getColor(R.color.dark_grey);
        nav_icon.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);

        return convertView;
    }
}
