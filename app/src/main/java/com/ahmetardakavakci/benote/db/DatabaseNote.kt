package com.ahmetardakavakci.benote.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note :: class], version = 1)
abstract class DatabaseNote : RoomDatabase() {

    abstract fun daoNote(): DaoNote

    companion object {

        @Volatile
        private var INSTANCE : DatabaseNote? = null

        fun getDatabase(context : Context): DatabaseNote{

            val tempInstance = INSTANCE
            if (tempInstance != null) {
               return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseNote::class.java,
                    "note_db"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }

        }

    }

}
