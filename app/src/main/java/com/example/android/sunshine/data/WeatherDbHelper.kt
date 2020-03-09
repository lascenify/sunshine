package com.example.android.sunshine.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.android.sunshine.data.WeatherContract.WeatherEntry.COLUMN_DATE
import com.example.android.sunshine.data.WeatherContract.WeatherEntry.COLUMN_DEGREES
import com.example.android.sunshine.data.WeatherContract.WeatherEntry.COLUMN_HUMIDITY
import com.example.android.sunshine.data.WeatherContract.WeatherEntry.COLUMN_MAX_TEMP
import com.example.android.sunshine.data.WeatherContract.WeatherEntry.COLUMN_MIN_TEMP
import com.example.android.sunshine.data.WeatherContract.WeatherEntry.COLUMN_PRESSURE
import com.example.android.sunshine.data.WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
import com.example.android.sunshine.data.WeatherContract.WeatherEntry.COLUMN_WIND_SPEED
import com.example.android.sunshine.data.WeatherContract.WeatherEntry.TABLE_NAME

class WeatherDbHelper(context: Context) :SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        const val DATABASE_NAME = "weather.db"
        const val DATABASE_VERSION = 3
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE " + TABLE_NAME + " ( "+
                WeatherContract.WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_DATE + " INTEGER NOT NULL UNIQUE, " +
                COLUMN_DEGREES + " REAL NOT NULL, "+
                COLUMN_HUMIDITY + " REAL NOT NULL, "+
                COLUMN_MAX_TEMP + " REAL NOT NULL, " +
                COLUMN_MIN_TEMP + " REAL NOT NULL,"+
                COLUMN_PRESSURE + " REAL NOT NULL,"+
                COLUMN_WEATHER_ID + " INTEGER NOT NULL,"+
                COLUMN_WIND_SPEED + " REAL NOT NULL);"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}