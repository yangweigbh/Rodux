package com.github.yangweigbh.androidredux;

import java.util.concurrent.Executor;

/**
 * Created by yangwei-os on 2017/11/15.
 */

public abstract class BasePresenter<A, S> extends StateHandler<S> implements LifeCycleCallback{
    protected Store<A, S> mStore;
    boolean isActivityRunning = false;

    public BasePresenter(Store<A, S> store, Executor executor) {
        super(executor);

        mStore = store;
    }

    public void onStart() {
        isActivityRunning = true;
        mStore.addStateHandler(this);
        handle(mStore.getState());
    }

    public void onResume() {}

    public void onPause() {}

    public void onStop() {
        isActivityRunning = false;
    }

    @Override
    public void handle(S next) {
        if (isActivityRunning) {
            handleState(next);
        }
    }

    abstract protected void handleState(S next);

    public void onDestroy() {
        mStore.removeStateHanlder(this);
    }
}
