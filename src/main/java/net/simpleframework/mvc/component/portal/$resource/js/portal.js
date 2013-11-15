/**
 * @author 陈侃(cknet@126.com, 13910090885)
 *         https://github.com/simpleframework
 *         http://www.simpleframework.net
 */
function _lo_initPortal(l, actName) {
	var columnIds = $w(l.readAttribute("columnIds"));

	if ((!columnIds || columnIds.length == 0) 
			&& (l.readAttribute("isManager") == "true")) {
  	l.insert(new Element("input", {
  		value : PORTAL_MSG.setColumns,
  		style : "margin: 4px;",
  		type  : "button"
    }).observe("click", function(e) {
			$Actions['layoutColumnsWindow'](l.params);
  	}));
	}
	
	l.isPortalDraggable = function() {
		return l.readAttribute("isDraggable") == "true";
	};
	
	l.setPortalDraggable = function(value) {
		l.setAttribute("isDraggable", value.toString());
	};

	l.updatePortalDraggable = function(draggable) {
		columnIds.each(function(ele) {
			setTitleCursor(ele, draggable);
    	if (draggable) {
    		createSortable(ele);
			} else {
				Sortable.destroy(ele);
			}
		});
	};
	    
  l.initPagelet = function(li, draggable) {
  	var t = li.down(".move");
  	if (!t) return;
  	li.params = l.params.addParameter(PORTAL_PAGELET_ID + "=" + li.id); 
    t.onselectstart = function() {
      return false;
    };
    var tb = t.select(".tb");
    if (tb && tb.length > 0) {
    	t.observe("mouseenter", function(e) {
    		tb[0].show();
      });
      t.observe("mouseleave", function(e) {
      	tb[0].hide();
      });
      t.down(".action_refresh").observe("click", function(e) {
      	$Actions['layoutContent'](li.params);
      	Event.stop(e);
      });
      t.down(".action_delete").observe("click", function(e) {
      	if (!confirm(PORTAL_MSG.deleteConfirm)) {
        	return;
        }
       	li = $(li);
       	showPortalTip(li);
       	li.$remove();
      	Event.stop(e);
      });
    }

    if (draggable) {
    	setTitleCursor(li, true);
      var ul = li.up("ul");
    	Sortable.destroy(ul);
    	createSortable(ul);
    }
    
    $Actions.callSafely("layoutContextMenu", null, function(act) {
    	var m = act.menu;
    	var menu = li.down(".action_menu");
    	if (menu) {
    		m.bindEvent(li.down(".move"));
      	m.bindEvent(menu, "click");
    	}
    	return true;
    });
    
    if (li.readAttribute("sync") != "true") {
    	$Actions.callSafely("layoutContent", li.params);
    }
  };

  columnIds.each(function(column) {
  	column = $(column); 
    if (!column) return;
		
    var draggable = l.isPortalDraggable();
    setTitleCursor(column, draggable);
    if (draggable)
      createSortable(column);

    var lis = column.select("li");
    if (lis.length > 0) {
    	lis.each(function(li) { l.initPagelet(li); });
    }
	});
    	
  function createSortable(column) {
  	column = $(column);
  	var options = {
        dropOnEmpty : true,
        containment : columnIds,
        handle : "move",
        hoverclass : "ho",
        scroll : window,
        constraint : false,
        onUpdate : function(ui) { showPortalTip(ui); }
      };
  	if (Browser.IEVersion <= 8) {
  		options.endeffect = function() {};
  	}
  	Sortable.create(column, options);
  }

  function setTitleCursor(ele, draggable) {
  	ele = $(ele); 
    if (!ele) return;
		ele.select(".move").each(function(t) {
			t.setStyle("cursor: " + (draggable ? "move" : "default"));
		});
  }

  function updatePortalColumns(a) {
  	var tip = a.up(".portal_tb_tip");
  	var p = l.params.toQueryParams();
    var ulIDs = []; 
  	tip.next(".portal").select(">.column").each(function(c) {
  		var ulID = c.down("ul").id;
  		ulIDs.push(ulID);
      var liIDs = []; 
      c.select(">ul>li").each(function(li) { liIDs.push(li.id); });
      p[ulID] = liIDs.join("#");
    });
    p["ul"] = ulIDs.join("#");
    var f = $Actions["ajaxPositionSave"];
    f.portal_tb_tip = tip;
    f(Object.toQueryString(p));
  }

  function showPortalTip(ele) {
  	var l = ele.up(".portal");
    if (l.previous(".portal_tb_tip")) return;
    var d = new Element("div", {
      className: "portal_tb_tip"
    });   
    d.insert(new Element("div", {
      className: "icon"  
    }).observe("click", function() {
      d.$remove();
    })).insert(PORTAL_MSG.tip0).insert(
    		$UI.createLink(PORTAL_MSG.tip1, null, function() {
    	updatePortalColumns(this);
    }));
    Insertion.Before(l, d);
  }
  
  $Actions[actName] = {
  	refresh: function() {
  		var f = $Actions["ajaxLayout"];
  		f.container = l.up();
  		f(l.params);
  	}    
  };
}

function _lo_getPagelet(menuitem) {
	var target = $Target(menuitem);
	return target.hasClassName("pagelet") ? target : target.up("li");
}

function _lo_getPortal(ele) {
	return ele.up(".portal");
}

function _lo_toggleSortable(menuitem) {
	var pagelet = _lo_getPagelet(menuitem);
	if (!pagelet)
		return;
	var l = _lo_getPortal(pagelet);
	if (!l)
		return;
	var draggable = menuitem.isItemChecked();
	l.updatePortalDraggable(draggable);
	var act = $Actions['ajaxDraggableSave'];
	act.itemChecked = function() {
		menuitem.hideMenu();
		l.setPortalDraggable(draggable);
	};
	act(pagelet.params.addParameter("checked=" + !draggable));
}

function _lo_fireMenuAction(menuitem, action) {
	var pagelet = _lo_getPagelet(menuitem);
	if (!pagelet)
		return;
	var act = $Actions[action];
	act.pagelet = pagelet;
	act(pagelet.params);
}

function _lo_fireOptionAction(menuitem) {
  var pagelet = _lo_getPagelet(menuitem);
  if (!pagelet)
		return;
  var w = $Actions["layoutOptionWindow"];
  w.setOptions({
    width  :  parseInt(pagelet.readAttribute("window_width")),
    height :  parseInt(pagelet.readAttribute("window_height"))
  });
  w.title = pagelet.readAttribute("window_title");
  w.pagelet = pagelet;
	w(pagelet.params);
}

function _lo_menuBeforeShow(menu) {
	var pagelet = _lo_getPagelet(menu);
	if (!pagelet)
		return false;
	var l = _lo_getPortal(pagelet);
	if (!l)
		return false;
	if (pagelet.readAttribute("showMenu") != "true")
		return false;

	var items = menu.getItems();
	items[0].setItemVisible(pagelet.readAttribute("showOption") == "true");
	items[4].setItemChecked(!l.isPortalDraggable());
}

function _lo_setPageletFontStyle(li, fontStyle, clear) {
	li = $(li);
	var defaultStyle = {
		fontFamily : '',
		fontStyle : 'normal',
		fontWeight : 'normal',
		fontSize : '',
		color : ''
	};
	li.select(".content, .content *").each(function(a) {
		if (clear)
			a.setStyle(defaultStyle);
		if (fontStyle && fontStyle != '') {
			a.setStyle(fontStyle);
		}
	});
}

function __portal_option_save() {
	var pagelet = $Actions['layoutOptionWindow'].pagelet;
	$Actions['layoutOptionSave'](pagelet ? pagelet.params : '');
}

function __portal_add_module(module) {
	var pagelet = $Actions['layoutModulesWindow'].pagelet;
	$Actions['addLayoutModule'](pagelet ? 
			pagelet.params + "&module=" + module : "module=" + module);
}

function __portal_option_close() {
	$Actions['layoutOptionWindow'].close();
}