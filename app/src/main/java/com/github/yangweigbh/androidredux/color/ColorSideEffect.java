package com.github.yangweigbh.androidredux.color;

import android.graphics.Color;

import com.github.yangweigbh.androidredux.SideEffect;
import com.github.yangweigbh.androidredux.Store;

import java.util.Random;
import java.util.concurrent.Executor;

/**
 * Created by yangwei-os on 2017/11/15.
 */

public class ColorSideEffect extends SideEffect<ColorAction> {
    public static final int MAX_COLOR = 256;

    private final Store<ColorAction, ColorState> mStore;
    private Random mRandom = new Random();

    public ColorSideEffect(Store<ColorAction, ColorState> store, Executor executor) {
        super(executor);
        mStore = store;
    }

    @Override
    public void handle(ColorAction next) {
        if (next instanceof ColorAction.ChangeColorAction) {
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
                mStore.dispatch(new ColorAction.ColorSuccessAction(rgb));
            } else if (rand == 1) {
                mStore.dispatch(new ColorAction.ColorFailAction("Network Error"));
            }
        }
    }
}
