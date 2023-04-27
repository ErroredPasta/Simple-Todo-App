package com.example.simpletodoapp.todo.data.repository

import com.example.simpletodoapp.todo.data.local.TodoDao
import com.example.simpletodoapp.todo.data.mapper.toTodo
import com.example.simpletodoapp.todo.domain.Todo
import com.example.simpletodoapp.todo.domain.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val dao: TodoDao
) : TodoRepository {
    override fun getTodos(): Flow<List<Todo>> {
        return dao.getTodos().map { todos ->
            todos.map { todo -> todo.toTodo() }
        }
    }
}