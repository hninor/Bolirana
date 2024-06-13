package com.hninor.adminbolirana.domain

import java.util.Date

data class Chico(val id: Int, val jugadores: List<Jugador>, var perdedor: Jugador?, val valorChico: Long, val fecha: Date, val puntosChico: Int)
