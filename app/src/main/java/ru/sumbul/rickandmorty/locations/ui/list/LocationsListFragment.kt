package ru.sumbul.rickandmorty.locations.ui.list

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
import ru.sumbul.rickandmorty.databinding.FragmentLocationsListBinding
import ru.sumbul.rickandmorty.episodes.ui.list.EpisodeViewModel
import ru.sumbul.rickandmorty.factory.EpisodesViewModelFactory
import ru.sumbul.rickandmorty.factory.LocationViewModelFactory
import ru.sumbul.rickandmorty.locations.ui.details.LocationDetailsFragment
import ru.sumbul.rickandmorty.locations.domain.model.Location
import javax.inject.Inject


class LocationsListFragment : Fragment() {

    @Inject
    lateinit var factory: LocationViewModelFactory

    @OptIn(ExperimentalCoroutinesApi::class)
    val viewModel by viewModels<LocationViewModel>(factoryProducer = { factory })

    @OptIn(ExperimentalCoroutinesApi::class)
    val locationDetailsFragment: LocationDetailsFragment =
        LocationDetailsFragment()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        LocationAdapter(object : OnInteractionListenerLocation {
            override fun onClick(location: Location) {
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

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
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

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.filterLocations("","","")
            adapter.refresh()
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                adapter.loadStateFlow.collectLatest { state ->
                    binding.swipeRefresh.isRefreshing = state.refresh is LoadState.Loading
                    state.append is LoadState.Loading || state.prepend is LoadState.Loading
                }
            }
        }

        //GO TO FILTER FRAGMENT
        val filterFragment: LocationFilterFragment = LocationFilterFragment()
        binding.filter.setOnClickListener {
            viewModel.filterLocations("", "","")
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frame_layout, filterFragment)
                .addToBackStack("toFilter")
                .commit()
        }

        //search name
        binding.textInputEdit.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val name = binding.textInputEdit.text.toString()
                viewModel.filterLocations(name, null, null)
                //   adapter.notifyDataSetChanged()
                adapter.refresh()
            }
            return@setOnEditorActionListener false
        }


        //приходим с фрагмента с фильтрами
        parentFragmentManager.setFragmentResultListener(
            "filterLocation", this
        ) { _, bundle ->
            val filterRequest = bundle.getBundle("filterLocation")
            val name = bundle.getString("name")
            val type = bundle.getString("type")
            val dimension = bundle.getString("dimension")
            if (name != null) {
                viewModel.filterLocations(name, type, dimension)
            }
            adapter.refresh()
        }

        return binding.root
    }

}