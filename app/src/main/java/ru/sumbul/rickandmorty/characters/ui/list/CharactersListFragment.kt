package ru.sumbul.rickandmorty.characters.ui.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.LoadState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import ru.sumbul.rickandmorty.R
import ru.sumbul.rickandmorty.application.appComponent
import ru.sumbul.rickandmorty.ui.LoadingStateAdapter
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.characters.ui.details.CharacterDetailsFragment
import ru.sumbul.rickandmorty.databinding.FragmentCharactersListBinding
import ru.sumbul.rickandmorty.factory.CharactersViewModelFactory
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
        lifecycleScope.launch {
            viewModel.characterPagingFlow.collect() { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        binding.list.adapter =
            adapter.withLoadStateHeaderAndFooter(header = LoadingStateAdapter { adapter.retry() },
                footer = LoadingStateAdapter { adapter.retry() })

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.filterCharacters("","","")
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
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frame_layout, filterFragment)
                .addToBackStack("toFilter")
                .commit()
        }

//приходим с фрагмента с фильтрами
        parentFragmentManager.setFragmentResultListener(
            "filter", this
        ) { _, bundle ->
            val filterRequest = bundle.getBundle("filter")
            val name = bundle.getString("name")
            val gender = bundle.getString("gender")
            val status = bundle.getString("status")
            if (name != null) {
                if (status != null) {
                    if (gender != null) {
                        viewModel.filterCharacters(name, status, gender)
                    }
                }
            }
            adapter.refresh()
        }

        return binding.root
    }

}