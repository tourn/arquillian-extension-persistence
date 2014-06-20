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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.arquillian.persistence.spi.dbunit.replacer.ReplacementProvider;
import org.reflections.Reflections;

public class Replacers {

    private static final Logger log = Logger.getLogger(Replacers.class.getName());

    private List<ReplacementProvider> replacers = new ArrayList<ReplacementProvider>();

    private Replacers() {
        initializeReplacers();
    }

    private static Replacers instance = null;

    public static Replacers getInstance() {
        if (instance == null) {
            instance = new Replacers();
        }
        return instance;
    }

    private void initializeReplacers() {
        log.info("Initializing replacers");
        Reflections replacerPackage = new Reflections();
        Set<Class<? extends ReplacementProvider>> replacerClasses = replacerPackage.getSubTypesOf(ReplacementProvider.class);
        for (Class<? extends ReplacementProvider> replacerClass : replacerClasses) {
            try {
                replacers.add(replacerClass.newInstance());
                log.info("Registered replacer " + replacerClass.getName());
            } catch (ReflectiveOperationException e) {
                log.log(Level.WARNING, "Failed instantiating replacer for " + replacerClass.getName(), e);
            }
        }
    }

    public List<ReplacementProvider> getReplacers() {
        return replacers;
    }

}
