package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

// Activity necessária para a navegação via Intent a partir de Calendario.kt
class Perfil : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PerfilScreen()
                }
            }
        }
    }
}

data class ProfileState(
    val userName: String = "Usuário",
    val totalPoints: Int = 0,
    val completedTasks: Int = 0
)

@Composable
fun rememberProfileState(
    tasks: List<TaskItemData>
): ProfileState {
    return remember(tasks) {
        ProfileState(
            userName = "Sophi",
            totalPoints = tasks.filter { it.isCompleted }.sumOf { it.reward },
            completedTasks = tasks.count { it.isCompleted }
        )
    }
}

@Composable
fun PerfilScreen(
    modifier: Modifier = Modifier,
    tasks: List<TaskItemData> = TaskRepository.tasks.collectAsStateWithLifecycle().value
) {
    val profileState = rememberProfileState(tasks = tasks)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = profileState.userName,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Minhas Estatísticas", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Pontos Totais: ${profileState.totalPoints}", fontSize = 18.sp)
                    Text(text = "Tarefas Concluídas: ${profileState.completedTasks}", fontSize = 18.sp)
                }
            }
        }

        item {
            Text(
                text = "Tarefas Concluídas",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        }

        val completedTasks = tasks.filter { it.isCompleted }

        if (completedTasks.isEmpty()) {
            item {
                Text(
                    text = "Nenhuma tarefa concluída ainda.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(completedTasks) { task ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = task.title)
                        Text(text = "+${task.reward} pts", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}