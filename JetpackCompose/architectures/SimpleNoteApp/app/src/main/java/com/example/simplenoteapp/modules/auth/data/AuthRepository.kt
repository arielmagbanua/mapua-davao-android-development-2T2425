package com.example.simplenoteapp.modules.auth.data

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.update
import java.util.UUID

class AuthRepository : AuthRepositoryInterface {
    private val auth: FirebaseAuth = Firebase.auth

    override suspend fun signInWithGoogle(context: Context, onSignIn: (user: FirebaseUser?) -> Unit) {
        // create google id option
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false) // allow user to pick any account
            .setServerClientId("555228050256-k94qc5ojtm791vs8tge8aqb2e9djmbg6.apps.googleusercontent.com")
            .setAutoSelectEnabled(false) // prevent the app from auto selecting the previous account
            .setNonce(UUID.randomUUID().toString())
            .build()

        // create google sign-in request
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        // create google credential
        val credentialManager = CredentialManager.create(context)
        val credentialResponse = credentialManager.getCredential(
            request = request,
            context = context,
        )
        val credential = credentialResponse.credential

        if (credential is CustomCredential) {
            // check the credential type
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                // Use googleIdTokenCredential and extract id to validate and
                // authenticate to firebase.
                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(credential.data)

                // create firebase credential
                val firebaseCredential = GoogleAuthProvider.getCredential(
                    googleIdTokenCredential.idToken, null
                )

                val authResult = auth.signInWithCredential(firebaseCredential)
                authResult.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentUser = task.result.user

                        if (currentUser != null) {
                            // user successfully logged in
                            onSignIn(currentUser)

                        }
                    } else {
                        Log.e("FIREBASE_LOGIN", "The login task failed.")
                    }
                }
            } else {
                // Catch any unrecognized credential type here.
                Log.e("GOOGLE_CREDENTIAL", "Unexpected type of credential")
            }
        } else {
            // Catch any unrecognized credential type here.
            Log.e("GOOGLE_CREDENTIAL", "Unexpected type of credential")
        }
    }

    override fun logout() {
        auth.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun registerUser(
        email: String,
        password: String,
        onRegister: ((successful: Boolean) -> Unit)?
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                val status = task.isSuccessful

                if (status) {
                    // sign out immediately after registering
                    Firebase.auth.signOut()
                }

                if (onRegister != null) {
                    onRegister(status)
                }

                Log.w("FIREBASE_REGISTER", "successful:$status")
            }
    }

    override fun signInUser(
        email: String,
        password: String,
        onSignIn: (FirebaseUser?) -> Unit
    ) {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = task.result.user

                    onSignIn(currentUser)
                } else {
                    onSignIn(null)
                    Log.w("FIREBASE_REGISTER", "signInWithEmailAndPassword:failure")
                }
            }
    }
}
