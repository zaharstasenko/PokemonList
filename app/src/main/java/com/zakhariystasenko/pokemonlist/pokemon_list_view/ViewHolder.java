package com.zakhariystasenko.pokemonlist.pokemon_list_view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zakhariystasenko.pokemonlist.R;

import butterknife.BindView;
import butterknife.ButterKnife;

class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.pokemonName)
    TextView mPokemonName;

    @BindView(R.id.pokemonImage)
    ImageView mPokemonImage;

    @BindView(R.id.pokemonId)
    TextView mPokemonId;

    ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
