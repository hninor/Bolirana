/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hninor.adminbolirana.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hninor.adminbolirana.MyAlertDialog
import com.hninor.adminbolirana.R
import com.hninor.adminbolirana.domain.Chico
import com.hninor.adminbolirana.domain.Jugador
import com.hninor.adminbolirana.ui.theme.AdminBoliranaTheme
import java.util.Date

/**
 * Composable that allows the user to select the desired cupcake quantity and expects
 * [onNextButtonClicked] lambda that expects the selected quantity and triggers the navigation to
 * next screen
 */
@Composable
fun ChicoEnJuegoScreen(
    chico: Chico,
    onPerdedorConfirmadoClicked: (name: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        val openDialog = remember { mutableStateOf(false) }
        val perdedor = remember { mutableStateOf("") }
        Text(text = "Puntos del chico: ${chico.puntosChico}", Modifier.padding(4.dp))
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        Text(
            text = "Número de jugadores: ${chico.jugadores.size}",
            Modifier.padding(4.dp)
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        Text(text = "Valor del chico: ${chico.valorChico.formatThousand()}", Modifier.padding(4.dp))
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        Text(text = context.getString(R.string.seleccione_perdedor), Modifier.padding(4.dp))
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        MessageList(true, chico.jugadores.map { it.nombre }) {
            openDialog.value = true
            perdedor.value = it
        }
        MyAlertDialog(
            "Atención",
            "¿Está seguro que ${perdedor.value} perdío el chico?",
            openDialog
        ) {
            onPerdedorConfirmadoClicked(perdedor.value)
        }

    }


}




@Preview
@Composable
fun ChicoEnJuegoPreview() {
    AdminBoliranaTheme {
        ChicoEnJuegoScreen(
            Chico(1, listOf(Jugador("Henry")), null, 1000L, Date(), 5000, 1),
            onPerdedorConfirmadoClicked = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}
