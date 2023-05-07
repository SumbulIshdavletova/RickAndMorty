package ru.sumbul.rickandmorty.locations.ui.details;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.ExperimentalCoroutinesApi;
import ru.sumbul.rickandmorty.R;
import ru.sumbul.rickandmorty.application.AppKt;
import ru.sumbul.rickandmorty.characters.domain.model.Character;
import ru.sumbul.rickandmorty.characters.ui.details.CharacterDetailsFragment;
import ru.sumbul.rickandmorty.characters.ui.list.CharacterViewModel;
import ru.sumbul.rickandmorty.databinding.FragmentLocationDetailsBinding;
import ru.sumbul.rickandmorty.episodes.ui.details.CharactersInDetailsAdapter;
import ru.sumbul.rickandmorty.episodes.ui.details.OnInteractionListenerCharacter;
import ru.sumbul.rickandmorty.factory.LocationDetailsViewModelFactory;
import ru.sumbul.rickandmorty.factory.LocationDetailsViewModelJavaFactory;
import ru.sumbul.rickandmorty.locations.domain.model.Location;


@ExperimentalCoroutinesApi
public class LocationDetailsFragment extends Fragment {

    public FragmentLocationDetailsBinding binding;
    List<String> residents = new ArrayList<>();

    CharacterDetailsFragment characterDetailsFragment = new CharacterDetailsFragment();
    LocationDetailsViewModelJava viewModelJava;
    LocationDetailsViewModel viewModel;

    @Inject
    LocationDetailsViewModelFactory factory;

    @Inject
    LocationDetailsViewModelJavaFactory factoryJava;


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
        binding = FragmentLocationDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        RecyclerView recyclerView = binding.list;

        viewModel = new ViewModelProvider(this, factory).get(LocationDetailsViewModel.class);
        viewModelJava = new ViewModelProvider(this, factoryJava).get(LocationDetailsViewModelJava.class);

        CharactersInDetailsAdapter adapter = new CharactersInDetailsAdapter((OnInteractionListenerCharacter) (new OnInteractionListenerCharacter() {
            public void onClick(@NotNull Character character) {
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("requestKey2", (Serializable) character);
                getParentFragmentManager().setFragmentResult("requestKey2", bundle2);
                getParentFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.frame_layout, characterDetailsFragment)
                        .addToBackStack("details")
                        .commit();
            }
        }

        ));
        recyclerView.setAdapter(adapter);


        getParentFragmentManager().setFragmentResultListener("requestKey1", this, (requestKey, bundle) -> {

            Location location = (Location) bundle.getSerializable("requestKey1");
            binding.id.setText(String.valueOf(location.getId()));
            binding.name.setText(location.getName());
            binding.type.setText(location.getType());
            binding.dimension.setText(location.getDimension());
            binding.created.setText(location.getCreated());
            residents = location.getResidents();
          //  viewModel.getCharacters(residents);

       viewModelJava.getCharacters(residents);
       //     Objects.requireNonNull(viewModel.getData()).observe(getViewLifecycleOwner(), adapter::submitList);
            viewModelJava.charactersLiveDataTransformed.observe(getViewLifecycleOwner(), adapter::submitList);

        });

        getParentFragmentManager().setFragmentResultListener("originUrl", this, (requestKey, bundle) -> {

            String result = bundle.getString("originUrl");
            viewModel.getLocationById(result);
            viewModel.getLoc().observe(getViewLifecycleOwner(), origin -> {
                binding.id.setText(String.valueOf(origin.getId()));
                binding.name.setText(origin.getName());
                binding.type.setText(origin.getType());
                binding.dimension.setText(origin.getDimension());
                binding.created.setText(origin.getCreated());
                residents = origin.getResidents();
                viewModel.getCharacters(residents);
                Objects.requireNonNull(viewModel.getData()).observe(getViewLifecycleOwner(), adapter::submitList);
            });
        });


        return view;
    }
}