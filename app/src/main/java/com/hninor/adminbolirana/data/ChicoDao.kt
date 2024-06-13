package com.hninor.adminbolirana.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ChicoDBDao {
    @Query("SELECT * FROM ChicoDB")
    fun getAll(): List<ChicoDB>

    @Query("SELECT * FROM ChicoDB WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<ChicoDB>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: ChicoDB)
    @Insert
    fun insertAll(vararg users: ChicoDB)

    @Delete
    fun delete(user: ChicoDB)
}