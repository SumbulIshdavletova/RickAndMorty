package ru.sumbul.rickandmorty.episodes.ui.list

import android.content.Context
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.sumbul.rickandmorty.R
import ru.sumbul.rickandmorty.application.appComponent
import ru.sumbul.rickandmorty.ui.LoadingStateAdapter
import ru.sumbul.rickandmorty.databinding.FragmentEpisodesListBinding
import ru.sumbul.rickandmorty.episodes.ui.details.EpisodeDetailsFragment
import ru.sumbul.rickandmorty.episodes.domain.model.Episode
import ru.sumbul.rickandmorty.factory.EpisodesViewModelFactory
import javax.inject.Inject

class EpisodesListFragment : Fragment() {

    @Inject
    lateinit var factory: EpisodesViewModelFactory
    @OptIn(ExperimentalCoroutinesApi::class)
    val viewModel by viewModels<EpisodeViewModel>(factoryProducer = { factory })

    @OptIn(ExperimentalCoroutinesApi::class)
    val episodeDetailsFragment: EpisodeDetailsFragment = EpisodeDetailsFragment()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        EpisodeAdapter(object : OnInteractionListenerCharacter {
            override fun onClick(episode: Episode) {
            //    viewModel.getById(episode.id)
                val bundle2 = Bundle()
                bundle2.putSerializable("requestKey", episode)
                parentFragmentManager.setFragmentResult("requestKey", bundle2)
                parentFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.frame_layout, episodeDetailsFragment)
                    .addToBackStack("details")
                    .commit()
            }
        })
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
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