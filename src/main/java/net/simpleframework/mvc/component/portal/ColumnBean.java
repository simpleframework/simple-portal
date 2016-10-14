package net.simpleframework.mvc.component.portal;

import java.util.ArrayList;
import java.util.UUID;

import net.simpleframework.common.StringUtils;
import net.simpleframework.ctx.common.xml.AbstractElementBean;
import net.simpleframework.ctx.common.xml.XmlElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class ColumnBean extends AbstractElementBean {
	private static final long serialVersionUID = 8162307649157713016L;

	private final PortalBean portalBean;

	private ArrayList<PageletBean> pagelets;

	private String width;

	public ColumnBean(final XmlElement element, final PortalBean portalBean) {
		super(element == null ? portalBean.getElement().addElement("column") : element);
		this.portalBean = portalBean;
	}

	@Override
	public void syncElement() {
		super.syncElement();
		getElement().remove("pagelet");
		for (final PageletBean pagelet : getPagelets()) {
			pagelet.syncElement();
			addElement(pagelet);
		}
	}

	public PortalBean getPortalBean() {
		return portalBean;
	}

	public ArrayList<PageletBean> getPagelets() {
		if (pagelets == null) {
			pagelets = new ArrayList<PageletBean>();
		}
		return pagelets;
	}

	public String getWidth() {
		return StringUtils.hasText(width) ? width : "auto";
	}

	public void setWidth(final String width) {
		this.width = width;
	}

	private String id;

	public String id() {
		if (id == null) {
			id = "ul_" + UUID.randomUUID().toString();
		}
		return id;
	}
}
