/*
 * Copyright 2008-present MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mongodb.reactivestreams.client.internal;

import com.mongodb.internal.async.client.AsyncListDatabasesIterable;
import com.mongodb.reactivestreams.client.ListDatabasesPublisher;
import org.bson.conversions.Bson;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.concurrent.TimeUnit;

import static com.mongodb.assertions.Assertions.notNull;


final class ListDatabasesPublisherImpl<TResult> implements ListDatabasesPublisher<TResult> {

    private final AsyncListDatabasesIterable<TResult> wrapped;

    ListDatabasesPublisherImpl(final AsyncListDatabasesIterable<TResult> wrapped) {
        this.wrapped = notNull("wrapped", wrapped);
    }

    @Override
    public ListDatabasesPublisher<TResult> maxTime(final long maxTime, final TimeUnit timeUnit) {
        notNull("timeUnit", timeUnit);
        wrapped.maxTime(maxTime, timeUnit);
        return this;
    }

    @Override
    public ListDatabasesPublisher<TResult> filter(final Bson filter) {
        wrapped.filter(filter);
        return this;
    }

    @Override
    public ListDatabasesPublisher<TResult> nameOnly(final Boolean nameOnly) {
        wrapped.nameOnly(nameOnly);
        return this;
    }

    @Override
    public ListDatabasesPublisher<TResult> authorizedDatabasesOnly(final Boolean authorizedDatabasesOnly) {
        wrapped.authorizedDatabasesOnly(authorizedDatabasesOnly);
        return this;
    }

    @Override
    public ListDatabasesPublisher<TResult> batchSize(final int batchSize) {
        wrapped.batchSize(batchSize);
        return this;
    }

    @Override
    public Publisher<TResult> first() {
        return Publishers.publish(wrapped::first);
    }

    @Override
    public void subscribe(final Subscriber<? super TResult> s) {
        Publishers.publish(wrapped).subscribe(s);
    }
}
