<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.portal.PortalUtils"%>
<div class="columns_portal">
  <div id="columnsLayoutEditor">
    <input type="hidden" name="<%=PortalUtils.BEAN_ID%>"
      value="<%=request.getParameter(PortalUtils.BEAN_ID)%>" /> <input type="hidden"
      name="<%=PortalUtils.PAGELET_ID%>" value="<%=request.getParameter(PortalUtils.PAGELET_ID)%>" />
  </div>
  <div style="text-align: right; margin-top: 6px;">
    <input type="button" class="button2" value="#(Button.Save)"
      onclick="$Actions['ajaxColumnsLayoutSave']();" /> <input type="button"
      value="#(Button.Cancel)" onclick="$Actions['layoutColumnsWindow'].close();" />
  </div>
</div>
