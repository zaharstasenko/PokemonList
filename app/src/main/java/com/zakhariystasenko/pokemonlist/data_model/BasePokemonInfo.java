package com.zakhariystasenko.pokemonlist.data_model;

import java.io.Serializable;

public class BasePokemonInfo implements Serializable {
    private String mName;
    private String mId;

    public BasePokemonInfo(String name, String id) {
        mName = name;
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public String getId() {
        return mId;
    }
}
