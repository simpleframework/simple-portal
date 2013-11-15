package net.simpleframework.mvc.component.portal;

import net.simpleframework.ctx.common.xml.AbstractElementBean;
import net.simpleframework.ctx.common.xml.XmlElement;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PageletTitle extends AbstractElementBean {

	private final PageletBean pagelet;

	private String value;

	private String icon;

	private String fontStyle;

	private String link;

	private String description;

	public PageletTitle(final XmlElement xmlElement, final PageletBean pagelet) {
		super(xmlElement == null ? pagelet.getBeanElement().addElement("title") : xmlElement);
		this.pagelet = pagelet;
	}

	public PageletTitle(final PageletBean pagelet) {
		this(null, pagelet);
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(final String icon) {
		this.icon = icon;
	}

	public String getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(final String fontStyle) {
		this.fontStyle = fontStyle;
	}

	public String getLink() {
		return link;
	}

	public void setLink(final String link) {
		this.link = link;
	}

	public PageletBean getPageletBean() {
		return pagelet;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "description" };
	}
}
