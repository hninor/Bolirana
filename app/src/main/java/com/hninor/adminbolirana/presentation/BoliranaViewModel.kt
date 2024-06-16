package com.hninor.adminbolirana.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hninor.adminbolirana.data.ChicoDB
import com.hninor.adminbolirana.data.ChicoRepository
import com.hninor.adminbolirana.domain.Chico
import com.hninor.adminbolirana.domain.Jugador
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.util.Date
import javax.inject.Inject


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

@HiltViewModel
class BoliranaViewModel @Inject constructor(private val repository: ChicoRepository) : ViewModel() {


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

    init {
        loadChicos()
    }

    fun loadChicos() {
        viewModelScope.launch {
            val listaChicosDB = repository.getAll()
            var count = 0
            listaChicos = listaChicosDB.map {
                Chico(
                    id = it.id,
                    jugadores = getJugadores(it.jugadores),
                    perdedor = getPerdedor(it.perdedor),
                    valorChico = it.valorChico,
                    fecha = it.fecha,
                    puntosChico = it.puntosChico,
                    orden = ++count,
                    pendienteDePago = it.pendienteDePago
                )
            }.reversed()
        }

    }

    private fun getPerdedor(perdedor: String?): Jugador? {
        if (perdedor != null) {
            return Jugador(perdedor)
        }

        return null
    }

    private fun getJugadores(jugadores: String): List<Jugador> {
        val myType = object : TypeToken<List<String>>() {}.type
        val lista = Gson().fromJson<List<String>>(jugadores, myType)
        return lista.map { Jugador(it) }
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

    fun update(idChico: Long, pededor: String) = viewModelScope.launch {
        repository.updatePerdedor(idChico, pededor)
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
            val jugadores = Gson().toJson(listaJugadores)
            val id = Date().time
            val chicoDB = ChicoDB(id, null, valor, Date(), puntos, jugadores)
            insert(chicoDB)
            val chico = Chico(id, lista, null, valor, chicoDB.fecha, puntos, listaChicos.size + 1)
            listaChicos = (listaChicos.reversed() + chico).reversed()
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
        update(chicoSeleccionado.id, name)
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

    fun repetirChico() {
        val jugadores = Gson().toJson(chicoSeleccionado.jugadores.map { it.nombre })
        val id = Date().time
        val chicoDB = ChicoDB(
            id,
            null,
            chicoSeleccionado.valorChico,
            Date(),
            chicoSeleccionado.puntosChico,
            jugadores
        )
        insert(chicoDB)
        val chico = chicoSeleccionado.copy(id = id, perdedor = null, fecha = chicoDB.fecha, orden = listaChicos.size + 1)
        listaChicos = (listaChicos.reversed() + chico).reversed()
        chicoSeleccionado = chico
    }

    fun pagarChico(chico: Chico) {
        val chicoAPagar = listaChicos.find { it.id == chico.id }
        chicoAPagar?.pendienteDePago = false
        updatePendientePago(chico.id, false)
        message = "Chico pagado"
    }

    fun updatePendientePago(idChico: Long, pendientePago: Boolean) = viewModelScope.launch {
        repository.updatePendientePago(idChico, pendientePago)
    }

    fun pagarDeudaTotal(jugador: String) {
        val listaChicosPerdidos = listaChicos.filter { it.perdedor?.nombre == jugador }
        listaChicosPerdidos.forEach {
            updatePendientePago(it.id, false)
            it.pendienteDePago = false
        }
        message = "Deuda pagada"

    }


}