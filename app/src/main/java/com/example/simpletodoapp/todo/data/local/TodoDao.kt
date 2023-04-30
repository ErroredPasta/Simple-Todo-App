package com.example.simpletodoapp.todo.data.local

import androidx.room.*
import com.example.simpletodoapp.todo.data.local.TodoEntity.Companion.TODO_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM $TODO_TABLE_NAME")
    fun getTodos(): Flow<List<TodoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoEntity)

    @Delete
    suspend fun deleteTodo(todo: TodoEntity)
}