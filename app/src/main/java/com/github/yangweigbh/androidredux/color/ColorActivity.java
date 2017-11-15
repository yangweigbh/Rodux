package com.github.yangweigbh.androidredux.color;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.yangweigbh.androidredux.LogMiddleware;
import com.github.yangweigbh.androidredux.LoggerImpl;
import com.github.yangweigbh.androidredux.R;
import com.github.yangweigbh.androidredux.Store;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ColorActivity extends AppCompatActivity implements ColorContract.View {

    private ColorPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoggerImpl<ColorAction, ColorState> logger = new LoggerImpl<>();

        LogMiddleware<ColorAction, ColorState> logMiddleware = new LogMiddleware<>(logger);
        ColorMiddleware colorMiddleware = new ColorMiddleware();

        Store<ColorAction, ColorState> store = new Store<>(Executors.newFixedThreadPool(1),
                ColorState.empty(), new ColorReducer(), Arrays.asList(logMiddleware, colorMiddleware));

        new ColorPresenter(store, this, new MainThreadExecutor());

        findViewById(R.id.button).setOnClickListener(v -> mPresenter.changeColor());
    }

    @Override
    public void setPresenter(ColorContract.Presenter presenter) {
        mPresenter = (ColorPresenter) presenter;
    }

    @Override
    public void render(ColorState state) {
        final TextView label = (TextView) findViewById(R.id.label);
        label.setBackgroundColor(getResources().getColor(android.R.color.background_light));
        label.setText("");
        if (state.isRefreshing) {
            label.setText("↺");
        } else if (state.error != null) {
            label.setText(state.error);
        } else if (state.color != ColorState.INVALID_COLOR) {
            label.setBackgroundColor(state.color);
        }
    }

    public static class MainThreadExecutor implements Executor {
        Handler mHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            mHandler.post(runnable);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mPresenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mPresenter.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.onResume();
    }
}
