package com.example.simplenoteapp

import android.app.Application
import com.example.simplenoteapp.modules.auth.data.AuthRepository
import com.example.simplenoteapp.modules.auth.data.AuthRepositoryInterface
import com.example.simplenoteapp.modules.auth.ui.AuthViewModel
import com.example.simplenoteapp.modules.notes.data.DataRepositoryInterface
import com.example.simplenoteapp.modules.notes.data.FirestoreRepository
import com.example.simplenoteapp.modules.notes.ui.NotesViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
class NotesApplication : Application() {
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRepositoryInterface(): AuthRepositoryInterface {
        return AuthRepository()
    }

    @Provides
    @Singleton
    fun provideAuthViewModel(authRepository: AuthRepositoryInterface): AuthViewModel {
        return AuthViewModel(authRepository)
    }

    @Provides
    @Singleton
    fun provideDataRepositoryInterface(): DataRepositoryInterface {
        return FirestoreRepository()
    }

    @Provides
    @Singleton
    fun provideNotesViewModel(notesRepository: DataRepositoryInterface): NotesViewModel {
        return NotesViewModel(notesRepository)
    }
}