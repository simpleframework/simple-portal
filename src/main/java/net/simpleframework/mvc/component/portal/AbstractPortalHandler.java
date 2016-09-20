package net.simpleframework.mvc.component.portal;

import java.util.Map;

import net.simpleframework.common.coll.KVMap;
import net.simpleframework.ctx.permission.PermissionConst;
import net.simpleframework.mvc.component.AbstractComponentHandler;
import net.simpleframework.mvc.component.ComponentParameter;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public abstract class AbstractPortalHandler extends AbstractComponentHandler
		implements IPortalHandler {

	@Override
	public Object getBeanProperty(final ComponentParameter cp, final String beanProperty) {
		if ("roleManager".equals(beanProperty)) {
			return PermissionConst.ROLE_MANAGER;
		}
		return super.getBeanProperty(cp, beanProperty);
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter cp) {
		return ((KVMap) super.getFormParameters(cp)).add(PortalUtils.BEAN_ID,
				cp.getParameter(PortalUtils.BEAN_ID));
	}
}
