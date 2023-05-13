package ru.sumbul.rickandmorty.episodes.ui.list

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
import ru.sumbul.rickandmorty.databinding.FragmentEpisodesListBinding
import ru.sumbul.rickandmorty.episodes.ui.details.EpisodeDetailsFragment
import ru.sumbul.rickandmorty.episodes.domain.model.Episode
import ru.sumbul.rickandmorty.factory.EpisodesViewModelFactory
import ru.sumbul.rickandmorty.locations.ui.list.LocationFilterFragment
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
                val episodeId = episode.id;
                val bundle2 = Bundle()
                bundle2.putInt("requestKey", episodeId)
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

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.filterEpisodes("", "")
           // viewModel.filterEpisodesOffline("","")
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
        val filterFragment = EpisodeFilterFragment()
        binding.filter.setOnClickListener {
            viewModel.filterEpisodes("", "")
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
                        viewModel.filterEpisodesOffline(name, null)
                            .collect() { pagingData ->
                                adapter.submitData(pagingData)
                            }
                    }
                } else {
                    viewModel.filterEpisodes(name, null)
                }
                adapter.refresh()
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
            return@setOnEditorActionListener false
        }


        //приходим с фрагмента с фильтрами
        parentFragmentManager.setFragmentResultListener(
            "filterEpisode", this
        ) { _, bundle ->
            val filterRequest = bundle.getBundle("filterEpisode")
            val name = bundle.getString("name")
            val episode = bundle.getString("episode")

            var isOnline = context?.let { checkForInternet(it) }
            if (isOnline == true) {
                if (name != null) {
                    viewModel.filterEpisodes(name, episode)
                }
            } else {
                lifecycleScope.launch {
                    viewModel.filterEpisodesOffline(name, episode)
                        .collect() { pagingData ->
                            adapter.submitData(pagingData)
                        }
                }
            }
            adapter.refresh()
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