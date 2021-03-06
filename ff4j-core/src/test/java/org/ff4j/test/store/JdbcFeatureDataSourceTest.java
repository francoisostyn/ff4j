package org.ff4j.test.store;

/*
 * #%L ff4j-core %% Copyright (C) 2013 Ff4J %% Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License. #L%
 */

import javax.sql.DataSource;

import org.ff4j.core.FeatureStore;
import org.ff4j.store.JdbcFeatureStore;
import org.ff4j.test.utils.JdbcTestHelper;
import org.junit.Before;

/**
 * This test is meant to access a Jfeature store in 'pure' JDBC.
 *
 * @author <a href="mailto:cedrick.lunven@gmail.com">Cedrick LUNVEN</a>
 */
public class JdbcFeatureDataSourceTest extends AbstractStoreTest {

    /** SQL DataSource. */
    private DataSource sqlDataSource;
    
    /** Should reinit tables on each test , except first */
    private static boolean dropTable = false;
  
    /** {@inheritDoc} */
    @Override
    protected FeatureStore initStore() {
    	sqlDataSource = JdbcTestHelper.createInMemoryHQLDataSource();
        return new JdbcFeatureStore(sqlDataSource);
    }
    
    /** {@inheritDoc} */
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        JdbcTestHelper.initDBSchema(sqlDataSource, dropTable);
        dropTable = true;
    }

}
