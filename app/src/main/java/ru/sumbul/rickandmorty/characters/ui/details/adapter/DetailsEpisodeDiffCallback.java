package ru.sumbul.rickandmorty.characters.ui.details.adapter;

import androidx.recyclerview.widget.DiffUtil;

import ru.sumbul.rickandmorty.episodes.domain.model.Episode;

public class DetailsEpisodeDiffCallback extends DiffUtil.ItemCallback<Episode> {

    @Override
    public boolean areItemsTheSame( Episode oldItem,  Episode newItem) {
        return oldItem.getId()==newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(Episode oldItem, Episode newItem) {
        return oldItem.equals(newItem);
    }
}