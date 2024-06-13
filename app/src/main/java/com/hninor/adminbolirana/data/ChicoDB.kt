package com.hninor.adminbolirana.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hninor.adminbolirana.domain.Jugador
import java.util.Date
@Entity
data class ChicoDB(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var perdedor: String?,
    val valorChico: Long,
    val fecha: Date,
    val puntosChico: Int
)