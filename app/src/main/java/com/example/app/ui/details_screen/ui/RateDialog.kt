package com.example.app.ui.details_screen.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app.ui.details_screen.data.RatingData


@Composable
fun RateDialog(
    ratingData: RatingData = RatingData(),
    onDismiss: () -> Unit = {},
    onSubmit: (Int, String) -> Unit = {_, _ ->},
    show: Boolean = false
) {
    var selectedRate by remember { mutableIntStateOf(0) }
    var messageState by remember { mutableStateOf("") }
    selectedRate = ratingData.rating
    messageState = ratingData.message

    if (show) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = {
                Text(text = "Оцените товар")
            },
            text = {
                Column(
                    Modifier
                        .fillMaxWidth()
                ) {

                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        for (rating in 1 ..5){
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clickable {
                                        selectedRate = rating
                                    }
                            ){
                                Icon(
                                    modifier = Modifier
                                        .size(32.dp),
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = if (rating <= selectedRate) {
                                        Color(0xFFFFC107)
                                    } else {
                                        Color.Gray
                                    }
                                )
                            }
                        }
                    }

                    TextField(
                        value = messageState,
                        onValueChange = { newValue ->
                            if (newValue.length <= 200) {
                                messageState = newValue
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp),
                        placeholder = {
                            Text(
                                text = "Комментарий"
                            )
                        },
                        maxLines = 10
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSubmit(selectedRate, messageState)
                    }
                ) {
                    Text(text = "Отправить")
                }
            }
        )
    }
}