package com.saiyanstudio.weathergod.handler;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.saiyanstudio.weathergod.model.Location;

/**
 * Created by root on 24/5/15.
 */

public class LocationDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "locationsManager";

    // Contacts table name
    private static final String TABLE_LOCATIONS = "locations";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_LOC = "location";
    private static final String KEY_COUNTRY = "country";

    public LocationDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOCATIONS_TABLE = "CREATE TABLE " + TABLE_LOCATIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LOC + " TEXT,"
                + KEY_COUNTRY + " TEXT" + ")";
        db.execSQL(CREATE_LOCATIONS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);

        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public void addLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOC, location.getLocationName()); // Contact Location Name
        values.put(KEY_COUNTRY, location.getCountryName()); // Contact Country name

        // Inserting Row
        db.insert(TABLE_LOCATIONS, null, values);
        db.close(); // Closing database connection
    }


    // Getting single location
    public Location getLocation(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOCATIONS, new String[] { KEY_ID,
                        KEY_LOC, KEY_COUNTRY }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Location location = new Location(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return location
        return location;
    }

    // Getting All Locations
    public List<Location> getAllLocations() {
        List<Location> locationList = new ArrayList<Location>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LOCATIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Location location = new Location();
                location.setId(Integer.parseInt(cursor.getString(0)));
                location.setLocationName(cursor.getString(1));
                location.setCountryName(cursor.getString(2));
                // Adding location to list
                locationList.add(location);
            } while (cursor.moveToNext());
        }

        // return location list
        return locationList;
    }

    // Getting locations Count
    public int getLocationsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOCATIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating single location
    public int updateLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOC, location.getLocationName());
        values.put(KEY_COUNTRY, location.getCountryName());

        // updating row
        return db.update(TABLE_LOCATIONS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(location.getId()) });
    }

    // Deleting single location
    public void deleteLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOCATIONS, KEY_ID + " = ?",
                new String[] { String.valueOf(location.getId()) });
        db.close();
    }
}
