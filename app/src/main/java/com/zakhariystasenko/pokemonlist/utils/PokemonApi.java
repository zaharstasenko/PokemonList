package com.zakhariystasenko.pokemonlist.utils;

import android.net.Uri;

public class PokemonApi {
    private static final String POKEMON_REQUEST = "pokemon/%s/";
    private static final String POKEMON_IMAGE =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/%s.png";

    public static final String API_URL = "http://pokeapi.co/api/v2/";
    public static final String ALL_POKEMONS_REQUEST = "pokemon?limit=802&offset=0";

    public static Uri pokemonImage(String pokemonId) {
        return Uri.parse(String.format(POKEMON_IMAGE, pokemonId));
    }

    public static String pokemonDetailsRequest(String pokemonId) {
        return String.format(POKEMON_REQUEST, pokemonId);
    }
}
