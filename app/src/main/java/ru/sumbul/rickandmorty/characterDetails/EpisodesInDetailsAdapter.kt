package ru.sumbul.rickandmorty.characterDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.sumbul.rickandmorty.databinding.CardEpisodeBinding
import ru.sumbul.rickandmorty.episodes.entity.Episode
import ru.sumbul.rickandmorty.episodes.ui.EpisodeDiffCallback


interface OnInteractionListenerFromCharacterToEpisode {
    fun onClick(episode: Episode) {}
}


class EpisodesInDetailsAdapter(
    private val onInteractionListener: OnInteractionListenerFromCharacterToEpisode
) : ListAdapter<Episode, EpisodesViewHolder>(DetailsEpisodeDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesViewHolder {
        val binding = CardEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodesViewHolder(onInteractionListener, binding)
    }

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        val episode = getItem(position)
        holder.bind(episode)
    }


}

class EpisodesViewHolder(
    private val onInteractionListener: OnInteractionListenerFromCharacterToEpisode,
    val binding: CardEpisodeBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(episode: Episode) {
        binding.apply {
            binding.name.text = episode.name
            binding.episode.text = episode.episode
            binding.airDate.text = episode.air_date

            itemView.setOnClickListener {
                onInteractionListener.onClick(episode)
            }

        }
    }
}


class DetailsEpisodeDiffCallback : DiffUtil.ItemCallback<Episode>() {
    override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
        return oldItem == newItem
    }
}
