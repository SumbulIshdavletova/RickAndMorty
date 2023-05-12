package ru.sumbul.rickandmorty.locations.ui.details.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

import ru.sumbul.rickandmorty.characters.domain.model.Character;
import ru.sumbul.rickandmorty.characters.ui.details.adapter.EpisodesViewHolderJava;
import ru.sumbul.rickandmorty.databinding.CardCharacterBinding;
import ru.sumbul.rickandmorty.episodes.domain.model.Episode;

public class ResidentsAdapter extends ListAdapter<Character, ResidentsViewHolder> {
    private OnClickListenerFromLocationToCharacter onClickListenerFromLocationToCharacter;

    public ResidentsAdapter(OnClickListenerFromLocationToCharacter onClickListenerFromLocationToCharacter) {
        super(new DetailsCharacterDiffCallback());
        this.onClickListenerFromLocationToCharacter = onClickListenerFromLocationToCharacter;
    }

    @NonNull
    @Override
    public ResidentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CardCharacterBinding binding = CardCharacterBinding.inflate(inflater, parent, false);
        return new ResidentsViewHolder(onClickListenerFromLocationToCharacter, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ResidentsViewHolder holder, int position) {
        Character character = getItem(position);
        holder.bind(character);
    }
}
