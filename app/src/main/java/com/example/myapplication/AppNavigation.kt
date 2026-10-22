package com.example.myapplication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

private data class RewardItem(val id: String, val name: String, val description: String)
private val rewardItems = mutableStateListOf(
    RewardItem("1", "Novo visual", "Um visual especial para o seu companheiro."),
    RewardItem("2", "Acessório", "Um acessório para personalizar o pet.")
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalRootNavController provides navController) {
        NavHost(navController = navController, startDestination = Rotas.HOME) {
            composable(Rotas.HOME) { PetHomeScreen(navController = navController) }
            composable(Rotas.CALENDARIO) { CalendarScreen() }
            composable(Rotas.TAREFAS) { TaskListScreen(onOpen = { navController.navigate(Rotas.detalheTarefa(it)) }, onAdd = { navController.navigate(Rotas.ADICIONAR_TAREFA) }, onRewards = { navController.navigate(Rotas.RECOMPENSAS) }, onAchievements = { navController.navigate(Rotas.CONQUISTAS) }) }
            composable(Rotas.LOJA) { StoreScreen() }
            composable(route = Rotas.PERFIL) {
                PerfilScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(Rotas.ADICIONAR_TAREFA) {
                AddTaskScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { description, _, reward ->
                        if (description.isNotBlank()) TaskRepository.addTask(description, reward)
                        navController.popBackStack()
                    },
                    onNavigate = { index -> navController.navigate(bottomNavEntries.getOrNull(index)?.route ?: Rotas.HOME) }
                )
            }
            composable(Rotas.CONQUISTAS) { TelaConquistasQuick(onVoltarClick = { navController.popBackStack() }) }
            composable(Rotas.RECOMPENSAS) { RewardsListScreen(onOpen = { navController.navigate(Rotas.detalheRecompensa(it)) }) }
            composable(Rotas.DETALHE_TAREFA, arguments = listOf(navArgument("taskId") { type = NavType.LongType })) { entry ->
                TaskDetailScreen(taskId = entry.arguments?.getLong("taskId") ?: -1L, onBack = { navController.popBackStack() })
            }
            composable(Rotas.DETALHE_RECOMPENSA, arguments = listOf(navArgument("rewardId") { type = NavType.StringType })) { entry ->
                RewardDetailScreen(rewardId = entry.arguments?.getString("rewardId").orEmpty(), onBack = { navController.popBackStack() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskListScreen(onOpen: (Long) -> Unit, onAdd: () -> Unit, onRewards: () -> Unit, onAchievements: () -> Unit) {
    val tasks by TaskRepository.tasks.collectAsState()
    Scaffold(
        containerColor = Color(0xFFFFFDF7),
        topBar = { TopAppBar(title = { Text("Tarefas", fontWeight = FontWeight.Bold) }, actions = { IconButton(onClick = onAdd) { Icon(Icons.Default.Add, "Adicionar tarefa") } }) },
        bottomBar = { CustomBottomNavigation(Rotas.TAREFAS) }
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item { Text("Toque em uma tarefa para ver os detalhes. Marque para concluir ou remova-a na tela de detalhes.") }
            items(tasks, key = { it.id }) { task ->
                Card(Modifier.fillMaxWidth().clickable { onOpen(task.id) }) {
                    Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(Modifier.weight(1f)) {
                            Text(task.title, fontWeight = FontWeight.SemiBold)
                            Text("Recompensa: ${task.reward} moedas")
                        }
                        Text(if (task.isCompleted) "Concluída ✓" else "Pendente")
                    }
                }
            }
            item { OutlinedButton(onClick = onRewards, modifier = Modifier.fillMaxWidth()) { Text("Abrir lista de recompensas") } }
            item { OutlinedButton(onClick = onAchievements, modifier = Modifier.fillMaxWidth()) { Text("Ver conquistas") } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskDetailScreen(taskId: Long, onBack: () -> Unit) {
    val tasks by TaskRepository.tasks.collectAsState()
    val task = tasks.firstOrNull { it.id == taskId }
    Scaffold(topBar = { TopAppBar(title = { Text("Detalhes da tarefa") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (task == null) Text("Esta tarefa não existe mais.") else {
                Text(task.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Identificador: ${task.id}")
                Text("Recompensa: ${task.reward} moedas")
                Text("Estado: ${if (task.isCompleted) "Concluída" else "Pendente"}")
                Button(onClick = { TaskRepository.toggleTaskCompletion(task.id) }, modifier = Modifier.fillMaxWidth()) { Text(if (task.isCompleted) "Marcar como pendente" else "Marcar como concluída") }
                OutlinedButton(onClick = { TaskRepository.removeTask(task.id); onBack() }, modifier = Modifier.fillMaxWidth()) { Text("Remover tarefa") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RewardDetailScreen(rewardId: String, onBack: () -> Unit) {
    val reward = rewardItems.firstOrNull { it.id == rewardId }
    Scaffold(topBar = { TopAppBar(title = { Text("Detalhes da recompensa") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (reward == null) Text("Esta recompensa não existe mais.") else {
                Text(reward.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Identificador: ${reward.id}")
                Text(reward.description)
                Button(onClick = { rewardItems.remove(reward); onBack() }, modifier = Modifier.fillMaxWidth()) { Text("Remover da lista") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RewardsListScreen(onOpen: (String) -> Unit) {
    var newReward by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Lista de recompensas", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        OutlinedTextField(value = newReward, onValueChange = { newReward = it }, label = { Text("Nova recompensa") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = { val title = newReward.trim(); if (title.isNotEmpty()) { val nextId = (rewardItems.maxOfOrNull { it.id.toIntOrNull() ?: 0 } ?: 0) + 1; rewardItems.add(RewardItem(nextId.toString(), title, "Recompensa adicionada por você.")); newReward = "" } }, modifier = Modifier.fillMaxWidth()) { Text("Adicionar recompensa") }
        rewardItems.forEach { reward -> Card(Modifier.fillMaxWidth().clickable { onOpen(reward.id) }) { Text(reward.name, Modifier.padding(16.dp)) } }
    }
}
