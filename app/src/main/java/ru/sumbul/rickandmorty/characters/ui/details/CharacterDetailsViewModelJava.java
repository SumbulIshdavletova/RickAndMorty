package ru.sumbul.rickandmorty.characters.ui.details;

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
import ru.sumbul.rickandmorty.characters.data.local.dao.CharacterDao;
import ru.sumbul.rickandmorty.characters.domain.CharacterRepository;
import ru.sumbul.rickandmorty.episodes.domain.model.Episode;
import ru.sumbul.rickandmorty.locations.domain.model.Location;

public class CharacterDetailsViewModelJava extends ViewModel {

    private final CharacterRepository repository;

    public CharacterDetailsViewModelJava(CharacterRepository repository) {
        this.repository = repository;
    }

    private MutableLiveData<List<Episode>> episodesLiveData = new MutableLiveData<List<Episode>>();
    LiveData<List<Episode>> episodesLiveDataTransformed =
            Transformations.map(episodesLiveData, episodes -> episodes);

    public void getEpisodes(List<String> urls) {

        ArrayList<Integer> ids = new ArrayList<>();
        for (String url : urls) {
            String result = StringsKt.substringAfterLast(url, "/", "0");
            int idCh = Integer.parseInt(result);
            ids.add(idCh);
        }
        String idsString = String.valueOf(ids);

        repository.getEpisodes(ids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<Episode>>() {

                    @Override
                    public void onNext(@NonNull List<Episode> episodes) {
                        episodesLiveData.setValue(episodes);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                     //   dao.getEpisodesByIds(ids);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private MutableLiveData<Location> getLocation = new MutableLiveData<>();

    public LiveData<Location> locationLiveDataTransformed() {
        return getLocation;
    }

    public void getLocationById(@NotNull String url) {
        String result  = StringsKt.substringAfterLast(url,"/", "0");
        int id = Integer.parseInt(result);
        repository.getLocationById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Location>() {
                    @Override
                    public void onSuccess(ru.sumbul.rickandmorty.locations.domain.model.@NonNull Location location) {
                        getLocation.setValue(location);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                });
    }

    private MutableLiveData<Episode> getEpisode = new MutableLiveData<>();

    public LiveData<Episode> episodeLiveDataTransformed() {
        return getEpisode;
    }

    public void getEpisodeById(@NotNull String url) {
        String result = StringsKt.substringAfterLast(url, "/", "0");
        int id = Integer.parseInt(result);

        repository.getEpisodeById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Episode>() {
                    @Override
                    public void onSuccess(@NonNull Episode episode) {
                        getEpisode.setValue(episode);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                });
    }

}
