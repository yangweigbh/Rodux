package com.github.yangweigbh.androidredux;

import java.util.concurrent.Executor;

/**
 * Created by yangwei on 2017/11/15.
 */

public class LogSideEffect<A> extends SideEffect<A> {
    private final ActionLogger<A> mLogger;

    public LogSideEffect(Executor executor, ActionLogger<A> logger) {
        super(executor);

        mLogger = logger;
    }

    @Override
    public void handle(A next) {
        mLogger.logAction(next);
    }
}
