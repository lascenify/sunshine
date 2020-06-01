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
    ForecastEntity::class
], version = 8, exportSchema = false)
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