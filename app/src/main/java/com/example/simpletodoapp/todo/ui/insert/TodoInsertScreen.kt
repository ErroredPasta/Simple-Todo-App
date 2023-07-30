package com.example.simpletodoapp.todo.ui.insert

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simpletodoapp.R
import com.example.simpletodoapp.todo.domain.TodoDetail
import com.example.simpletodoapp.todo.ui.insert.compose.SimpleTodoAppTheme

private val TODO_INSERT_SCREEN_PADDING = 15.dp
private val SPACE_BETWEEN_COMPONENTS = 15.dp

@Composable
fun TodoInsertScreen(
    modifier: Modifier = Modifier,
    viewModel: TodoInsertViewModel = viewModel(),
    onSuccess: () -> Unit,
) {
    var todoTitle by remember { mutableStateOf("") }
    var todoDescription by remember { mutableStateOf("") }
    var errorOccurred by remember { mutableStateOf(false) }

    TodoInsertScreenBody(
        modifier = modifier,
        todoTitle = todoTitle,
        onTodoTitleChange = { todoTitle = it },
        todoDescription = todoDescription,
        onTodoDescriptionChange = { todoDescription = it },
        onAddButtonClick = {
            val todoDetail = TodoDetail(
                todo = todoTitle,
                description = todoDescription.ifBlank { null }
            )

            viewModel.insertTodo(
                todoDetail = todoDetail,
                onSuccess = onSuccess,
                onError = { errorOccurred = true }
            )
        },
        errorOccurred = errorOccurred
    )
}

@Composable
fun TodoInsertScreenBody(
    modifier: Modifier = Modifier,
    todoTitle: String,
    onTodoTitleChange: (String) -> Unit,
    todoDescription: String,
    onTodoDescriptionChange: (String) -> Unit,
    onAddButtonClick: () -> Unit,
    errorOccurred: Boolean,
) {
    Column(
        modifier = modifier.padding(TODO_INSERT_SCREEN_PADDING)
    ) {
        OutlinedTextField(
            value = todoTitle,
            onValueChange = onTodoTitleChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.todo)) },
            isError = errorOccurred
        )
        if (errorOccurred) Text(
            text = stringResource(id = R.string.todo_must_not_be_blank),
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.error
        )

        Spacer(modifier = Modifier.height(SPACE_BETWEEN_COMPONENTS))

        OutlinedTextField(
            value = todoDescription,
            onValueChange = onTodoDescriptionChange,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.description)) },
        )
        Button(onClick = onAddButtonClick, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.add_todo).uppercase())
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TodoInsertScreenBodyPreview() {
    SimpleTodoAppTheme {
        TodoInsertScreenBody(
            todoTitle = "",
            onTodoTitleChange = {},
            todoDescription = "",
            onTodoDescriptionChange = {},
            onAddButtonClick = {},
            errorOccurred = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OutlinedTextFieldPreview() {
    SimpleTodoAppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    isError = true,
                    label = { Text(text = "Hello") },
                    placeholder = { Text(text = "placeholder") },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}