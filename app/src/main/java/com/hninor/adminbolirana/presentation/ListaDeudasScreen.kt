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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hninor.adminbolirana.MyAlertDialog
import com.hninor.adminbolirana.R
import com.hninor.adminbolirana.domain.Deuda
import com.hninor.adminbolirana.ui.theme.AdminBoliranaTheme

/**
 * Composable that allows the user to select the desired cupcake quantity and expects
 * [onNextButtonClicked] lambda that expects the selected quantity and triggers the navigation to
 * next screen
 */
@Composable
fun ListaDeudasScreen(
    lista: List<Deuda>,
    onPagarDeudaCliked: (Deuda) -> Unit,
    onOpenDetailClicked: (Deuda) -> Unit,
    modifier: Modifier = Modifier
) {


    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        items(items = lista) { chico ->

            DeudaItem(chico, onPagarDeudaCliked, onOpenDetailClicked)
        }
    }


}

@Composable
fun DeudaItem(
    deuda: Deuda,
    onPagarDeudaCliked: (Deuda) -> Unit,
    onOpenDetailClicked: (Deuda) -> Unit
) {
    val openDialog = remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)

    ) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
            Text("JUGADOR: ${deuda.jugador}", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            Text("Chicos perdidos: ${deuda.chicosPerdidos}")
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            Text("Deuda total:  $ ${deuda.deudaTotal.formatThousand()}")
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ClickableText(
                    style = TextStyle(
                        color = White,
                        fontSize = 26.sp,
                        fontFamily = FontFamily.Cursive
                    ),
                    text = AnnotatedString(stringResource(id = R.string.ver_detalle)),
                    onClick = {
                        onOpenDetailClicked(deuda)
                    })

                ClickableText(
                    style = TextStyle(
                        color = White,
                        fontSize = 26.sp,
                        fontFamily = FontFamily.Cursive
                    ),
                    text = AnnotatedString(stringResource(id = R.string.pagar)),
                    onClick = {
                        openDialog.value = true
                    })
            }


        }


    }



    MyAlertDialog(
        "Atención",
        "¿Recibió $ ${deuda.deudaTotal.formatThousand()} pesos?",
        openDialog
    ) {
        onPagarDeudaCliked(deuda)
    }
}


@Preview
@Composable
fun ListaDeudasPreview() {
    AdminBoliranaTheme {
        ListaDeudasScreen(
            listOf(
                Deuda("HENRY", 3, 5000L, emptyList()),
                Deuda("JUAN", 4, 50000L, emptyList()),
            ),
            onPagarDeudaCliked = {},
            onOpenDetailClicked = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}
