/*
 * Copyright 2014 Goldman Sachs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gs.collections.impl.lazy.parallel.bag;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.annotation.Beta;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.ParallelUnsortedBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.bag.mutable.HashBag;
import com.gs.collections.impl.lazy.parallel.AbstractParallelIterable;

@Beta
public abstract class AbstractParallelUnsortedBag<T> extends AbstractParallelIterable<T> implements ParallelUnsortedBag<T>
{
    protected abstract ExecutorService getExecutorService();

    protected abstract LazyIterable<UnsortedBagBatch<T>> split();

    public ParallelUnsortedBag<T> select(Predicate<? super T> predicate)
    {
        return new ParallelSelectUnsortedBag<T>(this, predicate);
    }

    @Override
    public <P> ParallelUnsortedBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S> ParallelUnsortedBag<S> selectInstancesOf(Class<S> clazz)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ParallelUnsortedBag<T> reject(Predicate<? super T> predicate)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <P> ParallelUnsortedBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        throw new UnsupportedOperationException();
    }

    public <V> ParallelUnsortedBag<V> collect(Function<? super T, ? extends V> function)
    {
        return new ParallelCollectUnsortedBag<T, V>(this, function);
    }

    @Override
    public <P, V> ParallelUnsortedBag<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> ParallelUnsortedBag<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> ParallelUnsortedBag<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public MutableBag<T> toBag()
    {
        // To replace with ConcurrentBag
        final MutableBag<T> result = HashBag.<T>newBag().asSynchronized();
        LazyIterable<UnsortedBagBatch<T>> chunks = this.split();
        LazyIterable<Future<?>> futures = chunks.collect(new Function<UnsortedBagBatch<T>, Future<?>>()
        {
            public Future<?> valueOf(final UnsortedBagBatch<T> chunk)
            {
                return AbstractParallelUnsortedBag.this.getExecutorService().submit(new Runnable()
                {
                    public void run()
                    {
                        chunk.forEachWithOccurrences(new ObjectIntProcedure<T>()
                        {
                            public void value(T each, int occurrences)
                            {
                                result.addOccurrences(each, occurrences);
                            }
                        });
                    }
                });
            }
        });
        // The call to to toList() is important to stop the lazy evaluation and force all the Runnables to start executing.
        MutableList<Future<?>> futuresList = futures.toList();
        for (Future<?> future : futuresList)
        {
            try
            {
                future.get();
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            catch (ExecutionException e)
            {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}