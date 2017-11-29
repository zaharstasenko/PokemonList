package com.zakhariystasenko.pokemonlist.utils;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class SimpleObserver<T> implements SingleObserver<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onSuccess(T t) {

    }

    @Override
    public void onError(Throwable e) {
    }
}
