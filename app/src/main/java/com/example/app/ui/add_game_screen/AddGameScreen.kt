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
import com.example.app.data.Game
import com.example.app.ui.add_game_screen.data.AddScreenObject
import com.example.app.ui.login.LoginButton
import com.example.app.ui.login.RoundedCornerTextField
import com.example.app.ui.theme.BoxFilterColor
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

@Preview(showBackground = true)
@Composable
fun AddGameScreen(
    navData: AddScreenObject = AddScreenObject(),
    onSaved: () -> Unit = {}
) {

    val selectedCategory = remember {
        mutableStateOf(navData.category)
    }

    val title = remember {
        mutableStateOf(navData.title)
    }

    val navImageUrl = remember {
        mutableStateOf(navData.imageUrl)
    }

    val description = remember {
        mutableStateOf(navData.description)
    }

    val price = remember {
        mutableStateOf(navData.price)
    }

    val selectedImageUri = remember {
        mutableStateOf<Uri?>(null)
    }

    val firestore = remember {
        Firebase.firestore
    }

    val storage = remember {
        Firebase.storage
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
            navImageUrl.value =""
            selectedImageUri.value = uri
        }

        Image(
            painter = rememberAsyncImagePainter(
                model = navImageUrl.value.ifEmpty { selectedImageUri.value }
            ),
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

        RoundedCornerDropDownMenu(selectedCategory.value) { selectedItem ->
            selectedCategory.value = selectedItem
        }

        Spacer(
            modifier = Modifier
                .height(10.dp)
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
            text = price.value,
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

            val game = Game(
                key = navData.key,
                title = title.value,
                description = description.value,
                price = price.value,
                category = selectedCategory.value
            )

            if (selectedImageUri.value != null) {

                saveGameImage(
                    navData.imageUrl,
                    selectedImageUri.value!!,
                    storage,
                    firestore,
                    game,
                    onSaved = {
                        onSaved()
                    },
                    onError = {

                    }
                )
            } else {

                saveGameToFireStore(
                    firestore,
                    game.copy(imageUrl = navData.imageUrl),
                    onSaved = {
                        onSaved()
                    },
                    onError = {

                    }
                )

            }

        }

    }
}

private fun saveGameImage(
    oldImageUrl: String,
    uri: Uri,
    storage: FirebaseStorage,
    firestore: FirebaseFirestore,
    game: Game,
    onSaved: () -> Unit,
    onError: () -> Unit
) {
    val timeStamp = System.currentTimeMillis()
    val storageRef = if(oldImageUrl.isEmpty()) {
        storage.reference
            .child("game_images")
            .child("image_$timeStamp.jpg")
    } else {
        storage.getReferenceFromUrl(oldImageUrl)
    }
    val uploadTask = storageRef.putFile(uri)
    uploadTask.addOnSuccessListener {
        storageRef.downloadUrl.addOnSuccessListener { url ->
            saveGameToFireStore(
                firestore,
                game.copy(imageUrl = url.toString()),
                onSaved = {
                    onSaved()
                },
                onError = {
                    onError()
                }
            )
        }
    }
}

private fun saveGameToFireStore(
    firestore: FirebaseFirestore,
    game: Game,
    onSaved: () -> Unit,
    onError: () -> Unit
) {
    val db = firestore.collection("games")
    val key = game.key.ifEmpty { db.document().id }
    db.document(key)
        .set(
            game.copy(
                key = key,
            )
        )
        .addOnSuccessListener {
            onSaved()
        }
        .addOnFailureListener {
            onError()
        }
}