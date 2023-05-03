package ru.sumbul.rickandmorty.episodes.ui.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.databinding.CardCharacterBinding
import ru.sumbul.rickandmorty.util.load


interface OnInteractionListenerCharacter {
    fun onClick(character: Character) {}
}

class CharactersInDetailsAdapter (
    private val onInteractionListener: OnInteractionListenerCharacter,
)  : ListAdapter<Character,
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

    fun bind(character: Character) {
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


class DetailsEpisodeDiffCallback : DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem == newItem
    }
}
