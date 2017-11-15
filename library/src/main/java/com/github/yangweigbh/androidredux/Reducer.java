package com.github.yangweigbh.androidredux;

/**
 * Created by yangwei-os on 2017/11/14.
 */

public interface Reducer<A, S> {
    S reduce(A action, S currentState);
}
