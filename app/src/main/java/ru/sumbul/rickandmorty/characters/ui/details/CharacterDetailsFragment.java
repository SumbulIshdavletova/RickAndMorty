package ru.sumbul.rickandmorty.characters.ui.details;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import ru.sumbul.rickandmorty.R;
import ru.sumbul.rickandmorty.application.AppKt;
import ru.sumbul.rickandmorty.characters.domain.model.Character;
import ru.sumbul.rickandmorty.characters.domain.model.Location;
import ru.sumbul.rickandmorty.characters.domain.model.Origin;
import ru.sumbul.rickandmorty.databinding.FragmentCharacterDetailsBinding;
import ru.sumbul.rickandmorty.episodes.ui.details.EpisodeDetailsFragment;
import ru.sumbul.rickandmorty.episodes.domain.model.Episode;
import ru.sumbul.rickandmorty.factory.CharacterDetailsViewModelJavaFactory;
import ru.sumbul.rickandmorty.locations.ui.details.LocationDetailsFragment;


@ExperimentalCoroutinesApi
public class CharacterDetailsFragment extends Fragment {

    Character character = new Character(0, "", "", "", "", "",
            new Origin("", ""), new Location("", ""), "", new ArrayList<>(), "", "");

    List<String> episodes = new ArrayList<>();
    Origin origin = new Origin("", "");
    Location location = new Location("", "");

    public FragmentCharacterDetailsBinding binding;

    public CharacterDetailsFragment() {
        super(R.layout.fragment_character_details);
    }

    CharacterDetailsViewModelJava viewModelJava;
    @Inject
    CharacterDetailsViewModelJavaFactory factoryJava;

    public void onAttach(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        AppKt.getAppComponent(context).inject(this);
        super.onAttach(context);
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


        viewModelJava = new ViewModelProvider(this, factoryJava).get(CharacterDetailsViewModelJava.class);


        RecyclerView recyclerView = binding.list;
        EpisodesInDetailsAdapter adapter = new EpisodesInDetailsAdapter((OnInteractionListenerFromCharacterToEpisode) (new OnInteractionListenerFromCharacterToEpisode() {
            public void onClick(@NotNull Episode episode) {
                Intrinsics.checkNotNullParameter(episode, "character");
              //  viewModelJava.getEpisodeById(episode.getId()); неправильно передается парамент с айди
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
            viewModelJava.getEpisodes(episodes);

            String originUrl = origin.getUrl();
            //    locationTOfOrigin = characterDetailViewModel.getLocationById(originUrl);

            viewModelJava.episodesLiveDataTransformed.observe(getViewLifecycleOwner(), adapter::submitList);

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
            viewModelJava.getEpisodes(episodes);

            viewModelJava.episodesLiveDataTransformed.observe(getViewLifecycleOwner(), adapter::submitList);

        });

        binding.originName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO MIGHT BE NOT EVEN NECESSARY
                viewModelJava.getLocationById(origin.getUrl());
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
                viewModelJava.getLocationById(location.getUrl());
                //TODO MIGHT BE NOT EVEN NECESSARY
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