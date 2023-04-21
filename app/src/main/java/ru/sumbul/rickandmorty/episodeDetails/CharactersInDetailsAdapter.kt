package ru.sumbul.rickandmorty.episodeDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.sumbul.rickandmorty.characters.entity.Character
import ru.sumbul.rickandmorty.databinding.CardCharacterBinding
import ru.sumbul.rickandmorty.view.load

class CharactersInDetailsAdapter () : ListAdapter<ru.sumbul.rickandmorty.characters.entity.Character,
        CharactersViewHolder>(DetailsEpisodeDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val binding = CardCharacterBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return CharactersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val episode = getItem(position)
        holder.bind(episode)
    }


}

class CharactersViewHolder(
    val binding: CardCharacterBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(character: ru.sumbul.rickandmorty.characters.entity.Character) {
        binding.apply {
            binding.name.text = character.name
            binding.gender.text = character.gender
            binding.species.text = character.species
            binding.status.text = character.status
            binding.avatar.load(character.image)

        }
    }
}


class DetailsEpisodeDiffCallback : DiffUtil.ItemCallback<ru.sumbul.rickandmorty.characters.entity.Character>() {
    override fun areItemsTheSame(oldItem: ru.sumbul.rickandmorty.characters.entity.Character, newItem: ru.sumbul.rickandmorty.characters.entity.Character): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ru.sumbul.rickandmorty.characters.entity.Character, newItem: ru.sumbul.rickandmorty.characters.entity.Character): Boolean {
        return oldItem == newItem
    }
}
