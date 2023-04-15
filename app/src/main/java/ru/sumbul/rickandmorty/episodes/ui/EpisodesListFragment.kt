package ru.sumbul.rickandmorty.episodes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.sumbul.rickandmorty.R
import ru.sumbul.rickandmorty.adapter.LoadingStateAdapter
import ru.sumbul.rickandmorty.databinding.FragmentEpisodesListBinding
import ru.sumbul.rickandmorty.episodes.EpisodeViewModel

@AndroidEntryPoint
class EpisodesListFragment : Fragment() {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val viewModel: EpisodeViewModel by viewModels()

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        EpisodeAdapter()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEpisodesListBinding.inflate(inflater, container, false)

        binding.list.adapter = adapter

        lifecycleScope.launch {
            viewModel.episodePagingFlow.collect { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoadingStateAdapter { adapter.retry() },
            footer = LoadingStateAdapter { adapter.retry() })

        binding.swipeRefresh.setOnRefreshListener(adapter::refresh)

        lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                adapter.loadStateFlow.collectLatest { state ->
                    binding.swipeRefresh.isRefreshing = state.refresh is LoadState.Loading
                    state.append is LoadState.Loading || state.prepend is LoadState.Loading
                }
            }
        }

        return binding.root
    }

}