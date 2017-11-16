package com.github.yangweigbh.androidredux;

import java.util.List;

/**
 * Created by yangwei on 2017/11/15.
 */

public class MiddlewareChain<A, S> implements Middleware.Chain<A, S> {
    private final Store<A, S> mStore;
    private final int mIndex;
    private final A mAction;
    private int mCall;

    public MiddlewareChain(Store<A, S> store, int index, A action) {
        mStore = store;
        mIndex = index;
        mAction = action;
    }

    @Override
    public void proceed(A action) {
        List<Middleware> middlewares = mStore.getMiddlewares();

        if (mIndex >= middlewares.size()) {
            throw new AssertionError();
        }

        mCall++;

        if (mCall > 1) {
            throw new IllegalStateException("middleware" + middlewares.get(mIndex - 1) + "can only proceed once");
        }

        MiddlewareChain<A, S> newChain = new MiddlewareChain<>(mStore, mIndex+1, action);
        Middleware<A, S> middleware = middlewares.get(mIndex);
        middleware.intercept(newChain);

        if (mIndex + 1 < middlewares.size() && newChain.mCall == 0) {
            throw new IllegalStateException("middleware" + middleware + "must call proceed once");
        }
    }

    @Override
    public S getState() {
        return mStore.getState();
    }

    @Override
    public A getAction() {
        return mAction;
    }

    @Override
    public Store<A, S> getStore() {
        return mStore;
    }


}
