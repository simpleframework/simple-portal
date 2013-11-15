package net.simpleframework.mvc.component.portal.module;

import java.util.Map;

import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.portal.PageletBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public interface IPortalModuleHandler {

	/**
	 * 初始化
	 * 
	 * @param pagelet
	 */
	void doInit(PageletBean pagelet);

	PortalModule getModuleBean();

	PageletBean getPagelet();

	IForward getPageletContent(ComponentParameter cp) throws Exception;

	/** ----- options ----- **/

	IForward getPageletOptionContent(ComponentParameter cp) throws Exception;

	void optionSave(ComponentParameter cp) throws Exception;

	void optionLoaded(PageParameter pp, final Map<String, Object> dataBinding) throws Exception;

	/** ----- options ui ----- **/

	String getOptionUITitle(ComponentParameter cp);

	OptionWindowUI getPageletOptionUI(ComponentParameter cp);
}
