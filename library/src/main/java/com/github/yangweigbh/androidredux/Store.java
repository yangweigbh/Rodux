package com.github.yangweigbh.androidredux;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executor;

/**
 * Created by yangwei-os on 2017/11/14.
 */

public class Store<A, S> {
    private final Reducer<A, S> mReducer;
    private List<StateHandler<S>> mStateHanlders = Collections.synchronizedList(new LinkedList<StateHandler<S>>());
    private Executor mStoreExecutor;
    private S mState;
    private List<Middleware> mMiddlewares = new ArrayList<>();

    public Store(Executor storeExecutor, S initState, Reducer<A, S> reducer, List<Middleware> customMiddlewares) {
        if (initState == null) throw new NullPointerException("initState can not be null");
        if (reducer == null) throw new NullPointerException("reducer can not be null");

        if (storeExecutor != null) {
            mStoreExecutor = new SerialExecutor(storeExecutor);
        }
        mState = initState;
        mReducer = reducer;

        mMiddlewares.add(new NotifySubscribersMiddleware());

        if (customMiddlewares != null && customMiddlewares.size() > 0) {
            mMiddlewares.addAll(customMiddlewares);
        }

        mMiddlewares.add(new CallReducerMiddleware());
    }

    public void dispatch(final A action) {
        if (mStoreExecutor != null) {
            mStoreExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    handle(action);
                }
            });
        } else {
            handle(action);
        }
    }

    private void handle(A action) {
        new MiddlewareChain<>(this, 0, action).proceed(action);
    }

    private S reduce(A action, S currentState) {
        return mReducer.reduce(action, currentState);
    }

    public void addStateHandler(StateHandler stateHandler) {
        mStateHanlders.add(stateHandler);
    }

    public void removeStateHanlder(StateHandler stateHandler) {
        mStateHanlders.remove(stateHandler);
    }

    public S getState() {
        return mState;
    }

    private void dispatchState(S state) {
        for (StateHandler<S> stateHandler: mStateHanlders) {
            stateHandler.onNext(state);
        }
    }

    List<Middleware> getMiddlewares() {
        return mMiddlewares;
    }

    private class CallReducerMiddleware implements Middleware<A, S> {

        @Override
        public void intercept(Chain<A, S> chain) {
            mState = reduce(chain.getAction(), mState);
        }
    }

    private class NotifySubscribersMiddleware implements Middleware<A, S> {

        @Override
        public void intercept(Chain<A, S> chain) {
            chain.proceed(chain.getAction());

            dispatchState(chain.getState());
        }
    }

    private static class SerialExecutor implements Executor {
        private Queue<Runnable> queue = new LinkedList<>();

        private final Executor mExecutor;
        private Runnable mActive;

        public SerialExecutor(Executor wrapped) {
            mExecutor = wrapped;
        }

        @Override
        public synchronized void execute(@NonNull final Runnable runnable) {
            queue.offer(new Runnable() {
                @Override
                public void run() {
                    try {
                        runnable.run();
                    } finally {
                        scheduleNext();
                    }
                }
            });

            if (mActive == null) {
                scheduleNext();
            }
        }

        private void scheduleNext() {
            while ((mActive = queue.poll()) != null) {
                mExecutor.execute(mActive);
            }
        }
    }
}
