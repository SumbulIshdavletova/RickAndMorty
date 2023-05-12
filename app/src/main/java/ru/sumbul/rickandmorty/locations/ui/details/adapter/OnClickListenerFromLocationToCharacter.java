package ru.sumbul.rickandmorty.locations.ui.details.adapter;

import ru.sumbul.rickandmorty.characters.domain.model.Character;
import ru.sumbul.rickandmorty.episodes.domain.model.Episode;

public interface OnClickListenerFromLocationToCharacter {
    public void onClick(Character character);
}
