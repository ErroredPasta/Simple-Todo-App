package com.example.simpletodoapp.todo.data.mapper

import com.example.simpletodoapp.todo.data.local.TodoEntity
import com.example.simpletodoapp.todo.domain.Todo

internal fun TodoEntity.toTodo() = Todo(
    id = id,
    todo = todo,
    description = description
)