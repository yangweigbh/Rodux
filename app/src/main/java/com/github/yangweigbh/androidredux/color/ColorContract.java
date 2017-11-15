package com.github.yangweigbh.androidredux.color;

import com.github.yangweigbh.androidredux.BaseView;

/**
 * Created by yangwei-os on 2017/11/15.
 */

public interface ColorContract {
    interface View extends BaseView<Presenter> {
        void render(ColorState state);
    }

    interface Presenter {
        void changeColor();
    }
}
