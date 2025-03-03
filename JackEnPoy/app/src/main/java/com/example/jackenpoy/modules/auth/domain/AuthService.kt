package com.example.jackenpoy.modules.auth.domain

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.jackenpoy.R
import com.example.jackenpoy.modules.auth.data.AuthRepositoryInterface
import com.example.jackenpoy.modules.auth.data.FirebaseAuthRepository
import com.example.jackenpoy.modules.auth.data.models.User
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class AuthService(private val authRepository: AuthRepositoryInterface) : AuthServiceInterface {
    private val auth: FirebaseAuth = Firebase.auth

    /**
     * Executes signing of user using their Google account.
     *
     * The [attempt] is the number attempt, because this function is called recursively in case of failures.
     */
    private suspend fun getCredential(context: Context, attempt: Int = 1): GetCredentialResponse? {
        if (attempt > 3) {
            // TODO: This means that there is no google account signed in the device, so ask user to log a google account
            throw Exception("Reached maximum retry attempts")
        }

        try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(attempt == 1)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .setAutoSelectEnabled(true)
                .setNonce(UUID.randomUUID().toString())
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val credentialManager = CredentialManager.create(context)

            return credentialManager.getCredential(
                request = request,
                context = context,
            )
        } catch (e: GetCredentialException) {
            e.printStackTrace()

            if (e is NoCredentialException) {
                // run again to force the un-authorize flag to false
                return getCredential(context, attempt + 1)
            } else if (e !is GetCredentialCancellationException) {
                throw e
            }
        }

        return null
    }

    /**
     * It handles google sign-in result and then will sign-in the user to firebase.
     *
     * The [result] argument is the credential response from Google sign-in.
     */
    private suspend fun handleGoogleSignIn(result: GetCredentialResponse): FirebaseUser? {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract id to validate and
                        // authenticate to firebase.
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)

                        val firebaseCredential = GoogleAuthProvider.getCredential(
                            googleIdTokenCredential.idToken, null
                        )

                        val authResult = auth.signInWithCredential(firebaseCredential).await()
                        return authResult.user
                    } catch (e: GoogleIdTokenParsingException) {
                        e.printStackTrace()
                    }
                } else {
                    Log.e("GOOGLE_CREDENTIAL", "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e("GOOGLE_CREDENTIAL", "Unexpected type of credential")
            }
        }

        return null
    }

    override fun signInWithGoogle(
        context: Context,
        onError: ((String) -> Unit)?,
        onSuccess: (User?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val credential = getCredential(context, 1)
            if (credential == null) {
                onError?.invoke("Unable to get any valid credentials.")
                return@launch
            }

            val firebaseUser = handleGoogleSignIn(credential)
            if (firebaseUser == null) {
                onError?.invoke("Failed to sign-in with Google")
                return@launch
            }

            onSuccess(
                User(
                    id = firebaseUser.uid,
                    name = firebaseUser.displayName ?: "",
                    email = firebaseUser.email ?: ""
                )
            )
        }
    }

    override fun getAuthenticatedUser(): User? {
        val firebaseUser = authRepository.getCurrentUser()

        if (firebaseUser == null) {
            return null
        }

        return User(
            id = firebaseUser.uid,
            name = firebaseUser.displayName ?: "",
            email = firebaseUser.email ?: ""
        )
    }
}
