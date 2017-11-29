package com.zakhariystasenko.pokemonlist.data_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PokemonListDownloadModel {
    @SerializedName("results")
    private List<Result> mResults;

    public class Result {
        @SerializedName("name")
        private String mName;

        public String getName() {
            return mName;
        }
    }

    public List<Result> getResult() {
        return mResults;
    }
}
