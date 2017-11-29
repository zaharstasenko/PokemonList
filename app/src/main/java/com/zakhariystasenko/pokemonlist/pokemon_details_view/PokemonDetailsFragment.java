package com.zakhariystasenko.pokemonlist.pokemon_details_view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zakhariystasenko.pokemonlist.R;
import com.zakhariystasenko.pokemonlist.data_model.DetailedPokemonInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PokemonDetailsFragment extends Fragment {
    private static final String DETAILS_KEY = "PokemonDetails";

    @BindView(R.id.pokemonHeight)
    TextView mPokemonHeight;

    @BindView(R.id.pokemonWeight)
    TextView mPokemonWeight;

    @BindView(R.id.pokemonBaseExperience)
    TextView mPokemonBaseExperience;

    @BindView(R.id.pokemonAbilities)
    TextView mPokemonAbilities;

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details_layout, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        initView();
    }

    static Bundle getStartBundle(DetailedPokemonInfo detailedPokemonInfo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DETAILS_KEY, detailedPokemonInfo);

        return bundle;
    }

    private void initView() {
        DetailedPokemonInfo detailedPokemonInfo = (DetailedPokemonInfo) getArguments().getSerializable(DETAILS_KEY);
        if (detailedPokemonInfo == null) {
            return;
        }

        mPokemonHeight.setText(String.format(getString(R.string.pokemon_height), detailedPokemonInfo.getHeight()));
        mPokemonWeight.setText(String.format(getString(R.string.pokemon_weight), detailedPokemonInfo.getWeight()));
        mPokemonBaseExperience.setText(String.format(getString(R.string.pokemon_base_experience), detailedPokemonInfo.getBaseExperience()));
        mPokemonAbilities.setText(String.format(getString(R.string.pokemon_abilities), detailedPokemonInfo.getAbilities()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
