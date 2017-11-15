package com.github.yangweigbh.androidredux.color;

import com.github.yangweigbh.androidredux.BasePresenter;
import com.github.yangweigbh.androidredux.Store;

import java.util.concurrent.Executor;

/**
 * Created by yangwei-os on 2017/11/15.
 */

public class ColorPresenter extends BasePresenter<ColorAction, ColorState> implements ColorContract.Presenter {
    private final ColorContract.View mView;

    public ColorPresenter(Store<ColorAction, ColorState> store, ColorContract.View view, Executor executor) {
        super(store, executor);
        this.mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void changeColor() {
        mStore.dispatch(new ColorAction.ChangeColorAction());
    }

    @Override
    public void handleState(ColorState next) {
        mView.render(next);
    }
}
