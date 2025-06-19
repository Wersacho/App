package com.example.app.ui.main_screen.top_app_bar

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.app.R
import com.example.app.ui.main_screen.utils.Categories
import com.example.app.ui.theme.BoxFilterColor
import com.example.app.ui.theme.DarkRed
import com.example.app.ui.theme.LightRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    titleIndex: Int,
    onSearch: (String) -> Unit,
    onFilter: () -> Unit
) {

    var targetState by remember {
        mutableStateOf(false)
    }
    var expandedState by remember {
        mutableStateOf(false)
    }
    var queryText by remember {
        mutableStateOf("")
    }

    Crossfade(targetState = targetState) { target ->

        if (target) {

            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                //поле ввода текста
                inputField = {
                    SearchBarDefaults.InputField(
                        colors = TextFieldDefaults.colors(
                            cursorColor = DarkRed
                        ),
                        query = queryText,
                        placeholder = {
                            Text(
                                text = "Искать игру"
                            )
                        },
                        //отправляются запросы при каждом вводе с клавы
                        onQueryChange = { text ->
                            queryText = text
                        },
                        //отправляется запрос при нажатии поиск с клавы
                        onSearch = { text ->
                            onSearch(text)
                        },
                        //контролируем состояние
                        expanded = expandedState,
                        onExpandedChange = {},
                        //закрыть кнопка
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    expandedState = false
                                    targetState = false
                                    queryText = ""
                                    onSearch("")
                                }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Закрыть"
                                )
                            }
                        }
                    )
                },
                //раскрытие списка
                expanded = expandedState,
                onExpandedChange = { expanded ->
                    expandedState = expanded
                },
                //когда expandedState = true
                content = {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    )  {
                        items(5){
                            Text(
                                text = "Item $it",
                                modifier = Modifier
                                    .padding(10.dp)
                            )
                        }
                    }
                },
                //colors = SearchBarDefaults.colors(
                //    containerColor =
                //)
            )

        } else {

            TopAppBar(
                title = {
                    Text(
                        text = when (titleIndex){
                            Categories.FAVORITES -> stringResource(id = R.string.favs)
                            Categories.ALL ->  stringResource(id = R.string.all)
                            else -> stringArrayResource(id = R.array.category_array)[titleIndex]
                        }
                    )
                },
                actions = {
                    //поиск
                    IconButton(
                        onClick = {
                            targetState = true
                        }
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Поиск"
                        )
                    }
                    //filters
                    IconButton(
                        onClick = {
                            onFilter()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_tune),
                            contentDescription = "Фильтр"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BoxFilterColor,
                    titleContentColor = DarkRed,
                    actionIconContentColor = DarkRed
                )

            )

        }

    }
}