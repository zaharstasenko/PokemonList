package com.zakhariystasenko.pokemonlist.data_management;

import com.zakhariystasenko.pokemonlist.data_model.PokemonListDownloadModel;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface PokemonListDownloader {
    @GET
    Single<PokemonListDownloadModel> getPokemon(@Url String url);
}
