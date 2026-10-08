package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Loja : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StoreScreen()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StoreScreenPreview() {
    StoreScreen()
}

data class StorePetUIModel(
    val name: String,
    val price: Int,
    val emoji: String,
    val circleColor: Color,
    val isEquipped: Boolean = false
)

data class StoreBackgroundUIModel(
    val name: String,
    val price: Int,
    val emoji: String,
    val gradientStart: Color,
    val gradientEnd: Color
)

private val storePets = listOf(
    StorePetUIModel("Cãozinho Mel", 0, "🐶", Color(0xFFFFD8A0), isEquipped = true),
    StorePetUIModel("Gatinho Mingau", 350, "🐱", Color(0xFFF8D7E0)),
    StorePetUIModel("Ursinho Teddy", 500, "🐻", Color(0xFFF5C6A5)),
    StorePetUIModel("Dragão Faísca", 750, "🐲", Color(0xFFDDF2D1))
)

private val storeBackgrounds = listOf(
    StoreBackgroundUIModel("Praia Ensolarada", 150, "🏝️", Color(0xFF4FC3F7), Color(0xFFFFE082)),
    StoreBackgroundUIModel("Quarto Gamer", 250, "🎮", Color(0xFF6D4C41), Color(0xFFBF8F6B)),
    StoreBackgroundUIModel("Espaço Sideral", 400, "🪐", Color(0xFF1A1446), Color(0xFF8E2DE2)),
    StoreBackgroundUIModel("Montanhas Geladas", 550, "🏔️", Color(0xFFB3E5FC), Color(0xFFE1F5FE))
)

/* ---------- Tela ---------- */

@Composable
fun StoreScreen() {
    Scaffold(
        containerColor = BackgroundColor,
        bottomBar = { CustomBottomNavigation(currentRoute = "store") }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { StoreTopHeader(coins = 150) }

            item { StoreSectionTitle("PETS COMPANHEIROS") }
            storePets.chunked(2).forEach { rowItems ->
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        rowItems.forEach { pet ->
                            PetStoreCard(pet = pet, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            item { StoreSectionTitle("PAISAGENS DE FUNDO") }
            storeBackgrounds.chunked(2).forEach { rowItems ->
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        rowItems.forEach { bg ->
                            BackgroundStoreCard(item = bg, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun StoreTopHeader(coins: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Loja",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        // Mesmo selo de moedas usado na tela de Calendário
        CoinBadge(coins = coins)
    }
}

@Composable
fun StoreSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.ExtraBold,
        color = DarkText,
        modifier = Modifier.padding(top = 4.dp)
    )
}

@Composable
fun PriceChip(price: Int) {
    Surface(
        color = YellowBadge,
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(GoldText, CircleShape)
            )
            Text(
                text = "$price",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
        }
    }
}

@Composable
fun PetStoreCard(pet: StorePetUIModel, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(pet.circleColor)
                    .border(3.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = pet.emoji, fontSize = 40.sp)
            }

            Text(
                text = pet.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (pet.isEquipped) {
                Surface(
                    color = SoftGreen,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Equipado",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            } else {
                PriceChip(price = pet.price)
            }
        }
    }
}

@Composable
fun BackgroundStoreCard(item: StoreBackgroundUIModel, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(item.gradientStart, item.gradientEnd)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item.emoji, fontSize = 28.sp)
            }

            Text(
                text = item.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            PriceChip(price = item.price)
        }
    }
}