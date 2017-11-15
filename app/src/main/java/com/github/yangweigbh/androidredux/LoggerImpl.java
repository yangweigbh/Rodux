package com.github.yangweigbh.androidredux;

import android.util.Log;

/**
 * Created by yangwei on 2017/11/15.
 */

public class LoggerImpl<A, S> implements Logger<A, S> {
    private static final String TAG = "LoggerImpl";

    @Override
    public void logState(S state) {
        Log.d(TAG, "state: " + state.toString());
    }

    @Override
    public void logAction(A action) {
        Log.d(TAG, "action: " + action.toString());
    }
}
