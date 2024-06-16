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

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hninor.adminbolirana.R
import com.hninor.adminbolirana.ui.theme.AdminBoliranaTheme

/**
 * Composable that allows the user to select the desired cupcake quantity and expects
 * [onNextButtonClicked] lambda that expects the selected quantity and triggers the navigation to
 * next screen
 */
@Composable
fun NuevoChicoScreen(
    viewModel: BoliranaViewModel,
    onNextButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier,

        ) {


        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {


            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.puntosChico,
                onValueChange = { username -> viewModel.updatePuntosChico(username) },
                label = { Text("Ingrese puntos del chico") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,

                )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.valorChico,
                onValueChange = { valor -> viewModel.updateValorChico(valor) },
                label = { Text("Ingrese valor del chico") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true

            )

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = viewModel.username,
                    onValueChange = { username -> viewModel.updateUsername(username) },
                    label = { Text("Ingrese jugador") },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true
                )
                IconButton(
                    onClick = { viewModel.agregarJugador() },
                    modifier = Modifier.size(60.dp),
                ){
                    Icon(imageVector = Icons.Default.PersonAdd, contentDescription = "Settings")
                }

            }




            SelectQuantityButton(labelResourceId = R.string.iniciar_chico, onClick = {
                if (viewModel.crearChico()) {
                    onNextButtonClicked()
                }
            })


        }




        MessageList(false, viewModel.listaJugadores) { jugador ->
            viewModel.eliminarJugador(jugador)
        }

        ShowMessage(viewModel)


    }


}


@Composable
fun MessageList(
    accionPerdedorChico: Boolean,
    messages: List<String>,
    onClickItem: (String) -> Unit
) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(messages) { message ->
            MessageRow(message, accionPerdedorChico, onClickItem)
        }
    }
}

@Composable
fun ShowMessage(viewModel: BoliranaViewModel) {
    val context = LocalContext.current
    if (viewModel.message.isNotEmpty()) {
        Toast.makeText(context, viewModel.message, Toast.LENGTH_LONG).show()
        viewModel.updateMessage("")
    }
}

/**
 * Customizable button composable that displays the [labelResourceId]
 * and triggers [onClick] lambda when this composable is clicked
 */
@Composable
fun SelectQuantityButton(
    @StringRes labelResourceId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.widthIn(min = 250.dp)
    ) {
        Text(stringResource(labelResourceId))
    }
}

@Composable
fun MessageRow(message: String, accionPerdedorChico: Boolean, onClickItem: (String) -> Unit) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        CardContent(name = message, accionPerdedorChico, onClickItem)
    }

}

@Composable
private fun CardContent(name: String, accionPerdedorChico: Boolean, onClickItem: (String) -> Unit) {


    Surface(
        color = MaterialTheme.colorScheme.primary,

        ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),


            ) {

            Image(
                painter = painterResource(id = R.drawable.sapo),
                contentDescription = "EMOTICON",
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .align(Alignment.CenterHorizontally)


            )

            Text(text = name, modifier = Modifier.align(Alignment.CenterHorizontally))


            IconButton(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    onClickItem(name)
                    /*                   if (accionPerdedorChico) {
                                           viewModel.mostrarDialogoConfirmacion(name)
                                       } else {
                                           viewModel.eliminarJugador(name)
                                       }*/
                },
            ) {
                Icon(
                    imageVector = if (accionPerdedorChico) Icons.Filled.Check else Icons.Filled.Delete,
                    contentDescription =
                    stringResource(R.string.show_less)

                )
            }


        }

    }
}

@Preview
@Composable
fun StartOrderPreview() {
    AdminBoliranaTheme {
        NuevoChicoScreen(
            viewModel = viewModel(),
            onNextButtonClicked = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}
