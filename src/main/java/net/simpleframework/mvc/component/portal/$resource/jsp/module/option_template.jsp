<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.portal.PortalUtils"%>
<%@ page import="net.simpleframework.mvc.component.portal.module.IPortalModuleHandler"%>
<%@ page import="net.simpleframework.mvc.component.portal.module.PortalModuleRegistryFactory"%>
<%@ page import="net.simpleframework.mvc.IForward"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>

<div id="optionEditorForm" class="option_template">
  <input type="hidden" name="<%=PortalUtils.BEAN_ID%>"
    value="<%=request.getParameter(PortalUtils.BEAN_ID)%>" /><input type="hidden"
    name="<%=PortalUtils.PAGELET_ID%>" value="<%=request.getParameter(PortalUtils.PAGELET_ID)%>" />
  <%
  	final ComponentParameter nCP = PortalUtils.get(
  			request, response);
  	final IPortalModuleHandler mh = PortalModuleRegistryFactory.get()
  			.getModuleHandler(
  					PortalUtils.getPageletBean(nCP));
  	if (mh != null) {
  		final IForward forward = mh
  				.getPageletOptionContent(nCP);
  		if (forward != null) {
  			out.write(forward.getResponseText(nCP));
  		}
  	}
  %>
</div>