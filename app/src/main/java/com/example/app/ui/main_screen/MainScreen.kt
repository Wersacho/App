package com.example.app.ui.main_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.app.data.Favorite
import com.example.app.data.Game
import com.example.app.ui.login.data.MainScreenDataObject
import com.example.app.ui.main_screen.bottom_menu.BottomMenu
import com.example.app.ui.main_screen.bottom_menu.BottomMenuItem
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),

    navData: MainScreenDataObject,
    onGameEditClick: (Game) -> Unit,
    onGameClick: (Game) -> Unit,
    onAdminClick: () -> Unit
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed) // drawer закрыт
    val coroutineScope = rememberCoroutineScope()

    val isAdminState = remember {
        mutableStateOf(false)
    }



    //при запуске
    LaunchedEffect(Unit) {
        if (viewModel.gamesListState.value.isEmpty()) {
            viewModel.getAllGames()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier
            .fillMaxWidth(),
        drawerContent = {
            Column(
                Modifier.fillMaxWidth(0.6f)
            ) {
                DrawerHeader(navData.email)
                DrawerBody(
                    onAdmin = { isAdmin ->
                        isAdminState.value = isAdmin
                    },

                    onFavClick = {
                        viewModel.selectedBottomItemState.value = BottomMenuItem.Favs.title

                        viewModel.getAllFavsGames()

                        coroutineScope.launch {
                            drawerState.close()
                        }
                    },

                    onAdminClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }

                            onAdminClick()
                    },

                    onCategoryClick = { category ->

                        viewModel.getGamesFromCategory(category)

                        viewModel.selectedBottomItemState.value = BottomMenuItem.Home.title

                        coroutineScope.launch {
                            drawerState.close()
                        }
                    }
                )
            }

        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomMenu(
                    viewModel.selectedBottomItemState.value,

                    onFavsClick = {
                        viewModel.selectedBottomItemState.value = BottomMenuItem.Favs.title

                        viewModel.getAllFavsGames()

                    },

                    onHomeClick = {
                        viewModel.selectedBottomItemState.value = BottomMenuItem.Home.title

                        viewModel.getAllGames()
                    }
                )
            }
        ) { paddingValues ->

            if (viewModel.isFavListEmptyState.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Избранное пустое — добавьте игры, \n которые вам нравятся!",
                        color = Color.LightGray,
                        fontSize = 16.sp,
                    )


                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(viewModel.gamesListState.value) { game ->
                    GameListItemUi(
                        isAdminState.value,
                        game,

                        onGameClick = { gm ->
                            onGameClick(gm)
                        },

                        onEditClick = {
                            onGameEditClick(it)
                        },

                        onFavClick = {

                            viewModel.onFavClick(game, viewModel.selectedBottomItemState.value)

                        }
                    )
                }
            }
        }
    }

}



