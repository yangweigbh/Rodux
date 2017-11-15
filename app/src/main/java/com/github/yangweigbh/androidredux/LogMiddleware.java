package com.github.yangweigbh.androidredux;

import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by yangwei on 2017/11/15.
 */

public class LogMiddleware<A, S> implements Middleware<A, S> {
    private final Logger<A, S> mLogger;

    private Executor mExecutor = Executors.newFixedThreadPool(1);

    public LogMiddleware(Logger<A, S> logger) {
        mLogger = logger;
    }

    @Override
    public void intercept(final Chain<A, S> chain) {
        mExecutor.execute(() -> mLogger.logAction(chain.getAction()));

        chain.proceed(chain.getAction());

        mExecutor.execute(() -> mLogger.logState(chain.getState()));
    }
}
