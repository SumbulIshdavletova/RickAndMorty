package ru.sumbul.rickandmorty.characters.ui.list

import android.content.Context
import android.content.SharedPreferences.Editor
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.LoadState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import ru.sumbul.rickandmorty.R
import ru.sumbul.rickandmorty.application.appComponent
import ru.sumbul.rickandmorty.ui.LoadingStateAdapter
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.characters.ui.details.CharacterDetailsFragment
import ru.sumbul.rickandmorty.characters.ui.list.CharacterFilterFragment.Companion.genderA
import ru.sumbul.rickandmorty.characters.ui.list.CharacterFilterFragment.Companion.nameA
import ru.sumbul.rickandmorty.characters.ui.list.CharacterFilterFragment.Companion.speciesA
import ru.sumbul.rickandmorty.characters.ui.list.CharacterFilterFragment.Companion.statusA
import ru.sumbul.rickandmorty.characters.ui.list.CharacterFilterFragment.Companion.typeA
import ru.sumbul.rickandmorty.databinding.FragmentCharactersListBinding
import ru.sumbul.rickandmorty.factory.CharactersViewModelFactory
import ru.sumbul.rickandmorty.util.StringArg
import javax.inject.Inject


class CharactersListFragment : Fragment() {


    @Inject
    lateinit var factory: CharactersViewModelFactory

    @OptIn(ExperimentalCoroutinesApi::class)
    val viewModel by viewModels<CharacterViewModel>(factoryProducer = { factory })

    @OptIn(ExperimentalCoroutinesApi::class)
    val characterDetailsFragment: CharacterDetailsFragment =
        CharacterDetailsFragment()


    @OptIn(ExperimentalCoroutinesApi::class)
    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        CharacterAdapter(object : OnInteractionListener {
            override fun onClick(character: Character) {
                val bundle2 = Bundle()
                bundle2.putSerializable("requestKey", character)
                parentFragmentManager.setFragmentResult("requestKey", bundle2)
                parentFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.frame_layout, characterDetailsFragment)
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCharactersListBinding.inflate(
            inflater,
            container,
            false
        )
        binding.list.adapter = adapter
        var isOnline = context?.let { checkForInternet(it) }

        lifecycleScope.launch {
            viewModel.characterPagingFlow.collect() { pagingData ->
                adapter.submitData(pagingData)
            }
        }


        binding.list.adapter =
            adapter.withLoadStateHeaderAndFooter(header = LoadingStateAdapter { adapter.retry() },
                footer = LoadingStateAdapter { adapter.retry() })

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.filterCharacters("", "", "", "", "")
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
        val filterFragment: CharacterFilterFragment = CharacterFilterFragment()
        binding.filter.setOnClickListener {
            viewModel.filterCharacters("", "", "", "", "")
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
                        viewModel.filterCharactersOffline(name, null, null, null, null)
                            .collect() { pagingData ->
                                adapter.submitData(pagingData)
                            }
                    }
                } else {
                    viewModel.filterCharacters(name, null, null, null, null)
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
            "filter", this
        ) { _, bundle ->
            val filterRequest = bundle.getBundle("filter")
            val name = bundle.getString("name")
            val status = bundle.getString("status")
            val species = bundle.getString("species")
            val type = bundle.getString("type")
            val gender = bundle.getString("gender")

            var isOnline = context?.let { checkForInternet(it) }
            if (isOnline == true) {
                if (name != null) {
                    viewModel.filterCharacters(name, status, species, type, gender)
                }
            } else {
                lifecycleScope.launch {
                    viewModel.filterCharactersOffline(name, status, species, type, gender)
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