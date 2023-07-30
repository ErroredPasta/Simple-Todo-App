package com.example.simpletodoapp.todo.ui.insert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.simpletodoapp.todo.ui.insert.compose.SimpleTodoAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoInsertFragment : Fragment() {
    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setContent {
            SimpleTodoAppTheme {
                TodoInsertScreen(
                    modifier = Modifier.fillMaxSize(),
                    onSuccess = { navController.navigateUp() },
                )
            }
        }
    }
}