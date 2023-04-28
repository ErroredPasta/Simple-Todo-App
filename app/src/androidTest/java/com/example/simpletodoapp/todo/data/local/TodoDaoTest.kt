package com.example.simpletodoapp.todo.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.simpletodoapp.core.TestCoroutineRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class TodoDaoTest {
    private lateinit var sut: TodoDao
    private lateinit var database: TodoDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    private val testDispatcher get() = testCoroutineRule.testDispatcher

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            context = ApplicationProvider.getApplicationContext(),
            klass = TodoDatabase::class.java
        ).build()

        sut = database.todoDao
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertMultipleTodos_thenGetAllTodosSuccessfully() = runTest {
        // arrange
        val todos = (1..10).map {
            TodoEntity(
                todo = "todo $it",
                description = "description $it"
            )
        }

        // act
        for (todo in todos) {
            sut.insertTodo(todo = todo)
        }

        // assert
        lateinit var todosFromDatabase: List<TodoEntity>

        val job = launch(testDispatcher) {
            sut.getTodos().collect {
                todosFromDatabase = it
            }
        }
        advanceUntilIdle()

        // remove id of todos
        assertThat(todosFromDatabase.map { it.copy(id = 0L) }).isEqualTo(todos)
        job.cancel()
    }

}