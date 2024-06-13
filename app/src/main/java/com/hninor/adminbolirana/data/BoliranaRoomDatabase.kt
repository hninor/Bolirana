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

}