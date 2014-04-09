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

import java.util.HashMap;
import java.util.Map;

import org.dbunit.dataset.AbstractDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.ReplacementTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mostly a copy of {@link ReplacementDataSet} to use {@link DynamicReplacementTable}
 * 
 * @author <a href="mailto:latzer.daniel@gmail.com">Daniel Latzer</a>
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DynamicReplacementDataSet extends AbstractDataSet {

    private static final Logger logger = LoggerFactory.getLogger(DynamicReplacementDataSet.class);

    private final IDataSet _dataSet;

    private final Map _objectMap;

    private final Map _substringMap;

    private String _startDelim;

    private String _endDelim;

    private boolean _strictReplacement;

    public DynamicReplacementDataSet(IDataSet dataSet) {
        _dataSet = dataSet;
        _objectMap = new HashMap();
        _substringMap = new HashMap();
    }

    public DynamicReplacementDataSet(IDataSet dataSet, Map objectMap, Map substringMap) {
        _dataSet = dataSet;
        _objectMap = objectMap == null ? new HashMap() : objectMap;
        _substringMap = substringMap == null ? new HashMap() : substringMap;
    }

    public void setStrictReplacement(boolean strictReplacement) {
        this._strictReplacement = strictReplacement;
    }

    public void addReplacementObject(Object originalObject, Object replacementObject) {
        logger.debug("addReplacementObject(originalObject={}, replacementObject={}) - start", originalObject, replacementObject);

        _objectMap.put(originalObject, replacementObject);
    }

    public void addReplacementSubstring(String originalSubstring, String replacementSubstring) {
        logger.debug("addReplacementSubstring(originalSubstring={}, replacementSubstring={}) - start", originalSubstring, replacementSubstring);

        if (originalSubstring == null || replacementSubstring == null) {
            throw new NullPointerException();
        }

        _substringMap.put(originalSubstring, replacementSubstring);
    }

    public void setSubstringDelimiters(String startDelimiter, String endDelimiter) {
        logger.debug("setSubstringDelimiters(startDelimiter={}, endDelimiter={}) - start", startDelimiter, endDelimiter);

        if (startDelimiter == null || endDelimiter == null) {
            throw new NullPointerException();
        }

        _startDelim = startDelimiter;
        _endDelim = endDelimiter;
    }

    private ReplacementTable createReplacementTable(ITable table) {
        logger.debug("createReplacementTable(table={}) - start", table);

        ReplacementTable replacementTable = new DynamicReplacementTable(table, _objectMap, _substringMap, _startDelim, _endDelim);
        replacementTable.setStrictReplacement(_strictReplacement);
        return replacementTable;
    }

    // //////////////////////////////////////////////////////////////////////////
    // AbstractDataSet class

    protected ITableIterator createIterator(boolean reversed) throws DataSetException {
        if (logger.isDebugEnabled())
            logger.debug("createIterator(reversed={}) - start", String.valueOf(reversed));

        return new ReplacementIterator(reversed ? _dataSet.reverseIterator() : _dataSet.iterator());
    }

    // //////////////////////////////////////////////////////////////////////////
    // IDataSet interface

    public String[] getTableNames() throws DataSetException {
        logger.debug("getTableNames() - start");

        return _dataSet.getTableNames();
    }

    public ITableMetaData getTableMetaData(String tableName) throws DataSetException {
        logger.debug("getTableMetaData(tableName={}) - start", tableName);

        return _dataSet.getTableMetaData(tableName);
    }

    public ITable getTable(String tableName) throws DataSetException {
        logger.debug("getTable(tableName={}) - start", tableName);

        return createReplacementTable(_dataSet.getTable(tableName));
    }

    // //////////////////////////////////////////////////////////////////////////
    // ReplacementIterator class

    private class ReplacementIterator implements ITableIterator {

        /**
         * Logger for this class
         */
        private final Logger logger = LoggerFactory.getLogger(ReplacementIterator.class);

        private final ITableIterator _iterator;

        public ReplacementIterator(ITableIterator iterator) {
            _iterator = iterator;
        }

        // //////////////////////////////////////////////////////////////////////
        // ITableIterator interface

        public boolean next() throws DataSetException {
            logger.debug("next() - start");

            return _iterator.next();
        }

        public ITableMetaData getTableMetaData() throws DataSetException {
            logger.debug("getTableMetaData() - start");

            return _iterator.getTableMetaData();
        }

        public ITable getTable() throws DataSetException {
            logger.debug("getTable() - start");

            return createReplacementTable(_iterator.getTable());
        }
    }

}
