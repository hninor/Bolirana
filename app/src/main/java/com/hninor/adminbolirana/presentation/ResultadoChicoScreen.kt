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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun ResultadoChicoScreen(
    chico: Chico,
    deudaTotal: Long,
    onRepetirChicoCliked: () -> Unit,
    onNuevoChicoClicked: () -> Unit,
    onPagarChico: (chico: Chico) -> Unit,
    onPagoTotalDeuda: (jugador: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val openDialog = remember { mutableStateOf(false) }
    val openDialogTotal = remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(
            "${chico.perdedor?.nombre}",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            "has perdido este chico!",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        Text("Deuda chico: $ ${chico.valorChico.formatThousand()}", fontSize = 24.sp)
        Button(

            onClick = {
                openDialog.value = true
            }
        ) {
            Text(context.getString(R.string.pagar))
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))


        Text(
            "Deuda total: $ ${deudaTotal.formatThousand()}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Button(

            onClick = {
                openDialogTotal.value =  true
            }
        ) {
            Text(context.getString(R.string.pagar))
        }


        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))



        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(

                onClick = {
                    onRepetirChicoCliked()
                }
            ) {
                Text("Repetir Chico")
            }

            Button(

                onClick = {
                    onNuevoChicoClicked()
                }
            ) {
                Text("Nuevo Chico")
            }
        }

        MyAlertDialog(
            "Atención",
            "¿Recibió $ ${chico.valorChico.formatThousand()} pesos?",
            openDialog
        ) {
            onPagarChico(chico)
        }

        MyAlertDialog(
            "Atención",
            "¿Recibió $ ${deudaTotal.formatThousand()} pesos?",
            openDialogTotal
        ) {
            onPagoTotalDeuda(chico.perdedor?.nombre ?: "")
        }

    }

}


@Preview
@Composable
fun ResultadoChicoPreview() {
    AdminBoliranaTheme {
        ResultadoChicoScreen(

            Chico(1, listOf(Jugador("Henry")), null, 1000L, Date(), 5000, 1),
            50000,
            onRepetirChicoCliked = {},
            onNuevoChicoClicked = {},
            onPagarChico = {},
            onPagoTotalDeuda = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}
