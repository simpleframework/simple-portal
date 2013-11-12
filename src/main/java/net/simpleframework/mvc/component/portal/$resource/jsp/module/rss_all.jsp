<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.simpleframework.mvc.component.portal.module.RssUtils"%>
<%@ page import="net.simpleframework.mvc.PageRequestResponse"%>
<%
	out.write(RssUtils.rssAll(PageRequestResponse
			.get(request, response)));
%>

