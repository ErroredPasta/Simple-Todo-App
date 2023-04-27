package com.example.simpletodoapp.todo.data.local

import androidx.room.Dao
import androidx.room.Query
import com.example.simpletodoapp.todo.data.local.TodoEntity.Companion.TODO_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM $TODO_TABLE_NAME")
    fun getTodos(): Flow<List<TodoEntity>>
}