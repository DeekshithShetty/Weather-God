package com.saiyanstudio.weathergod.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.saiyanstudio.weathergod.R;

/**
 * Created by root on 27/5/15.
 */
public class AppPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.my_settings);
    }
}
