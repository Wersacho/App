package com.example.app.ui.add_game_screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.app.R
import com.example.app.ui.login.LoginButton
import com.example.app.ui.login.RoundedCornerTextField
import com.example.app.ui.theme.BoxFilterColor

@Preview(showBackground = true)
@Composable
fun AddBookScreen() {
    val title = remember {
        mutableStateOf("")
    }

    val description = remember {
        mutableStateOf("")
    }

    val price = remember {
        mutableStateOf("")
    }

    val selectedImageUri = remember {
        mutableStateOf<Uri?>(null)
    }

    Image(painter = painterResource(id = R.drawable.games_store_bg),
        contentDescription = "Background",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Box(
        modifier = Modifier.fillMaxSize()
            .background(BoxFilterColor)
    )

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {

        //картинка
        val imageLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            selectedImageUri.value = uri
        }

        Image(
            painter = rememberAsyncImagePainter(model = selectedImageUri.value),
            contentDescription = "logo",
            modifier = Modifier.size(90.dp)
        )

        Spacer(
            modifier = Modifier
                .height(30.dp)
        )

        Text(
            text = "Добавить игру",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
            color = Color.White
        )

        Spacer(
            modifier = Modifier
                .height(30.dp)
        )

        //поле ввода заголовок

        RoundedCornerTextField(
            text = title.value,
            label = "Заголовок"
        ) {
            title.value = it
        }

        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

        //поле ввода описание

        RoundedCornerTextField(
            maxLines = 5,
            singleLine = false,
            text = description.value,
            label = "Описание"
        ) {
            description.value = it
        }

        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

        //поле ввода цена

        RoundedCornerTextField(
            text = title.value,
            label = "Цена"
        ) {
            price.value = it
        }

        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

        LoginButton(
            text = "Выбрать картинку"
        ) {
            imageLauncher.launch("image/*")
        }

        LoginButton(
            text = "Сохранить"
        ) {

        }

    }
}