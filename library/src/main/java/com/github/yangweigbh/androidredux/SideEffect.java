package com.github.yangweigbh.androidredux;

import java.util.concurrent.Executor;

/**
 * Created by yangwei-os on 2017/11/14.
 */

public abstract class SideEffect<A> extends Subscriber<A> {

    public SideEffect(Executor executor) {
        super(executor);
    }
}
