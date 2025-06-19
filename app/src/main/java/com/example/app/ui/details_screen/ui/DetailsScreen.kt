package com.example.app.ui.details_screen.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.app.R
import com.example.app.ui.comments_screen.CommentsNavData
import com.example.app.ui.details_screen.DetailsScreenViewModel
import com.example.app.ui.details_screen.data.DetailsNavObject
import com.example.app.ui.details_screen.data.RatingData
import com.example.app.ui.details_screen.data.toCommentsNavData
import com.example.app.ui.theme.LightRed

@SuppressLint("DefaultLocale")
@Composable
fun DetailsScreen(
    onCommentClick: (CommentsNavData) -> Unit = {},
    navObject: DetailsNavObject = DetailsNavObject(),

    viewModel: DetailsScreenViewModel = hiltViewModel()
) {
    var showRateDialog by remember { mutableStateOf(false) }
    var showCommentDialog by remember { mutableStateOf(false) }
    var ratingDataToShow by remember { mutableStateOf(RatingData()) }

    LaunchedEffect(key1 = Unit) {
        viewModel.getGameComments(navObject.gameId)
    }

    RateDialog(
        ratingData = viewModel.ratingDataState.value ?: RatingData(),
        onDismiss = {
            showRateDialog = false
        },
        onSubmit = { rating, message ->
            val ratingData = RatingData(
                name ="",
                rating = rating,
                message = message,
                lastRating = viewModel.ratingDataState.value?.rating ?: 0
            )
            showRateDialog = false
            viewModel.insertRating(ratingData, navObject.gameId)
        },
        show = showRateDialog
    )

    CommentDialog(
        showDialog = showCommentDialog,
        onDismiss = {
            showCommentDialog = false
        },
        ratingData = ratingDataToShow
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
        ) {
            AsyncImage(
                model = navObject.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .align(Alignment.Center),
                contentScale = ContentScale.Fit
            )
        }


        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {

                Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = navObject.title,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.Black
            )


            Spacer(modifier = Modifier.height(12.dp))

            Column {
                Text(text = "Платформа", color = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_windows),
                    contentDescription = "Windows",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Категория", color = Color.Gray)
            Text(
                text = stringArrayResource(id = R.array.category_array)[navObject.categoryIndex],
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Издатель", color = Color.Gray)
            Text(text = "BANDAI NAMCO Entertainment", fontSize = 16.sp, color = Color.Black)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Дата выхода", color = Color.Gray)
            Text(text = "29 мая 2025 г.", fontSize = 16.sp, color = Color.Black)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Оценка", color = Color.Gray)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onCommentClick(
                            navObject.toCommentsNavData()
                        )
                    }
            ) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (navObject.ratingsList.isEmpty()) {
                        "--"
                    } else {
                        String.format("%.1f", navObject.ratingsList.average())
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFFC107)
                )
                Text(
                    text = if (navObject.ratingsList.isEmpty()) {
                        " (0)"
                    } else {
                        " (${navObject.ratingsList.size})"
                    },
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Об игре", color = Color.Gray, fontSize = 14.sp)

            Text(text = navObject.description, fontSize = 16.sp, color = Color.Black)


        }



        if (viewModel.commentsState.value.isNotEmpty()) {

            Text(
                text = "Комментарии",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
                    .padding(horizontal = 16.dp)
            ) {
                items(viewModel.commentsState.value) { RatingData ->
                    CommentListItem(
                        modifier = Modifier.width(250.dp).fillMaxHeight(),
                        onClick = { rData ->
                            showCommentDialog = true
                            ratingDataToShow = rData
                        },
                        ratingData = RatingData,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {

            Button(
                onClick = {
                    viewModel.getUserRating(gameId = navObject.gameId)
                    showRateDialog = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                border = BorderStroke(1.dp, LightRed),
            ) {
                Text(text = "Оценить игру", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightRed
                )
            ) {
                Text(text = "Купить ключ за ${navObject.price} руб.", color = Color.White)
            }
        }
    }
}
