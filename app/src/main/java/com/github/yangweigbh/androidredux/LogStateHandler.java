package com.github.yangweigbh.androidredux;


import java.util.concurrent.Executor;

/**
 * Created by yangwei on 2017/11/15.
 */

public class LogStateHandler<S> extends StateHandler<S> {
    private final StateLogger mLogger;

    public LogStateHandler(Executor executor, StateLogger<S> logger) {
        super(executor);

        mLogger = logger;
    }

    @Override
    public void handle(S next) {
        mLogger.logState(next);
    }
}
