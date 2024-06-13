package com.hninor.adminbolirana

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.hninor.adminbolirana.presentation.BoliranaApp
import com.hninor.adminbolirana.ui.theme.AdminBoliranaTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)


        setContent {
            AdminBoliranaTheme {
                BoliranaApp()
            }
        }


    }

    fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun FilledButtonExample(text: String, onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Text(text)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AdminBoliranaTheme {
        Column {
            Greeting("Android")
            FilledButtonExample("Nuevo chico") {

            }
        }

    }
}


@Composable
fun MyAlertDialog(
    title: String,
    content: String,
    shouldShowDialog: MutableState<Boolean>,
    lamda: () -> Unit
) {

    if (shouldShowDialog.value) { // 2
        AlertDialog( // 3
            onDismissRequest = { // 4
                shouldShowDialog.value = false
            },
            // 5
            title = { Text(text = title) },
            text = { Text(text = content) },
            confirmButton = { // 6
                Button(
                    onClick = {
                        shouldShowDialog.value = false
                        lamda()
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.aceptar),
                        color = Color.White
                    )
                }
            }
        )
    }
}
