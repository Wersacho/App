package com.example.app.ui.login

import androidx.lifecycle.ViewModel
import com.example.app.utils.firebase.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authManager: AuthManager
): ViewModel() {
}