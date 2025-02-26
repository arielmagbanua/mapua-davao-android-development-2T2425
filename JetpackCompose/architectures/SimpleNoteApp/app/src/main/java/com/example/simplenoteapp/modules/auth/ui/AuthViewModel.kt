package com.example.simplenoteapp.modules.auth.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenoteapp.modules.auth.data.AuthRepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepositoryInterface) : ViewModel() {
    // Expose screen UI state
    private val _uiState = MutableStateFlow(AuthState())
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    init {
        val currentUser = authRepository.getCurrentUser()

        if (currentUser != null) {
            _uiState.update { currentState ->
                currentState.copy(
                    email = currentUser.email,
                )
            }
        }
    }

    fun logout() {
        authRepository.logout()

        _uiState.update { currentState
            ->
            currentState.copy(
                email = null,
            )
        }
    }

    fun registerUser(email: String, password: String) {
        authRepository.registerUser(email, password)
    }

    fun signInUserWithEmailAndPassword(email: String, password: String) {
        authRepository.signInUser(email, password) { currentUser ->
            if (currentUser != null) {
                _uiState.update { currentState
                    ->
                    currentState.copy(
                        email = currentUser.email,
                    )
                }
            }
        }
    }

    fun signInWithGoogle(appContext: Context) {
        viewModelScope.launch {
            authRepository.signInWithGoogle(appContext) { currentUser ->
                _uiState.update { currentState
                    ->
                    currentState.copy(
                        email = currentUser?.email,
                    )
                }
            }
        }
    }
}
