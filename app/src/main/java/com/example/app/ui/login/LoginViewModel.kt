package com.example.app.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.app.ui.login.data.MainScreenDataObject
import com.example.app.utils.firebase.AuthManager
import com.example.app.utils.store.StoreManager
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val storeManager: StoreManager
): ViewModel() {

    val currentUser = mutableStateOf<FirebaseUser?>(null)
    val showResetPasswordDialog = mutableStateOf(false)
    val errorState = mutableStateOf("")
    val emailState = mutableStateOf("")
    val passwordState = mutableStateOf("")
    val resetPasswordState = mutableStateOf(false)

    fun signIn(
        onSignInSuccess: (MainScreenDataObject) -> Unit
    ){
        errorState.value = "" //очищаем errorstate
        authManager.signIn(
            emailState.value,
            passwordState.value,
            onSignInSuccess = { navData ->
                onSignInSuccess(navData)
            },
            onSignInFailure = { errorMessage ->
                errorState.value = errorMessage
            }
        )
    }

    fun getEmail(){
        emailState.value = storeManager.getString(StoreManager.EMAIL_KEY, "")
    }

    fun saveLastEmail(){
        storeManager.saveString(StoreManager.EMAIL_KEY, emailState.value)
    }

    fun signUp(
        onSignUpSuccess: (MainScreenDataObject) -> Unit
    ){
        errorState.value = "" //очищаем errorstate
        if (resetPasswordState.value){
            authManager.resetPassword(
                emailState.value,
                onResetPasswordSuccess = {
                    resetPasswordState.value = false
                    showResetPasswordDialog.value = true
                },
                onResetPasswordFailure = { errorMessage ->
                    errorState.value = errorMessage
                }
            )
            return
        }
        authManager.signUp(
            emailState.value,
            passwordState.value,
            onSignUpSuccess = { navData ->
                onSignUpSuccess(navData)
            },
            onSignUpFailure = { errorMessage ->
                errorState.value = errorMessage
            }
        )
    }

    fun getAccountState() {
        currentUser.value = authManager.getCurrentUser()
    }

    fun signOut() {
        authManager.signOut()
        currentUser.value = null
    }

}