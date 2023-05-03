package com.example.simpletodoapp.todo

import com.example.simpletodoapp.todo.domain.Todo

fun IntRange.createTodoFromRange(createNewTodo: Boolean = false): List<Todo> = map {
    Todo(
        id = if (createNewTodo) 0L else it.toLong(),
        todo = "todo $it",
        description = "description $it"
    )
}