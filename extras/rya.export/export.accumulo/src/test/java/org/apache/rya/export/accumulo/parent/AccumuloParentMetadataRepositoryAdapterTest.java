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
package org.apache.rya.export.accumulo.parent;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.apache.rya.export.InstanceType;
import org.apache.rya.export.accumulo.util.AccumuloInstanceDriver;
import org.apache.rya.export.api.metadata.MergeParentMetadata;
import org.apache.rya.export.api.metadata.ParentMetadataDoesNotExistException;
import org.apache.rya.export.api.metadata.ParentMetadataExistsException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the methods of {@link AccumuloParentMetadataRepository}.
 *
 */
public class AccumuloParentMetadataRepositoryAdapterTest {
    private final static String TEST_INSTANCE = "test_instance";
    private final static Date TEST_TIMESTAMP = new Date(8675309L);
    private final static Date TEST_FILTER_TIMESTAMP = new Date(1234567L);
    private final static long TEST_TIME_OFFSET = 123L;

    private static final InstanceType INSTANCE_TYPE = InstanceType.MOCK;

    private static final boolean IS_MOCK = INSTANCE_TYPE == InstanceType.MOCK;
    private static final String USER_NAME = IS_MOCK ? "test_user" : AccumuloInstanceDriver.ROOT_USER_NAME;
    private static final String PASSWORD = "password";
    private static final String INSTANCE_NAME = "test_instance";
    private static final String AUTHS = "test_auth";
    private static final String RYA_TABLE_PREFIX = "test_";
    private static final String ZOOKEEPERS = "localhost";

    private static AccumuloInstanceDriver accumuloInstanceDriver;
    private static AccumuloParentMetadataRepository accumuloParentMetadataRepository;

    @BeforeClass
    public static void setUp() throws Exception {
        accumuloInstanceDriver = new AccumuloInstanceDriver("Test Repository", INSTANCE_TYPE, false, false, true, USER_NAME, PASSWORD, INSTANCE_NAME, RYA_TABLE_PREFIX, AUTHS, ZOOKEEPERS);
        accumuloInstanceDriver.setUp();

        accumuloParentMetadataRepository = new AccumuloParentMetadataRepository(accumuloInstanceDriver.getDao());
    }

    @AfterClass
    public static void tearDownPerClass() throws Exception {
        accumuloInstanceDriver.tearDown();
    }

    @Test
    public void setAndGetTest() throws ParentMetadataExistsException, ParentMetadataDoesNotExistException {
        final MergeParentMetadata mergeParentMetadata = new MergeParentMetadata.Builder()
            .setRyaInstanceName(TEST_INSTANCE)
            .setTimestamp(TEST_TIMESTAMP)
            .setFilterTimestmap(TEST_FILTER_TIMESTAMP)
            .setParentTimeOffset(TEST_TIME_OFFSET)
            .build();

        accumuloParentMetadataRepository.set(mergeParentMetadata);

        final MergeParentMetadata actual = accumuloParentMetadataRepository.get();
        assertEquals(mergeParentMetadata, actual);
    }
}

