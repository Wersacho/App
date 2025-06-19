package com.example.app.ui.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.app.ui.theme.LightRed

@Composable
fun LoginButton(
    modifier: Modifier = Modifier,
    text: String,
    showLoadIndicator: Boolean = false,
    onClick: () -> Unit
) {
    Button(onClick = {
        onClick()
    },
        modifier = modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = LightRed
        )
    ) {
        if (showLoadIndicator){
           CircularProgressIndicator(
               modifier = Modifier
                   .size(30.dp),
               color = Color.White
           )
        } else {
           Text(text = text)
        }

    }
}