package net.simpleframework.mvc.component.portal;

import java.util.Properties;
import java.util.UUID;

import net.simpleframework.common.Convert;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.object.ObjectFactory;
import net.simpleframework.ctx.common.xml.AbstractElementBean;
import net.simpleframework.ctx.common.xml.XmlElement;
import net.simpleframework.mvc.common.element.ETextAlign;
import net.simpleframework.mvc.component.portal.module.IPortalModuleHandler;
import net.simpleframework.mvc.component.portal.module.PortalModule;
import net.simpleframework.mvc.component.portal.module.PortalModuleRegistryFactory;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PageletBean extends AbstractElementBean {

	private final ColumnBean columnBean;

	private PageletTitle title;

	private int height;

	private ETextAlign align;

	private String fontStyle;

	private String module;

	private boolean sync;

	private String options;

	public PageletBean(final XmlElement element, final ColumnBean columnBean) {
		super(element == null ? columnBean.getElement().addElement("pagelet") : element);
		this.columnBean = columnBean;
	}

	public PageletBean(final ColumnBean columnBean) {
		this(null, columnBean);
	}

	@Override
	public void syncElement() {
		super.syncElement();
		if (title != null) {
			title.syncElement();
		}
	}

	public ColumnBean getColumnBean() {
		return columnBean;
	}

	public PageletTitle getTitle() {
		if (title == null) {
			title = new PageletTitle(this);
		}
		return title;
	}

	public void setTitle(final PageletTitle title) {
		this.title = title;
	}

	public String getModule() {
		return StringUtils.text(module, PortalModuleRegistryFactory.DEFAULT_MODULE_NAME);
	}

	public void setModule(final String module) {
		this.module = module;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(final int height) {
		this.height = height;
	}

	public ETextAlign getAlign() {
		return align == null ? ETextAlign.left : align;
	}

	public void setAlign(final ETextAlign align) {
		this.align = align;
	}

	public String getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(final String fontStyle) {
		this.fontStyle = fontStyle;
	}

	public boolean isSync() {
		return sync;
	}

	public void setSync(final boolean sync) {
		this.sync = sync;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(final String options) {
		this.options = options;
	}

	public Properties getOptionProperties() {
		return Convert.toProperties(getOptions());
	}

	public String getOptionProperty(final String key) {
		final Properties properties = getOptionProperties();
		return properties != null ? properties.getProperty(key) : null;
	}

	public void setOptionProperty(final String key, final Object value) {
		Properties properties = getOptionProperties();
		if (properties == null) {
			properties = new Properties();
		}
		properties.setProperty(key, Convert.toString(value));
		setOptions(Convert.toString(properties));
	}

	public boolean removeOptionProperty(final String key) {
		final Properties properties = getOptionProperties();
		if (properties != null && properties.remove(key) != null) {
			setOptions(Convert.toString(properties));
			return true;
		}
		return false;
	}

	public PortalModule getModuleBean() {
		return PortalModuleRegistryFactory.get().getModule(getModule());
	}

	private IPortalModuleHandler layoutModuleHandle;

	public IPortalModuleHandler getModuleHandler() {
		if (layoutModuleHandle == null) {
			final PortalModule moduleBean = getModuleBean();
			if (moduleBean == null) {
				return null;
			}
			final String handleClass = moduleBean.getHandleClass();
			if (StringUtils.hasText(handleClass)) {
				layoutModuleHandle = (IPortalModuleHandler) ObjectFactory.create(handleClass);
				layoutModuleHandle.doInit(this);
			}
		}
		return layoutModuleHandle;
	}

	private String id;

	public String id() {
		if (id == null) {
			id = "li_" + UUID.randomUUID().toString();
		}
		return id;
	}

	@Override
	protected String[] elementAttributes() {
		return new String[] { "options" };
	}
}
