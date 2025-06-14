package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.app.ui.add_game_screen.AddGameScreen
import com.example.app.ui.add_game_screen.data.AddScreenObject
import com.example.app.ui.details_screen.data.DetailsNavObject
import com.example.app.ui.details_screen.ui.DetailsScreen
import com.example.app.ui.login.LoginScreen
import com.example.app.ui.login.data.LoginScreenObject
import com.example.app.ui.login.data.MainScreenDataObject
import com.example.app.ui.main_screen.DrawerBody
import com.example.app.ui.main_screen.MainScreen
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = LoginScreenObject
            ) {

                composable<LoginScreenObject> {
                    LoginScreen { navData ->
                        navController.navigate(navData)
                    }
                }

                composable<MainScreenDataObject> { navEntry ->
                    // получение данных
                    val navData = navEntry.toRoute<MainScreenDataObject>()
                    MainScreen(
                        navData = navData,
                        onGameClick = { gm ->
                            navController.navigate(DetailsNavObject(
                                title = gm.title,
                                description = gm.description,
                                price = gm.price,
                                category = gm.category,
                                imageUrl = gm.imageUrl
                            ))
                        },
                        onGameEditClick = { game ->
                            navController.navigate(
                                AddScreenObject(
                                    key = game.key,
                                    title = game.title,
                                    description = game.description,
                                    price = game.price,
                                    category = game.category,
                                    imageUrl = game.imageUrl
                                )
                            )
                        }
                    ){
                        navController.navigate(AddScreenObject())
                    }
                }

                composable<AddScreenObject> { navEntry ->
                    val navData = navEntry.toRoute<AddScreenObject>()

                    AddGameScreen(navData)
                    {
                        navController.popBackStack()
                    }
                }

                composable<DetailsNavObject> { navEntry ->
                    val navData = navEntry.toRoute<DetailsNavObject>()
                    DetailsScreen(navData)
                }
            }

        }

    }
}