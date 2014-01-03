package net.simpleframework.mvc.component.portal;

import java.util.Collection;
import java.util.Map;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.portal.module.IPortalModuleHandler;
import net.simpleframework.mvc.component.portal.module.PortalModuleRegistryFactory;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PortalEditPageLoad extends DefaultPageHandler {

	public void optionLoaded(final PageParameter pp, final Map<String, Object> dataBinding,
			final PageSelector selector) throws Exception {
		final IPortalModuleHandler mh = PortalModuleRegistryFactory.get().getModuleHandler(
				PortalUtils.getPageletBean(pp));
		if (mh != null) {
			mh.optionLoaded(pp, dataBinding);
		}
	}

	public void uiOptionLoaded(final PageParameter pp, final Map<String, Object> dataBinding,
			final PageSelector selector) {
		final PageletBean pagelet = PortalUtils.getPageletBean(pp);
		if (pagelet == null) {
			return;
		}

		final PageletTitle title = pagelet.getTitle();
		if (title != null) {
			dataBinding.put("ui_options_title", title.getValue());
			dataBinding.put("ui_options_link", title.getLink());
			final String icon = title.getIcon();
			if (StringUtils.hasText(icon)) {
				dataBinding.put("__homepath", ComponentUtils.getResourceHomePath(PortalBean.class)
						+ "/jsp/icons/");
				dataBinding.put("ui_options_icon", icon);
			}
			final String f = title.getFontStyle();
			if (StringUtils.hasText(f)) {
				dataBinding.put("ui_options_fontstyle", f);
			}
			dataBinding.put("ui_options_desc", title.getDescription());
		}

		final int height = pagelet.getHeight();
		if (height > 0) {
			dataBinding.put("ui_options_height", height);
		}
		dataBinding.put("ui_options_align", pagelet.getAlign());
		final String fontStyle = pagelet.getFontStyle();
		if (StringUtils.hasText(fontStyle)) {
			dataBinding.put("ui_options_c_fontstyle", fontStyle);
		}

		dataBinding.put("ui_options_sync", pagelet.isSync());
	}

	public void columnSizeLoaded(final PageParameter pp, final Map<String, Object> dataBinding,
			final PageSelector selector) {
		final ComponentParameter nCP = PortalUtils.get(pp);
		if (nCP.componentBean == null) {
			return;
		}
		final Collection<ColumnBean> columns = PortalUtils.getColumns(nCP);
		dataBinding.put("_columns_select", columns.size() - 1);
		int i = 1;
		for (final ColumnBean column : columns) {
			dataBinding.put("_cw" + i++, column.getWidth());
		}
	}
}
