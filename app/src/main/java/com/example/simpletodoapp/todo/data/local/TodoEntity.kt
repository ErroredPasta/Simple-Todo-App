package com.example.simpletodoapp.todo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val todo: String,
    val description: String? = null
)