package com.mongodb.async.client;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mongodb.Block;
import com.mongodb.Function;
import com.mongodb.async.AsyncBatchCursor;
import com.mongodb.async.SingleResultCallback;

public interface MongoIterable<TResult> {
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Copied from original (Java6 compatible) MongoIterable
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Helper to return the first item in the iterator or null.
     *
     * @param callback a callback that is passed the first item or null.
     */
    void first(SingleResultCallback<TResult> callback);

    /**
     * Iterates over all documents in the view, applying the given block to each, and completing the returned
     * future after all documents
     * have been iterated, or an exception has occurred.
     *
     * @param block the block to apply to each document
     * @param callback a callback that completed once the iteration has completed
     */
    void forEach(Block<? super TResult> block, SingleResultCallback<Void> callback);

    /**
     * Iterates over all the documents, adding each to the given target.
     *
     * @param target the collection to insert into
     * @param <A> the collection type
     * @param callback a callback that will be passed the target containing all documents
     */
    <A extends Collection<? super TResult>> void into(A target, SingleResultCallback<A> callback);

    /**
     * Maps this iterable from the source document type to the target document type.
     *
     * @param mapper a function that maps from the source to the target document type
     * @param <U> the target document type
     *
     * @return an iterable which maps T to U
     */
    <U> MongoIterable<U> map(Function<TResult, U> mapper);

    /**
     * Sets the number of documents to return per batch.
     *
     * @param batchSize the batch size
     *
     * @return this
     *
     * @mongodb.driver.manual reference/method/cursor.batchSize/#cursor.batchSize Batch Size
     */
    MongoIterable<TResult> batchSize(int batchSize);

    /**
     * Provide the underlying {@link com.mongodb.async.AsyncBatchCursor} allowing fine grained control of the
     * cursor.
     *
     * @param callback a callback that will be passed the AsyncBatchCursor
     */
    void batchCursor(SingleResultCallback<AsyncBatchCursor<TResult>> callback);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Default method for Java8
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Helper to return the first item in the iterator or null.
     * <p>
     * It is Java8 version of {@link #first(SingleResultCallback)}.
     *
     * @return CompletableFuture contains first item in the iterator or null when complete.
     */
    default CompletableFuture<TResult> first() {
        final CompletableSingleResultCallback<TResult> future = new CompletableSingleResultCallback<>();
        first(future);
        return future;
    }

    /**
     * Iterates over all documents in the view, applying the given block to each, and completing the returned
     * future after all documents have been iterated, or an exception has occurred.
     * <p>
     * It is Java8 version of {@link #forEach(Block, SingleResultCallback)}.
     *
     * @param consumer the consumer to apply to each document
     *
     * @return CompletableFuture which will be complete when all callbacks are done.
     */
    default CompletableFuture<Void> forEach(final Consumer<? super TResult> consumer) {
        final CompletableSingleResultCallback<Void> future = new CompletableSingleResultCallback<>();
        forEach(consumer::accept, future);
        return future;
    }

    /**
     * Iterates over all the documents and return it with CompletableFuture wrapping.
     * <p>
     * It is Java8 version of {@link #into(Collection, SingleResultCallback)}.
     *
     * @param collectionFactory Factory supplier to create collection.
     *
     * @return CompletableFuture contains Collection of find iterable.
     */
    default <A extends Collection<? super TResult>>
    CompletableFuture<Collection<? super TResult>> into(Supplier<A> collectionFactory) {
        final CompletableSingleResultCallback<Collection<? super TResult>> future =
                new CompletableSingleResultCallback<>();
        into(collectionFactory.get(), future);
        return future;
    }
}
