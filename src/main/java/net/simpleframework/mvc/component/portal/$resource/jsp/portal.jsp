<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.portal.PortalUtils"%>
<%@ page import="net.simpleframework.mvc.component.ComponentParameter"%>
<%
	final ComponentParameter nCP = PortalUtils.get(request, response);
	out.write(PortalUtils.renderHtml(nCP));
%>
<script type="text/javascript">
	var PORTAL_MSG = {
		tip0            : "#(portal.8)",
		tip1            : "#(portal.9)",
		deleteConfirm   : "#(portal.10)",
		setColumns      : "#(portal.11)"
	};
	
	var PORTAL_PAGELET_ID = "<%=PortalUtils.PAGELET_ID%>";
	
  $ready(function() {
  	var l = $("layout_<%=request.getParameter(PortalUtils.BEAN_ID)%>");
  	l.params = $Form(l.down(".parameters"));
  	_lo_initPortal(l, "<%=nCP.getComponentName()%>");
	});
</script>