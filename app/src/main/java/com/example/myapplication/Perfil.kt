package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest

/* ============================================================
 *                 TELA DE PERFIL (VISUALIZAÇÃO)
 * ============================================================ */

class Perfil : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileScreen()
        }
    }
}

@Composable
fun ProfileScreen(
    onEditClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    // Estado global compartilhado com EditProfileScreen (veja ProfileViewModel)
    val profile = rememberProfileState()

    Scaffold(
        containerColor = CreamBg,
        bottomBar = { CustomBottomNavigation(currentRoute = "profile") }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Cabeçalho com botão editar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Meu Perfil",
                    color = TextDark,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardWhite)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar perfil",
                        tint = TealPrimary
                    )
                }
            }

            // Card do avatar + nome + bio
            ProfileHeaderCard(
                photoUri = profile.photoUri,
                name = profile.name,
                bio = profile.bio
            )

            // Estatísticas rápidas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("150", "Moedas", Modifier.weight(1f))
                StatCard("2", "Nível", Modifier.weight(1f))
                StatCard("12", "Tarefas", Modifier.weight(1f))
            }

            Text(
                text = "CONQUISTAS",
                color = TextDark,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 8.dp)
            )

            AchievementRow("🏆", "Primeira tarefa concluída")
            AchievementRow("🔥", "7 dias seguidos")
            AchievementRow("⭐", "Alcançou o nível 2")

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProfileHeaderCard(photoUri: Uri?, name: String, bio: String) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(YellowCoinBg)
                    .border(3.dp, TealPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (photoUri != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(photoUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Foto de perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Sem foto",
                        tint = TealPrimary,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }

            Text(
                text = name.ifBlank { "Sem nome" },
                color = TextDark,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(OrangeBadge)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "NÍVEL 2",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = bio.ifBlank { "Sem bio ainda..." },
                color = TextGray,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                color = TealPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = label,
                color = TextGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun AchievementRow(emoji: String, title: String) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(YellowCoinBg),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 20.sp)
            }
            Text(
                text = title,
                color = TextDark,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/* ============================================================
 *              TELA DE EDIÇÃO DE PERFIL (CUSTOMIZAÇÃO)
 * ============================================================ */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit = {}
) {
    val profile = rememberProfileState()
    val context = LocalContext.current

    // TextField estados locais (sincronizados ao salvar)
    var nameInput by remember { mutableStateOf(profile.name) }
    var bioInput by remember { mutableStateOf(profile.bio) }

    // Launcher para escolher foto da galeria
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            // Persistir acesso ao URI
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) { /* ignora se não for persistível */ }

            profile.photoUri = uri
        }
    }

    Scaffold(
        containerColor = CreamBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Editar Perfil",
                        color = TextDark,
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = TextDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CreamBg
                )
            )
        },
        bottomBar = { CustomBottomNavigation(currentRoute = "profile") }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(Modifier.height(4.dp))

            // ===== Avatar editável =====
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(YellowCoinBg)
                        .border(3.dp, TealPrimary, CircleShape)
                        .clickable { photoPicker.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (profile.photoUri != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(profile.photoUri)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Foto de perfil",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Sem foto",
                            tint = TealPrimary,
                            modifier = Modifier.size(70.dp)
                        )
                    }

                    // Overlay com ícone de câmera
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(TealPrimary)
                            .border(3.dp, CreamBg, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Trocar foto",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))
                TextButton(onClick = { photoPicker.launch("image/*") }) {
                    Text(
                        "Trocar foto",
                        color = TealPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // ===== Campo: Nome =====
            FieldLabel("Nome de perfil")
            OutlinedTextField(
                value = nameInput,
                onValueChange = { if (it.length <= 30) nameInput = it },
                singleLine = true,
                placeholder = { Text("Digite seu nome", color = TextGray) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardWhite,
                    unfocusedContainerColor = CardWhite,
                    focusedBorderColor = TealPrimary,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedTextColor = TextDark,
                    unfocusedTextColor = TextDark,
                    cursorColor = TealPrimary
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // ===== Campo: Bio =====
            FieldLabel("Bio")
            OutlinedTextField(
                value = bioInput,
                onValueChange = { if (it.length <= 150) bioInput = it },
                placeholder = {
                    Text("Conte um pouco sobre você...", color = TextGray)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardWhite,
                    unfocusedContainerColor = CardWhite,
                    focusedBorderColor = TealPrimary,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedTextColor = TextDark,
                    unfocusedTextColor = TextDark,
                    cursorColor = TealPrimary
                ),
                shape = RoundedCornerShape(16.dp),
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "${bioInput.length}/150",
                color = TextGray,
                fontSize = 11.sp,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(Modifier.height(8.dp))

            // ===== Botão Salvar =====
            Button(
                onClick = {
                    profile.name = nameInput.trim()
                    profile.bio = bioInput.trim()
                    onBack()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = TealPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    "Salvar alterações",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun FieldLabel(text: String) {
    Text(
        text = text,
        color = TextDark,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}