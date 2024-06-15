package com.hninor.adminbolirana.domain

import java.util.Date

data class Chico(
    val id: Long,
    val jugadores: List<Jugador>,
    var perdedor: Jugador?,
    val valorChico: Long,
    val fecha: Date,
    val puntosChico: Int,
    val orden: Int,
    var pendienteDePago: Boolean = true
)
