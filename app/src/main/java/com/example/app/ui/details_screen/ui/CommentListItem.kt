package com.example.app.ui.details_screen.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.custom.StarsIndicator
import com.example.app.ui.details_screen.data.RatingData
import com.example.app.ui.theme.LightGray1
import com.example.app.ui.theme.PalePeriwinkle

@Composable
fun CommentListItem(
    modifier: Modifier = Modifier,
    onClick: (RatingData) -> Unit,
    ratingData: RatingData,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Card(
        modifier = modifier
            .clickable {
                onClick(ratingData)
            },
        colors = CardDefaults.cardColors(
            containerColor = PalePeriwinkle
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            StarsIndicator(
                rating = ratingData.rating
            )

            Spacer(
                modifier = Modifier
                    .width(8.dp)
            )

            Text(
                text = ratingData.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier
                    .width(6.dp)
            )

            Text(
                text = ratingData.message,
                fontSize = 14.sp,
                maxLines = maxLines,
                overflow = overflow
            )
        }
    }
}