package com.lascenify.sunshine.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.lascenify.sunshine.data.WeatherContract.WeatherEntry.Companion.COLUMN_DATE
import com.lascenify.sunshine.data.WeatherContract.WeatherEntry.Companion.COLUMN_DEGREES
import com.lascenify.sunshine.data.WeatherContract.WeatherEntry.Companion.COLUMN_HUMIDITY
import com.lascenify.sunshine.data.WeatherContract.WeatherEntry.Companion.COLUMN_MAX_TEMP
import com.lascenify.sunshine.data.WeatherContract.WeatherEntry.Companion.COLUMN_MIN_TEMP
import com.lascenify.sunshine.data.WeatherContract.WeatherEntry.Companion.COLUMN_PRESSURE
import com.lascenify.sunshine.data.WeatherContract.WeatherEntry.Companion.COLUMN_WEATHER_ID
import com.lascenify.sunshine.data.WeatherContract.WeatherEntry.Companion.COLUMN_WIND_SPEED
import com.lascenify.sunshine.data.WeatherContract.WeatherEntry.Companion.TABLE_NAME

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