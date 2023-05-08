package com.example.simpletodoapp.todo.ui.list

import com.example.simpletodoapp.core.TestCoroutineRule
import com.example.simpletodoapp.core.regexPatternForSearching
import com.example.simpletodoapp.todo.createTodoDetailFromRange
import com.example.simpletodoapp.todo.data.FakeTodoRepository
import com.example.simpletodoapp.todo.domain.Todo
import com.example.simpletodoapp.todo.domain.TodoRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class TodoListViewModelTest {
    private lateinit var sut: TodoListViewModel
    private lateinit var repository: TodoRepository

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val initialTodoList = (1..10).createTodoDetailFromRange(createNewTodo = false)

    @Before
    fun setup() {
        repository = FakeTodoRepository(initialList = initialTodoList)
        sut = TodoListViewModel(repository = repository)
    }

    @Test
    fun `collect all todos without any manipulation, it matches the initial list`() =
        runTest {
            // act
            lateinit var collectedTodos: List<Todo>

            val job = launch {
                sut.todos.collect {
                    collectedTodos = it
                }
            }
            advanceUntilIdle()

            // assert
            assertThat(collectedTodos).isEqualTo(initialTodoList)
            job.cancel()
        }

    @Test
    fun `delete todo, deleted todo is not in collected todos`() = runTest {
        // act
        val todoToDelete = initialTodoList[0]
        sut.deleteTodo(todo = todoToDelete)

        // assert
        lateinit var collectedTodos: List<Todo>

        val job = launch {
            sut.todos.collect {
                collectedTodos = it
            }
        }
        advanceUntilIdle()

        assertThat(collectedTodos).isEqualTo(initialTodoList - todoToDelete)
        job.cancel()
    }

    @Test
    fun `set keyword to search, only todos containing the keyword should be collected`() = runTest {
        // arrange
        (1..10).createTodoDetailFromRange(createNewTodo = true).forEach { todoDetail ->
            repository.insertTodo(todoDetail = todoDetail)
        }

        // act
        val keyword = "todo 1"
        sut.setSearchKeyword(keyword)

        // assert
        lateinit var collectedTodos: List<Todo>
        val job = launch {
            sut.todos.collect {
                collectedTodos = it
            }
        }
        advanceUntilIdle()

        println(collectedTodos)

        val regex = Regex(keyword.regexPatternForSearching, RegexOption.IGNORE_CASE)
        assertThat(collectedTodos.filterNot { it.todo.contains(regex) }).isEmpty()

        job.cancel()
    }
}