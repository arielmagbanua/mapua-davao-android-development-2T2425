package com.example.jackenpoy

import android.app.Application
import com.example.jackenpoy.modules.auth.data.AuthRepositoryInterface
import com.example.jackenpoy.modules.auth.data.FirebaseAuthRepository
import com.example.jackenpoy.modules.auth.domain.AuthService
import com.example.jackenpoy.modules.auth.domain.AuthServiceInterface
import com.example.jackenpoy.modules.auth.ui.AuthViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
class JackEnPoyApplication : Application()

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepositoryInterface {
        return FirebaseAuthRepository()
    }

    @Provides
    @Singleton
    fun provideAuthService(authRepository: AuthRepositoryInterface): AuthServiceInterface {
        return AuthService(authRepository)
    }

    @Provides
    @Singleton
    fun provideAuthViewModel(authService: AuthServiceInterface): AuthViewModel {
        return AuthViewModel(authService)
    }
}
