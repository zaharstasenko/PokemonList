package com.zakhariystasenko.pokemonlist.modules;

import android.content.Context;

import com.zakhariystasenko.pokemonlist.data_management.DataBaseHelper;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseHelperModule {
    @Provides
    DataBaseHelper provideDatabaseHelperHelper(Context context){
        return new DataBaseHelper(context);
    }
}