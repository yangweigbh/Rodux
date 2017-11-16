package com.github.yangweigbh.androidredux;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by yangwei-os on 2017/11/16.
 */
public class MiddlewareChainTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    Store<Action, State> mStore;

    @Test(expected = AssertionError.class)
    public void proceed_index_out_of_range() throws Exception {
        TestMiddleware middleware1 = Mockito.spy(new TestMiddleware());
        TestMiddleware middleware2 = Mockito.spy(new TestMiddleware());
        MiddlewareChain<Action, State> chain = new MiddlewareChain<>(mStore, 2, new Action());

        when(mStore.getMiddlewares()).thenReturn(Arrays.<Middleware>asList(middleware1, middleware2));

        chain.proceed(new Action());
    }

    @Test(expected = IllegalStateException.class)
    public void proceed_twice() throws Exception {
        TestMiddleware2 middleware1 = Mockito.spy(new TestMiddleware2());
        TestMiddleware0 middleware2 = Mockito.spy(new TestMiddleware0());
        MiddlewareChain<Action, State> chain = new MiddlewareChain<>(mStore, 0, new Action());

        when(mStore.getMiddlewares()).thenReturn(Arrays.<Middleware>asList(middleware1, middleware2));

        chain.proceed(new Action());
    }

    @Test(expected = IllegalStateException.class)
    public void proceed_no_proceed_called() throws Exception {
        TestMiddleware0 middleware1 = Mockito.spy(new TestMiddleware0());
        TestMiddleware0 middleware2 = Mockito.spy(new TestMiddleware0());
        MiddlewareChain<Action, State> chain = new MiddlewareChain<>(mStore, 0, new Action());

        when(mStore.getMiddlewares()).thenReturn(Arrays.<Middleware>asList(middleware1, middleware2));

        chain.proceed(new Action());
    }

    @Test
    public void proceed() throws Exception {
        TestMiddleware middleware1 = Mockito.spy(new TestMiddleware());
        TestMiddleware0 middleware2 = Mockito.spy(new TestMiddleware0());
        MiddlewareChain<Action, State> chain = new MiddlewareChain<>(mStore, 0, new Action());

        when(mStore.getMiddlewares()).thenReturn(Arrays.<Middleware>asList(middleware1, middleware2));

        chain.proceed(new Action());

        verify(middleware1, times(1)).intercept(any(Middleware.Chain.class));
        verify(middleware2, times(1)).intercept(any(Middleware.Chain.class));
    }

    private static class TestMiddleware implements Middleware<Action, State> {
        @Override
        public void intercept(Chain<Action, State> chain) {
            chain.proceed(chain.getAction());
        }
    }

    private static class TestMiddleware2 implements Middleware<Action, State> {
        @Override
        public void intercept(Chain<Action, State> chain) {
            chain.proceed(chain.getAction());
            chain.proceed(chain.getAction());
        }
    }

    private static class TestMiddleware0 implements Middleware<Action, State> {
        @Override
        public void intercept(Chain<Action, State> chain) {
        }
    }

    @Test
    public void getState() throws Exception {
        MiddlewareChain<Action, State> chain = new MiddlewareChain<>(mStore, 0, new Action());

        when(mStore.getState()).thenReturn(new State("test"));

        assertThat(chain.getState().tag, is("test"));
    }

    @Test
    public void getAction() throws Exception {
        Action action = new Action();
        MiddlewareChain<Action, State> chain = new MiddlewareChain<>(mStore, 0, action);

        assertThat(chain.getAction(), is(action));
    }

    @Test
    public void getStore() throws Exception {
        Action action = new Action();
        MiddlewareChain<Action, State> chain = new MiddlewareChain<>(mStore, 0, action);

        assertThat(chain.getStore(), is(mStore));
    }

}