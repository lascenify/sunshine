package com.example.android.sunshine.framework.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.sunshine.framework.Constants.Database.DATABASE_NAME
import com.example.android.sunshine.framework.db.dao.ForecastDao
import com.example.android.sunshine.framework.db.entities.*
import com.example.android.sunshine.utilities.DataConverter


@Database(entities = [
    //CityEntity::class,
    //CloudsEntity::class,
    //CoordinatesEntity::class,
    ForecastEntity::class
    //ForecastMainInfoEntity::class,
    //WindEntity::class
], version = 6, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase(){
    companion object{
        private val LOG_TAG = AppDatabase::class.java.simpleName
        private val LOCK = Any()
        private lateinit var sInstance: AppDatabase

        fun getInstance(context: Context): AppDatabase {
            synchronized(LOCK){
                Log.d(LOG_TAG, "Creating new database instance")
                sInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                .build()
                }

            Log.d(LOG_TAG, "Getting the database instance")
            return sInstance
        }
    }

    abstract fun weatherForecastDao(): ForecastDao
}

/*
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

}*/