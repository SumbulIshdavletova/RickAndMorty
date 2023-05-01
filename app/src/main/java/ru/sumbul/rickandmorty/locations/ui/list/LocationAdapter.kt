package ru.sumbul.rickandmorty.locations.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.sumbul.rickandmorty.databinding.CardLocationBinding
import ru.sumbul.rickandmorty.locations.domain.model.Location

interface OnInteractionListenerLocation {
    fun onClick(location: Location) {}
}

class LocationAdapter(
    private val onInteractionListener: OnInteractionListenerLocation
) :
    PagingDataAdapter<Location, RecyclerView.ViewHolder>(
        LocationDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            CardLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(onInteractionListener, binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            (holder as LocationViewHolder).bind(item)
        }
    }
}

class LocationViewHolder(
    private val onInteractionListener: OnInteractionListenerLocation,
    private val binding: CardLocationBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(location: Location) {
        binding.name.text = location.name
        binding.dimension.text = location.dimension
        binding.type.text = location.type
        itemView.setOnClickListener {
            onInteractionListener.onClick(location)
        }

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
