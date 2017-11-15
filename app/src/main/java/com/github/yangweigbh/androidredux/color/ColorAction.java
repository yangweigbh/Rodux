package com.github.yangweigbh.androidredux.color;

/**
 * Created by yangwei-os on 2017/11/15.
 */

public interface ColorAction {
    class ChangeColorAction implements ColorAction {
        @Override
        public String toString() {
            return "ChangeColorAction{}";
        }
    }

    class ColorSuccessAction implements ColorAction {
        int color;

        public ColorSuccessAction(int color) {
            this.color = color;
        }

        @Override
        public String toString() {
            return "ColorSuccessAction{" +
                    "color=" + color +
                    '}';
        }
    }

    class ColorFailAction implements ColorAction {
        String errorMessage;

        public ColorFailAction(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        @Override
        public String toString() {
            return "ColorFailAction{" +
                    "errorMessage='" + errorMessage + '\'' +
                    '}';
        }
    }
}
