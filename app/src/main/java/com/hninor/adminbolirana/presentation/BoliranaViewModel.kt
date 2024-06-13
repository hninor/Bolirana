package com.hninor.adminbolirana.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hninor.adminbolirana.data.ChicoDB
import com.hninor.adminbolirana.data.ChicoRepository
import com.hninor.adminbolirana.domain.Chico
import com.hninor.adminbolirana.domain.Jugador
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.util.Date


fun Long.formatThousand(): String {
    val decimalFormatter = DecimalFormat("#,###")
    return decimalFormatter.format(this)
}

fun String.clearThousandFormat(): String {
    return this.replace(",", "")
}

fun String.clearOtherCharacters(): String {
    return this.replace("[^(0-9)]".toRegex(), "").trim()
}

class BoliranaViewModel (private val repository: ChicoRepository): ViewModel() {


    var valorChico by mutableStateOf(TextFieldValue(""))
        private set

    var message by mutableStateOf("")
        private set

    var username by mutableStateOf("")
        private set

    var puntosChico by mutableStateOf("")
        private set

    var listaJugadores by mutableStateOf(listOf<String>())
        private set

    var listaChicos by mutableStateOf(listOf<Chico>())
        private set

    lateinit var chicoSeleccionado: Chico

    fun updateUsername(input: String) {
        username = input
    }

    fun updatePuntosChico(input: String) {
        puntosChico = input
    }



    fun updateValorChico(input: TextFieldValue) {
        val newValue = if (input.text.isNotEmpty()) {
            input.text.clearThousandFormat().clearOtherCharacters().toLong().formatThousand()
        } else {
            input.text
        }
        valorChico = input.copy(
            text = newValue,
            selection = TextRange(newValue.length)
        )
    }


    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(word: ChicoDB) = viewModelScope.launch {
        repository.insert(word)
    }
    fun agregarJugador() {
        val input = username.uppercase().trim()
        if (input.isNotEmpty()) {
            val jugador = listaJugadores.find { it == input }
            if (jugador == null) {
                listaJugadores = listaJugadores + input
                username = ""
                message = "Jugador $input agregado"
            } else {
                message = "El jugador ya se encuentra en la lista"
            }

        } else {
            message = "Nombre vacío"
        }
    }

    fun crearChico(): Boolean {
        if (valorChico.text.isNotEmpty() && listaJugadores.size > 1) {
            val puntos = if (puntosChico.isEmpty()) 0 else puntosChico.toInt()
            val lista = listaJugadores.map { Jugador(it) }
            val valor = valorChico.text.clearThousandFormat().clearThousandFormat().toLong()
            val chico = Chico(listaChicos.size + 1, lista, null, valor, Date(), puntos)
            listaChicos = listaChicos + chico
            chicoSeleccionado = chico
            return true
        } else {

            message = "Deben existir al menos dos jugadores y/o ingresar el valor del chico"

            return false

        }

    }

    fun updateMessage(s: String) {
        message = s
    }

    fun eliminarJugador(name: String) {

        listaJugadores = listaJugadores.toMutableList().apply {
            remove(name)
        }
    }

    fun terminarChico(name: String) {
        chicoSeleccionado.perdedor = chicoSeleccionado.jugadores.find { it.nombre == name }
    }

    fun getDeudaTotal(): Long {
        var result = 0L
        val jugadorPerdedor = chicoSeleccionado.perdedor
        jugadorPerdedor?.let { perdedor ->
            val listaChicosPerdidos = listaChicos.filter { it.perdedor == perdedor }
            result = listaChicosPerdidos.sumOf { it.valorChico }
        }

        return result
    }

    fun clearNuevoChico() {
        listaJugadores = emptyList()
        puntosChico = ""
        valorChico = TextFieldValue("")
    }


}