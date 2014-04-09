/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.persistence.dbunit.dataset.xml.replacer;

import java.util.Collection;
import java.util.Map;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementTable;
import org.jboss.arquillian.persistence.spi.dbunit.replacer.ReplacementProvider;
import org.jboss.arquillian.persistence.util.JavaSPIExtensionLoader;

/**
 * Extends {@link ReplacementTable} with the functionality to use {@link ReplacementProvider}s. Replacements added via
 * {@link #addReplacementObject(Object, Object)} have precedence over any {@link ReplacementProvider}
 * 
 * @author <a href="mailto:latzer.daniel@gmail.com">Daniel Latzer</a>
 * 
 */
public class DynamicReplacementTable extends ReplacementTable {

    /**
     * A {@link ReplacementTable} with support for {@link ReplacementProvider}s
     * 
     */
    @SuppressWarnings("rawtypes")
    public DynamicReplacementTable(ITable table, Map objectMap, Map substringMap, String startDelimiter, String endDelimiter) {
        super(table, objectMap, substringMap, startDelimiter, endDelimiter);
    }

    @Override
    public Object getValue(int row, String column) throws DataSetException {
        Object value = super.getValue(row, column);
        if (value instanceof String) {
            for (ReplacementProvider replacer : getReplacers()) {
                if (replacer.match((String) value)) {
                    return replacer.getReplacement();
                }
            }
        }
        return value;
    }
    
    Collection<ReplacementProvider> getReplacers(){
       return new JavaSPIExtensionLoader().all(getClass().getClassLoader(), ReplacementProvider.class);
    }
    
}
