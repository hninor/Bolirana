package com.hninor.adminbolirana.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ChicoDBDao {
    @Query("SELECT * FROM ChicoDB")
    suspend fun getAll(): List<ChicoDB>

    @Query("SELECT * FROM ChicoDB WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<ChicoDB>

    @Query("UPDATE ChicoDB SET perdedor = :perdedor WHERE id = :id")
    suspend fun update(perdedor: String, id: Long)


    @Query("UPDATE ChicoDB SET pendienteDePago = :pendientePago WHERE id = :id")
    suspend fun updatePendientePago(pendientePago: Boolean, id: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: ChicoDB)

    @Insert
    fun insertAll(vararg users: ChicoDB)

    @Delete
    fun delete(user: ChicoDB)
}