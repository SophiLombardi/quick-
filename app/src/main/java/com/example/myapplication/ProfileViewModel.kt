package com.example.myapplication

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Estado global do perfil do usuário.
 * Compartilhado entre ProfileScreen e EditProfileScreen.
 */
class ProfileState {
    var name by mutableStateOf("João Silva")
    var bio by mutableStateOf("Amante de pets e café ☕")
    var photoUri by mutableStateOf<Uri?>(null)
}

// Instância única em memória (sobrevive enquanto o app estiver vivo)
private val globalProfileState = ProfileState()

@Composable
fun rememberProfileState(): ProfileState {
    return remember { globalProfileState }
}