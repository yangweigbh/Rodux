package com.github.yangweigbh.androidredux;

import java.util.concurrent.Executor;

/**
 * Created by yangwei-os on 2017/11/14.
 */

public abstract class StateHandler<S> {
    private final Executor executor;

    public StateHandler(Executor executor) {
        this.executor = executor;
    }

    public void onNext(final S next) {
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

    public abstract void handle(S next);
}
