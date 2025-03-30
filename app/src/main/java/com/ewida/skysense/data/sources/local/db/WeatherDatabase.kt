package com.ewida.skysense.data.sources.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ewida.skysense.data.model.WeatherAlert
import com.ewida.skysense.data.model.WeatherDetails
import com.ewida.skysense.data.sources.local.db.typeconverters.WeatherConverters

@Database(entities = [WeatherDetails::class, WeatherAlert::class], version = 2)
@TypeConverters(WeatherConverters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun getDao(): WeatherDao

    companion object {
        private const val DATABASE_NAME = "WEATHER_DATABASE"
        @Volatile
        private var instance: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase {
            return instance ?: synchronized(lock = this) {
                val db = Room.databaseBuilder(
                    context = context,
                    klass = WeatherDatabase::class.java,
                    name = DATABASE_NAME
                ).fallbackToDestructiveMigration().build()
                instance = db
                db
            }
        }
    }
}