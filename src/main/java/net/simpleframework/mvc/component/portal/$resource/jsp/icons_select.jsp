<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.File"%>
<%@ page import="net.simpleframework.mvc.MVCUtils"%>
<div>
  <%
  	String iconPath = request.getParameter("iconPath");
    	File iconDir = new File(MVCUtils.getRealPath(iconPath));
    	if (!iconDir.exists()) {
    		return;
    	}
    	File[] icons = iconDir.listFiles();
    	if (icons != null) {
    		for (int i = 0; i < icons.length; i++) {
  %>
  <div class="portal_icon_select"
    style=" background-image: url(<%=iconPath + icons[i].getName()%>);"
    onclick="__setTitleColor(this);"></div>
  <%
  	}
  	}
  %>
</div>
<script type="text/javascript">
  function __setTitleColor(object) {
    var c = $('d_ui_options_icon');
    if (!c) {
      return;
    }
    var bg = object.getStyle("background-image");
    c.setStyle("background-image:" + bg);
    bg = bg.replace(/\"/g, '');
    $('ui_options_icon').value = bg.substring(bg.lastIndexOf("/") + 1, bg.indexOf(")"));
    $Actions['iconsSelect'].close();
  }
</script>