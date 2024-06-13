package com.hninor.adminbolirana

import android.app.Application
import com.hninor.adminbolirana.data.BoliranaRoomDatabase
import com.hninor.adminbolirana.data.ChicoRepository

class BoliranaApp : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { BoliranaRoomDatabase.getDatabase(this) }
    val repository by lazy { ChicoRepository(database.chicoDao()) }
}