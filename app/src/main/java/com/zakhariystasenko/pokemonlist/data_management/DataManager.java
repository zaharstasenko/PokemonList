package com.zakhariystasenko.pokemonlist.data_management;

import com.zakhariystasenko.pokemonlist.data_model.BasePokemonInfo;
import com.zakhariystasenko.pokemonlist.data_model.DetailedPokemonInfo;
import com.zakhariystasenko.pokemonlist.data_model.PokemonDownloadModel;
import com.zakhariystasenko.pokemonlist.data_model.PokemonListDownloadModel.Result;
import com.zakhariystasenko.pokemonlist.data_model.PokemonListDownloadModel;
import com.zakhariystasenko.pokemonlist.utils.DatabaseMissingDataException;
import com.zakhariystasenko.pokemonlist.utils.PokemonApi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class DataManager {
    private DataBaseHelper mDataBaseHelper;
    private Retrofit mRetrofit;

    public DataManager(DataBaseHelper dataBaseHelper, Retrofit retrofit) {
        mDataBaseHelper = dataBaseHelper;
        mRetrofit = retrofit;
    }

    public void requestPokemonsList(SingleObserver<List<BasePokemonInfo>> observer) {
        mDataBaseHelper.requestPokemonList()
                .onErrorResumeNext(new Function<Throwable,  SingleSource<? extends List<BasePokemonInfo>>>() {
                    @Override
                    public SingleSource<? extends List<BasePokemonInfo>> apply(Throwable throwable) throws Exception {
                        if (throwable instanceof DatabaseMissingDataException) {
                            return downloadPokemonsList();
                        } else {
                            return Single.error(throwable);
                        }
                    }
                })
                .subscribe(observer);
    }

    public void requestPokemonDetails(SingleObserver<DetailedPokemonInfo> observer, final String pokemonId) {
        mDataBaseHelper.requestPokemonDetails(pokemonId)
                .onErrorResumeNext(new Function<Throwable,  SingleSource<? extends DetailedPokemonInfo>>() {
                    @Override
                    public SingleSource<? extends DetailedPokemonInfo> apply(Throwable throwable) throws Exception {
                        if (throwable instanceof DatabaseMissingDataException) {
                            return downloadPokemonDetails(pokemonId);
                        } else {
                            return Single.error(throwable);
                        }
                    }
                }).subscribe(observer);
    }

    private Single<List<BasePokemonInfo>> downloadPokemonsList() {
        return mRetrofit.create(PokemonListDownloader.class)
                .getPokemon(PokemonApi.ALL_POKEMONS_REQUEST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<PokemonListDownloadModel, List<BasePokemonInfo>>() {
                    @Override
                    public List<BasePokemonInfo> apply(PokemonListDownloadModel pokemonListDownloadModel) throws Exception {
                        ArrayList<BasePokemonInfo> basePokemonInfos = new ArrayList<>();
                        Integer id = 0;

                        for (Result result : pokemonListDownloadModel.getResult()) {
                            basePokemonInfos.add(
                                    new BasePokemonInfo(
                                            firstCharUpperCase(result.getName()),
                                            (++id).toString()));
                        }

                        mDataBaseHelper.writeToDatabase(basePokemonInfos);

                        return basePokemonInfos;
                    }
                });
    }

    private Single<DetailedPokemonInfo> downloadPokemonDetails(final String pokemonId) {
        return mRetrofit.create(PokemonDetailsDownloader.class).
                getPokemon(PokemonApi.pokemonDetailsRequest(pokemonId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<PokemonDownloadModel, DetailedPokemonInfo>() {
                    @Override
                    public DetailedPokemonInfo apply(PokemonDownloadModel pokemonDownloadModel) throws Exception {
                        String abilities = "";
                        for (int i = 0; i < pokemonDownloadModel.getAbilities().length; ++i) {
                            abilities += "\n" + firstCharUpperCase(pokemonDownloadModel.getAbilities()[i].getAbility().getName());
                        }

                        DetailedPokemonInfo detailedPokemonInfo = new DetailedPokemonInfo(pokemonDownloadModel.getWeight(),
                                pokemonDownloadModel.getHeight(),
                                pokemonDownloadModel.getBaseExperience(),
                                abilities);

                        mDataBaseHelper.writeToDatabase(pokemonId, detailedPokemonInfo);
                        return detailedPokemonInfo;
                    }
                });
    }

    private String firstCharUpperCase(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
