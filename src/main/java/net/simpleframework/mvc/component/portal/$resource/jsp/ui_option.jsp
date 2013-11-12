<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.portal.PortalUtils"%>
<div class="ui_option">
  <div id="_optionsEditor">
    <input type="hidden" name="<%=PortalUtils.BEAN_ID%>"
      value="<%=request.getParameter(PortalUtils.BEAN_ID)%>" /> <input type="hidden"
      name="<%=PortalUtils.PAGELET_ID%>" value="<%=request.getParameter(PortalUtils.PAGELET_ID)%>" />
    <div id="_optionsEditor1" style="margin-bottom: 4px;"></div>
    <div id="_optionsEditor2" style="margin-bottom: 4px;"></div>
  </div>
  <div style="text-align: right;">
    <input type="button" class="button2" value="#(Button.Save)" id="userSave"
      onclick="$Actions['ajaxUIOptionSave']();" /> <input type="button" value="#(Button.Cancel)"
      onclick="$Actions['layoutUIOptionWindow'].close();" />
  </div>
</div>