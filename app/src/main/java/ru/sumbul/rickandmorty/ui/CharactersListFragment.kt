package ru.sumbul.rickandmorty.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.sumbul.rickandmorty.characters.CharacterAdapter
import ru.sumbul.rickandmorty.characters.CharacterViewModel
import ru.sumbul.rickandmorty.databinding.FragmentCharactersListBinding
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class CharactersListFragment : Fragment() {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val viewModel: CharacterViewModel by viewModels()

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        CharacterAdapter()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCharactersListBinding.inflate(inflater, container, false)

        binding.list.adapter = adapter

        lifecycleScope.launch {
            viewModel.characterPagingFlow.collectLatest(adapter::submitData)
        }
        return binding.root
    }

}