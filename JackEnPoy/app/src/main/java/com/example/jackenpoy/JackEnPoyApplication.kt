package com.example.jackenpoy

import android.app.Application
import com.example.jackenpoy.modules.auth.data.repositories.AuthRepositoryInterface
import com.example.jackenpoy.modules.auth.data.repositories.FirebaseAuthRepository
import com.example.jackenpoy.modules.auth.domain.AuthService
import com.example.jackenpoy.modules.auth.domain.AuthServiceInterface
import com.example.jackenpoy.modules.auth.ui.AuthViewModel
import com.example.jackenpoy.modules.game.data.repositories.FirestoreGameRepository
import com.example.jackenpoy.modules.game.data.repositories.GameRepositoryInterface
import com.example.jackenpoy.modules.game.domain.GameService
import com.example.jackenpoy.modules.game.domain.GameServiceInterface
import com.example.jackenpoy.modules.game.ui.GameViewModel
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

    @Provides
    @Singleton
    fun provideGameRepositoryInterface(): GameRepositoryInterface {
        return FirestoreGameRepository()
    }
    @Provides
    @Singleton
    fun provideGameServiceInterface(gameRepository: GameRepositoryInterface): GameServiceInterface {
        return GameService(gameRepository)
    }
    @Provides
    @Singleton
    fun provideGameViewModel(gameService: GameServiceInterface): GameViewModel {
        return GameViewModel(gameService)
    }
}
