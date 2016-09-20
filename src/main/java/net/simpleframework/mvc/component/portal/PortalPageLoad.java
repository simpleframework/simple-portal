package net.simpleframework.mvc.component.portal;

import static net.simpleframework.common.I18n.$m;

import net.simpleframework.mvc.DefaultPageHandler;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.ajaxrequest.AjaxRequestBean;
import net.simpleframework.mvc.component.ui.menu.EMenuEvent;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
import net.simpleframework.mvc.component.ui.tabs.TabsBean;
import net.simpleframework.mvc.component.ui.tooltip.ETipPosition;
import net.simpleframework.mvc.component.ui.tooltip.TipBean;
import net.simpleframework.mvc.component.ui.tooltip.TipBean.Hook;
import net.simpleframework.mvc.component.ui.tooltip.TooltipBean;
import net.simpleframework.mvc.component.ui.window.WindowBean;

/**
 * Licensed under the Apache License, Version 2.0
 * 
 * @author 陈侃(cknet@126.com, 13910090885) https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
public class PortalPageLoad extends DefaultPageHandler {

	@Override
	public void onBeforeComponentRender(final PageParameter pp) {
		super.onBeforeComponentRender(pp);

		final ComponentParameter nCP = PortalUtils.get(pp);

		pp.addComponentBean("ajaxLayout", AjaxRequestBean.class).setDisabledTriggerAction(false)
				.setHandlerMethod("portalRequest").setHandlerClass(PortalAction.class);

		final StringBuilder js = new StringBuilder();
		js.append("var li = $(json['li']);");
		js.append("if (!li) return;");
		js.append(
				"new $UI.AjaxRequest(li.down('.content'), json['text'], json['ajaxRequestId'], false, true);");
		js.append("_lo_setPageletFontStyle(li, json['fontStyle']);");
		pp.addComponentBean("layoutContent", AjaxRequestBean.class).setParallel(true)
				.setDisabledTriggerAction(false).setJsCompleteCallback(js.toString())
				.setHandlerMethod("contentRequest").setHandlerClass(PortalAction.class);

		// title tooltip
		final TooltipBean tooltip = pp.addComponentBean("layoutTitleTooltip", TooltipBean.class);
		tooltip.getTips()
				.add(new TipBean(tooltip).setSelector(".portal .titlebar>table")
						.setStem(ETipPosition.rightTop).setHideAfter(0.5)
						.setHook(new Hook(ETipPosition.leftTop, ETipPosition.rightTop)).setWidth(320));

		// admin
		if (PortalUtils.isManager(nCP)) {
			pp.addComponentBean("ajaxDraggableSave", AjaxRequestBean.class)
					.setJsCompleteCallback("$Actions['ajaxDraggableSave'].itemChecked();")
					.setHandlerMethod("draggableSave").setHandlerClass(PortalAction.class);

			// update layout
			js.setLength(0);
			js.append("var f = $Actions['ajaxPositionSave'];");
			js.append("if (f.portal_tb_tip) f.portal_tb_tip.remove();");
			pp.addComponentBean("ajaxPositionSave", AjaxRequestBean.class)
					.setJsCompleteCallback(js.toString()).setHandlerMethod("positionSave")
					.setHandlerClass(PortalAction.class);

			// option window
			pp.addComponentBean("layoutOptionRequest", AjaxRequestBean.class)
					.setUrlForward("module/option_template.jsp");
			pp.addComponentBean("layoutOptionWindow", WindowBean.class)
					.setContentRef("layoutOptionRequest").setHeight(400).setWidth(350);
			js.setLength(0);
			js.append("var win = $Actions['layoutOptionWindow'];");
			js.append("win.close();");
			js.append("$Actions['layoutContent'](win.pagelet.params);");
			js.append("var t = json['title'];");
			js.append("if (t) $(win.pagelet).down('.titlebar').update(t);");
			pp.addComponentBean("layoutOptionSave", AjaxRequestBean.class)
					.setJsCompleteCallback(js.toString()).setHandlerMethod("optionSave")
					.setHandlerClass(PortalAction.class).setSelector("#optionEditorForm");

			// ui option window
			pp.addComponentBean("layoutUIOptionRequest", AjaxRequestBean.class)
					.setUrlForward("ui_option.jsp");
			pp.addComponentBean("layoutUIOptionWindow", WindowBean.class)
					.setContentRef("layoutUIOptionRequest").setTitle($m("portal.0")).setHeight(450)
					.setWidth(360);

			// module window
			pp.addComponentBean("layoutModulesTabs", TabsBean.class)
					.setHandlerClass(PortalModulesHandler.class);
			pp.addComponentBean("layoutModulesWindow", WindowBean.class)
					.setContentRef("layoutModulesTabs").setTitle($m("portal.1")).setHeight(450)
					.setWidth(720);
			js.setLength(0);
			js.append("var ul = $(json['column']);");
			js.append("var li = json['created'].makeElement();");
			js.append("ul.insert(li);");
			js.append("$Actions['layoutModulesWindow'].close();");
			js.append("_lo_getPortal(ul).initPagelet(li, json['draggable']);");
			pp.addComponentBean("addLayoutModule", AjaxRequestBean.class)
					.setJsCompleteCallback(js.toString()).setConfirmMessage($m("portal_admin.0"))
					.setHandlerMethod("addModule").setHandlerClass(PortalAction.class);

			// layout window
			pp.addComponentBean("layoutColumnsRequest", AjaxRequestBean.class)
					.setUrlForward("columns_portal.jsp");
			pp.addComponentBean("layoutColumnsWindow", WindowBean.class)
					.setContentRef("layoutColumnsRequest").setTitle($m("portal.2")).setHeight(190)
					.setWidth(380);

			// contextMenu
			final MenuBean menu = pp.addComponentBean("layoutContextMenu", MenuBean.class)
					.setMenuEvent(EMenuEvent.contextmenu)
					.setJsBeforeShowCallback("return _lo_menuBeforeShow(menu);");
			menu.addItem(MenuItem.of($m("portal.3"), "portal_icon_set")
					.setOnclick("_lo_fireOptionAction(item);"))
					.addItem(MenuItem.of($m("portal.4"), "portal_icon_opt")
							.setOnclick("_lo_fireMenuAction(item, 'layoutUIOptionWindow');"))
					.addItem(MenuItem.sep())
					.addItem(MenuItem.of($m("portal.5"), "portal_icon_add_let")
							.setOnclick("_lo_fireMenuAction(item, 'layoutModulesWindow');"))
					.addItem(MenuItem.of($m("portal.6"), "portal_icon_columns")
							.setOnclick("_lo_fireMenuAction(item, 'layoutColumnsWindow');"))
					.addItem(MenuItem.sep()).addItem(MenuItem.of($m("portal.7")).setCheckbox(true)
							.setOnclick("_lo_toggleSortable(item);"));
		}
	}
}
