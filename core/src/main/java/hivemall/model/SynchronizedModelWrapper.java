/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package hivemall.model;

import hivemall.utils.collections.IMapIterator;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Nonnull;

public final class SynchronizedModelWrapper implements PredictionModel {

    private final PredictionModel model;
    private final Lock lock;

    public SynchronizedModelWrapper(PredictionModel model) {
        this.model = model;
        this.lock = new ReentrantLock();
    }

    // ------------------------------------------------------------
    // Non-synchronized methods with care

    public PredictionModel getModel() {
        return model;
    }

    @Override
    public ModelUpdateHandler getUpdateHandler() {
        return model.getUpdateHandler();
    }

    @Override
    public void configureMix(ModelUpdateHandler handler, boolean cancelMixRequest) {
        model.configureMix(handler, cancelMixRequest);
    }

    @Override
    public long getNumMixed() {
        return model.getNumMixed();
    }

    @Override
    public boolean hasCovariance() {
        return model.hasCovariance();
    }

    @Override
    public void configureParams(boolean sum_of_squared_gradients, boolean sum_of_squared_delta_x,
            boolean sum_of_gradients) {
        model.configureParams(sum_of_squared_gradients, sum_of_squared_delta_x, sum_of_gradients);
    }

    @Override
    public void configureClock() {
        model.configureClock();
    }

    @Override
    public boolean hasClock() {
        return model.hasClock();
    }

    @Override
    public <K, V extends IWeightValue> IMapIterator<K, V> entries() {
        return model.entries();
    }

    // ------------------------------------------------------------
    // The below is synchronized methods

    @Override
    public void resetDeltaUpdates(int feature) {
        try {
            lock.lock();
            model.resetDeltaUpdates(feature);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        try {
            lock.lock();
            return model.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean contains(@Nonnull final Object feature) {
        try {
            lock.lock();
            return model.contains(feature);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <T extends IWeightValue> T get(@Nonnull final Object feature) {
        try {
            lock.lock();
            return model.get(feature);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <T extends IWeightValue> void set(@Nonnull final Object feature,
            @Nonnull final T value) {
        try {
            lock.lock();
            model.set(feature, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void delete(@Nonnull final Object feature) {
        try {
            lock.lock();
            model.delete(feature);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public float getWeight(@Nonnull final Object feature) {
        try {
            lock.lock();
            return model.getWeight(feature);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void setWeight(@Nonnull final Object feature, final float value) {
        try {
            lock.lock();
            model.setWeight(feature, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public float getCovariance(@Nonnull final Object feature) {
        try {
            lock.lock();
            return model.getCovariance(feature);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void set(@Nonnull final Object feature, final float weight, final float covar,
            final short clock) {
        try {
            lock.lock();
            model.set(feature, weight, covar, clock);
        } finally {
            lock.unlock();
        }
    }

}
