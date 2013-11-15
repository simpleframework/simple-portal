package net.simpleframework.mvc.component.portal;

import java.util.Collection;

import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.IComponentHandler;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IPortalHandler extends IComponentHandler {

	Collection<ColumnBean> getPortalColumns(ComponentParameter cp);

	void updatePortal(ComponentParameter cp, Collection<ColumnBean> columns, boolean draggable);
}
