package ru.sumbul.rickandmorty.locations.ui.details

//TODO COPY THE CHARACTER IN DETAILS ADAPTER FROM EPISODES!!
//class LocationResidentsAdapter : ListAdapter<Character, ResidentsViewHolder>(DetailsEpisodeDiffCallback()) {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResidentsViewHolder {
//        val binding = CardCharacterBinding.inflate(LayoutInflater.from(parent.context), parent,false)
//        return ResidentsViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
//        val episode = getItem(position)
//        holder.bind(episode)
//    }
//
//
//}
//
//class ResidentsViewHolder(
//    val binding: CardEpisodeBinding
//): RecyclerView.ViewHolder(binding.root) {
//
//    fun bind(character: ru.sumbul.rickandmorty.characters.entity.Character) {
//        binding.apply {
//            binding.name.text = episode.name
//            binding.episode.text = episode.episode
//            binding.airDate.text = episode.air_date
//
//        }
//    }
//}
//
//
//class DetailsEpisodeDiffCallback : DiffUtil.ItemCallback<Episode>() {
//    override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
//        return oldItem == newItem
//    }
//}