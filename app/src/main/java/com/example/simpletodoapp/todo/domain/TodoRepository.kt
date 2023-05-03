package com.example.simpletodoapp.todo.domain

import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTodos(): Flow<List<Todo>>

    @Throws(TodoException.NoTodoDetailFound::class)
    suspend fun getTodoDetail(id: Long): TodoDetail

    suspend fun insertTodo(todo: Todo)
    suspend fun deleteTodo(todo: Todo)
}