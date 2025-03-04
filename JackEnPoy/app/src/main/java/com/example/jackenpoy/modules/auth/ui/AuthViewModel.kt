package com.example.jackenpoy.modules.auth.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.jackenpoy.modules.auth.domain.AuthServiceInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthServiceInterface
) : ViewModel() {
    // auth state
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        val user = authService.getAuthenticatedUser()
        if (user != null) {
            _authState.update { currentState ->
                currentState.copy(
                    currentUser = user
                )
            }
        }
    }

    fun signInWithGoogle(context: Context) {
        authService.signInWithGoogle(context = context) { user ->
            _authState.update { currentState ->
                currentState.copy(
                    currentUser = user
                )
            }
        }
    }

    fun logout() {
        authService.logout()
        _authState.update { currentState ->
            currentState.copy(
                currentUser = null
            )
        }
    }
}