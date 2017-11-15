package com.github.yangweigbh.androidredux.color;

import com.github.yangweigbh.androidredux.Reducer;

/**
 * Created by yangwei-os on 2017/11/15.
 */


public class ColorReducer implements Reducer<ColorAction, ColorState> {

    @Override
    public ColorState reduce(ColorAction action, ColorState currentState) {
        if (action instanceof ColorAction.ChangeColorAction) {
            return ColorState.refreshing();
        } else if (action instanceof ColorAction.ColorSuccessAction) {
            return ColorState.success(((ColorAction.ColorSuccessAction) action).color);
        } else if (action instanceof ColorAction.ColorFailAction) {
            return ColorState.error(((ColorAction.ColorFailAction) action).errorMessage);
        }

        return currentState;
    }
}
