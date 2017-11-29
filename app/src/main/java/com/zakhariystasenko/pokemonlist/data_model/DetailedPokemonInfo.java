package com.zakhariystasenko.pokemonlist.data_model;

import java.io.Serializable;

public class DetailedPokemonInfo implements Serializable {
    private String mWeight;
    private String mHeight;
    private String mBaseExperience;
    private String mAbilities;

    public DetailedPokemonInfo(String weight, String height, String baseExperience, String abilities) {
        mWeight = weight;
        mHeight = height;
        mBaseExperience = baseExperience;
        mAbilities = abilities;
    }

    public String getWeight() {
        return mWeight;
    }

    public String getHeight() {
        return mHeight;
    }

    public String getBaseExperience() {
        return mBaseExperience;
    }

    public String getAbilities() {
        return mAbilities;
    }
}
