package com.rpmates.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


import com.rpmates.R


@Composable
fun SecondScreen(onBack: () -> Unit,usuario:String,password:String) {
    Column(
        modifier = Modifier
            .background(Color.Gray)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Créditos",
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f)
            )
        Image(
            painter = painterResource(id = R.drawable.vinyl),
            contentDescription = "Vinilo",
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f),
            contentScale = ContentScale.Fit
        )
        Text(
            text = "User: $usuario\nPassword: $password",
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f)
        )


        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
            ) {
            Text("Volver al inicio")
        }
    }
}
