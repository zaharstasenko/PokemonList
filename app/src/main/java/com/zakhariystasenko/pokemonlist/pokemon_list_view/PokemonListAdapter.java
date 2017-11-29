package com.zakhariystasenko.pokemonlist.pokemon_list_view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.zakhariystasenko.pokemonlist.R;
import com.zakhariystasenko.pokemonlist.data_model.BasePokemonInfo;
import com.zakhariystasenko.pokemonlist.utils.PokemonApi;

import java.util.ArrayList;

public class PokemonListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Picasso mPicasso;
    private Context mContext;

    private ArrayList<BasePokemonInfo> mBasePokemonInfos = new ArrayList<>();
    private ItemClickCallback mCallback;

    PokemonListAdapter(ArrayList<BasePokemonInfo> basePokemonInfos,
                       Context context,
                       ItemClickCallback callback) {
        mBasePokemonInfos = basePokemonInfos;
        mPicasso = Picasso.with(context);
        mContext = context;
        mCallback = callback;
    }

    ArrayList<BasePokemonInfo> getData() {
        return mBasePokemonInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final BasePokemonInfo basePokemonInfo = mBasePokemonInfos.get(position);

        holder.mPokemonName.setText(basePokemonInfo.getName());
        holder.mPokemonId.setText(
                String.format(
                        mContext.getString(R.string.pokemon_id),
                        basePokemonInfo.getId()));

        mPicasso.load(PokemonApi.pokemonImage(basePokemonInfo.getId()))
                .placeholder(R.drawable.pokeball)
                .fit()
                .into(holder.mPokemonImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onItemTouch(basePokemonInfo,
                        holder.mPokemonImage,
                        holder.mPokemonName,
                        holder.mPokemonId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBasePokemonInfos.size();
    }

    interface ItemClickCallback {
        void onItemTouch(BasePokemonInfo basePokemonInfo,
                         View pokemonImage,
                         View pokemonName,
                         View pokemonId);
    }
}
