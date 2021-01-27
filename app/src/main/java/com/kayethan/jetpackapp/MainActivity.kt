package com.kayethan.jetpackapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kayethan.jetpackapp.ui.theme.JetpackAppTheme
import java.util.stream.IntStream.range

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ScreenContent()
                }
            }
        }
    }
}

fun czyPoprawna(pesel: String): Boolean
{
    val wagi = intArrayOf(
        1, 3, 7, 9, 1, 3, 7, 9, 1, 3, 1
    )
    var sum: Int = 0

    for (i in range(0, 11))
    {
        sum += pesel[i].toInt() * wagi[i]
    }

    return sum % 10 == 0
}

fun dataUrodzenia(pesel: String): String
{
    Log.i("TEST", pesel)
    var rok = 1900
    Log.i("TEST", "$rok")

    var temp = pesel[2].toInt() - 48
    Log.i("TEST", "$temp")

    if (temp > 1)
    {
        rok += 100
        temp -= 2
    }

    Log.i("TEST", "$temp")
    Log.i("TEST", "$rok")

    rok += ((pesel[0].toInt() - 48) * 10 + pesel[1].toInt() - 48)
    Log.i("TEST", "$rok")
    val miesiacStr = "${temp}${pesel[3]}"
    Log.i("TEST", "$miesiacStr")
    val dzienStr = "${pesel[4]}${pesel[5]}"
    Log.i("TEST", "$dzienStr")

    return "$dzienStr.$miesiacStr.$rok"
}

@Composable
fun ScreenContent() {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (text, peselInput, button, message) = createRefs()

        Text(
            "PESEL",
            modifier = Modifier.constrainAs(text) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }
        )

        val peselState = savedInstanceState {""}
        OutlinedTextField(
            value = peselState.value,
            singleLine = true,
            textStyle = TextStyle(textAlign = TextAlign.Center),
            onValueChange = {
                peselState.value = it
            },
            label = { Text("Wpisz PESEL") },
            modifier = Modifier.constrainAs(peselInput) {
                top.linkTo(text.bottom, margin = 8.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        val peselValidationMessage = savedInstanceState {""}
        Button(
                onClick = {
                    peselValidationMessage.value = ""
                    if (peselState.value.length != 11) {
                        peselValidationMessage.value = "Numer PESEL musi mieć 11 cyfr!"
                    } else {
                        val dataUrodzenia = dataUrodzenia(peselState.value)
                        val plec = if (peselState.value[9].toInt() % 2 == 0) "kobieta" else "mężczyzna"
                        val czySumaKontrolnaPoprawna = if (czyPoprawna(peselState.value)) "Suma kontrolna poprawna" else "Suma kontrolna niepoprawna"
                        peselValidationMessage.value = "Data urodzenia: $dataUrodzenia\nPłeć: $plec\n$czySumaKontrolnaPoprawna"
                    }
                },
                modifier = Modifier.constrainAs(button) {
                    top.linkTo(peselInput.bottom, margin = 8.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
        ) {
            Text(
                text = "Sprawdź PESEL",
                color = Color.White
            )
        }

        Text(
            peselValidationMessage.value,
            modifier = Modifier.constrainAs(message) {
                top.linkTo(button.bottom, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetpackAppTheme {
        ScreenContent()
    }
}