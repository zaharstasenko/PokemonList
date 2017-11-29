package com.zakhariystasenko.pokemonlist.pokemon_details_view;

import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.Explode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zakhariystasenko.pokemonlist.R;
import com.zakhariystasenko.pokemonlist.data_management.DataManager;
import com.zakhariystasenko.pokemonlist.data_model.BasePokemonInfo;
import com.zakhariystasenko.pokemonlist.data_model.DetailedPokemonInfo;
import com.zakhariystasenko.pokemonlist.root.PokemonApplication;
import com.zakhariystasenko.pokemonlist.utils.SimpleObserver;
import com.zakhariystasenko.pokemonlist.utils.PokemonApi;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PokemonDetailsActivity extends Activity {
    private static final String POKEMON_KEY = "BasePokemonInfo";

    @BindView(R.id.pokemonName)
    TextView mPokemonName;

    @BindView(R.id.pokemonImage)
    ImageView mPokemonImage;

    @BindView(R.id.pokemonId)
    TextView mPokemonId;

    @BindView(R.id.message)
    TextView mMessage;

    @Inject
    DataManager mDataManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity_layout);

        PokemonApplication.injector(this).inject(this);
        ButterKnife.bind(this);

        initAnimation();
        initBaseView();
    }

    private void initAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode());
        }
    }

    private void initBaseView() {
        BasePokemonInfo basePokemonInfo = (BasePokemonInfo) getIntent().getSerializableExtra(POKEMON_KEY);
        if (basePokemonInfo == null) {
            return;
        }

        mPokemonName.setText(basePokemonInfo.getName());
        mPokemonId.setText(String.format(getString(R.string.pokemon_id), basePokemonInfo.getId()));
        Picasso.with(this)
                .load(PokemonApi.pokemonImage(basePokemonInfo.getId()))
                .fit()
                .placeholder(R.drawable.pokeball)
                .into(mPokemonImage);

        mDataManager.requestPokemonDetails(pokemonObserver(), basePokemonInfo.getId());

    }

    public static Bundle getStartBundle(BasePokemonInfo basePokemonInfo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(POKEMON_KEY, basePokemonInfo);
        return bundle;
    }

    private SimpleObserver<DetailedPokemonInfo> pokemonObserver() {
        return new SimpleObserver<DetailedPokemonInfo>() {
            @Override
            public void onSuccess(DetailedPokemonInfo detailedPokemonInfo) {
                mMessage.setVisibility(View.GONE);
                initDetails(detailedPokemonInfo);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mMessage.setText(R.string.error_text);
            }
        };
    }

    void initDetails(DetailedPokemonInfo detailedPokemonInfo) {
        Fragment fragment = new PokemonDetailsFragment();
        fragment.setArguments(PokemonDetailsFragment.getStartBundle(detailedPokemonInfo));
        getFragmentManager().beginTransaction().add(R.id.details,
                fragment).commit();
    }
}
