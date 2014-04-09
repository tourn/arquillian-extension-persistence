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

import static org.junit.Assert.assertEquals;

import java.io.InputStream;

import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Test;

public class ReplacerTest {

    @Test
    public void testDynamicReplacementDataSet() throws Exception {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        builder.setCaseSensitiveTableNames(true);
        InputStream xml = getClass().getClassLoader().getResourceAsStream("datasets/xml/replacer.xml");
        FlatXmlDataSet dataSet = builder.build(xml);
        DynamicReplacementDataSet replacementSet = new DynamicReplacementDataSet(dataSet);
        String name = (String) replacementSet.getTable("useraccount").getValue(0, "firstname");
        assertEquals("arquillian", name);
    }

}
