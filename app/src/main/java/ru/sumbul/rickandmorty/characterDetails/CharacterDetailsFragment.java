package ru.sumbul.rickandmorty.characterDetails;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import ru.sumbul.rickandmorty.R;
import ru.sumbul.rickandmorty.characters.CharacterViewModel;
import ru.sumbul.rickandmorty.characters.entity.Character;
import ru.sumbul.rickandmorty.characters.entity.Location;
import ru.sumbul.rickandmorty.characters.entity.Origin;
import ru.sumbul.rickandmorty.databinding.FragmentCharacterDetailsBinding;


public class CharacterDetailsFragment extends Fragment {
    Character character = new Character(0, "", "", "", "", "",
            new Origin("", ""), new Location("", ""), "", new ArrayList<>(), "", "");

    private CharacterViewModel characterViewModel;

    public FragmentCharacterDetailsBinding binding;

    public CharacterDetailsFragment() {
        super(R.layout.fragment_character_details);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OptIn(markerClass = ExperimentalCoroutinesApi.class)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCharacterDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                character = (Character) bundle.getSerializable("requestKey");
                int id = character.getId();
                String name = character.getName();
                binding.id.setText(String.valueOf(id));

                binding.name.setText(name);
                binding.gender.setText(character.getGender());
            }
        });


        return view;
    }
}