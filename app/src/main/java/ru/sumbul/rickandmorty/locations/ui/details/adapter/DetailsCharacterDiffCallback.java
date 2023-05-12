package ru.sumbul.rickandmorty.locations.ui.details.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import ru.sumbul.rickandmorty.characters.domain.model.Character;
import ru.sumbul.rickandmorty.episodes.domain.model.Episode;

public class DetailsCharacterDiffCallback extends DiffUtil.ItemCallback<Character> {
    @Override
    public boolean areItemsTheSame(@NonNull Character oldItem, @NonNull Character newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Character oldItem, @NonNull Character newItem) {
        return oldItem.equals(newItem);
    }
}
