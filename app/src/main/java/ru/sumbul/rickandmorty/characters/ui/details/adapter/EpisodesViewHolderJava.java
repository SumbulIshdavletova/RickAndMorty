package ru.sumbul.rickandmorty.characters.ui.details.adapter;

import androidx.recyclerview.widget.RecyclerView;

import ru.sumbul.rickandmorty.databinding.CardEpisodeBinding;
import ru.sumbul.rickandmorty.episodes.domain.model.Episode;

public class EpisodesViewHolderJava extends RecyclerView.ViewHolder {
    private final OnInteractionListenerFromCharacterToEpisodeJava onInteractionListener;
    private final CardEpisodeBinding binding;

    public EpisodesViewHolderJava(OnInteractionListenerFromCharacterToEpisodeJava onInteractionListener, CardEpisodeBinding binding) {
        super(binding.getRoot());
        this.onInteractionListener = onInteractionListener;
        this.binding = binding;
    }

    public void bind(Episode episode) {
        binding.name.setText(episode.getName());
        binding.episode.setText(episode.getEpisode());
        binding.airDate.setText(episode.getAir_date());

        itemView.setOnClickListener(v -> onInteractionListener.onClick(episode));
    }

}