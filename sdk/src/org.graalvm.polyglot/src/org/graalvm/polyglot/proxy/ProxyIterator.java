/*
 * Copyright (c) 2020, 2021, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.graalvm.polyglot.proxy;

import org.graalvm.polyglot.Value;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Interface to be implemented to mimic guest language iterators.
 *
 * @see Proxy
 * @see ProxyIterable
 * @since 21.1
 */
public interface ProxyIterator extends Proxy {

    /**
     * Returns <code>true</code> if the iterator has more elements, else <code>false</code>.
     * Multiple calls to the {@link #hasNext()} might lead to different results if the underlying
     * data structure is modified.
     *
     * @see #getNext()
     * @since 21.1
     */
    boolean hasNext();

    /**
     * Returns the next element in the iteration. When the underlying data structure is modified the
     * {@link #getNext()} may throw the {@link NoSuchElementException} even when the
     * {@link #hasNext()} returned {@code true}.
     *
     * @throws NoSuchElementException if the iteration has no more elements, the {@link #hasNext()}
     *             returns <code>false</code>.
     * @throws UnsupportedOperationException when the underlying iterator element exists but is not
     *             readable.
     *
     * @see #hasNext()
     * @since 21.1
     */
    Object getNext() throws NoSuchElementException, UnsupportedOperationException;

    /**
     * Creates a proxy iterator backed by a Java {@link Iterator}. If the set values are host values
     * then they will be {@link Value#asHostObject() unboxed}. If the Java {@link Iterator} throws
     * the {@link ConcurrentModificationException} the exception is translated into the host
     * exception.
     *
     * @since 21.1
     */
    static ProxyIterator from(Iterator<Object> iterator) {
        Objects.requireNonNull(iterator, "Iterator must be non null.");
        return new DefaultProxyIterator(iterator);
    }
}

final class DefaultProxyIterator implements ProxyIterator {

    private final Iterator<Object> iterator;

    DefaultProxyIterator(Iterator<Object> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Object getNext() {
        return iterator.next();
    }
}
