package ru.sumbul.rickandmorty.characters.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.databinding.CardCharacterBinding
import ru.sumbul.rickandmorty.util.load

interface OnInteractionListener {
    fun onClick(character: Character) {}
}

class CharacterAdapter(
    private val onInteractionListener: OnInteractionListener,
) :
    PagingDataAdapter<Character, RecyclerView.ViewHolder>(
        CharacterDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            CardCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder( onInteractionListener, binding,)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            (holder as CharacterViewHolder).bind(item)
        }
    }
}

class CharacterViewHolder(
    private val onInteractionListener: OnInteractionListener,
    private val binding: CardCharacterBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(character: Character) {
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

class CharacterDiffCallback : DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(
        oldItem: Character,
        newItem: Character
    ): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Character,
        newItem: Character
    ): Boolean {
        return oldItem == newItem
    }

}