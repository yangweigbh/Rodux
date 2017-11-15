package com.github.yangweigbh.androidredux;

import java.util.concurrent.Executor;

/**
 * Created by yangwei-os on 2017/11/14.
 */

public abstract class StateHandler<S> extends Subscriber<S> {
    public StateHandler(Executor executor) {
        super(executor);
    }
}
