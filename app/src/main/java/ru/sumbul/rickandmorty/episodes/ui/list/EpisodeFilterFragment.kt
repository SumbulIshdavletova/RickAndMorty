package ru.sumbul.rickandmorty.episodes.ui.list

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.sumbul.rickandmorty.R
import ru.sumbul.rickandmorty.application.appComponent
import ru.sumbul.rickandmorty.databinding.FragmentEpisodeFilterBinding

class EpisodeFilterFragment : Fragment() {

    val episodeListFragment: EpisodesListFragment = EpisodesListFragment()

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentEpisodeFilterBinding.inflate(
            inflater,
            container,
            false
        )

        binding.search.setOnClickListener {
            var name = binding.nameSearch.text.toString()
            name = if (name == "") {
                ""
            } else {
                "$name"
            }

            var episode = ""
            episode = if (binding.episodeSearch.text.toString() == "") {
                ""
            } else {
                binding.episodeSearch.text.toString()
            }


            val bundle = Bundle()
            bundle.apply {
                putString("name", name)
                putString("episode", episode)
            }
            parentFragmentManager.setFragmentResult("filterEpisode", bundle)
            parentFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frame_layout, episodeListFragment)
                .addToBackStack("filterEpisode")
                .commit()
        }



        return binding.root
    }




}