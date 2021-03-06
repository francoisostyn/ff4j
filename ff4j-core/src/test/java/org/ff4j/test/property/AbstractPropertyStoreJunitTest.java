package org.ff4j.test.property;

import java.util.Date;

/*
 * #%L
 * ff4j-test
 * %%
 * Copyright (C) 2013 - 2015 Ff4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.ff4j.core.FeatureStore;
import org.ff4j.exception.PropertyAlreadyExistException;
import org.ff4j.exception.PropertyNotFoundException;
import org.ff4j.property.AbstractProperty;
import org.ff4j.property.Property;
import org.ff4j.property.PropertyDate;
import org.ff4j.property.PropertyLogLevel;
import org.ff4j.property.PropertyLogLevel.LogLevel;
import org.ff4j.property.store.PropertyStore;
import org.ff4j.store.InMemoryFeatureStore;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * SuperClass to test stores within core project
 *
 * @author <a href="mailto:cedrick.lunven@gmail.com">Cedrick LUNVEN</a>
 */
public abstract class AbstractPropertyStoreJunitTest {

    /** Tested Store. */
    protected PropertyStore testedStore;

    /** Default InMemoryStore for test purposes. */
    protected FeatureStore defaultStore = new InMemoryFeatureStore();
    
    /** {@inheritDoc} */
    @Before
    public void setUp() throws Exception {
        testedStore = initPropertyStore();
    }

    /**
     * Any store test will declare its store through this callback.
     * 
     * @return working feature store
     * @throws Exception
     *             error during building feature store
     */
    protected abstract PropertyStore initPropertyStore();
    
    
    // --------------- exist -----------
    
    /** TDD. */
    @Test(expected = IllegalArgumentException.class)
    public void existKO_Null() {
        // given
        testedStore.exist(null);
        // then expect to fail
    }
    
    /** TDD. */
    @Test(expected = IllegalArgumentException.class)
    public void existKO_Empty() {
        // Given
        testedStore.exist("");
        // Then
        // then expect to fail
    }
    
    /** TDD. */
    @Test
    public void exist_false() {
        // When-Then
        Assert.assertFalse(testedStore.exist("toto"));
    }
    
    // --------------- create -----------    
    
    /** TDD. */
    @Test
    public void addPropertyOK_simple() {
        // Given
        Assert.assertFalse(testedStore.exist("toto"));
        // When
        testedStore.create(new Property("toto", "ff4j"));
        // Then
        Assert.assertTrue(testedStore.exist("toto"));
    }
    
    /** TDD. */
    @Test
    public void addPropertyOK_LogLevel() {
        // Given
        //Assert.assertFalse(testedStore.exist("log"));
        // When
        testedStore.create(new PropertyLogLevel("log", LogLevel.DEBUG));
        // Then
        Assert.assertTrue(testedStore.exist("log"));
    }
    
    /** TDD. */
    @Test
    public void addPropertyOK_Date() {
        // Given
        //Assert.assertFalse(testedStore.exist("log"));
        // When
        testedStore.create(new PropertyDate("ddate", new Date()));
        // Then
        Assert.assertTrue(testedStore.exist("ddate"));
    }
    
    /** TDD. */
    @Test(expected = PropertyAlreadyExistException.class)
    public void addPropertyKO_AlreadyExist() {
        // Given
        testedStore.create(new PropertyLogLevel("log", LogLevel.DEBUG));
        Assert.assertTrue(testedStore.exist("log"));
        // When
        testedStore.create(new PropertyLogLevel("log", LogLevel.DEBUG));
        // Then expect to fail
    }
    
    /** TDD. */
    @Test(expected = IllegalArgumentException.class)
    public void addPropertyKO_Null() {
        // Given
        testedStore.create(null);
        // Then expect to fail
    }
    
    /** TDD. */
    @Test(expected = IllegalArgumentException.class)
    public void addPropertyKO_NullName() {
        // Given
        testedStore.create(new Property(null, ""));
        // Then expect to fail
    }
    
    /** TDD. */
    @Test(expected = IllegalArgumentException.class)
    public void addPropertyKO_EmptyName() {
        // Given
        testedStore.create(new Property("", ""));
        // Then expect to fail
    }
    
    /** TDD. */
    @Test(expected = IllegalArgumentException.class)
    public void addPropertyKO_NullValue() {
        // Given
        testedStore.create(new Property("hi", null));
        // Then No error
    }
    
    /** TDD. */
    @Test(expected = IllegalArgumentException.class)
    public void addPropertyKO_InvalidValue() {
        // Given
        testedStore.create(new PropertyLogLevel("log", "TRUC"));
        // Then No error
    }
    
    
    // ------------------ read --------------------
    
    @Test
    public void readOK() {
        // Given
        testedStore.create(new Property("toto", "ff4j"));
        // When
        AbstractProperty<?> ap = testedStore.read("toto");
        // Then
        Assert.assertNotNull(ap);
        Assert.assertNotNull(ap.getName());
        Assert.assertEquals("toto", ap.getName());
        Assert.assertEquals("ff4j", ap.getValue());
        Assert.assertEquals("ff4j", ap.asString());
        Assert.assertNull(ap.getFixedValues());
        
    }
    
    @Test
    public void readOKFixed() {
        // Given
        testedStore.create(new PropertyLogLevel("log", LogLevel.ERROR));
        // When
        AbstractProperty<?> log = testedStore.read("log");
        // Then
        Assert.assertNotNull(log);
        Assert.assertNotNull(log.getName());
        Assert.assertEquals("log", log.getName());
        Assert.assertEquals(LogLevel.ERROR, log.getValue());
        Assert.assertEquals("ERROR", log.asString());
        Assert.assertNotNull(log.getFixedValues());
    }
    
    /** TDD. */
    @Test(expected = IllegalArgumentException.class)
    public void readKO_null() {
        // Given
        testedStore.read(null);
    }
    
    /** TDD. */
    @Test(expected = IllegalArgumentException.class)
    public void readKO_empty() {
        // Given
        testedStore.read("");
        // Expected error
        Assert.fail();
    }
    
    /** TDD. */
    @Test(expected = PropertyNotFoundException.class)
    public void readKO_notExist() {
        // Given
        Assert.assertFalse(testedStore.exist("invalid"));
        // When
        testedStore.read("invalid");
        // Expected error
        Assert.fail();
    }
    
    // ------------------ update --------------------
    
    /** TDD. */
    @Test(expected = PropertyNotFoundException.class)
    public void updateKO_doesnotExist() {
        // Given
        Assert.assertFalse(testedStore.exist("invalid"));
        // When
        testedStore.update("invalid", "aa");
        // Expected error
        Assert.fail();
    }
    
    /** TDD. */
    @Test(expected = IllegalArgumentException.class)
    public void updateKO_null() {
        // When
        testedStore.update(null, "aa");
        // Expected error
        Assert.fail();
    }
    
    /** TDD. */
    @Test(expected = IllegalArgumentException.class)
    public void updateKO_empty() {
        // When
        testedStore.update("", "aa");
        // Expected error
        Assert.fail();
    }
    
    /** TDD. */
    @Test(expected = IllegalArgumentException.class)
    public void updateKO_InvalidValue() {
        // Given
        testedStore.create(new PropertyLogLevel("log", LogLevel.ERROR));
        // When
        testedStore.update("log", "KO");
        // Expected error
        Assert.fail();
        
    }
    
    /** TDD. */
    @Test
    public void updateOK() {
        // Given
        testedStore.create(new PropertyLogLevel("log", LogLevel.ERROR));
        // When
        testedStore.update("log", "INFO");
        // Then
        Assert.assertEquals(LogLevel.INFO, testedStore.read("log").getValue());
    }
    
    // ------------------ delete -------------------- 

    /** TDD. */
    @Test(expected = IllegalArgumentException.class)
    public void deleteKO_null() {
        // When
        testedStore.delete(null);
        // Expected Error
        Assert.fail();
    }
    
    /** TDD. */
    @Test(expected = IllegalArgumentException.class)
    public void deleteKO_empty() {
        // When
        testedStore.delete("");
        // Expected Error
        Assert.fail();
    }
    
    /** TDD. */
    @Test(expected = PropertyNotFoundException.class)
    public void deleteKO_doesnotexist() {
        // Given
        Assert.assertFalse(testedStore.exist("invalid"));
        // When
        testedStore.delete("invalid");
        // Expected Error
        Assert.fail();
    }
    
    /** TDD. */
    @Test
    public void deleteOK() {
        // Given
        testedStore.create(new Property("toto", "ff4j"));
        Assert.assertTrue(testedStore.exist("toto"));
        // When
        testedStore.delete("toto");
        // Then
        Assert.assertFalse(testedStore.exist("toto"));
    }
    
}
