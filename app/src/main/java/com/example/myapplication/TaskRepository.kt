package com.example.myapplication

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TaskItemData(
    val id: Long,
    val title: String,
    val reward: Int,
    val isCompleted: Boolean = false
)

object TaskRepository {

    private val _tasks = MutableStateFlow(
        listOf(
            TaskItemData(id = 1, title = "Arrumar cama", reward = 10, isCompleted = true),
            TaskItemData(id = 2, title = "Escovar os dentes", reward = 10, isCompleted = true),
            TaskItemData(id = 3, title = "Fazer lição de casa", reward = 25),
            TaskItemData(id = 4, title = "Ler livro por 15min", reward = 15),
            TaskItemData(id = 5, title = "Alimentar o companheiro", reward = 10),
            TaskItemData(id = 6, title = "Tomar banho", reward = 15)
        )
    )

    val tasks: StateFlow<List<TaskItemData>> = _tasks.asStateFlow()

    fun addTask(title: String, reward: Int) {
        _tasks.update { current ->
            val nextId = (current.maxOfOrNull { it.id } ?: 0L) + 1L
            current + TaskItemData(id = nextId, title = title, reward = reward)
        }
    }

    fun toggleTaskCompletion(taskId: Long) {
        _tasks.update { current ->
            current.map { task ->
                if (task.id == taskId) task.copy(isCompleted = !task.isCompleted) else task
            }
        }
    }
}