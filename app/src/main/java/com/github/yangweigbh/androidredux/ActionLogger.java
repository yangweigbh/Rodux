package com.github.yangweigbh.androidredux;

/**
 * Created by yangwei on 2017/11/15.
 */

public interface ActionLogger<A> {
    void logAction(A action);
}
