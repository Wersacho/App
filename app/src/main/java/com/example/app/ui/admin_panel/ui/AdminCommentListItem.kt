package com.example.app.ui.admin_panel.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.custom.StarsIndicator
import com.example.app.ui.details_screen.data.RatingData
import com.example.app.ui.login.ui.LoginButton
import com.example.app.ui.theme.LightGray1

@Composable
fun AdminCommentListItem(
    ratingData: RatingData = RatingData(
        name = "a",
        rating = 4,
        message = "s"
    ),
    onClickDecline: (RatingData) -> Unit = {},
    onClickAccept: (RatingData) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightGray1
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            StarsIndicator(
                rating = ratingData.rating
            )

            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )

            Text(
                text = ratingData.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(
                modifier = Modifier
                    .height(6.dp)
            )

            Text(
                text = ratingData.message,
                fontSize = 16.sp
            )

            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )

            Row(
                Modifier.fillMaxWidth(),
            ) {
                LoginButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = "Отклонить"
                ) {
                    onClickDecline(
                        ratingData
                    )
                }

                Spacer(
                    modifier = Modifier
                        .width(6.dp)
                )

                LoginButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = "Принять"
                ) {
                    onClickAccept(
                        ratingData
                    )
                }
            }
        }
    }
}