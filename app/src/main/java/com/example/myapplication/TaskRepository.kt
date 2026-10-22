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
    // O _tasks PRECISA estar aqui dentro do object
    private val _tasks = MutableStateFlow(
        listOf(
            TaskItemData(id = 1, title = "Arrumar cama", reward = 10, isCompleted = true),
            TaskItemData(id = 2, title = "Lavar a louça", reward = 15, isCompleted = false),
            TaskItemData(id = 3, title = "Estudar Kotlin", reward = 20, isCompleted = false)
        )
    )
    val tasks: StateFlow<List<TaskItemData>> = _tasks.asStateFlow()

    fun addTask(title: String, reward: Int) {
        _tasks.update { currentTasks ->
            val nextId = (currentTasks.maxOfOrNull { it.id } ?: 0L) + 1L
            currentTasks + TaskItemData(id = nextId, title = title, reward = reward)
        }
    }

    fun toggleTaskCompletion(taskId: Long) {
        _tasks.update { currentTasks ->
            currentTasks.map { task ->
                if (task.id == taskId) task.copy(isCompleted = !task.isCompleted) else task
            }
        }
    }

    // Função adicionada
    fun removeTask(taskId: Long) {
        _tasks.update { currentTasks ->
            // O 'it' aqui dentro é resolvido automaticamente porque currentTasks é uma List<TaskItemData>
            currentTasks.filterNot { it.id == taskId }
        }
    }
}