package ru.sumbul.rickandmorty.locations.ui.list

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
            viewModel.filterLocations("", "", "")
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
            viewModel.filterLocations("", "", "")
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

                var isOnline = context?.let { checkForInternet(it) }
                if (isOnline == false) {
                    lifecycleScope.launch {
                        viewModel.filterLocationsOffline(name, null, null)
                            .collect() { pagingData ->
                                adapter.submitData(pagingData)
                            }
                    }
                    if (adapter.itemCount == 0) {
                        context?.let {
                            MaterialAlertDialogBuilder(
                                it,
                                R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
                            )
                                .setMessage(resources.getString(R.string.filter))
                                .show()
                        }
                    }
                } else {
                    viewModel.filterLocations(name, null, null)
                    if (adapter.itemCount == 0) {
                        context?.let {
                            MaterialAlertDialogBuilder(
                                it,
                                R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
                            )
                                .setMessage(resources.getString(R.string.filter))
                                .show()
                        }
                    }
                }

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

            var isOnline = context?.let { checkForInternet(it) }
            if (isOnline == true) {
                if (name != null) {
                    viewModel.filterLocations(name, type, dimension)
                }
                if (adapter.itemCount == 0) {
                    context?.let {
                        MaterialAlertDialogBuilder(
                            it,
                            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
                        )
                            .setMessage(resources.getString(R.string.filter))
                            .show()
                    }
                }
            } else {
                lifecycleScope.launch {
                    viewModel.filterLocationsOffline(name, type, dimension)
                        .collect() { pagingData ->
                            adapter.submitData(pagingData)
                        }
                }
                if (adapter.itemCount == 0) {
                    context?.let {
                        MaterialAlertDialogBuilder(
                            it,
                            R.style.ThemeOverlay_MaterialComponents_Dialog_Alert
                        )
                            .setMessage(resources.getString(R.string.filter))
                            .show()
                    }
                }
            }
            adapter.refresh()
        }

        return binding.root
    }

    private fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

}