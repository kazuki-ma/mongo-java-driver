package com.mongodb.async.client;

import java.util.concurrent.CompletableFuture;

import com.mongodb.async.SingleResultCallback;

/**
 * Subclass of CompletableSingleResultCallback that implements mongo's {@link SingleResultCallback}.
 *
 * @param <T> the result type
 *
 * @since 3.4
 */
public class CompletableSingleResultCallback<T>
        extends CompletableFuture<T>
        implements SingleResultCallback<T> {

    @Override
    public void onResult(T result, Throwable t) {
        if (t == null) {
            complete(result);
        } else {
            completeExceptionally(t);
        }
    }
}
