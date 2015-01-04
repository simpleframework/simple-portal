package net.simpleframework.mvc.component.portal;

import static net.simpleframework.common.I18n.$m;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.logger.Log;
import net.simpleframework.common.logger.LogFactory;
import net.simpleframework.common.web.HttpUtils;
import net.simpleframework.common.web.html.HtmlConst;
import net.simpleframework.common.web.html.HtmlUtils;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.IMVCContextVar;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.UrlForward;
import net.simpleframework.mvc.common.element.LinkElement;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRenderUtils;
import net.simpleframework.mvc.component.ComponentUtils;
import net.simpleframework.mvc.component.portal.module.IPortalModuleHandler;
import net.simpleframework.mvc.component.portal.module.OptionWindowUI;
import net.simpleframework.mvc.component.portal.module.PortalModule;
import net.simpleframework.mvc.component.portal.module.PortalModuleRegistryFactory;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class PortalUtils implements IMVCContextVar {
	static Log log = LogFactory.getLogger(PortalUtils.class);

	static Collection<ColumnBean> getColumns(final ComponentParameter cp) {
		Collection<ColumnBean> columns = null;
		final IPortalHandler lh = (IPortalHandler) cp.getComponentHandler();
		if (lh != null) {
			columns = lh.getPortalColumns(cp);
		}
		if (columns == null) {
			columns = ((PortalBean) cp.componentBean).getColumns();
		}
		return columns == null ? new ArrayList<ColumnBean>() : columns;
	}

	static boolean isManager(final ComponentParameter cp) {
		return (Boolean) cp.getBeanProperty("showMenu")
				|| mvcContext.getPermission().getLogin(cp).isMember(cp.getBeanProperty("roleManager"));
	}

	public static String renderHtml(final ComponentParameter cp) throws Exception {
		final Collection<ColumnBean> columns = getColumns(cp);
		final StringBuilder sb = new StringBuilder();
		String columnIds = "";
		for (final ColumnBean column : columns) {
			columnIds += column.id() + " ";
		}
		final PortalBean portalBean = (PortalBean) cp.componentBean;
		sb.append("<div id=\"layout_").append(portalBean.hashId());
		sb.append("\" class=\"portal\"");
		final boolean isManager = isManager(cp);
		if (isManager) {
			sb.append(" isDraggable=\"");
			sb.append(cp.getBeanProperty("draggable"));
			sb.append("\"");
		}
		sb.append(" isManager=\"").append(isManager);
		sb.append("\" columnIds=\"").append(columnIds).append("\">");
		sb.append(ComponentRenderUtils.genParameters(cp));

		for (final ColumnBean c : columns) {
			sb.append("<div class=\"column\" style=\"width:").append(c.getWidth()).append("\">");
			sb.append("<ul id=\"").append(c.id()).append("\" class=\"sortable\">");
			final ArrayList<PageletBean> pagelets = c.getPagelets();
			if (pagelets.size() == 0) {
				final String module = (String) cp.getBeanProperty("autoPagelet");
				if (StringUtils.hasText(module) && !module.equals("false")) {
					final PageletBean pagelet = new PageletBean(c);
					pagelet.setModule(module);
					pagelets.add(pagelet);
				}
			}
			for (final PageletBean pagelet : pagelets) {
				sb.append(createPagelet(cp, pagelet, isManager));
			}
			sb.append("</ul></div>");
		}
		sb.append("</div>");
		return sb.toString();
	}

	static String createPagelet(final ComponentParameter cp, final PageletBean pagelet)
			throws Exception {
		return createPagelet(cp, pagelet, isManager(cp));
	}

	static String createPagelet(final ComponentParameter cp, final PageletBean pagelet,
			final boolean isManager) throws Exception {
		final StringBuilder sb = new StringBuilder();
		final IPortalModuleHandler lmHandle = pagelet.getModuleHandler();
		if (lmHandle == null) {
			return sb.toString();
		}
		sb.append("<div class=\"move\">");
		sb.append("<table style=\"width: 100%;\" cellpadding=\"0\" cellspacing=\"0\">");
		sb.append("<tr>");
		sb.append("<td class=\"titlebar\">").append(getTitleString(cp, pagelet)).append("</td>");
		sb.append("<td class=\"act\">");
		if (isManager) {
			sb.append("<div class=\"tb\" style=\"display:none;\">");
			sb.append("<div class=\"action_delete\"></div>");
			sb.append("<div class=\"action_refresh\"></div>");
			sb.append("<div class=\"action_menu\"></div>");
			sb.append("</div>");
		}
		sb.append("</td></tr>");
		sb.append("</table>");
		sb.append("</div>");
		final int h = pagelet.getHeight();
		sb.append("<div class=\"content\" style=\"height:");
		sb.append(h > 0 ? h + "px" : "auto").append(";text-align:").append(pagelet.getAlign())
				.append("\">");

		final String pageletId = pagelet.id();
		if (pagelet.isSync()) {
			final IForward forward = lmHandle.getPageletContent(cp);
			if (forward != null) {
				HttpUtils.putParameter(cp.request, PortalUtils.PAGELET_ID, pageletId);
				try {
					sb.append(UrlForward.includeResponseText(forward.getResponseText(cp)));
				} catch (final Exception e) {
					sb.append(e.getMessage());
				}
			}
		} else {
			sb.append($m("LayoutUtils.renderHtml.0"));
		}
		sb.append("</div>");

		final Properties properties = new Properties();
		properties.setProperty("class", "pagelet");
		properties.setProperty("id", pageletId);
		properties.setProperty("sync", String.valueOf(pagelet.isSync()));
		properties.setProperty("showMenu", String.valueOf(isManager));
		final IPortalModuleHandler mh = PortalModuleRegistryFactory.get().getModuleHandler(pagelet);
		if (mh != null) {
			if (mh.getPageletOptionContent(cp) != null) {
				properties.setProperty("showOption", "true");
			}
			OptionWindowUI optionWindowUI;
			if ((optionWindowUI = mh.getPageletOptionUI(cp)) != null) {
				final String title = optionWindowUI.getTitle();
				if (StringUtils.hasText(title)) {
					properties.setProperty("window_title", title);
				}
				final int height = optionWindowUI.getHeight();
				if (height > 0) {
					properties.setProperty("window_height", String.valueOf(height));
				}
				final int width = optionWindowUI.getWidth();
				if (width > 0) {
					properties.setProperty("window_width", String.valueOf(width));
				}
			}
		}
		return HtmlUtils.tag("li", wrapRound(cp, sb.toString()), properties);
	}

	public static String getTitleString(final ComponentParameter cp, final PageletBean pagelet) {
		String titleValue = null;
		final PageletTitle pageletTitle = pagelet.getTitle();
		final String value = pageletTitle != null ? pageletTitle.getValue() : null;
		if (StringUtils.hasText(value)) {
			titleValue = value;
		} else {
			final PortalModule moduleBean = PortalModuleRegistryFactory.get().getModule(
					pagelet.getModule());
			if (moduleBean != null) {
				final String text = moduleBean.getText();
				if (StringUtils.hasText(text)) {
					titleValue = text;
				}
			}
		}
		titleValue = StringUtils.text(titleValue, HtmlConst.NBSP);
		final StringBuilder sb = new StringBuilder();
		if (pageletTitle != null) {
			final String id = pagelet.id();
			sb.append("<table style=\"width: 100%;\" id=\"title_").append(id);
			sb.append("\" cellpadding=\"0\" cellspacing=\"0\"><tr>");
			final String icon = pageletTitle.getIcon();
			if (StringUtils.hasText(icon)) {
				sb.append("<td class=\"icon\">");
				sb.append("<img src=\"");
				sb.append(ComponentUtils.getResourceHomePath(PortalBean.class));
				sb.append("/jsp/icons/").append(icon).append("\"/>");
				sb.append("</td>");
			}
			sb.append("<td class=\"title\">");
			final String link = pageletTitle.getLink();
			if (StringUtils.hasText(link)) {
				sb.append(new LinkElement(titleValue).setHref(link)
						.addStyle(pageletTitle.getFontStyle()).setTarget("_blank"));
			} else {
				sb.append("<span");
				final String fontStyle = pageletTitle.getFontStyle();
				if (StringUtils.hasText(fontStyle)) {
					sb.append(" style=\"").append(fontStyle).append("\"");
				}
				sb.append(">").append(titleValue).append("</span>");
			}
			sb.append("</td>");
			sb.append("</tr></table>");
			final String desc = pagelet.getTitle().getDescription();
			if (StringUtils.hasText(desc)) {
				// for tooltip
				sb.append("<div style=\"display: none;\">");
				sb.append(HtmlUtils.convertHtmlLines(desc));
				sb.append("</div>");
			}
		} else {
			sb.append(titleValue);
		}
		return sb.toString();
	}

	private static String wrapRound(final ComponentParameter cp, final String s) {
		final StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"jbox\">");
		final Float ver = cp.getIEVersion();
		final boolean j = ver != null && ver == 8.0;
		if (j) {
			sb.append("<div class=\"j1\"></div><div class=\"j2\"></div>");
		}
		sb.append(s);
		if (j) {
			sb.append("<div class=\"j3\"></div><div class=\"j4\"></div>");
		}
		sb.append("</div>");
		return sb.toString();
	}

	public static void savePortal(final ComponentParameter cp) {
		savePortal(cp, null);
	}

	public static void savePortal(final ComponentParameter cp, final Collection<ColumnBean> columns) {
		savePortal(cp, columns, (Boolean) cp.getBeanProperty("draggable"));
	}

	public static void savePortal(final ComponentParameter cp, Collection<ColumnBean> columns,
			final boolean draggable) {
		final IPortalHandler lh = (IPortalHandler) cp.getComponentHandler();
		if (lh == null) {
			return;
		}
		if (columns == null) {
			columns = getColumns(cp);
		}
		lh.updatePortal(cp, columns, draggable);
	}

	public static ColumnBean getColumnBeanByHashId(final ComponentParameter cp, final String hashId) {
		if (hashId != null) {
			for (final ColumnBean column : getColumns(cp)) {
				if (hashId.equals(column.id())) {
					return column;
				}
			}
		}
		return null;
	}

	public static PageletBean getPageletByHashId(final ComponentParameter cp, final String hashId) {
		if (hashId != null) {
			for (final ColumnBean column : getColumns(cp)) {
				for (final PageletBean pagelet : column.getPagelets()) {
					if (hashId.equals(pagelet.id())) {
						return pagelet;
					}
				}
			}
		}
		return null;
	}

	public static final String PAGELET_ID = "pageletId";

	public static PageletBean getPageletBean(final PageRequestResponse rRequest) {
		final ComponentParameter nCP = get(rRequest);
		return getPageletByHashId(nCP, nCP.getParameter(PAGELET_ID));
	}

	public static final String BEAN_ID = "portal_@bid";

	public static ComponentParameter get(final PageRequestResponse rRequest) {
		return ComponentParameter.get(rRequest, BEAN_ID);
	}

	public static ComponentParameter get(final HttpServletRequest request,
			final HttpServletResponse response) {
		return ComponentParameter.get(request, response, BEAN_ID);
	}
}
