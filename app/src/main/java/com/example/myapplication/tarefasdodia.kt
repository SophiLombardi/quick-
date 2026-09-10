package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TarefasDoDiaScreen()
                }
            }
        }
    }
}

private data class Tarefa(
    val titulo: String,
    val recompensa: Int,
    val concluida: Boolean
)

private val CorFundo = Color(0xFFFDF6E9)
private val CorCard = Color(0xFFFFFFFF)
private val CorMoeda = Color(0xFFF4B740)
private val CorTextoPrincipal = Color(0xFF3B3730)
private val CorTextoSecundario = Color(0xFF8A8578)
private val CorVerdeCheck = Color(0xFF3FBF7F)
private val CorAzulProgresso = Color(0xFF5AA9E6)
private val CorTrilhaProgresso = Color(0xFFE7E1D2)

private val tarefasMock = listOf(
    Tarefa("Arrumar cama", 10, concluida = true),
    Tarefa("Escovar os dentes", 10, concluida = true),
    Tarefa("Fazer lição de casa", 25, concluida = false),
    Tarefa("Ler livro por 15min", 15, concluida = false),
    Tarefa("Alimentar o companheiro", 10, concluida = false),
    Tarefa("Tomar banho", 15, concluida = false)
)

@Composable
fun TarefasDoDiaScreen() {
    val concluidas = tarefasMock.count { it.concluida }
    val total = tarefasMock.size

    Scaffold(
        containerColor = CorFundo,
        bottomBar = { TarefasBottomNavBar() }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CorFundo)
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            TarefasHeader(moedas = 150)

            Spacer(modifier = Modifier.height(20.dp))
            ProgressoDoDia(concluidas = concluidas, total = total)

            Spacer(modifier = Modifier.height(20.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                tarefasMock.forEach { tarefa ->
                    TarefaCard(tarefa = tarefa)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TarefasHeader(moedas: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Tarefas do Dia",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = CorTextoPrincipal
        )
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Color(0xFFFFF3D6))
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(CorMoeda)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$moedas",
                fontWeight = FontWeight.Bold,
                color = CorTextoPrincipal
            )
        }
    }
}

@Composable
private fun ProgressoDoDia(concluidas: Int, total: Int) {
    val fracao = if (total > 0) concluidas.toFloat() / total.toFloat() else 0f

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CorCard),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Progresso do dia",
                    fontSize = 13.sp,
                    color = CorTextoSecundario,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "$concluidas / $total concluídos",
                    fontSize = 13.sp,
                    color = CorTextoSecundario,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(CorTrilhaProgresso)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = fracao)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(50))
                        .background(CorAzulProgresso)
                )
            }
        }
    }
}

@Composable
private fun TarefaCard(tarefa: Tarefa) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CorCard),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CheckboxCircular(marcado = tarefa.concluida)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = tarefa.titulo,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (tarefa.concluida) CorTextoSecundario else CorTextoPrincipal
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFFFF3D6))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(CorMoeda)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "+${tarefa.recompensa}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CorTextoPrincipal
                    )
                }
            }
        }
    }
}

@Composable
private fun CheckboxCircular(marcado: Boolean) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(if (marcado) CorVerdeCheck else Color.Transparent)
            .border(
                width = 2.dp,
                color = if (marcado) Color.Transparent else CorTrilhaProgresso,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (marcado) {
            Text(
                text = "✓",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun TarefasBottomNavBar() {
    NavigationBar(containerColor = CorCard) {
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Text(text = "🏠", fontSize = 18.sp) },
            label = { Text(text = "Início", fontSize = 11.sp) },
            colors = itemColors()
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Text(text = "📅", fontSize = 18.sp) },
            label = { Text(text = "Calendário", fontSize = 11.sp) },
            colors = itemColors()
        )
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Text(text = "📋", fontSize = 18.sp) },
            label = { Text(text = "Tarefas", fontSize = 11.sp) },
            colors = itemColors()
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Text(text = "🛒", fontSize = 18.sp) },
            label = { Text(text = "Loja", fontSize = 11.sp) },
            colors = itemColors()
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Text(text = "👤", fontSize = 18.sp) },
            label = { Text(text = "Perfil", fontSize = 11.sp) },
            colors = itemColors()
        )
    }
}

@Composable
private fun itemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = CorTextoPrincipal,
    selectedTextColor = CorTextoPrincipal,
    unselectedIconColor = CorTextoSecundario,
    unselectedTextColor = CorTextoSecundario,
    indicatorColor = Color(0xFFFFF3D6)
)

@Preview(showBackground = true)
@Composable
private fun TarefasDoDiaScreenPreview() {
    MaterialTheme {
        TarefasDoDiaScreen()
    }
}