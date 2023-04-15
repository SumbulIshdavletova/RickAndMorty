package ru.sumbul.rickandmorty.characters.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.sumbul.rickandmorty.BuildConfig
import ru.sumbul.rickandmorty.databinding.CardCharacterBinding
import ru.sumbul.rickandmorty.view.load
import ru.sumbul.rickandmorty.view.loadCircleCrop

class CharacterAdapter() :
    PagingDataAdapter<ru.sumbul.rickandmorty.characters.entity.Character, RecyclerView.ViewHolder>(
        CharacterDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            CardCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            (holder as CharacterViewHolder).bind(item)
        }
    }
}

class CharacterViewHolder(
    private val binding: CardCharacterBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(character: ru.sumbul.rickandmorty.characters.entity.Character) {
        binding.name.text = character.name
        binding.gender.text = character.gender
        binding.species.text = character.species
        binding.status.text = character.status
        binding.avatar.load(character.image)

    }
}

class CharacterDiffCallback : DiffUtil.ItemCallback<ru.sumbul.rickandmorty.characters.entity.Character>() {
    override fun areItemsTheSame(
        oldItem: ru.sumbul.rickandmorty.characters.entity.Character,
        newItem: ru.sumbul.rickandmorty.characters.entity.Character
    ): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ru.sumbul.rickandmorty.characters.entity.Character,
        newItem: ru.sumbul.rickandmorty.characters.entity.Character
    ): Boolean {
        return oldItem == newItem
    }

}