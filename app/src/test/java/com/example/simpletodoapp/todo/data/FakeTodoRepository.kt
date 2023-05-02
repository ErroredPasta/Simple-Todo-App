package com.example.simpletodoapp.todo.data

import com.example.simpletodoapp.todo.domain.Todo
import com.example.simpletodoapp.todo.domain.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeTodoRepository(
    initialList: List<Todo>
) : TodoRepository {
    private val list = MutableStateFlow(initialList)

    override fun getTodos(): Flow<List<Todo>> {
        return list
    }

    override suspend fun insertTodo(todo: Todo) {
        val todoToInsert = if (todo.id == 0L) todo.copy(id = list.value.size.toLong() + 1) else todo
        list.update { it + todoToInsert }
    }

    override suspend fun deleteTodo(todo: Todo) {
        list.update { it - todo }
    }
}