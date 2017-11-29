package com.zakhariystasenko.pokemonlist.root;

import android.app.Application;
import android.content.Context;

import com.zakhariystasenko.pokemonlist.modules.ContextModule;

public class PokemonApplication extends Application {
    private Injector mInjector;

    public static Injector injector(Context context) {
        return ((PokemonApplication) context.getApplicationContext()).mInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInjector = DaggerInjector.builder()
                .contextModule(new ContextModule(this))
                .build();
    }
}
