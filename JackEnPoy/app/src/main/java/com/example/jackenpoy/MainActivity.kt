package com.example.jackenpoy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.jackenpoy.modules.auth.data.FirebaseAuthRepository
import com.example.jackenpoy.modules.auth.domain.AuthService
import com.example.jackenpoy.modules.auth.ui.AuthViewModel
import com.example.jackenpoy.modules.auth.ui.LoginScreen
import com.example.jackenpoy.ui.theme.JackEnPoyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authViewModel =
            AuthViewModel(authService = AuthService(authRepository = FirebaseAuthRepository()))

        enableEdgeToEdge()
        setContent {
            JackEnPoyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current

                    LoginScreen(
                        modifier = Modifier.padding(innerPadding),
                        onGoogleSignIn = {
                            authViewModel.signInWithGoogle(context);
                        },
                        onFacebookSignIn = {},
                        onLogin = { username, password -> },
                        onRegister = {},
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}
