package com.example.app.ui.login.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.app.R
import com.example.app.custom.MyDialog
import com.example.app.ui.login.LoginViewModel
import com.example.app.ui.login.data.MainScreenDataObject
import com.example.app.ui.theme.MidnightBlack


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),

    onNavigateToMainScreen: (MainScreenDataObject) -> Unit
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.getAccountState()
        viewModel.getEmail()
    }

    //при разрушении экрана
    DisposableEffect(Unit) {
        onDispose {
            viewModel.saveLastEmail()
            viewModel.passwordState.value = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(180.dp)
                .clip(RoundedCornerShape(24.dp))
        )

        Spacer(
            modifier = Modifier
                .height(16.dp)
        )

        Text(
            text = stringResource(R.string.app_name_ru),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Default,
            color = MidnightBlack
        )

        Spacer(
            modifier = Modifier
                .height(32.dp)
        )

        if (viewModel.currentUser.value == null) {

            RoundedCornerTextField(
                text = viewModel.emailState.value,
                label = stringResource(R.string.email),
            ) {
                viewModel.emailState.value = it
            }

            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )
            if (!viewModel.resetPasswordState.value) {

                RoundedCornerTextField(
                    text = viewModel.passwordState.value,
                    label = stringResource(R.string.password),
                    isPassword = true
                ) {
                    viewModel.passwordState.value = it
                }

                Spacer(
                    modifier = Modifier
                        .height(10.dp)
                )

            }

            // сообщение об ошибке errorMessage
            if (viewModel.errorState.value.isNotEmpty()) {
                Text(
                    text = viewModel.errorState.value,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }

            //если не сброс пароля то показываем войти
            if (!viewModel.resetPasswordState.value) {

                LoginButton(
                    text = stringResource(R.string.sign_in)
                ) {
                    viewModel.signIn(
                        onSignInSuccess = { navData ->
                            onNavigateToMainScreen(navData)
                        }
                    )
                }

            }

            LoginButton(
                text = if (viewModel.resetPasswordState.value){
                    stringResource(R.string.reset_password)
                } else {
                    stringResource(R.string.sign_up)
                }
            ) {
                viewModel.signUp(
                    onSignUpSuccess = { navData ->
                        onNavigateToMainScreen(navData)
                    }
                )
            }

            Spacer(modifier = Modifier
                .height(10.dp)
            )

            if (!viewModel.resetPasswordState.value) {
                Text(
                    modifier = Modifier.clickable {
                        viewModel.errorState.value = "" //очищаем errorstate
                        viewModel.resetPasswordState.value = true
                    },
                    text = stringResource(R.string.forgot_password),
                    color = Color.Black
                )
            }

        } else {

            LoginButton(
                text = stringResource(R.string.enter)
            ) {
                onNavigateToMainScreen(
                    MainScreenDataObject(
                        viewModel.currentUser.value!!.uid,
                        viewModel.currentUser.value!!.email!!
                    )
                )
            }

            LoginButton(
                text = stringResource(R.string.sign_out)
            ) {
                viewModel.signOut()
            }

        }

        MyDialog(
            showDialog = viewModel.showResetPasswordDialog.value,
            onDismiss = {
                viewModel.showResetPasswordDialog.value = false
            },
            onConfirm = {
                viewModel.showResetPasswordDialog.value = false
            },
            message = stringResource(R.string.reset_password_message)
        )

    }

}
