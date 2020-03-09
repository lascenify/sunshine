package com.example.android.sunshine.data;

import android.net.Uri;
import android.provider.BaseColumns;
import com.example.android.sunshine.utilities.SunshineDateUtils;
public class WeatherContract{
    static final String CONTENT_AUTHORITY =  "com.example.android.sunshine";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    static final String PATH_WEATHER = "weather";
    /* Inner class that defines the table contents of the weather table */
    public static final class WeatherEntry implements BaseColumns{

        /**
         * Returns just the selection part of the weather query from a normalized today value.
         * This is used to get a weather forecast from today's date. To make this easy to use
         * in compound selection, we embed today's date as an argument in the query.
         *
         * @return The selection part of the weather query for today onwards
         */
        String sqlSelectForTodayOnwards;
        public static String getSqlSelectForTodayOnwards() {
            long normalizedUtcNow = SunshineDateUtils.normalizeDate(System.currentTimeMillis());
            return COLUMN_DATE + " >= " + normalizedUtcNow;
        }

        /**
         * Builds a URI that adds the weather date to the end of the forecast content URI path.
         * This is used to query details about a single weather entry by date. This is what we
         * use for the detail view query. We assume a normalized date is passed to this method.
         *
         * @param date Normalized date in milliseconds
         * @return Uri to query details about a single weather entry
         */
        public static Uri buildWeatherUriWithDate(Long date){
            return CONTENT_URI.buildUpon()
                    .appendPath(date.toString())
                    .build();
        }

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_WEATHER)
                .build();
        /* Used internally as the name of our weather table. */
        public static final String TABLE_NAME = "weather";
        public static final String _ID = "_id";
        public static final String COLUMN_DATE = "date";
        /* Weather ID as returned by API, used to identify the icon to be used */
        public static final String COLUMN_WEATHER_ID = "weather_id";
        /* Min and max temperatures in °C for the day (stored as floats in the database) */
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_MAX_TEMP = "max";
        /* Humidity is stored as a float representing percentage */
        public static final String COLUMN_HUMIDITY = "humidity";
        /* Pressure is stored as a float representing percentage */
        public static final String COLUMN_PRESSURE = "pressure";
        /* Wind speed is stored as a float representing wind speed in mph */
        public static final String COLUMN_WIND_SPEED = "wind";
        /*
         * Degrees are meteorological degrees (e.g, 0 is north, 180 is south).
         * Stored as floats in the database.
         *
         * Note: These degrees are not to be confused with temperature degrees of the weather.
         */
        public static final String COLUMN_DEGREES = "degrees";

    }

}