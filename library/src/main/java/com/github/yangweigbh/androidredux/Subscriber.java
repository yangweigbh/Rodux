package com.github.yangweigbh.androidredux;

import java.util.concurrent.Executor;

/**
 * Created by yangwei-os on 2017/11/14.
 */

public abstract class Subscriber<T> {
    private final Executor executor;

    public Subscriber(Executor executor) {
        this.executor = executor;
    }

    public void onNext(final T next) {
        if (executor != null) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    handle(next);
                }
            });
        } else {
            handle(next);
        }
    }

    public abstract void handle(T next);
}
