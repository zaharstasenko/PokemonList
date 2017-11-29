package com.zakhariystasenko.pokemonlist.modules;

import com.zakhariystasenko.pokemonlist.data_management.DataManager;
import com.zakhariystasenko.pokemonlist.data_management.DataBaseHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class DataManagerModule {
    @Provides
    @Singleton
    DataManager provideDataManager(DataBaseHelper dataBaseHelper, Retrofit retrofit){
        return new DataManager(dataBaseHelper, retrofit);
    }
}
