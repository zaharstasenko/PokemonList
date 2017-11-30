package com.zakhariystasenko.pokemonlist.root;

import com.zakhariystasenko.pokemonlist.modules.ContextModule;
import com.zakhariystasenko.pokemonlist.modules.DataManagerModule;
import com.zakhariystasenko.pokemonlist.modules.DatabaseHelperModule;
import com.zakhariystasenko.pokemonlist.modules.OkHttpClientModule;
import com.zakhariystasenko.pokemonlist.modules.RetrofitModule;
import com.zakhariystasenko.pokemonlist.pokemon_details_view.PokemonDetailsActivity;
import com.zakhariystasenko.pokemonlist.pokemon_list_view.PokemonListActivity;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = {
        ContextModule.class,
        OkHttpClientModule.class,
        RetrofitModule.class,
        DatabaseHelperModule.class,
        DataManagerModule.class
})
public interface Injector {
    void inject(PokemonListActivity activity);
    void inject(PokemonDetailsActivity activity);
}
