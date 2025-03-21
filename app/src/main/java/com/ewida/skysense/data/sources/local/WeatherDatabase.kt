package com.ewida.skysense.data.sources.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase


abstract class WeatherDatabase : RoomDatabase() {
    abstract fun getDao(): WeatherDao

    companion object {
        private const val DATABASE_NAME = "WEATHER_DATABASE"
        @Volatile private var instance: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase {
            return instance ?: synchronized(lock = this) {
                val db = Room.databaseBuilder(
                    context = context,
                    klass = WeatherDatabase::class.java,
                    name = DATABASE_NAME
                ).build()
                instance = db
                db
            }
        }
    }
}