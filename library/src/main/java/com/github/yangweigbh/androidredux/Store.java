package com.github.yangweigbh.androidredux;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by yangwei-os on 2017/11/14.
 */

public class Store<A, S> {
    private final Reducer<A, S> mReducer;
    private List<SideEffect<A>> mSideEffects = Collections.synchronizedList(new LinkedList<SideEffect<A>>());
    private List<StateHandler<S>> mStateHanlders = Collections.synchronizedList(new LinkedList<StateHandler<S>>());
    private Executor mStoreExecutor;
    private S mState;

    public Store(Executor storeExecutor, S initState, Reducer<A, S> reducer) {
        if (initState == null) throw new NullPointerException("initState can not be null");
        if (reducer == null) throw new NullPointerException("reducer can not be null");

        mStoreExecutor = storeExecutor;
        mState = initState;
        mReducer = reducer;
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
        mState = reduce(action, mState);
        dispatchState(mState);
        dispatchActionToSideEffects(action);
    }

    private void dispatchActionToSideEffects(A action) {
        for (SideEffect<A> sideEffect: mSideEffects) {
            sideEffect.onNext(action);
        }
    }

    private S reduce(A action, S currentState) {
        return mReducer.reduce(action, currentState);
    }

    public void addSideEffect(SideEffect sideEffect) {
        mSideEffects.add(sideEffect);
    }

    public void removeSideEffect(SideEffect sideEffect) {
        mSideEffects.remove(sideEffect);
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
}
