package ru.sumbul.rickandmorty.episodes.ui.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.sumbul.rickandmorty.R
import ru.sumbul.rickandmorty.application.appComponent
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.characters.ui.details.CharacterDetailsFragment
import ru.sumbul.rickandmorty.characters.ui.list.CharacterViewModel
import ru.sumbul.rickandmorty.databinding.FragmentEpisodeDetailsBinding
import ru.sumbul.rickandmorty.episodes.domain.model.Episode
import ru.sumbul.rickandmorty.episodes.ui.list.EpisodeViewModel
import ru.sumbul.rickandmorty.factory.EpisodeDetailsViewModelFactory
import ru.sumbul.rickandmorty.factory.EpisodesViewModelFactory
import javax.inject.Inject


@ExperimentalCoroutinesApi
class EpisodeDetailsFragment : Fragment() {

    @Inject
    lateinit var factory: EpisodeDetailsViewModelFactory
    @OptIn(ExperimentalCoroutinesApi::class)
    val viewModel by viewModels<EpisodeDetailsViewModel>(factoryProducer = { factory })

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    val characterDetailsFragment: CharacterDetailsFragment =
        CharacterDetailsFragment()


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEpisodeDetailsBinding.inflate(inflater, container, false)
   //     val viewModel = ViewModelProvider(requireActivity())[EpisodeDetailsViewModel::class.java]
        val recyclerView = binding.list
//        val characterListViewModel =
//            ViewModelProvider(requireActivity())[CharacterViewModel::class.java]


        val adapter = CharactersInDetailsAdapter(
            (object : OnInteractionListenerCharacter {
                override fun onClick(character: Character) {
                  //  characterListViewModel.getById(character.id)
                    val bundle2 = Bundle()
                    bundle2.putSerializable("requestKey2", character)
                    parentFragmentManager.setFragmentResult("requestKey2", bundle2)
                    parentFragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.frame_layout, characterDetailsFragment)
                        .addToBackStack("details")
                        .commit()
                }
            }
                    ))
        recyclerView.adapter = adapter


        parentFragmentManager.setFragmentResultListener(
            "requestKey", this
        ) { _, bundle ->
            val episode: Episode = bundle.getSerializable("requestKey") as Episode
            binding.id.text = episode.id.toString()
            binding.name.text = episode.name
            binding.airDate.text = episode.air_date
            binding.created.text = episode.created
            binding.episode.text = episode.episode

            val charactersUrls: List<String> = episode.characters

            viewModel.getCharacters(charactersUrls)
            viewModel.getCharacters().observe(viewLifecycleOwner) { characters ->
                adapter.submitList(characters)
            }

        }


        parentFragmentManager.setFragmentResultListener(
            "requestKey3", this
        ) { _, bundle ->
            val episode: Episode = bundle.getSerializable("requestKey3") as Episode
            binding.id.text = episode.id.toString()
            binding.name.text = episode.name
            binding.airDate.text = episode.air_date
            binding.created.text = episode.created
            binding.episode.text = episode.episode

            val charactersUrls: List<String> = episode.characters

            viewModel.getCharacters(charactersUrls)
            viewModel.getCharacters().observe(viewLifecycleOwner) { characters ->
                adapter.submitList(characters)
            }

        }


        return binding.root
    }

}

