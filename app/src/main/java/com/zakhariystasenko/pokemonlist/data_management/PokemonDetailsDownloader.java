package com.zakhariystasenko.pokemonlist.data_management;

import com.zakhariystasenko.pokemonlist.data_model.PokemonDownloadModel;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface PokemonDetailsDownloader {
    @GET
    Single<PokemonDownloadModel> getPokemon(@Url String url);
}
