package com.example.app.ui.main_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.app.data.Game
import com.example.app.ui.main_screen.utils.Categories

@Preview (showBackground = true)
@Composable
fun GameListItemUi(
    showEditButton: Boolean = true,
    game: Game = Game(
        title = "Title",
        description = "Description",
        price = "100",
        category = Categories.ACTION
    ),
    onEditClick: (Game) -> Unit = {},
    onFavClick: () -> Unit = {},
    onGameClick: (Game) -> Unit = {},
    onDeleteClick: (Game) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onGameClick(game)
            }
    ) {
        AsyncImage(
            model = game.imageUrl,
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(15.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

        //название
        Text(
            text = game.title,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(
            modifier = Modifier
                .height(5.dp)
        )

        //описание
        Text(
            text = game.description,
            color = Color.Gray,
            fontSize = 16.sp,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis // многоточие в конце
        )

        Spacer(
            modifier = Modifier
                .height(5.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            //цена
            Text(
                modifier = Modifier.fillMaxWidth()
                    .weight(1f),
                text = game.price,
                color = Color.Blue,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            //избранное
            IconButton(
                onClick = {
                    onFavClick()
                }
            ) {
                Icon(
                    if (game.isFavorite) {
                        Icons.Default.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    },
                    contentDescription = "Favorites"
                )
            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            //редактор и удаление
            if (showEditButton) {
                IconButton(
                    onClick = {
                        onEditClick(game)
                    }
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }

                IconButton(
                    onClick = {
                        onDeleteClick(game)
                    }
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }

            }
        }

    }
}