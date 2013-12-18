package net.simpleframework.mvc.component.portal;

import java.util.ArrayList;
import java.util.List;

import net.simpleframework.common.StringUtils;
import net.simpleframework.mvc.component.AbstractContainerBean;
import net.simpleframework.mvc.component.portal.module.PortalModuleRegistryFactory;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PortalBean extends AbstractContainerBean {

	private ArrayList<ColumnBean> columns;

	private boolean draggable = false;

	private boolean showMenu = false; // 无组织机构依赖时

	private String autoPagelet = PortalModuleRegistryFactory.DEFAULT_MODULE_NAME;

	private String roleManager;

	public List<ColumnBean> getColumns() {
		if (columns == null) {
			columns = new ArrayList<ColumnBean>();
		}
		return columns;
	}

	public boolean isDraggable() {
		return draggable;
	}

	public void setDraggable(final boolean draggable) {
		this.draggable = draggable;
	}

	public boolean isShowMenu() {
		return showMenu;
	}

	public void setShowMenu(final boolean showMenu) {
		this.showMenu = showMenu;
	}

	@Override
	public void syncElement() {
		setElementAttribute("draggable", isDraggable());
		setElementAttribute("showMenu", isShowMenu());
		setElementAttribute("autoPagelet", getAutoPagelet());
		removeChildren("column");
		for (final ColumnBean column : getColumns()) {
			column.syncElement();
			addElement(column);
		}
	}

	public String getAutoPagelet() {
		return autoPagelet;
	}

	public void setAutoPagelet(final String autoPagelet) {
		this.autoPagelet = autoPagelet;
	}

	public String getRoleManager() {
		return StringUtils.text(roleManager, settings.getDefaultRole());
	}

	public void setRoleManager(final String roleManager) {
		this.roleManager = roleManager;
	}

	{
		setHandleClass(DefaultPortalHandler.class);
	}
}
