package com.example.simpletodoapp.todo.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpletodoapp.todo.domain.Todo
import com.example.simpletodoapp.todo.domain.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoRepository,
) : ViewModel() {
    private val searchKeyword = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val todos = searchKeyword.flatMapLatest { keyword ->
        if (keyword.isBlank()) {
            repository.getTodos()
        } else {
            repository.getTodosContainingKeyword(keyword = keyword)
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            repository.deleteTodo(todo = todo)
        }
    }

    fun setSearchKeyword(keyword: String) {
        searchKeyword.update { keyword }
    }
}