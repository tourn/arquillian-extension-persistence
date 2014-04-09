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
package org.jboss.arquillian.persistence.spi.dbunit.replacer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Any subclass of this automatically registers with Arquillian. If the pattern returned by {@link #getPattern()} matches any
 * attribute in an XML dataset parsed by the Arquillian Persistence Extension, the whole match is replaced by the value returned
 * by {@link #getReplacement()}
 * 
 * @author <a href="mailto:latzer.daniel@gmail.com">Daniel Latzer</a>
 * 
 */
public abstract class ReplacementProvider {

    private Matcher matcher;

    /**
     * @return the matcher containing the match data
     */
    protected Matcher getMatcher() {
        return matcher;
    }

    /**
     * @param value the candidate for substitution
     * @return whether the value matches the pattern
     */
    public boolean match(String value) {
        matcher = getPattern().matcher(value);
        return matcher.matches();
    }

    /**
     * @return the pattern used to match this replacer
     */
    protected abstract Pattern getPattern();

    /**
     * Generate the replacement object for the previously matched value. Use {@link #getMatcher()} to retrieve match data for
     * processing.
     * 
     * @return the substitution for the previously matched value
     */
    public abstract Object getReplacement();
}