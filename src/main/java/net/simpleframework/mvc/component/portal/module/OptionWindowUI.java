package net.simpleframework.mvc.component.portal.module;

import net.simpleframework.common.Convert;
import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.component.portal.PageletBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class OptionWindowUI {
	public static OptionWindowUI getOptionWindowUI(final PageletBean pagelet,
			final String uiString) {
		final OptionWindowUI optionWindowUI = new OptionWindowUI(pagelet);
		final String[] arr = StringUtils.split(uiString, ";");
		optionWindowUI.title = arr[0].substring(6);
		optionWindowUI.height = Convert.toInt(arr[1].substring(7));
		optionWindowUI.width = Convert.toInt(arr[2].substring(6));
		return optionWindowUI;
	}

	private final PageletBean pagelet;

	public OptionWindowUI(final PageletBean pagelet) {
		this.pagelet = pagelet;
	}

	private String title;

	private int height, width;

	public String getTitle() {
		if (StringUtils.hasText(title)) {
			return title;
		} else {
			final PortalModule pModule = PortalModuleRegistryFactory.get()
					.getModule(pagelet.getModule());
			return pModule != null ? pModule.getText() : null;
		}
	}

	public int getHeight() {
		return height > 0 ? height : 240;
	}

	public int getWidth() {
		return width > 0 ? width : 400;
	}

	@Override
	public String toString() {
		return "title=" + StringUtils.blank(getTitle()) + ";height=" + getHeight() + ";width="
				+ getWidth();
	}
}
