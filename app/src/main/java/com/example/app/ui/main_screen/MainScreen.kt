package com.example.app.ui.main_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.app.R
import com.example.app.custom.FilterDialog
import com.example.app.custom.MyDialog
import com.example.app.data.Favorite
import com.example.app.data.Game
import com.example.app.ui.login.data.MainScreenDataObject
import com.example.app.ui.main_screen.bottom_menu.BottomMenu
import com.example.app.ui.main_screen.bottom_menu.BottomMenuItem
import com.example.app.ui.main_screen.top_app_bar.MainTopBar
import com.example.app.ui.main_screen.utils.Categories
import com.example.app.ui.theme.DarkRed
import com.example.app.ui.theme.LightRed
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),

    navData: MainScreenDataObject,
    onGameEditClick: (Game) -> Unit,
    onGameClick: (Game) -> Unit,
    onAdminClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    val context = LocalContext.current

    val drawerState = rememberDrawerState(DrawerValue.Closed) // drawer закрыт
    val coroutineScope = rememberCoroutineScope()
    //управление диалогом
    val showDeleteDialog = remember {
        mutableStateOf(false)
    }

    val games = viewModel.games.collectAsLazyPagingItems()

    val state = rememberPullToRefreshState()

    val isAdminState = remember {
        mutableStateOf(false)
    }

    //состояние диалога фильтров
    var showFilterDialog by remember {
        mutableStateOf(false)
    }

    //запускается один раз отслеживаем ошибку
    LaunchedEffect(Unit) {
        viewModel.uiState.collect{ uiState ->
            if (uiState is MainScreenViewModel.MainUiState.Error){
                Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    //запускается при каждом refresh
    LaunchedEffect(games.loadState.refresh) {
        if (games.loadState.refresh is LoadState.Error) {
            val errorMessage = (games.loadState.refresh as LoadState.Error).error.message
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
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

                    onAdminClick = {

                        //закрытие drawerbody
                        coroutineScope.launch {
                            drawerState.close()
                        }

                        onAdminClick()

                    },

                    onCategoryClick = { categoryIndex ->

                        if (categoryIndex == Categories.FAVORITES) {
                            viewModel.selectedBottomItemState.intValue = BottomMenuItem.Favs.titleId
                        } else {
                            viewModel.selectedBottomItemState.intValue = BottomMenuItem.Home.titleId
                        }

                        viewModel.getGamesFromCategory(categoryIndex)

                        //обновляем категории принудительно
                        games.refresh()

                        //закрытие drawerbody
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    }
                )
            }

        }
    ) {
        Scaffold(

            //топ бар
            topBar = {
                MainTopBar(
                    viewModel.categoryState.intValue,
                    onSearch = { searchText ->
                        viewModel.searchGame(searchText)
                        games.refresh()
                    },
                    onFilter = {
                        showFilterDialog = true
                    }
                )
            },

            modifier = Modifier.fillMaxSize(),

            bottomBar = {
                BottomMenu(
                    viewModel.selectedBottomItemState.intValue,

                    onFavsClick = {
                        viewModel.selectedBottomItemState.intValue = BottomMenuItem.Favs.titleId

                        viewModel.getGamesFromCategory(Categories.FAVORITES)

                        //обновляем категории принудительно
                        games.refresh()

                    },

                    onHomeClick = {
                        viewModel.selectedBottomItemState.intValue = BottomMenuItem.Home.titleId
                        viewModel.getGamesFromCategory(Categories.ALL)
                        //обновляем категории принудительно
                        games.refresh()
                    },

                    onSettingsClick = {
                        onSettingsClick()
                    }
                )
            }
        ) { paddingValues ->

            if (games.itemCount == 0) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.favs_empty),
                        color = Color.LightGray,
                        fontSize = 16.sp,
                    )


                }
            }

            MyDialog(
                showDialog = showDeleteDialog.value,
                onDismiss = {
                    showDeleteDialog.value = false
                },
                title = stringResource(R.string.attention),
                message = stringResource(R.string.attention_message),
                onConfirm = {
                    showDeleteDialog.value = false
                    viewModel.deleteGame(games.itemSnapshotList.items)
                }
            )

            //если показывается индикатор загрузки
            if (games.loadState.refresh is LoadState.Loading){
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(90.dp),
                        color = LightRed
                    )
                }

            }

            PullToRefreshBox(
                isRefreshing = games.loadState.refresh is LoadState.Loading,
                onRefresh = {
                    games.refresh()
                },
                state = state,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                indicator = {
                    Indicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        isRefreshing = games.loadState.refresh is LoadState.Loading,
                        containerColor = Color.White,
                        color = LightRed,
                        state = state
                    )
                }
            ) {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(count = games.itemCount) { index ->
                        //достаем товары по индексу
                        val game = games[index]
                        if (game != null) {
                            GameListItemUi(
                                isAdminState.value,
                                game,

                                onGameClick = { gm ->
                                    onGameClick(gm)
                                },

                                onEditClick = {
                                    onGameEditClick(it)
                                },

                                onDeleteClick = { gameToDelete ->
                                    showDeleteDialog.value = true
                                    viewModel.gameToDelete = gameToDelete
                                },

                                onFavClick = {

                                    viewModel.onFavClick(
                                        game,
                                        viewModel.selectedBottomItemState.intValue,
                                        games.itemSnapshotList.items
                                    )

                                }
                            )
                        }
                    }
                }

            }


            //фильтры
            FilterDialog(
                showDialog = showFilterDialog,
                onConfirm = {
                    showFilterDialog = false
                    games.refresh()
                },
                onDismiss = {
                    showFilterDialog = false
                }
            )
        }
    }

}



