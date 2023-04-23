package ru.sumbul.rickandmorty.locations.ui

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
import ru.sumbul.rickandmorty.databinding.FragmentLocationsListBinding
import ru.sumbul.rickandmorty.episodes.EpisodeViewModel
import ru.sumbul.rickandmorty.episodes.ui.EpisodeAdapter
import ru.sumbul.rickandmorty.locationDetails.LocationDetailsFragment
import ru.sumbul.rickandmorty.locations.LocationViewModel
import ru.sumbul.rickandmorty.locations.entity.Location

@AndroidEntryPoint
class LocationsListFragment : Fragment() {


    @OptIn(ExperimentalCoroutinesApi::class)
    private val viewModel: LocationViewModel by viewModels()

    @OptIn(ExperimentalCoroutinesApi::class)
    val locationDetailsFragment: LocationDetailsFragment = LocationDetailsFragment()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        LocationAdapter(object : OnInteractionListenerLocation {
            override fun onClick(location: Location) {
                viewModel.getById(location.id)
                val bundle2 = Bundle()
                bundle2.putSerializable("requestKey1", location)
                parentFragmentManager.setFragmentResult("requestKey1", bundle2)
                parentFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.frame_layout, locationDetailsFragment)
                    .addToBackStack("details")
                    .commit()
            }
        })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLocationsListBinding.inflate(inflater, container, false)

        binding.list.adapter = adapter

        lifecycleScope.launch {
            viewModel.locationPagingFlow.collect { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoadingStateAdapter { adapter.retry() },
            footer = LoadingStateAdapter { adapter.retry() })

        binding.swipeRefresh.setOnRefreshListener(adapter::refresh)

        lifecycleScope.launch {
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