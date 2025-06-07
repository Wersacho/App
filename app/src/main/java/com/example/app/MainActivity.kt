package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.app.ui.add_game_screen.AddBookScreen
import com.example.app.ui.add_game_screen.data.AddScreenObject
import com.example.app.ui.login.LoginScreen
import com.example.app.ui.login.data.LoginScreenObject
import com.example.app.ui.login.data.MainScreenDataObject
import com.example.app.ui.main_screen.DrawerBody
import com.example.app.ui.main_screen.MainScreen
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = AddScreenObject
            ) {

                composable<LoginScreenObject> {
                    LoginScreen { navData ->
                        navController.navigate(navData)
                    }
                }

                composable<MainScreenDataObject> { navEntry ->
                    // получение данных
                    val navData = navEntry.toRoute<MainScreenDataObject>()
                    MainScreen(navData)
                }

                composable<AddScreenObject> { navEntry ->
                    AddBookScreen()
                }
            }

        }

    }
}