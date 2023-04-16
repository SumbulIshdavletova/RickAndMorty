package ru.sumbul.rickandmorty

import ru.sumbul.rickandmorty.characters.entity.Character

interface OnFragmentInteractionListener {
    fun onCharacterSelected(character: Character?)
}