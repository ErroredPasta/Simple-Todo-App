package com.example.simpletodoapp.todo.ui.insert

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.simpletodoapp.R
import com.example.simpletodoapp.di.todo.TodoDataModule
import com.example.simpletodoapp.todo.data.repository.FakeTodoRepository
import com.example.simpletodoapp.todo.domain.TodoDetail
import com.example.simpletodoapp.todo.domain.TodoRepository
import com.google.common.truth.Truth.assertThat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
@UninstallModules(TodoDataModule::class)
class TodoInsertScreenTest {
    @Module
    @InstallIn(SingletonComponent::class)
    object FakeTodoDataModule {
        @Provides
        fun provideFakeTodoRepository(): TodoRepository = FakeTodoRepository()
    }

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createComposeRule()

    private lateinit var viewModel: TodoInsertViewModel

    @Inject
    lateinit var todoRepository: TodoRepository

    private lateinit var onSuccessInvoked: AtomicBoolean

    @Before
    fun setUp() {
        hiltRule.inject()

        onSuccessInvoked = AtomicBoolean(false)
        viewModel = TodoInsertViewModel(repository = todoRepository)

        composeRule.setContent {
            TodoInsertScreen(
                viewModel = viewModel,
                onSuccess = { onSuccessInvoked.set(true) }
            )
        }
    }

    @Test
    fun correctlyWriteTodoAndDescription_thenOnSuccessLambdaInvoked() {
        // act
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.onNodeWithText(context.getString(R.string.todo))
            .performTextInput("New Todo")

        composeRule.onNodeWithText(context.getString(R.string.description))
            .performTextInput("New Description")

        composeRule.onNodeWithText(context.getString(R.string.add_todo), ignoreCase = true)
            .performClick()

        // assert
        assertThat(onSuccessInvoked.get()).isTrue()
    }

    @Test
    fun correctlyWriteTodoAndDescription_thenSuccessfullyInsertTheTodo() = runTest {
        // assert
        lateinit var todos: List<TodoDetail>
        val job = launch {
            todoRepository.getTodos().collect {
                todos = it
            }
        }
        advanceUntilIdle()

        assertThat(todos.map { it.copy(id = 0L) })
            .doesNotContain(TodoDetail(todo = "New Todo", description = "New Description"))

        // act
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.onNodeWithText(context.getString(R.string.todo))
            .performTextInput("New Todo")

        composeRule.onNodeWithText(context.getString(R.string.description))
            .performTextInput("New Description")

        composeRule.onNodeWithText(context.getString(R.string.add_todo), ignoreCase = true)
            .performClick()

        // assert
        advanceUntilIdle()

        println(todos)
        assertThat(todos.map { it.copy(id = 0L) })
            .contains(TodoDetail(todo = "New Todo", description = "New Description"))
        job.cancel()
    }

    @Test
    fun todoIsEmpty_thenErrorMessageShouldBeDisplayed() {
        // act
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.onNodeWithText(context.getString(R.string.description))
            .performTextInput("New Description")

        composeRule.onNodeWithText(context.getString(R.string.add_todo), ignoreCase = true)
            .performClick()

        // assert
        composeRule.onNodeWithText(context.getString(R.string.todo_must_not_be_blank))
            .assertIsDisplayed()
    }

    @Test
    fun todoIsEmpty_thenNothingInsertedIntoTheRepository() = runTest {
        // assert
        lateinit var todos: List<TodoDetail>
        val job = launch {
            todoRepository.getTodos().collect {
                todos = it
            }
        }
        advanceUntilIdle()

        assertThat(todos.map { it.copy(id = 0L) })
            .doesNotContain(TodoDetail(todo = "New Todo", description = "New Description"))

        // act
        val context = ApplicationProvider.getApplicationContext<Context>()

        composeRule.onNodeWithText(context.getString(R.string.description))
            .performTextInput("New Description")

        composeRule.onNodeWithText(context.getString(R.string.add_todo), ignoreCase = true)
            .performClick()

        // assert
        advanceUntilIdle()

        println(todos)
        assertThat(todos.map { it.copy(id = 0L) })
            .doesNotContain(TodoDetail(todo = "New Todo", description = "New Description"))
        job.cancel()
    }
}