package com.zakhariystasenko.pokemonlist.data_model;

import com.google.gson.annotations.SerializedName;

public class PokemonDownloadModel {
    @SerializedName("base_experience")
    private String mBaseExperience;

    @SerializedName("height")
    private String mHeight;

    @SerializedName("weight")
    private String mWeight;

    @SerializedName("abilities")
    private Abilities[] mAbilities;

    public String getBaseExperience() {
        return mBaseExperience;
    }

    public String getHeight() {
        return mHeight;
    }

    public String getWeight() {
        return mWeight;
    }

    public Abilities[] getAbilities() {
        return mAbilities;
    }

    public class Abilities {
        @SerializedName("ability")
        private Ability mAbility;

        public Ability getAbility() {
            return mAbility;
        }

        public class Ability {
            @SerializedName("name")
            private String mName;

            public String getName() {
                return mName;
            }
        }
    }
}
