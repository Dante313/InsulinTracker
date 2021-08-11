package com.dante.android.insulintracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dante.android.insulintracker.model.Insulin

@Database(entities = [Insulin::class], version = 1, exportSchema = false)
abstract class InsulinDatabase: RoomDatabase() {

    abstract fun insulinDao(): InsulinDao

    companion object {
        @Volatile
        private var INSTANCE: InsulinDatabase? = null

        fun getDatabase(context: Context): InsulinDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InsulinDatabase::class.java,
                    "insulin_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}