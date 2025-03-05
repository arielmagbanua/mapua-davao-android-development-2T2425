package com.example.jackenpoy.modules.auth.data.repositories

import com.google.firebase.auth.FirebaseUser

interface AuthRepositoryInterface {
    fun getCurrentUser(): FirebaseUser?
}
