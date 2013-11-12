package net.simpleframework.mvc.component.portal;

import static net.simpleframework.common.I18n.$m;

import java.util.Collection;
import java.util.Map;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.portal.module.PortalModule;
import net.simpleframework.mvc.component.portal.module.PortalModuleRegistryFactory;
import net.simpleframework.mvc.component.ui.tabs.DefaultTabsHandler;
import net.simpleframework.mvc.component.ui.tabs.TabItem;
import net.simpleframework.mvc.component.ui.tabs.TabItems;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class PortalModulesHandler extends DefaultTabsHandler {

	@Override
	public TabItems tabItems(final ComponentParameter cp) {
		final TabItems al = TabItems.of();
		final Map<String, Collection<PortalModule>> modules = PortalModuleRegistryFactory.get()
				.getModulesByCatalog();
		for (final Map.Entry<String, Collection<PortalModule>> entry : modules.entrySet()) {
			final TabItem item = new TabItem();
			al.add(item);
			item.setTitle(entry.getKey());
			final StringBuilder sb = new StringBuilder();
			sb.append("<div class=\"lm\">");
			for (final PortalModule module : entry.getValue()) {
				sb.append("<div class=\"module\"><table style=\"width: 100%;\"><tr>");
				sb.append("<td class=\"icon\"><img src=\"");
				sb.append(cp.wrapContextPath(module.getIcon())).append("\" /></td>");
				sb.append("<td><div class=\"act\">").append(module.getText()).append("<a onclick=\"")
						.append("__portal_add_module('").append(module.getName()).append("');")
						.append("\">").append($m("Add")).append("</a></div>");
				sb.append("<div class=\"md\">").append(StringUtils.blank(module.getDescription()))
						.append("</div></td>");
				sb.append("</tr></table></div>");
			}
			sb.append("</div>");
			item.setContent(sb.toString());
		}
		return al;
	}
}
