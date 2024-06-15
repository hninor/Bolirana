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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun ListaChicosScreen(
    lista: List<Chico>,
    onChicoCliked: (Chico) -> Unit,
    onNextButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {



    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        items(items = lista) { chico ->

            ChicoItem(chico, onChicoCliked)
        }
    }


}

@Composable
fun ChicoItem(chico: Chico, onChicoCliked: (Chico) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable {
                onChicoCliked(chico)
            }
    ) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
            Text("Chico No. ${chico.orden}", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            Text("Número de jugadores: ${chico.jugadores.size}")
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            Text("Valor del chico:  $ ${chico.valorChico.formatThousand()}")
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            Text("Perdedor: ${chico.perdedor?.nombre ?: ""}")
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

            Text(if (chico.pendienteDePago) "PENDIENTE PAGO" else "PAGADO")
            Row(
                modifier = Modifier
                    .fillMaxWidth(), horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = if (chico.perdedor == null) "En curso" else "Finalizado",
                    color = MaterialTheme.colorScheme.inversePrimary,
                    fontSize = 24.sp
                )
            }
        }


    }
}


@Preview
@Composable
fun ListaChicosPreview() {
    AdminBoliranaTheme {
        ListaChicosScreen(
            listOf(
                Chico(1, listOf(Jugador("Henry")), null, 1000L, Date(), 5000, 1),
                Chico(2, listOf(Jugador("Paola")), null, 1000L, Date(), 5000, 2)
            ),
            onChicoCliked = {},
            onNextButtonClicked = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}
