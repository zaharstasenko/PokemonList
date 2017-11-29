package com.zakhariystasenko.pokemonlist.data_management;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zakhariystasenko.pokemonlist.data_model.BasePokemonInfo;
import com.zakhariystasenko.pokemonlist.data_model.DetailedPokemonInfo;
import com.zakhariystasenko.pokemonlist.utils.DatabaseMissingDataException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PokemonDatabase";
    private static final String POKEMON_TABLE = "PokemonTable";
    private static final int VERSION = 1;

    private static final String KEY_ID = "Id";
    private static final String KEY_NAME = "Name";
    private static final String KEY_WEIGHT = "Weight";
    private static final String KEY_HEIGHT = "Height";
    private static final String KEY_BASE_EXPERIENCE = "Experience";
    private static final String KEY_ABILITIES = "Abilities";

    private static final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + POKEMON_TABLE + "(" + KEY_ID + " text, " +
                KEY_NAME + " text, " + KEY_WEIGHT + " text, " + KEY_HEIGHT + " text, " +
                KEY_BASE_EXPERIENCE + " text, " + KEY_ABILITIES + " text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    Single<List<BasePokemonInfo>> requestPokemonList() {
        return Single.create(new SingleOnSubscribe<List<BasePokemonInfo>>() {
            @Override
            public void subscribe(final SingleEmitter<List<BasePokemonInfo>> e) throws Exception {
                mExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        List<BasePokemonInfo> basePokemonInfos = searchPokemonsList();

                        if (basePokemonInfos != null) {
                            e.onSuccess(basePokemonInfos);
                        } else {
                            e.onError(new DatabaseMissingDataException());
                        }
                    }
                });
            }
        });
    }

    Single<DetailedPokemonInfo> requestPokemonDetails(final String pokemonId) {
        return Single.create(new SingleOnSubscribe<DetailedPokemonInfo>() {
            @Override
            public void subscribe(final SingleEmitter<DetailedPokemonInfo> e) throws Exception {
                mExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        DetailedPokemonInfo detailedPokemonInfo = searchPokemonDetails(pokemonId);

                        if (detailedPokemonInfo != null) {
                            e.onSuccess(detailedPokemonInfo);
                        } else {
                            e.onError(new DatabaseMissingDataException());
                        }
                    }
                });
            }
        });
    }

    private List<BasePokemonInfo> searchPokemonsList() {
        Cursor cursor = getReadableDatabase()
                .query(POKEMON_TABLE,
                new String[] {KEY_ID, KEY_NAME},
                null,
                null,
                null,
                null,
                null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        ArrayList<BasePokemonInfo> basePokemonInfos = new ArrayList<>();

        int idIndex = cursor.getColumnIndex(KEY_ID);
        int nameIndex = cursor.getColumnIndex(KEY_NAME);

        do {
            basePokemonInfos.add(new BasePokemonInfo(cursor.getString(nameIndex),
                    cursor.getString(idIndex)));
        } while (cursor.moveToNext());

        cursor.close();
        return basePokemonInfos;
    }

    private DetailedPokemonInfo searchPokemonDetails(String pokemonId) {
        Cursor cursor = getReadableDatabase()
                .query(POKEMON_TABLE,
                        null,
                        KEY_ID + " = " + pokemonId,
                        null,
                        null,
                        null,
                        null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        String weight = cursor.getString(cursor.getColumnIndex(KEY_WEIGHT));
        String height = cursor.getString(cursor.getColumnIndex(KEY_HEIGHT));
        String baseExperience = cursor.getString(cursor.getColumnIndex(KEY_BASE_EXPERIENCE));
        String abilities = cursor.getString(cursor.getColumnIndex(KEY_ABILITIES));

        cursor.close();
        return weight == null || height == null || baseExperience == null || abilities == null ?
                null : new DetailedPokemonInfo(weight, height, baseExperience, abilities);
    }

    void writeToDatabase(final List<BasePokemonInfo> basePokemonInfos) {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase database = getWritableDatabase();
                for (BasePokemonInfo item : basePokemonInfos) {
                    database.insert(POKEMON_TABLE, null, createContentValues(item));
                }
                close();
            }
        });
    }

    void writeToDatabase(final String id, final DetailedPokemonInfo detailedPokemonInfo) {
        mExecutor.submit(new Runnable() {
            @Override
            public void run() {
                getWritableDatabase().update(POKEMON_TABLE,
                        createContentValues(detailedPokemonInfo),
                        KEY_ID + " = " + id,
                        null);
                close();
            }
        });
    }

    private ContentValues createContentValues(BasePokemonInfo basePokemonInfo) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DataBaseHelper.KEY_ID,
                basePokemonInfo.getId());
        contentValues.put(DataBaseHelper.KEY_NAME,
                basePokemonInfo.getName());

        return contentValues;
    }

    private ContentValues createContentValues(DetailedPokemonInfo detailedPokemonInfo) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DataBaseHelper.KEY_WEIGHT,
                detailedPokemonInfo.getWeight());
        contentValues.put(DataBaseHelper.KEY_HEIGHT,
                detailedPokemonInfo.getHeight());
        contentValues.put(DataBaseHelper.KEY_BASE_EXPERIENCE,
                detailedPokemonInfo.getBaseExperience());
        contentValues.put(DataBaseHelper.KEY_ABILITIES,
                detailedPokemonInfo.getAbilities());

        return contentValues;
    }
}
