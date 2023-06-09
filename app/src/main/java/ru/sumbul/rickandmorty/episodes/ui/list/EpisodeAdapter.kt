package ru.sumbul.rickandmorty.episodes.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.sumbul.rickandmorty.databinding.CardEpisodeBinding
import ru.sumbul.rickandmorty.episodes.domain.model.Episode

interface OnInteractionListenerCharacter {
    fun onClick(episode: Episode) {}
}

class EpisodeAdapter(
    private val onInteractionListener: OnInteractionListenerCharacter,
) :
    PagingDataAdapter<Episode, RecyclerView.ViewHolder>(
        EpisodeDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            CardEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodeViewHolder(onInteractionListener, binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            (holder as EpisodeViewHolder).bind(item)
        }
    }
}

class EpisodeViewHolder(
    private val onInteractionListener: OnInteractionListenerCharacter,
    private val binding: CardEpisodeBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(episode: Episode) {
        binding.name.text = episode.name
        binding.episode.text = episode.episode
        binding.airDate.text = episode.air_date
        itemView.setOnClickListener {
            onInteractionListener.onClick(episode)
        }

    }
}

class EpisodeDiffCallback : DiffUtil.ItemCallback<Episode>() {
    override fun areItemsTheSame(
        oldItem: Episode,
        newItem: Episode
    ): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Episode,
        newItem: Episode
    ): Boolean {
        return oldItem == newItem
    }

}