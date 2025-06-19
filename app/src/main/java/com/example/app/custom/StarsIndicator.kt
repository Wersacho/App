package com.example.app.custom

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.app.ui.details_screen.data.RatingData

@Composable
fun StarsIndicator(
    rating: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        for (i in 1..rating) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Rating",
                tint = Color(0xFFFFC107)
            )

            Spacer(
                modifier = Modifier
                    .width(5.dp)
            )
        }
    }
}