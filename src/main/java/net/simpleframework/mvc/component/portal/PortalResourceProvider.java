package net.simpleframework.mvc.component.portal;

import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.IComponentResourceProvider.AbstractComponentResourceProvider;
import net.simpleframework.mvc.impl.DefaultPageResourceProvider;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PortalResourceProvider extends AbstractComponentResourceProvider {

	@Override
	public String[] getJavascriptPath(final PageParameter pp) {
		final String rPath = getResourceHomePath(DefaultPageResourceProvider.class);
		return new String[] { rPath + DefaultPageResourceProvider.EFFECTS_FILE,
				rPath + DefaultPageResourceProvider.DRAGDROP_FILE,
				getResourceHomePath() + "/js/portal.js" };
	}

	@Override
	public String[] getCssPath(final PageParameter pp) {
		return new String[] { getCssResourceHomePath(pp) + "/portal.css" };
	}
}
