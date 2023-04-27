package com.example.simpletodoapp.todo.domain

data class Todo(
    val id: Long,
    val todo: String,
    val description: String? = null
)
