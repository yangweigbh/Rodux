package com.github.yangweigbh.androidredux;

/**
 * Created by yangwei on 2017/11/16.
 */

public interface Logger<A, S> {
    void logAction(A action);

    void logState(S state);
}
