package ru.sumbul.rickandmorty.episodeDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.sumbul.rickandmorty.databinding.CardCharacterBinding
import ru.sumbul.rickandmorty.view.load


interface OnInteractionListenerCharacter {
    fun onClick(character: ru.sumbul.rickandmorty.characters.domain.model.Character) {}
}

class CharactersInDetailsAdapter (
    private val onInteractionListener: OnInteractionListenerCharacter,
)  : ListAdapter<ru.sumbul.rickandmorty.characters.domain.model.Character,
        CharactersViewHolder>(DetailsEpisodeDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val binding = CardCharacterBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return CharactersViewHolder(onInteractionListener, binding)
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val episode = getItem(position)
        holder.bind(episode)
    }


}

class CharactersViewHolder(
    private val onInteractionListener: OnInteractionListenerCharacter,
    val binding: CardCharacterBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(character: ru.sumbul.rickandmorty.characters.domain.model.Character) {
        binding.apply {
            binding.name.text = character.name
            binding.gender.text = character.gender
            binding.species.text = character.species
            binding.status.text = character.status
            binding.avatar.load(character.image)

            itemView.setOnClickListener {
                onInteractionListener.onClick(character)
            }
        }
    }
}


class DetailsEpisodeDiffCallback : DiffUtil.ItemCallback<ru.sumbul.rickandmorty.characters.domain.model.Character>() {
    override fun areItemsTheSame(oldItem: ru.sumbul.rickandmorty.characters.domain.model.Character, newItem: ru.sumbul.rickandmorty.characters.domain.model.Character): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ru.sumbul.rickandmorty.characters.domain.model.Character, newItem: ru.sumbul.rickandmorty.characters.domain.model.Character): Boolean {
        return oldItem == newItem
    }
}
