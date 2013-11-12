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
<script type="text/javascript">
  function _columnsChange(initValue) {
    var v = $F('_columns_select');
    var jv = parseInt(v);
    var _cw = [ $("_cw1"), $("_cw2"), $("_cw3"), $("_cw4"), $("_cw5") ];
    for ( var i = 0; i < 5; i++) {
      var o = _cw[i];
      if (i < jv) {
        o.enable();
        o.setStyle("background-color: #fff;");
      } else {
        o.disable();
        o.clear();
        o.setStyle("background-color: #eee;");
      }
    }
    if (!initValue)
      return;
    if (jv == 1) {
      _cw[0].value = "100%";
    } else if (jv == 2) {
      _cw[0].value = "40%";
      _cw[1].value = "60%";
    } else if (jv == 3) {
      _cw[0].value = "30%";
      _cw[1].value = "40%";
      _cw[2].value = "30%";
    } else if (jv == 4) {
      _cw[0].value = "20%";
      _cw[1].value = "30%";
      _cw[2].value = "30%";
      _cw[3].value = "20%";
    } else if (jv == 5) {
      _cw[0].value = "20%";
      _cw[1].value = "20%";
      _cw[2].value = "20%";
      _cw[3].value = "20%";
      _cw[4].value = "20%";
    }
  }
</script>
