package com.example.app.ui.add_game_screen

import android.net.Uri
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.app.R
import com.example.app.data.Game
import com.example.app.ui.add_game_screen.data.AddScreenObject
import com.example.app.ui.login.LoginButton
import com.example.app.ui.login.RoundedCornerTextField
import com.example.app.ui.main_screen.MainScreenViewModel
import com.example.app.ui.theme.BoxFilterColor
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect

@Preview(showBackground = true)
@Composable
fun AddGameScreen(
    navData: AddScreenObject = AddScreenObject(),
    onSaved: () -> Unit = {},

    viewModel: AddGameViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    //val selectedCategory = remember {
    //    mutableIntStateOf(navData.categoryIndex)
    //}

    val showLoadIndicator = remember {
        mutableStateOf(false)
    }

    val navImageUrl = remember {
        mutableStateOf(navData.imageUrl)
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

    LaunchedEffect(Unit) {
        viewModel.setDefaultsData(navData)
        viewModel.uiState.collect{ state ->
            when (state) {
                is MainScreenViewModel.MainUiState.Loading -> {
                    showLoadIndicator.value = true
                }
                is MainScreenViewModel.MainUiState.Success -> {
                    onSaved()
                }
                is MainScreenViewModel.MainUiState.Error -> {
                    showLoadIndicator.value = false
                    Toast.makeText(context,"Ошибка: ${state.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

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
            viewModel.selectedImageUri.value = uri
        }

        Image(
            painter = rememberAsyncImagePainter(
                model = navImageUrl.value.ifEmpty { viewModel.selectedImageUri.value }
            ),
            contentDescription = "logo",
            modifier = Modifier.size(90.dp)
        )

        Spacer(
            modifier = Modifier
                .height(30.dp)
        )

        Text(
            text = stringResource(R.string.admin_panel),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
            color = Color.White
        )

        Spacer(
            modifier = Modifier
                .height(30.dp)
        )

        RoundedCornerDropDownMenu(viewModel.selectedCategory.intValue) { selectedItemIndex ->
            viewModel.selectedCategory.intValue = selectedItemIndex
        }

        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

        //поле ввода заголовок

        RoundedCornerTextField(
            text = viewModel.title.value,
            label = stringResource(R.string.title)
        ) {
            viewModel.title.value = it
        }

        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

        //поле ввода описание

        RoundedCornerTextField(
            maxLines = 5,
            singleLine = false,
            text = viewModel.description.value,
            label = stringResource(R.string.description)
        ) {
            viewModel.description.value = it
        }

        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

        //поле ввода цена

        RoundedCornerTextField(
            text = viewModel.price.value,
            label = stringResource(R.string.price)
        ) {
            viewModel.price.value = it
        }

        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

        LoginButton(
            text = stringResource(R.string.choose_image)
        ) {
            imageLauncher.launch("image/*")
        }

        LoginButton(
            text = stringResource(R.string.save),
            showLoadIndicator = showLoadIndicator.value
        ) {
            viewModel.uploadGame(navData)
        }

    }
}

