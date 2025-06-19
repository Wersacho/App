package com.example.app.ui.main_screen.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.R
import com.example.app.ui.theme.DarkRed
import com.example.app.ui.theme.PaleWhite

@Composable
fun DrawerHeader(
    email: String
) {
    Column(
        Modifier
            .fillMaxWidth()
            .height(170.dp)
            .background(DarkRed),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(90.dp).clip(RoundedCornerShape(24.dp)),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = ""
        )

        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

        Text(
            text = "ИгроХаб",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier
                .height(8.dp)
        )

        Text(
            text = email,
            color = PaleWhite,
        )
    }
}