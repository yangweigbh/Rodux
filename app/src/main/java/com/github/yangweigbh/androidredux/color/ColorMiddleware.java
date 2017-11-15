package com.github.yangweigbh.androidredux.color;

import android.graphics.Color;
import android.util.Log;

import com.github.yangweigbh.androidredux.Middleware;
import com.github.yangweigbh.androidredux.Store;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by yangwei-os on 2017/11/15.
 */

public class ColorMiddleware implements Middleware<ColorAction,ColorState> {
    public static final int MAX_COLOR = 256;

    private Random mRandom = new Random();
    private Executor mExecutor = Executors.newFixedThreadPool(1);

    public ColorMiddleware() {
    }

    @Override
    public void intercept(Chain<ColorAction, ColorState> chain) {
        Log.d("YangWei", "ColorMiddleware intercept");
        if (chain.getAction() instanceof ColorAction.ChangeColorAction) {
            Runnable runnable = () -> {
                //simulate network request
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int rand = mRandom.nextInt(2);
                if (rand == 0) {
                    final int rgb =
                            Color.rgb(mRandom.nextInt(MAX_COLOR), mRandom.nextInt(MAX_COLOR), mRandom.nextInt(MAX_COLOR));
                    chain.getStore().dispatch(new ColorAction.ColorSuccessAction(rgb));
                } else if (rand == 1) {
                    chain.getStore().dispatch(new ColorAction.ColorFailAction("Network Error"));
                }
            };

            mExecutor.execute(runnable);
        }

        chain.proceed(chain.getAction());
    }
}
