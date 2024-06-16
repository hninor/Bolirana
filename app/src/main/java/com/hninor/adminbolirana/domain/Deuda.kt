package com.hninor.adminbolirana.domain

data class Deuda(val jugador: String, val chicosPerdidos: Int, val deudaTotal: Long, val listaChicos: List<Chico>)
