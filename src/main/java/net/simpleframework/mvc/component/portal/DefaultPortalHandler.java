package net.simpleframework.mvc.component.portal;

import java.io.IOException;
import java.util.Collection;

import net.simpleframework.mvc.component.ComponentHandlerException;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class DefaultPortalHandler extends AbstractPortalHandler {

	@Override
	public Collection<ColumnBean> getPortalColumns(final ComponentParameter cp) {
		return ((PortalBean) cp.componentBean).getColumns();
	}

	@Override
	public void updatePortal(final ComponentParameter cp, final Collection<ColumnBean> columns,
			final boolean draggable) {
		final PortalBean portalBean = (PortalBean) cp.componentBean;
		portalBean.setDraggable(draggable);
		if (columns != null) {
			final Collection<ColumnBean> _columns = portalBean.getColumns();
			if (!_columns.equals(columns)) {
				_columns.clear();
				_columns.addAll(columns);
			}
		}
		try {
			portalBean.saveToFile();
		} catch (final IOException e) {
			throw ComponentHandlerException.of(e);
		}
	}
}
