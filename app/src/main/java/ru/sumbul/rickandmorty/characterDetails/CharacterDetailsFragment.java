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

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import ru.sumbul.rickandmorty.R;
import ru.sumbul.rickandmorty.characters.CharacterViewModel;
import ru.sumbul.rickandmorty.characters.entity.Character;
import ru.sumbul.rickandmorty.characters.entity.Location;
import ru.sumbul.rickandmorty.characters.entity.Origin;
import ru.sumbul.rickandmorty.databinding.FragmentCharacterDetailsBinding;
import ru.sumbul.rickandmorty.episodeDetails.EpisodeDetailsFragment;
import ru.sumbul.rickandmorty.episodes.entity.Episode;
import ru.sumbul.rickandmorty.episodes.ui.EpisodeAdapter;
import ru.sumbul.rickandmorty.locationDetails.LocationDetailsFragment;


@ExperimentalCoroutinesApi
public class CharacterDetailsFragment extends Fragment {

    Character character = new Character(0, "", "", "", "", "",
            new Origin("", ""), new Location("", ""), "", new ArrayList<>(), "", "");


    List<String> episodes = new ArrayList<>();
    Origin origin = new Origin("", "");
    Location location = new Location("", "");
    ru.sumbul.rickandmorty.locations.entity.Location locationTOfOrigin = new ru.sumbul.rickandmorty.locations.entity.Location(
            0,
            "",
            "",
            "",
            new ArrayList<>(),
            "",
            ""
    );
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
        CharacterDetailViewModel characterDetailViewModel = new ViewModelProvider(requireActivity()).get(CharacterDetailViewModel.class);

        RecyclerView recyclerView = binding.list;
        EpisodesInDetailsAdapter adapter = new EpisodesInDetailsAdapter((OnInteractionListenerFromCharacterToEpisode) (new OnInteractionListenerFromCharacterToEpisode() {
            public void onClick(@NotNull Episode episode) {
                Intrinsics.checkNotNullParameter(episode, "character");
                characterDetailViewModel.getEpisodeById(episode.getId());
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("requestKey3", (Serializable) episode);
                getParentFragmentManager().setFragmentResult("requestKey3", bundle2);
                Fragment EpisodeDetailsFragment = new EpisodeDetailsFragment();
                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.frame_layout, EpisodeDetailsFragment)
                        .addToBackStack("details")
                        .commit();
            }
        }

        ));
        recyclerView.setAdapter(adapter);

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
            origin = character.getOrigin();
            location = character.getLocation();
            binding.originName.setText(character.getOrigin().getName());
            binding.locationName.setText(character.getLocation().getName());
            String url = character.getImage();
            Glide.with(this)
                    .load(url)
                    .into(binding.avatar);
            episodes = character.getEpisode();
            characterDetailViewModel.getEpisodes(episodes);

            String originUrl = origin.getUrl();
        //    locationTOfOrigin = characterDetailViewModel.getLocationById(originUrl);


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
            origin = character.getOrigin();
            location = character.getLocation();
            episodes = character.getEpisode();
            characterDetailViewModel.getEpisodes(episodes);

            characterDetailViewModel.getData1().observe(getViewLifecycleOwner(), adapter::submitList);

        });

        binding.originName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                characterDetailViewModel.getLocationById(origin.getUrl());
                String str = origin.getUrl();
                Bundle bundle2 = new Bundle();
                bundle2.putString("originUrl", str);
                getParentFragmentManager().setFragmentResult("originUrl", bundle2);
                Fragment LocationDetailsFragment = new LocationDetailsFragment();
                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.frame_layout, LocationDetailsFragment)
                        .addToBackStack("details")
                        .commit();
            }
        });

        binding.locationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                characterDetailViewModel.getLocationById(location.getUrl());
                String str = location.getUrl();
                Bundle bundle2 = new Bundle();
                bundle2.putString("originUrl", str);
                getParentFragmentManager().setFragmentResult("originUrl", bundle2);
                Fragment LocationDetailsFragment = new LocationDetailsFragment();
                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.frame_layout, LocationDetailsFragment)
                        .addToBackStack("details")
                        .commit();
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }
}