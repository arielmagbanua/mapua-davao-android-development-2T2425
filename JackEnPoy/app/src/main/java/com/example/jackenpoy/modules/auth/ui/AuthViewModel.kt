package com.example.jackenpoy.modules.auth.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.jackenpoy.modules.auth.data.models.User
import com.example.jackenpoy.modules.auth.domain.AuthServiceInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel(private val authService: AuthServiceInterface) : ViewModel() {
    // auth state
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun signInWithGoogle(context: Context) {
        authService.signInWithGoogle(context = context) { user ->
            _authState.update { currentState ->
                currentState.copy(
                    currentUser = user
                )
            }
        }
    }
}