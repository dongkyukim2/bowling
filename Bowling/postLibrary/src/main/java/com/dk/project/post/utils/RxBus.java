package com.dk.project.post.utils;

import androidx.core.util.Pair;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by dkkim on 2017-12-17.
 */

public class RxBus {

    private static RxBus instance;
    private PublishSubject<Pair<Integer, Object>> eventSubject;

    public RxBus() {
        eventSubject = PublishSubject.create();
    }

    public static RxBus getInstance() {
        if (instance == null) {
            instance = new RxBus();
        }
        return instance;
    }

    public void eventPost(Pair<Integer, Object> event) {
        eventSubject.onNext(event);
    }

    public Disposable registerRxObserver(Consumer<Pair<Integer, Object>> onNext, Consumer<? super Throwable> onError) {
        return eventSubject.subscribe(onNext, onError);
    }

    public Disposable registerRxObserver(Consumer<Pair<Integer, Object>> onNext) {
        return eventSubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, throwable -> {
                    System.out.println("bbbbbbbbbbbbbbbbbb   registerRxObserver   " + throwable.toString());
                    for (StackTraceElement element : throwable.getStackTrace()) {
                        System.out.println("bbbbbbbbbbbbbbbbbb   registerRxObserver   " + element.toString());
                    }
                    System.out.println("bbbbbbbbbbbbbbbbbb   registerRxObserver   ====================================");
                });
    }
}