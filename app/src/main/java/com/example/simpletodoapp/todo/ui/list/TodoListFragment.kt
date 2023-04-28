package com.example.simpletodoapp.todo.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.simpletodoapp.R
import com.example.simpletodoapp.databinding.FragmentTodoListBinding
import com.example.simpletodoapp.todo.ui.mapper.toTodoUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TodoListFragment : Fragment() {
    private val viewModel by viewModels<TodoListViewModel>()
    private val adapter by lazy { TodoAdapter() }
    private var recyclerView: RecyclerView? = null
    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTodoListBinding.inflate(inflater, container, false).apply {
        todoRecyclerView.adapter = adapter
        todoRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))
    }.also { binding ->
        recyclerView = binding.todoRecyclerView

        viewModel.todos
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { todos ->
                val todoUiStates = todos.map { todo ->
                    todo.toTodoUiState(onClick = {
                        navController.navigate(R.id.action_todoListFragment_to_todoInsertFragment)
                    })
                }

                adapter.submitList(todoUiStates)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
    }.root

    override fun onDestroy() {
        super.onDestroy()
        recyclerView?.adapter = null
        recyclerView = null
    }
}