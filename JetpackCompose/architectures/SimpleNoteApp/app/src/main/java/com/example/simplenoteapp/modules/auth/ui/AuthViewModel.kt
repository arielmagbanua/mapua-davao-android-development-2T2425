package com.example.simplenoteapp.modules.auth.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel: ViewModel() {
    // Expose screen UI state
    private val _uiState = MutableStateFlow(AuthState())
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (email == "notes@test.com" && password == "112233") {
            _uiState.update { currentState
                -> currentState.copy(
                    email = email,
                )
            }
        }
    }

    fun logout() {
        _uiState.update { currentState
            -> currentState.copy(
                email = null,
            )
        }
    }
}