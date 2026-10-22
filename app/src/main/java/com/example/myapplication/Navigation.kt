package com.example.myapplication

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

val LocalRootNavController = staticCompositionLocalOf<NavHostController?> { null }

/** Rotas centralizadas do aplicativo. */
object Rotas {
    const val HOME = "home"
    const val CALENDARIO = "calendario"
    const val TAREFAS = "tarefas"
    const val LOJA = "loja"
    const val PERFIL = "perfil"
    const val ADICIONAR_TAREFA = "adicionar_tarefa"
    const val CONQUISTAS = "conquistas"
    const val RECOMPENSAS = "recompensas"
    const val DETALHE_TAREFA = "detalhe_tarefa/{taskId}"
    const val DETALHE_RECOMPENSA = "detalhe_recompensa/{rewardId}"
    fun detalheTarefa(id: Long) = "detalhe_tarefa/$id"
    fun detalheRecompensa(id: String) = "detalhe_recompensa/$id"
}

data class BottomNavEntry(val route: String, val icon: ImageVector, val label: String)

val bottomNavEntries = listOf(
    BottomNavEntry(Rotas.HOME, Icons.Default.Home, "Início"),
    BottomNavEntry(Rotas.CALENDARIO, Icons.Default.DateRange, "Calendário"),
    BottomNavEntry(Rotas.TAREFAS, Icons.AutoMirrored.Filled.List, "Tarefas"),
    BottomNavEntry(Rotas.LOJA, Icons.Default.ShoppingCart, "Loja"),
    BottomNavEntry(Rotas.PERFIL, Icons.Default.Person, "Perfil")
)

@Composable
fun CustomBottomNavigation(currentRoute: String = Rotas.HOME, navController: NavHostController? = LocalRootNavController.current) {
    Surface(color = Color.White, shadowElevation = 8.dp) {
        NavigationBar(containerColor = Color.White) {
            bottomNavEntries.forEach { entry ->
                NavigationBarItem(
                    selected = currentRoute == entry.route,
                    onClick = {
                        navController?.navigate(entry.route) {
                            popUpTo(Rotas.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(entry.icon, contentDescription = entry.label) },
                    label = { Text(entry.label, fontSize = 10.sp, fontWeight = if (currentRoute == entry.route) FontWeight.Bold else FontWeight.Normal) }
                )
            }
        }
    }
}
