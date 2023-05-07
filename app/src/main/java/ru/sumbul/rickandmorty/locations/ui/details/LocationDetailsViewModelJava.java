package ru.sumbul.rickandmorty.locations.ui.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.text.StringsKt;
import ru.sumbul.rickandmorty.characters.domain.model.Character;
import ru.sumbul.rickandmorty.locations.domain.LocationRepository;
import ru.sumbul.rickandmorty.locations.domain.model.Location;

public final class LocationDetailsViewModelJava extends ViewModel {

    private final LocationRepository repository;

    public LocationDetailsViewModelJava(LocationRepository repository) {
        this.repository = repository;
    }

    private MutableLiveData<List<Character>> charactersLiveData = new MutableLiveData<>();
    LiveData<List<Character>> charactersLiveDataTransformed =
            Transformations.map(charactersLiveData, characters -> characters);

    public void getCharacters(@NotNull List<String> urls) {

        ArrayList<Integer> ids = new ArrayList<>();
        for (String url : urls) {
            String result = StringsKt.substringAfterLast(url, "/", "0");
            int idCh = Integer.parseInt(result);
            ids.add(idCh);
        }
        String check = String.valueOf(ids);

        repository.getCharacters(check)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<Character>>() {
                    @Override
                    public void onNext(List<Character> characters) {
                        // Set the value of the LiveData object
                        charactersLiveData.setValue(characters);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Handle the error
                    }

                    @Override
                    public void onComplete() {
                        // Handle the completion
                    }
                });
    }




    private MutableLiveData<Location> getLocation = new MutableLiveData<>();
    public LiveData<Location> locationLiveDataTransformed() {
        return getLocation;
    }

    public void getLocationById(@NotNull String url) {
        String result = StringsKt.substringAfterLast(url, "/", "0");
        int id = Integer.parseInt(result);
        repository.getById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<ru.sumbul.rickandmorty.locations.domain.model.Location>() {
                    @Override
                    public void onSuccess(ru.sumbul.rickandmorty.locations.domain.model.@NonNull Location location) {
                        getLocation.setValue(location);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Handle the error
                    }

                });
    }



}













