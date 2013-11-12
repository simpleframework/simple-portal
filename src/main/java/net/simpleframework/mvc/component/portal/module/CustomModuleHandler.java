package net.simpleframework.mvc.component.portal.module;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.web.html.HtmlUtils;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.TextForward;
import net.simpleframework.mvc.UrlForward;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class CustomModuleHandler extends AbstractPortalModuleHandler {

	private static String[] defaultOptions = new String[] { "_custom_c=" };

	@Override
	protected String[] getDefaultOptions() {
		return defaultOptions;
	}

	@Override
	public IForward getPageletOptionContent(final ComponentParameter cp) {
		return new UrlForward(getModuleHomeUrl() + "/custom_option.jsp");
	}

	@Override
	public IForward getPageletContent(final ComponentParameter cp) {
		final String url = getPagelet().getOptionProperty("_custom_url");
		if (StringUtils.hasText(url)) {
			return new UrlForward(url);
		} else {
			String input = getPagelet().getOptionProperty("_custom_c");
			if (!HtmlUtils.hasTag(input)) {
				input = HtmlUtils.convertHtmlLines(input);
			}
			return StringUtils.hasText(input) ? new TextForward(input) : null;
		}
	}

	@Override
	public OptionWindowUI getPageletOptionUI(final ComponentParameter cp) {
		return new OptionWindowUI(getPagelet()) {

			@Override
			public int getHeight() {
				return 260;
			}
		};
	}
}
