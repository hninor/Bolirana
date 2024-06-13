package com.hninor.adminbolirana

import android.content.Context
import androidx.room.Room
import com.hninor.adminbolirana.data.BoliranaRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, BoliranaRoomDatabase::class.java, "app_database").build()

    @Provides
    fun provideClavesDao(db: BoliranaRoomDatabase) = db.chicoDao()
}