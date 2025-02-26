package com.example.simplenoteapp.modules.auth.data

import android.content.Context
import com.google.firebase.auth.FirebaseUser

interface AuthRepositoryInterface {
    suspend fun signInWithGoogle(context: Context, onSignIn: (user: FirebaseUser?) -> Unit)

    fun logout()

    fun getCurrentUser(): FirebaseUser?

    fun registerUser(
        email: String,
        password: String,
        onRegister: ((successful: Boolean) -> Unit)? = null
    )

    fun signInUser(email: String, password: String, onSignIn: (user: FirebaseUser?) -> Unit)
}
