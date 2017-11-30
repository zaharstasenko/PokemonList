package com.zakhariystasenko.pokemonlist.pokemon_list_view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zakhariystasenko.pokemonlist.R;
import com.zakhariystasenko.pokemonlist.data_management.DataManager;
import com.zakhariystasenko.pokemonlist.data_model.BasePokemonInfo;
import com.zakhariystasenko.pokemonlist.pokemon_details_view.PokemonDetailsActivity;
import com.zakhariystasenko.pokemonlist.root.PokemonApplication;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class PokemonListActivity extends Activity implements PokemonListAdapter.ItemClickCallback {
    @Inject
    DataManager mDataManager;

    @BindView(R.id.message)
    TextView mMessage;

    private Disposable mDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity_layout);
        PokemonApplication.injector(this).inject(this);
        ButterKnife.bind(this);
        mDataManager.requestPokemonsList(pokemonObserver());
    }

    private SingleObserver<List<BasePokemonInfo>> pokemonObserver() {
        return new SingleObserver<List<BasePokemonInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onSuccess(List<BasePokemonInfo> basePokemonInfos) {
                initPokemonList((ArrayList<BasePokemonInfo>) basePokemonInfos);
                mMessage.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                mMessage.setText(R.string.error_text);
            }
        };
    }

    private void initPokemonList(ArrayList<BasePokemonInfo> basePokemonInfos) {
        RecyclerView recyclerView = findViewById(R.id.pokemonList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PokemonListAdapter(
                basePokemonInfos,
                this,
                this));
    }

    @Override
    public void onItemTouch(BasePokemonInfo basePokemonInfo,
                            View pokemonImage,
                            View pokemonName,
                            View pokemonId) {
        Intent intent = new Intent(this, PokemonDetailsActivity.class);
        intent.putExtras(PokemonDetailsActivity.getStartBundle(basePokemonInfo));

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this,
                        Pair.create(pokemonImage, getString(R.string.transition_pokemon_image)),
                        Pair.create(pokemonName, getString(R.string.transition_pokemon_name)),
                        Pair.create(pokemonId, getString(R.string.transition_pokemon_id)));

        startActivity(intent, options.toBundle());
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposable.dispose();
    }
}
