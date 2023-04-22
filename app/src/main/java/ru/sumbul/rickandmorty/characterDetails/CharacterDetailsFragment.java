package ru.sumbul.rickandmorty.characterDetails;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import ru.sumbul.rickandmorty.R;
import ru.sumbul.rickandmorty.characters.CharacterViewModel;
import ru.sumbul.rickandmorty.characters.entity.Character;
import ru.sumbul.rickandmorty.characters.entity.Location;
import ru.sumbul.rickandmorty.characters.entity.Origin;
import ru.sumbul.rickandmorty.databinding.FragmentCharacterDetailsBinding;
import ru.sumbul.rickandmorty.episodes.entity.Episode;
import ru.sumbul.rickandmorty.episodes.ui.EpisodeAdapter;


@ExperimentalCoroutinesApi
public class CharacterDetailsFragment extends Fragment {

    Character character = new Character(0, "", "", "", "", "",
            new Origin("", ""), new Location("", ""), "", new ArrayList<>(), "", "");


    List<String> episodes = new ArrayList<>();
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
        RecyclerView recyclerView = binding.list;
        EpisodesInDetailsAdapter adapter = new EpisodesInDetailsAdapter();
        recyclerView.setAdapter(adapter);
        CharacterDetailViewModel characterDetailViewModel = new ViewModelProvider(requireActivity()).get(CharacterDetailViewModel.class);


        getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            character = (Character) bundle.getSerializable("requestKey");
            int id = character.getId();
            String name = character.getName();
            binding.id.setText(String.valueOf(id));

            binding.name.setText(name);
            binding.species.setText(character.getSpecies());
            binding.status.setText(character.getStatus());
            binding.gender.setText(character.getGender());
            String type = character.getType();
            if (type.equals("")) {
                binding.type.setText("unknown");
            } else {
                binding.type.setText(character.getType());
            }
            binding.created.setText(character.getCreated());
            binding.originName.setText(character.getOrigin().getName());
            binding.locationName.setText(character.getLocation().getName());
            String url = character.getImage();
            Glide.with(this)
                    .load(url)
                    .into(binding.avatar);
            episodes = character.getEpisode();
            characterDetailViewModel.getEpisodes(episodes);


            characterDetailViewModel.getData1().observe(getViewLifecycleOwner(), adapter::submitList);

        });

        getParentFragmentManager().setFragmentResultListener("requestKey2", this, (requestKey, bundle) -> {
            character = (Character) bundle.getSerializable("requestKey2");
            int id = character.getId();
            String name = character.getName();
            binding.id.setText(String.valueOf(id));

            binding.name.setText(name);
            binding.species.setText(character.getSpecies());
            binding.status.setText(character.getStatus());
            binding.gender.setText(character.getGender());
            String type = character.getType();
            if (type.equals("")) {
                binding.type.setText("unknown");
            } else {
                binding.type.setText(character.getType());
            }
            String url = character.getImage();
            Glide.with(this)
                    .load(url)
                    .into(binding.avatar);

            binding.created.setText(character.getCreated());
            binding.originName.setText(character.getOrigin().getName());
            binding.locationName.setText(character.getLocation().getName());
            episodes = character.getEpisode();
            characterDetailViewModel.getEpisodes(episodes);

            characterDetailViewModel.getData1().observe(getViewLifecycleOwner(), adapter::submitList);

        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        CharacterDetailViewModel characterDetailViewModel = new ViewModelProvider(requireActivity()).get(CharacterDetailViewModel.class);
        characterDetailViewModel.getData1().removeObservers(getViewLifecycleOwner());
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                characterDetailViewModel.getData1().removeObservers(getViewLifecycleOwner());

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }
}