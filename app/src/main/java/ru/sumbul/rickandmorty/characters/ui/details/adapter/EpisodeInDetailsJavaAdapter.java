package ru.sumbul.rickandmorty.characters.ui.details.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import ru.sumbul.rickandmorty.databinding.CardEpisodeBinding;
import ru.sumbul.rickandmorty.episodes.domain.model.Episode;

public class EpisodeInDetailsJavaAdapter extends ListAdapter<Episode, EpisodesViewHolderJava> {
    private OnInteractionListenerFromCharacterToEpisodeJava onInteractionListener;

    public EpisodeInDetailsJavaAdapter(OnInteractionListenerFromCharacterToEpisodeJava onInteractionListener) {
        super(new DetailsEpisodeDiffCallback());
        this.onInteractionListener = onInteractionListener;
    }

    @Override
    public EpisodesViewHolderJava onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CardEpisodeBinding binding = CardEpisodeBinding.inflate(inflater, parent, false);
        return new EpisodesViewHolderJava(onInteractionListener, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodesViewHolderJava holder, int position) {
        Episode episode = getItem(position);
        holder.bind(episode);
    }

};



