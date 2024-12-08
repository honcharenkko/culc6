package com.example.culc6
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlin.math.pow
import kotlin.math.sqrt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ElectricLoadCalculator()
        }
    }
}



@Composable
fun ElectricLoadCalculator() {
    var efficiency by remember { mutableStateOf("") } // ηн
    var powerFactor by remember { mutableStateOf("") } // cos φ
    var voltage by remember { mutableStateOf("") } // Uн
    var quantity by remember { mutableStateOf("") } // n
    var nominalPower by remember { mutableStateOf("") } // Рн
    var usageFactor by remember { mutableStateOf("") } // КВ
    var reactivePowerFactor by remember { mutableStateOf("") } // tgφ

    var activePower by remember { mutableStateOf(0.0) } // Pp
    var reactivePower by remember { mutableStateOf(0.0) } // Qp
    var apparentPower by remember { mutableStateOf(0.0) } // Sp
    var calculatedCurrent by remember { mutableStateOf(0.0) } // Ip

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Electric Load Calculator", style = MaterialTheme.typography.titleLarge)

        // Input fields
        InputField("Efficiency (ηн)", efficiency) { efficiency = it }
        InputField("Power Factor (cos φ)", powerFactor) { powerFactor = it }
        InputField("Voltage (Uн, kV)", voltage) { voltage = it }
        InputField("Quantity (n)", quantity) { quantity = it }
        InputField("Nominal Power (Pн, kW)", nominalPower) { nominalPower = it }
        InputField("Usage Factor (КВ)", usageFactor) { usageFactor = it }
        InputField("Reactive Power Factor (tgφ)", reactivePowerFactor) { reactivePowerFactor = it }

        Button(
            onClick = {
                // Парсинг введених значень
                val ηн = efficiency.toDoubleOrNull() ?: 0.0
                val cosφ = powerFactor.toDoubleOrNull() ?: 0.0
                val Uн = voltage.toDoubleOrNull() ?: 0.0
                val n = quantity.toDoubleOrNull() ?: 0.0
                val Pн = nominalPower.toDoubleOrNull() ?: 0.0
                val КВ = usageFactor.toDoubleOrNull() ?: 0.0
                val tgφ = reactivePowerFactor.toDoubleOrNull() ?: 0.0

                // Розрахунки
                activePower = n * КВ * Pн
                reactivePower = activePower * tgφ
                apparentPower = sqrt(activePower.pow(2) + reactivePower.pow(2))
                calculatedCurrent = (apparentPower * 1000) / (1.732 * Uн * 1000)
            }
        ) {
            Text("Calculate")
        }


        // Results
        Text("Results:", style = MaterialTheme.typography.titleMedium)
        Text("Active Power (Pp): ${"%.2f".format(activePower)} kW")
        Text("Reactive Power (Qp): ${"%.2f".format(reactivePower)} kvar")
        Text("Apparent Power (Sp): ${"%.2f".format(apparentPower)} kVA")
        Text("Calculated Current (Ip): ${"%.2f".format(calculatedCurrent)} A")
    }
}

@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}
