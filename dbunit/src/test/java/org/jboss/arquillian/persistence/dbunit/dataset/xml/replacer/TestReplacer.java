package org.jboss.arquillian.persistence.dbunit.dataset.xml.replacer;

import java.util.regex.Pattern;

import org.jboss.arquillian.persistence.spi.dbunit.replacer.ReplacementProvider;

public class TestReplacer extends ReplacementProvider {
	
	private static final Pattern pattern = Pattern.compile("\\[test\\]");

	@Override
	public Object getReplacement() {
		return "arquillian";
	}

	@Override
	protected Pattern getPattern() {
		return pattern;
	}

}
