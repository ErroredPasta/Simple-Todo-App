package com.example.simpletodoapp.todo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simpletodoapp.todo.data.local.TodoEntity.Companion.TODO_TABLE_NAME

@Entity(tableName = TODO_TABLE_NAME)
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val todo: String,
    val description: String? = null
) {
    companion object {
        const val TODO_TABLE_NAME = "todo_table"
    }
}

