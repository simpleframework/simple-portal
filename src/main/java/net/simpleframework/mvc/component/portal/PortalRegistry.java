package net.simpleframework.mvc.component.portal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.ctx.script.IScriptEval;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.AbstractComponentBean;
import net.simpleframework.mvc.component.AbstractComponentRegistry;
import net.simpleframework.mvc.component.ComponentBean;
import net.simpleframework.mvc.component.ComponentHtmlRenderEx;
import net.simpleframework.mvc.component.ComponentName;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ComponentRender;
import net.simpleframework.mvc.component.ComponentResourceProvider;
import net.simpleframework.mvc.component.portal.module.PortalModuleRegistryFactory;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@ComponentName(PortalRegistry.portal)
@ComponentBean(PortalBean.class)
@ComponentRender(PortalRender.class)
@ComponentResourceProvider(PortalResourceProvider.class)
public class PortalRegistry extends AbstractComponentRegistry {

	public static final String portal = "portal";

	public PortalRegistry() {
		PortalModuleRegistryFactory.get().init();
	}

	@Override
	public PortalBean createComponentBean(final PageParameter pp, final Object attriData) {
		final PortalBean portalBean = (PortalBean) super.createComponentBean(pp, attriData);
		ComponentHtmlRenderEx.createAjaxRequest(ComponentParameter.get(pp, portalBean));
		return portalBean;
	}

	@Override
	protected void initComponentFromXml(final PageParameter pp,
			final AbstractComponentBean componentBean, final XmlElement xmlElement) {
		super.initComponentFromXml(pp, componentBean, xmlElement);

		final PortalBean portalBean = (PortalBean) componentBean;
		final IScriptEval scriptEval = pp.getScriptEval();

		final List<ColumnBean> cols = portalBean.getColumns();
		cols.addAll(loadBean(portalBean, scriptEval, xmlElement));
	}

	public List<ColumnBean> loadBean(final PortalBean portalBean, final IScriptEval scriptEval,
			final XmlElement xmlElement) {
		final ArrayList<ColumnBean> al = new ArrayList<ColumnBean>();
		final Iterator<?> it = xmlElement.elementIterator("column");
		while (it.hasNext()) {
			final XmlElement ele = (XmlElement) it.next();
			final ColumnBean column = new ColumnBean(ele, portalBean);
			al.add(column);
			column.parseElement(scriptEval);

			final Iterator<?> it2 = ele.elementIterator("pagelet");
			while (it2.hasNext()) {
				final XmlElement ele2 = (XmlElement) it2.next();
				final PageletBean pagelet = new PageletBean(ele2, column);
				pagelet.parseElement(scriptEval);
				column.getPagelets().add(pagelet);

				final XmlElement titleElement = ele2.element("title");
				if (titleElement != null) {
					final PageletTitle title = new PageletTitle(titleElement, pagelet);
					title.parseElement(scriptEval);
					pagelet.setTitle(title);
				}
			}
		}
		return al;
	}
}
