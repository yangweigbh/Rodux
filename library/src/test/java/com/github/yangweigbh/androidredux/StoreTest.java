package com.github.yangweigbh.androidredux;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by yangwei-os on 2017/11/15.
 */
public class StoreTest {
    @Mock
    Reducer<Action, State> mReducer;

    @Mock
    Executor mExecutor;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private Store<Action, State> mStore;

    private State initState = new State("init");

    @Test
    public void addStateHandler() throws Exception {
        TestStateHanlder stateHanlder = Mockito.spy(new TestStateHanlder(null));
        mStore = new Store<>(null, initState, mReducer, null);
        mStore.addStateHandler(stateHanlder);

        assertThat(mStore.getState().tag, is("init"));

        when(mReducer.reduce(any(Action.class), any(State.class))).thenReturn(new State("second"));

        mStore.dispatch(new Action());

        assertThat(mStore.getState().tag, is("second"));

        ArgumentCaptor<State> captor = ArgumentCaptor.forClass(State.class);
        verify(stateHanlder, times(1)).handle(captor.capture());

        State state = captor.getValue();
        assertThat(state.tag, is("second"));
    }

    private static class TestStateHanlder extends StateHandler<State> {

        public TestStateHanlder(Executor executor) {
            super(executor);
        }

        @Override
        public void handle(State next) {

        }
    }

    @Test
    public void removeStateHanlder() throws Exception {
        TestStateHanlder stateHanlder = Mockito.spy(new TestStateHanlder(null));
        mStore = new Store<>(null, initState, mReducer, null);
        mStore.addStateHandler(stateHanlder);
        mStore.removeStateHanlder(stateHanlder);

        assertThat(mStore.getState().tag, is("init"));

        when(mReducer.reduce(any(Action.class), any(State.class))).thenReturn(new State("second"));

        mStore.dispatch(new Action());

        assertThat(mStore.getState().tag, is("second"));

        verify(stateHanlder, never()).handle(any(State.class));
    }

    @Test
    public void testStore1() throws Exception {
        mStore = new Store<Action, State>(null, initState, mReducer, null);

        assertThat(mStore.getState().tag, is("init"));

        when(mReducer.reduce(any(Action.class), any(State.class))).thenReturn(new State("second"));

        mStore.dispatch(new Action());

        assertThat(mStore.getState().tag, is("second"));
    }

    @Test
    public void testStore2() throws Exception {
        mStore = new Store<Action, State>(mExecutor, initState, mReducer, null);

        mStore.dispatch(new Action());

        verify(mExecutor, times(1)).execute(any(Runnable.class));
    }

    @Test
    public void test_execute_on_executor() throws Exception {
        mStore = new Store<Action, State>(Executors.newSingleThreadExecutor(), initState, mReducer, null);

        assertThat(mStore.getState().tag, is("init"));

        when(mReducer.reduce(any(Action.class), any(State.class))).thenReturn(new State("second"));

        mStore.dispatch(new Action());

        //wait for result be computed on another thread
        Thread.sleep(100);

        assertThat(mStore.getState().tag, is("second"));
    }

    @Test
    public void test_Middleware_can_be_called() throws Exception {
        TestMiddleware middleware = Mockito.spy(new TestMiddleware());
        mStore = new Store<>(null, initState, mReducer, Arrays.<Middleware>asList(middleware));

        assertThat(mStore.getState().tag, is("init"));

        when(mReducer.reduce(any(Action.class), any(State.class))).thenReturn(new State("second"));

        mStore.dispatch(new Action());

        assertThat(mStore.getState().tag, is("second"));
        verify(middleware, times(1)).intercept(any(Middleware.Chain.class));
    }

    private static class TestMiddleware implements Middleware<Action, State> {
        @Override
        public void intercept(Chain<Action, State> chain) {
            chain.proceed(chain.getAction());
        }
    }
}