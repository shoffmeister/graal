/*
 * Copyright (c) 2020, 2021, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.oracle.truffle.espresso.substitutions;

import com.oracle.truffle.espresso.ffi.NativeSignature;
import com.oracle.truffle.espresso.ffi.NativeType;

public abstract class CallableFromNative extends SubstitutionProfiler {

    public abstract static class Factory {
        public abstract CallableFromNative create(Meta meta);

        private final String methodName;
        private final NativeSignature nativeSignature;
        private final int parameterCount;
        private final boolean prependEnv;

        protected Factory(String methodName, NativeSignature nativeSignature, int parameterCount, boolean prependEnv) {
            this.methodName = methodName;
            this.nativeSignature = nativeSignature;
            this.parameterCount = parameterCount;
            this.prependEnv = prependEnv;
        }

        public String methodName() {
            return methodName;
        }

        public NativeSignature jniNativeSignature() {
            return nativeSignature;
        }

        public int parameterCount() {
            return parameterCount;
        }

        public NativeType returnType() {
            return nativeSignature.getReturnType();
        }

        public boolean prependEnv() {
            return prependEnv;
        }
    }

    public abstract Object invoke(Object env, Object[] args);
}
