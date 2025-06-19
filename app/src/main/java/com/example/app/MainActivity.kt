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
import com.example.app.ui.admin_panel.data.AdminPanelNavObject
import com.example.app.ui.admin_panel.ui.AdminPanelScreen
import com.example.app.ui.admin_panel.data.ModerationNavObject
import com.example.app.ui.admin_panel.ui.ModerationScreen
import com.example.app.ui.comments_screen.CommentsNavData
import com.example.app.ui.comments_screen.CommentsScreen
import com.example.app.ui.details_screen.data.DetailsNavObject
import com.example.app.ui.details_screen.ui.DetailsScreen
import com.example.app.ui.login.LoginScreen
import com.example.app.ui.login.data.LoginScreenObject
import com.example.app.ui.login.data.MainScreenDataObject
import com.example.app.ui.main_screen.MainScreen
import com.example.app.ui.main_screen.bottom_menu.BottomMenuItem
import com.example.app.ui.settings_screen.data.SettingsNavData
import com.example.app.ui.settings_screen.ui.SettingsScreen
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
                                gameId = gm.key,
                                title = gm.title,
                                description = gm.description,
                                price = gm.price.toString(),
                                categoryIndex = gm.category,
                                imageUrl = gm.imageUrl,
                                ratingsList = gm.ratingsList
                            ))
                        },
                        onGameEditClick = { game ->
                            navController.navigate(
                                AddScreenObject(
                                    key = game.key,
                                    title = game.title,
                                    description = game.description,
                                    price = game.price.toString(),
                                    categoryIndex = game.category,
                                    imageUrl = game.imageUrl
                                )
                            )
                        },
                        onAdminClick = {
                            navController.navigate(AdminPanelNavObject)
                        },
                        onSettingsClick = {
                            navController.navigate(SettingsNavData)
                        }
                    )
                }

                composable<AddScreenObject> { navEntry ->
                    val navData = navEntry.toRoute<AddScreenObject>()

                    AddGameScreen(
                        navData,
                        onSaved = {
                            navController.popBackStack()
                        }
                    )

                }

                composable<DetailsNavObject> { navEntry ->
                    val navData = navEntry.toRoute<DetailsNavObject>()
                    DetailsScreen(
                        onCommentClick = { commentNavData ->
                            navController.navigate(commentNavData)
                        },
                        navObject = navData
                    )
                }

                composable<AdminPanelNavObject> {
                    AdminPanelScreen(
                        onAddGameClick = {
                            navController.navigate(AddScreenObject())
                        },
                        onModerationClick = {
                            navController.navigate(ModerationNavObject)
                        }
                    )
                }

                composable<ModerationNavObject> {
                    ModerationScreen()
                }

                composable<CommentsNavData> { navEntry ->
                    val navData = navEntry.toRoute<CommentsNavData>()
                    CommentsScreen(navData)
                }

                composable<SettingsNavData> { navEntry ->
                    SettingsScreen(
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }

        }

    }
}