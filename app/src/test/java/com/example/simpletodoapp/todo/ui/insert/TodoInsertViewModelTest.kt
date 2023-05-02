package com.example.simpletodoapp.todo.ui.insert

import com.example.simpletodoapp.core.TestCoroutineRule
import com.example.simpletodoapp.todo.data.FakeTodoRepository
import com.example.simpletodoapp.todo.domain.Todo
import com.example.simpletodoapp.todo.domain.TodoRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Before
import org.junit.Rule


@OptIn(ExperimentalCoroutinesApi::class)
class TodoInsertViewModelTest {
    private lateinit var sut: TodoInsertViewModel
    private lateinit var repository: TodoRepository

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val initialTodoList = (1..10).map {
        Todo(
            id = it.toLong(),
            todo = "todo $it",
            description = "description $it"
        )
    }

    @Before
    fun setup() {
        repository = FakeTodoRepository(initialList = initialTodoList)
        sut = TodoInsertViewModel(repository = repository)
    }

    @Test
    fun `insert todo, the added one is in todos from repository`() = runTest {
        // act
        val todoToAdd = Todo(
            // not assigned id as it should be assigned by database
            todo = "new todo to add"
        )
        sut.insertTodo(todo = todoToAdd)

        // assert
        lateinit var collectedTodos: List<Todo>

        val job = launch {
            repository.getTodos().collect {
                collectedTodos = it
            }
        }
        advanceUntilIdle()

        // remove id of collected todos
        assertThat(collectedTodos.map { it.copy(id = 0L) }).contains(todoToAdd)
        job.cancel()
    }
}