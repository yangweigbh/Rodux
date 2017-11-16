package com.github.yangweigbh.androidredux;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by yangwei-os on 2017/11/16.
 */
public class StateHandlerTest {
    @Test
    public void handle_no_executor() throws Exception {
        TestStateHanlder stateHandler = Mockito.spy(new TestStateHanlder(null));

        stateHandler.onNext(new State("test"));

        ArgumentCaptor<State> captor = ArgumentCaptor.forClass(State.class);
        verify(stateHandler, times(1)).handle(captor.capture());

        State state = captor.getValue();
        assertThat(state.tag, is("test"));
    }

    @Test
    public void handle_with_executor() throws Exception {
        TestStateHanlder stateHandler = Mockito.spy(new TestStateHanlder(Executors.newSingleThreadExecutor()));

        stateHandler.onNext(new State("test"));

        Thread.sleep(100);

        ArgumentCaptor<State> captor = ArgumentCaptor.forClass(State.class);
        verify(stateHandler, times(1)).handle(captor.capture());

        State state = captor.getValue();
        assertThat(state.tag, is("test"));
    }

    private static class TestStateHanlder extends StateHandler<State> {

        public TestStateHanlder(Executor executor) {
            super(executor);
        }

        @Override
        public void handle(State next) {

        }
    }
}