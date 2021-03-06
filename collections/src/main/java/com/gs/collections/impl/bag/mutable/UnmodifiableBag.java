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

package com.gs.collections.impl.bag.mutable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

import com.gs.collections.api.bag.ImmutableBag;
import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.primitive.MutableBooleanBag;
import com.gs.collections.api.bag.primitive.MutableByteBag;
import com.gs.collections.api.bag.primitive.MutableCharBag;
import com.gs.collections.api.bag.primitive.MutableDoubleBag;
import com.gs.collections.api.bag.primitive.MutableFloatBag;
import com.gs.collections.api.bag.primitive.MutableIntBag;
import com.gs.collections.api.bag.primitive.MutableLongBag;
import com.gs.collections.api.bag.primitive.MutableShortBag;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.primitive.BooleanFunction;
import com.gs.collections.api.block.function.primitive.ByteFunction;
import com.gs.collections.api.block.function.primitive.CharFunction;
import com.gs.collections.api.block.function.primitive.DoubleFunction;
import com.gs.collections.api.block.function.primitive.FloatFunction;
import com.gs.collections.api.block.function.primitive.IntFunction;
import com.gs.collections.api.block.function.primitive.LongFunction;
import com.gs.collections.api.block.function.primitive.ShortFunction;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.predicate.primitive.IntPredicate;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.map.MapIterable;
import com.gs.collections.api.multimap.bag.MutableBagMultimap;
import com.gs.collections.api.partition.bag.PartitionMutableBag;
import com.gs.collections.api.set.MutableSet;
import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.collection.mutable.AbstractUnmodifiableMutableCollection;
import com.gs.collections.impl.factory.Bags;

/**
 * An unmodifiable view of a bag.
 *
 * @see MutableBag#asUnmodifiable()
 * @since 1.0
 */
public class UnmodifiableBag<T>
        extends AbstractUnmodifiableMutableCollection<T>
        implements MutableBag<T>, Serializable
{
    UnmodifiableBag(MutableBag<? extends T> mutableBag)
    {
        super(mutableBag);
    }

    /**
     * This method will take a MutableBag and wrap it directly in a UnmodifiableBag.
     */
    public static <E, B extends MutableBag<E>> UnmodifiableBag<E> of(B bag)
    {
        if (bag == null)
        {
            throw new IllegalArgumentException("cannot create an UnmodifiableBag for null");
        }
        return new UnmodifiableBag<E>(bag);
    }

    protected MutableBag<T> getMutableBag()
    {
        return (MutableBag<T>) this.getMutableCollection();
    }

    @Override
    public MutableBag<T> asUnmodifiable()
    {
        return this;
    }

    @Override
    public MutableBag<T> asSynchronized()
    {
        return SynchronizedBag.of(this);
    }

    @Override
    public ImmutableBag<T> toImmutable()
    {
        return Bags.immutable.ofAll(this);
    }

    @Override
    public boolean equals(Object obj)
    {
        return this.getMutableBag().equals(obj);
    }

    @Override
    public int hashCode()
    {
        return this.getMutableBag().hashCode();
    }

    public String toStringOfItemToCount()
    {
        return this.getMutableBag().toStringOfItemToCount();
    }

    @Override
    public MutableBag<T> newEmpty()
    {
        return this.getMutableBag().newEmpty();
    }

    public MutableBag<T> selectByOccurrences(IntPredicate predicate)
    {
        return this.getMutableBag().selectByOccurrences(predicate);
    }

    @Override
    public MutableBag<T> select(Predicate<? super T> predicate)
    {
        return this.getMutableBag().select(predicate);
    }

    @Override
    public <P> MutableBag<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getMutableBag().selectWith(predicate, parameter);
    }

    @Override
    public MutableBag<T> reject(Predicate<? super T> predicate)
    {
        return this.getMutableBag().reject(predicate);
    }

    @Override
    public <P> MutableBag<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getMutableBag().rejectWith(predicate, parameter);
    }

    @Override
    public PartitionMutableBag<T> partition(Predicate<? super T> predicate)
    {
        return this.getMutableBag().partition(predicate);
    }

    @Override
    public <P> PartitionMutableBag<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getMutableBag().partitionWith(predicate, parameter);
    }

    @Override
    public <S> MutableBag<S> selectInstancesOf(Class<S> clazz)
    {
        return this.getMutableBag().selectInstancesOf(clazz);
    }

    @Override
    public <V> MutableBag<V> collect(Function<? super T, ? extends V> function)
    {
        return this.getMutableBag().collect(function);
    }

    @Override
    public MutableBooleanBag collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        return this.getMutableBag().collectBoolean(booleanFunction);
    }

    @Override
    public MutableByteBag collectByte(ByteFunction<? super T> byteFunction)
    {
        return this.getMutableBag().collectByte(byteFunction);
    }

    @Override
    public MutableCharBag collectChar(CharFunction<? super T> charFunction)
    {
        return this.getMutableBag().collectChar(charFunction);
    }

    @Override
    public MutableDoubleBag collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        return this.getMutableBag().collectDouble(doubleFunction);
    }

    @Override
    public MutableFloatBag collectFloat(FloatFunction<? super T> floatFunction)
    {
        return this.getMutableBag().collectFloat(floatFunction);
    }

    @Override
    public MutableIntBag collectInt(IntFunction<? super T> intFunction)
    {
        return this.getMutableBag().collectInt(intFunction);
    }

    @Override
    public MutableLongBag collectLong(LongFunction<? super T> longFunction)
    {
        return this.getMutableBag().collectLong(longFunction);
    }

    @Override
    public MutableShortBag collectShort(ShortFunction<? super T> shortFunction)
    {
        return this.getMutableBag().collectShort(shortFunction);
    }

    @Override
    public <V> MutableBag<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.getMutableBag().flatCollect(function);
    }

    @Override
    public <P, A> MutableBag<A> collectWith(Function2<? super T, ? super P, ? extends A> function, P parameter)
    {
        return this.getMutableBag().collectWith(function, parameter);
    }

    @Override
    public <V> MutableBag<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return this.getMutableBag().collectIf(predicate, function);
    }

    @Override
    public <V> MutableBagMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.getMutableBag().groupBy(function);
    }

    @Override
    public <V> MutableBagMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.getMutableBag().groupByEach(function);
    }

    public void addOccurrences(T item, int occurrences)
    {
        throw new UnsupportedOperationException("Cannot call addOccurrences() on " + this.getClass().getSimpleName());
    }

    public boolean removeOccurrences(Object item, int occurrences)
    {
        throw new UnsupportedOperationException("Cannot call removeOccurrences() on " + this.getClass().getSimpleName());
    }

    public boolean setOccurrences(T item, int occurrences)
    {
        throw new UnsupportedOperationException("Cannot call setOccurrences() on " + this.getClass().getSimpleName());
    }

    public int sizeDistinct()
    {
        return this.getMutableBag().sizeDistinct();
    }

    public int occurrencesOf(Object item)
    {
        return this.getMutableBag().occurrencesOf(item);
    }

    public void forEachWithOccurrences(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        this.getMutableBag().forEachWithOccurrences(objectIntProcedure);
    }

    public MapIterable<T, Integer> toMapOfItemToCount()
    {
        return this.getMutableBag().toMapOfItemToCount();
    }

    @Override
    public <S> MutableBag<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.getMutableBag().zip(that);
    }

    @Override
    public MutableSet<Pair<T, Integer>> zipWithIndex()
    {
        return this.getMutableBag().zipWithIndex();
    }

    @Override
    public MutableBag<T> with(T element)
    {
        throw new UnsupportedOperationException("Cannot call with() on " + this.getClass().getSimpleName());
    }

    @Override
    public MutableBag<T> without(T element)
    {
        throw new UnsupportedOperationException("Cannot call without() on " + this.getClass().getSimpleName());
    }

    @Override
    public MutableBag<T> withAll(Iterable<? extends T> elements)
    {
        throw new UnsupportedOperationException("Cannot call withAll() on " + this.getClass().getSimpleName());
    }

    @Override
    public MutableBag<T> withoutAll(Iterable<? extends T> elements)
    {
        throw new UnsupportedOperationException("Cannot call withoutAll() on " + this.getClass().getSimpleName());
    }

    protected Object writeReplace()
    {
        return new UnmodifiableBagSerializationProxy<T>(this.getMutableBag());
    }

    private static class UnmodifiableBagSerializationProxy<T> implements Externalizable
    {
        private static final long serialVersionUID = 1L;

        private MutableBag<T> mutableBag;

        @SuppressWarnings("UnusedDeclaration")
        public UnmodifiableBagSerializationProxy()
        {
            // Empty constructor for Externalizable class
        }

        private UnmodifiableBagSerializationProxy(MutableBag<T> bag)
        {
            this.mutableBag = bag;
        }

        public void writeExternal(ObjectOutput out) throws IOException
        {
            try
            {
                out.writeObject(this.mutableBag);
            }
            catch (RuntimeException e)
            {
                if (e.getCause() instanceof IOException)
                {
                    throw (IOException) e.getCause();
                }
                throw e;
            }
        }

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
        {
            this.mutableBag = (MutableBag<T>) in.readObject();
        }

        protected Object readResolve()
        {
            return this.mutableBag.asUnmodifiable();
        }
    }
}
