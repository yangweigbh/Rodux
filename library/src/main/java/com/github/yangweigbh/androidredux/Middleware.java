package com.github.yangweigbh.androidredux;

/**
 * Created by yangwei on 2017/11/15.
 */

public interface Middleware<A, S> {

    void intercept(Chain<A, S> chain);

    interface Chain<A, S> {
        void proceed(A action);

        S getState();

        A getAction();

        Store<A, S> getStore();
    }
}
