package ru.sumbul.rickandmorty.episodeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.sumbul.rickandmorty.characterDetails.CharacterDetailViewModel
import ru.sumbul.rickandmorty.characterDetails.CharacterDetailsFragment
import ru.sumbul.rickandmorty.characterDetails.EpisodesInDetailsAdapter
import ru.sumbul.rickandmorty.characters.character
import ru.sumbul.rickandmorty.databinding.FragmentEpisodeDetailsBinding
import ru.sumbul.rickandmorty.episodes.entity.Episode


@ExperimentalCoroutinesApi
class EpisodeDetailsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @kotlin.OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEpisodeDetailsBinding.inflate(inflater, container, false)
        //    val viewModel: EpisodeDetailsViewModel by viewModels()
        val viewModel = ViewModelProvider(requireActivity())[EpisodeDetailsViewModel::class.java]
        val recyclerView = binding.list
        val adapter = CharactersInDetailsAdapter()
        recyclerView.adapter = adapter


        parentFragmentManager.setFragmentResultListener(
            "requestKey", this
        ) { requestKey, bundle ->
            val episode: Episode = bundle.getSerializable("requestKey") as Episode
            binding.id.text = episode.id.toString()
            binding.name.text = episode.name
            binding.airDate.text = episode.air_date
            binding.created.text = episode.created
            binding.episode.text = episode.episode

            val charactersUrls: List<String> = episode.characters

            viewModel.getCharacters(charactersUrls)
            viewModel.getData()?.observe(viewLifecycleOwner) { characters ->
                adapter.submitList(characters)
            }

        }

        return binding.root
    }

//    companion object {
//
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            EpisodeDetailsFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}

