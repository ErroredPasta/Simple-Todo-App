package com.example.simpletodoapp.todo.ui.list

import androidx.lifecycle.ViewModel
import com.example.simpletodoapp.todo.domain.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {
    val todos = repository.getTodos()
}