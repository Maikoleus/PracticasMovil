package com.example.imc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Añadido tema Material3 para consistencia
            MaterialTheme {
                CalculadoraIMC()
            }
        }
    }
}

@Composable
fun CalculadoraIMC() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "entrada") {
        composable("entrada") {
            PantallaEntrada(navController)
        }
        composable("resultado/{imc}") { backStackEntry ->
            val imcTexto = backStackEntry.arguments?.getString("imc") ?: "0.0"
            val imc = imcTexto.toDoubleOrNull() ?: 0.0
            PantallaResultado(imc) {
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun PantallaEntrada(navController: NavController) {
    var altura by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Calculadora de IMC",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = altura,
            onValueChange = {
                altura = it
                error = null // Limpiar error al editar
            },
            label = { Text("Altura (m)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = error != null,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = peso,
            onValueChange = {
                peso = it
                error = null // Limpiar error al editar
            },
            label = { Text("Peso (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = error != null,
            modifier = Modifier.fillMaxWidth()
        )

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                try {
                    val alturaNum = altura.toDouble()
                    val pesoNum = peso.toDouble()

                    if (alturaNum <= 0 || pesoNum <= 0) {
                        error = "Los valores deben ser mayores a cero"
                        return@Button
                    }

                    val imc = pesoNum / (alturaNum * alturaNum)
                    navController.navigate("resultado/${"%.2f".format(imc)}")
                } catch (e: NumberFormatException) {
                    error = "Ingresa valores numéricos válidos"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calcular IMC")
        }
    }
}

@Composable
fun PantallaResultado(imc: Double, onBack: () -> Unit) {
    val (clasificacion, color) = when {
        imc < 18.5 -> Pair("Bajo peso", MaterialTheme.colorScheme.primary)
        imc < 25 -> Pair("Normal", MaterialTheme.colorScheme.tertiary)
        imc < 30 -> Pair("Sobrepeso", MaterialTheme.colorScheme.error.copy(alpha = 0.8f))
        else -> Pair("Obesidad", MaterialTheme.colorScheme.error)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tu IMC es:",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "%.2f".format(imc),
            style = MaterialTheme.typography.displayMedium,
            color = color
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = clasificacion,
            style = MaterialTheme.typography.headlineSmall,
            color = color
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text("Volver a calcular")
        }
    }
}