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
package hivemall.sketch.bloom;

import java.io.IOException;
import java.util.Random;

import javax.annotation.Nonnull;

import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.bloom.DynamicBloomFilter;
import org.apache.hadoop.util.bloom.Key;
import org.junit.Assert;
import org.junit.Test;

public class BloomContainsUDFTest {

    @Test
    public void test() throws IOException, HiveException {
        BloomContainsUDF udf = new BloomContainsUDF();
        final long seed = 43L;
        final int size = 100;

        DynamicBloomFilter dbf = createBloomFilter(seed, size);
        Text bfstr = BloomFilterUtils.serialize(dbf, new Text());

        final Text key = new Text();
        final Random rnd1 = new Random(seed);
        for (int i = 0; i < size; i++) {
            double d = rnd1.nextGaussian();
            String s = Double.toHexString(d);
            key.set(s);
            Assert.assertEquals(Boolean.TRUE, udf.evaluate(bfstr, key));
        }
    }

    @Nonnull
    private static DynamicBloomFilter createBloomFilter(long seed, int size) {
        DynamicBloomFilter dbf = BloomFilterUtils.newDynamicBloomFilter(30);
        final Key key = new Key();

        final Random rnd1 = new Random(seed);
        for (int i = 0; i < size; i++) {
            double d = rnd1.nextGaussian();
            String s = Double.toHexString(d);
            Text t = new Text(s);
            key.set(t.getBytes(), 1.0);
            dbf.add(key);
        }

        return dbf;
    }

}
