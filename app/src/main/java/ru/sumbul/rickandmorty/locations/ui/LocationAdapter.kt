package ru.sumbul.rickandmorty.locations.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.sumbul.rickandmorty.databinding.CardLocationBinding
import ru.sumbul.rickandmorty.locations.entity.Location

class LocationAdapter :
    PagingDataAdapter<Location, RecyclerView.ViewHolder>(
        LocationDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            CardLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            (holder as LocationViewHolder).bind(item)
        }
    }
}

class LocationViewHolder(
    private val binding: CardLocationBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(location: Location) {
        binding.name.text = location.name
        binding.dimension.text = location.dimension
        binding.type.text = location.type

    }
}

class LocationDiffCallback : DiffUtil.ItemCallback<Location>() {
    override fun areItemsTheSame(
        oldItem: Location,
        newItem: Location
    ): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Location,
        newItem: Location
    ): Boolean {
        return oldItem == newItem
    }

}
