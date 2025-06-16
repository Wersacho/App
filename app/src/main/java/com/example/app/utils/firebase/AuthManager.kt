package com.example.app.utils.firebase

import com.example.app.ui.login.data.MainScreenDataObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Singleton

@Singleton
class AuthManager(
    private val auth: FirebaseAuth
) {

    fun signUp(
        email: String,
        password: String,
        onSignUpSuccess: (MainScreenDataObject) -> Unit,
        onSignUpFailure: (String) -> Unit
    ) {
        if (email.isBlank() || password.isBlank()) {
            onSignUpFailure("Пустые поля ввода")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSignUpSuccess(
                        MainScreenDataObject(
                            task.result.user?.uid!!,
                            task.result.user?.email!!
                        )
                    )
                }
            }
            .addOnFailureListener {
                onSignUpFailure(it.message ?: "Ошибка регистрации")
            }
    }

    fun signIn(
        email: String,
        password: String,
        onSignInSuccess: (MainScreenDataObject) -> Unit,
        onSignInFailure: (String) -> Unit
    ) {
        if (email.isBlank() || password.isBlank()) {
            onSignInFailure("Пустые поля ввода")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSignInSuccess(
                        MainScreenDataObject(
                            task.result.user?.uid!!,
                            task.result.user?.email!!
                        )
                    )
                }
            }
            .addOnFailureListener {
                onSignInFailure(it.message ?: "Ошибка входа")
            }
    }

    //запрос на восстановление пароля
    fun resetPassword(
        email: String,
        onResetPasswordSuccess: () -> Unit,
        onResetPasswordFailure: (String) -> Unit
    ) {
        if (email.isEmpty()) {
            onResetPasswordFailure("Пустое поле почты")
            return
        }
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResetPasswordSuccess()
                }
            }
            .addOnFailureListener { result ->
                onResetPasswordFailure(result.message ?: "Ошибка сброса пароля")
            }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun signOut() {
        auth.signOut()
    }

}