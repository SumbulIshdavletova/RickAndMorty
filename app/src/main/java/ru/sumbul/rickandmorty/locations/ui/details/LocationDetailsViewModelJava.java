package ru.sumbul.rickandmorty.locations.ui.details;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import kotlin.text.StringsKt;
import ru.sumbul.rickandmorty.characters.domain.model.Character;
import ru.sumbul.rickandmorty.locations.domain.LocationRepository;


public final class LocationDetailsViewModelJava extends ViewModel {

    private final LocationRepository repository;

    public LocationDetailsViewModelJava(LocationRepository repository) {
        this.repository = repository;
    }

    //    @Inject
//    public LocationDetailsViewModel(LocationRepository repository) {
//        this.repository = repository;
//    }
    private @NonNull Observable<Observable<List<Character>>> data;
    @NotNull
    private List ids;
    private MutableLiveData loc;
    private MutableLiveData _dataState1;


//    @Nullable
//    public final LiveData getData() {
//        return this.data;
//    }

//    @NotNull
//    public final List getIds() {
//        return this.ids;
//    }
//
//    public final void setIds(@NotNull List<Integer> var1) {
//        //  Intrinsics.checkNotNullParameter(var1, "<set-?>");
//        this.ids = var1;
//    }

    private MutableLiveData<List<Character>> charactersLiveData = new MutableLiveData<>();

    public void getCharacters(@NotNull List<String> urls) {

        ArrayList<Integer> ids = new ArrayList<>();
        for (String url : urls) {
            //  String residentId = String.valueOf(id);
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

    LiveData<List<Character>> charactersLiveDataTransformed =
            Transformations.map(charactersLiveData, characters -> characters);


}













