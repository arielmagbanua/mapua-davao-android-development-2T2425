package com.example.jackenpoy.modules.auth.domain

import android.content.Context
import com.example.jackenpoy.modules.auth.data.models.User

interface AuthServiceInterface {
    fun signInWithGoogle(
        context: Context,
        onError: ((error: String) -> Unit)? = null,
        onSuccess: (user: User?) -> Unit,
    )

    fun getAuthenticatedUser(): User?
}
