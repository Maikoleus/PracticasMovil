package com.example.imc
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.transiciones.ui.theme.TransicionesTheme
import org.w3c.dom.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavegacion()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppNavegacion (){
    val navControlador = rememberNavController()
    //definir el host
    NavHost(navControlador, startDestination = "actividad1")
    {
        composable("actividad1"){
            Actividad1(navControlador)
        }
        composable("actividad2/{mensaje}"){
            //TODO: actividad2
                backStackEntry -> Actividad2(
            backStackEntry.arguments?.getString("mensaje")?:
            "sin mensaje"
        )

        }
    }
}
@Composable
fun Actividad1(navControlador: NavController){
    Column (Modifier.fillMaxSize()){
        Text("Actividad 1")
        Button(onClick ={
            navControlador.navigate("actividad1/saludosCosa")
        }) {
            Text("ir a actividad2")
        }
    }
}

@Composable
fun Actividad2(mensaje:String){
    Column(Modifier.fillMaxSize()) {
        Text("Actividad2")

    }
}
