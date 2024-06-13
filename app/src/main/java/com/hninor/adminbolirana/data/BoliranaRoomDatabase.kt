package com.hninor.adminbolirana.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hninor.adminbolirana.domain.Chico

@Database(entities = arrayOf(ChicoDB::class), version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
public abstract class BoliranaRoomDatabase : RoomDatabase() {

    abstract fun chicoDao(): ChicoDBDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: BoliranaRoomDatabase? = null

        fun getDatabase(context: Context): BoliranaRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BoliranaRoomDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}