package com.github.yangweigbh.androidredux;

/**
 * Created by yangwei-os on 2017/11/15.
 */

public interface LifeCycleCallback {
    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
