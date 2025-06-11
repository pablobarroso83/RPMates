package com.rpmates.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rpmates.R
import androidx.compose.foundation.layout.*
import kotlinx.coroutines.delay
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rpmates.RPMatesApplication
import com.rpmates.viewmodel.UserViewModel

@Composable
fun SplashScreen(navController: NavHostController) {
    val application = LocalContext.current.applicationContext as RPMatesApplication
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModel.Factory(application.repository)
    )
    val currentUser by userViewModel.currentUser.collectAsState()

    LaunchedEffect(key1 = true) {
        delay(2500)
        // Redirigir según el estado de autenticación
        if (currentUser != null) {
            navController.navigate("HomeScreen") {
                popUpTo("SplashScreen") { inclusive = true }
            }
        } else {
            navController.navigate("LoginScreen") {
                popUpTo("SplashScreen") { inclusive = true }
            }
        }
    }

    Splash()
}

@Composable
fun Splash(){

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(id = R.drawable.vinyl),
            contentDescription = "Logo vinilo",
            modifier = Modifier
                .size(100.dp,100.dp)

        )

        Text(
            text = "RPMmates",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
            )
    }

}
