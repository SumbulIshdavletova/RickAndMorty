package ru.sumbul.rickandmorty.locations.ui.details.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import ru.sumbul.rickandmorty.characters.domain.model.Character;
import ru.sumbul.rickandmorty.databinding.CardCharacterBinding;
import ru.sumbul.rickandmorty.util.ViewExtensionsKt;

public class ResidentsViewHolder extends RecyclerView.ViewHolder {
    private OnClickListenerFromLocationToCharacter onInteractionListener;
    private final CardCharacterBinding binding;

    public ResidentsViewHolder(OnClickListenerFromLocationToCharacter onInteractionListener,
                               CardCharacterBinding binding) {
        super(binding.getRoot());
        this.onInteractionListener = onInteractionListener;
        this.binding = binding;
    }

    public void bind(Character character) {
        binding.name.setText(character.getName());
        binding.gender.setText(character.getGender());
        binding.species.setText(character.getSpecies());
        binding.status.setText(character.getStatus());
        Glide.with(binding.avatar)
                .load(character.getImage())
                .timeout(10_000)
                //.transform(*transforms)
                .into(binding.avatar);
        //ViewExtensionsKt.load(binding.avatar, character.getImage(), (BitmapTransformation) null, (BitmapTransformation) 2, (Object)null);

        itemView.setOnClickListener(v -> onInteractionListener.onClick(character));

    }
}
