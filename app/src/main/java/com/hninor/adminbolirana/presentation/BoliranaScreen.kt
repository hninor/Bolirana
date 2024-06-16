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

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hninor.adminbolirana.MyAlertDialog
import com.hninor.adminbolirana.R

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@Composable
fun CupcakeAppBar(
    currentScreen: CupcakeScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    goToListaDeudas: () -> Unit,
    deleteChicos: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = { goToListaDeudas() }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }

            IconButton(onClick = { deleteChicos() }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}

@Composable
fun BoliranaApp(
    viewModel: BoliranaViewModel,
    navController: NavHostController = rememberNavController()
) {


    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = CupcakeScreen.valueOf(
        backStackEntry?.destination?.route ?: CupcakeScreen.ListaChicos.name
    )
    Scaffold(
        topBar = {
            val openDialog = remember { mutableStateOf(false) }
            CupcakeAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                goToListaDeudas = {
                    viewModel.loadDeudas()
                    navController.navigate(CupcakeScreen.ListaDeudas.name) {
                        popUpTo(CupcakeScreen.ListaChicos.name)
                    }
                },
                deleteChicos = {
                    openDialog.value = true
                }
            )

            MyAlertDialog(
                "Atención",
                "¿Está seguro de eliminar todos los chicos?",
                openDialog
            ) {
                viewModel.deleteChicos()
            }
        },
        floatingActionButton = {
            if (currentScreen == CupcakeScreen.ListaChicos) {
                FloatingActionButton(onClick = {
                    viewModel.clearNuevoChico()
                    navController.navigate(CupcakeScreen.CrearChico.name)
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }

        },
        snackbarHost = { SnackbarHost(viewModel.snackbarHostState) },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CupcakeScreen.ListaChicos.name,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(route = CupcakeScreen.ListaChicos.name) {
                ListaChicosScreen(
                    viewModel.listaChicos,
                    onChicoCliked = {
                        if (it.perdedor == null) {
                            viewModel.chicoSeleccionado = it
                            navController.navigate(CupcakeScreen.ChicoEnJuego.name)
                        }

                    },
                    onNextButtonClicked = {

                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }

            composable(route = CupcakeScreen.CrearChico.name) {

                NuevoChicoScreen(
                    viewModel,
                    onNextButtonClicked = {
                        navController.navigate(CupcakeScreen.ChicoEnJuego.name) {
                            popUpTo(CupcakeScreen.ListaChicos.name)
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }

            composable(route = CupcakeScreen.ChicoEnJuego.name) {

                ChicoEnJuegoScreen(
                    viewModel.chicoSeleccionado,
                    onPerdedorConfirmadoClicked = {
                        viewModel.terminarChico(it)
                        navController.navigate(CupcakeScreen.ResultadoChico.name) {
                            popUpTo(CupcakeScreen.ListaChicos.name)
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }

            composable(route = CupcakeScreen.ResultadoChico.name) {
                ResultadoChicoScreen(
                    chico = viewModel.chicoSeleccionado,
                    deudaTotal = viewModel.getDeudaTotal(),
                    onRepetirChicoCliked = {
                        viewModel.repetirChico()
                        navController.navigate(CupcakeScreen.ChicoEnJuego.name) {
                            popUpTo(CupcakeScreen.ListaChicos.name)
                        }
                    },
                    onNuevoChicoClicked = {
                        viewModel.clearNuevoChico()
                        navController.navigate(CupcakeScreen.CrearChico.name) {
                            popUpTo(CupcakeScreen.ListaChicos.name)
                        }
                    },
                    onPagarChico = {
                        viewModel.pagarChico(it)
                    },
                    onPagoTotalDeuda = {
                        viewModel.pagarDeudaTotal(it)
                    },

                    modifier = Modifier.fillMaxHeight()
                )
            }


            composable(route = CupcakeScreen.ListaDeudas.name) {
                val context = LocalContext.current
                ListaDeudasScreen(lista = viewModel.listaDeudas, onPagarDeudaCliked = {
                    viewModel.pagarDeudaTotal(it.jugador)
                    viewModel.loadDeudas()
                }, onOpenDetailClicked = {
                    viewModel.deudaSeleccionada = it
                    navController.navigate(CupcakeScreen.DetalleDeudas.name)
                })
            }

            composable(route = CupcakeScreen.DetalleDeudas.name) {
                ListaChicosScreen(
                    viewModel.deudaSeleccionada.listaChicos,
                    onChicoCliked = {

                    },
                    onNextButtonClicked = {

                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
        }
    }
}


private fun cancelOrderAndNavigateToStart(
    viewModel: BoliranaViewModel,
    navController: NavHostController
) {
    //viewModel.resetOrder()
    navController.popBackStack(CupcakeScreen.CrearChico.name, inclusive = false)
}


enum class CupcakeScreen(@StringRes val title: Int) {
    CrearChico(title = R.string.crear_chico),
    ChicoEnJuego(title = R.string.chico_en_juego),
    ListaChicos(title = R.string.lista_chicos),
    ResultadoChico(title = R.string.resultado_chico),
    ListaDeudas(title = R.string.lista_deudas),
    DetalleDeudas(title = R.string.detalle_deudas)
}
