package ru.sumbul.rickandmorty.characters.presentation.list.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.sumbul.rickandmorty.characters.domain.model.CharacterDomain
import ru.sumbul.rickandmorty.databinding.CardCharacterBinding
import ru.sumbul.rickandmorty.view.load

interface OnInteractionListener {
    fun onClick(character: CharacterDomain) {}
}

class CharacterAdapter(
    private val onInteractionListener: OnInteractionListener,
) :
    PagingDataAdapter<CharacterDomain, RecyclerView.ViewHolder>(
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
    fun bind(character: CharacterDomain) {
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

class CharacterDiffCallback : DiffUtil.ItemCallback<CharacterDomain>() {
    override fun areItemsTheSame(
        oldItem: CharacterDomain,
        newItem: CharacterDomain
    ): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: CharacterDomain,
        newItem: CharacterDomain
    ): Boolean {
        return oldItem == newItem
    }

}