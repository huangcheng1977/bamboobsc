<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="gs" uri="http://www.gsweb.org/controller/tag" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>
<!doctype html>
<html itemscope="itemscope" itemtype="http://schema.org/WebPage">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <base href="<%=basePath%>">
    
    <title>bambooCORE</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="bambooCORE">
	<meta http-equiv="description" content="bambooCORE">
	
<style type="text/css">

</style>

<script type="text/javascript">

function CORE_PROG001D0009Q_GridFieldStructure() {
	return [
			{ name: "${action.getText('CORE_PROG001D0009Q_grid_01')}", field: "oid", formatter: CORE_PROG001D0009Q_GridButtonClick, width: "10%" },  
			{ name: "${action.getText('CORE_PROG001D0009Q_grid_02')}", field: "className", width: "30%" },
			{ name: "${action.getText('CORE_PROG001D0009Q_grid_03')}", field: "system", width: "20%" },
			{ name: "${action.getText('CORE_PROG001D0009Q_grid_04')}", field: "type", width: "10%" },
			{ name: "${action.getText('CORE_PROG001D0009Q_grid_05')}", field: "description", width: "30%" }
		];	
}

function CORE_PROG001D0009Q_GridButtonClick(itemOid) {
	var rd="";
	rd += "<img src=\"" + _getSystemIconUrl('PROPERTIES') + "\" border=\"0\" alt=\"edit\" onclick=\"CORE_PROG001D0009Q_edit('" + itemOid + "');\" />";
	rd += "&nbsp;&nbsp;&nbsp;&nbsp;";
	rd += "<img src=\"" + _getSystemIconUrl('REMOVE') + "\" border=\"0\" alt=\"delete\" onclick=\"CORE_PROG001D0009Q_confirmDelete('" + itemOid + "');\" />";
	return rd;	
}

function CORE_PROG001D0009Q_clear() {
	dijit.byId('CORE_PROG001D0009Q_systemOid').set('value', _gscore_please_select_id);
	dijit.byId('CORE_PROG001D0009Q_className').set('value', '');
	clearQuery_${programId}_grid();	
}

function CORE_PROG001D0009Q_edit(oid) {
	CORE_PROG001D0009E_TabShow(oid);
}

function CORE_PROG001D0009Q_confirmDelete(oid) {
	confirmDialog(
			"${programId}_managementDialogId000", 
			_getApplicationProgramNameById('${programId}'), 
			"${action.getText('CORE_PROG001D0009Q_confirmDelete')}", 
			function(success) {
				if (!success) {
					return;
				}	
				xhrSendParameter(
						'core.systemContextBeanDeleteAction.action', 
						{ 'fields.oid' : oid }, 
						'json', 
						_gscore_dojo_ajax_timeout,
						_gscore_dojo_ajax_sync, 
						true, 
						function(data) {
							alertDialog(_getApplicationProgramNameById('${programId}'), data.message, function(){}, data.success);
							getQueryGrid_${programId}_grid();
						}, 
						function(error) {
							alert(error);
						}
				);	
			}, 
			(window.event ? window.event : null) 
	);	
}

function CORE_PROG001D0009Q_editExpression(oid) {
	CORE_PROG003D0003E_S00_TabShow(oid);
}

//------------------------------------------------------------------------------
function ${programId}_page_message() {
	var pageMessage='<s:property value="pageMessage" escapeJavaScript="true"/>';
	if (null!=pageMessage && ''!=pageMessage && ' '!=pageMessage) {
		alert(pageMessage);
	}	
}
//------------------------------------------------------------------------------

</script>

<body class="flat">

	<gs:toolBar
		id="${programId}" 
		cancelEnable="Y" 
		cancelJsMethod="${programId}_TabClose();" 
		createNewEnable="Y"
		createNewJsMethod="CORE_PROG001D0009A_TabShow()"		 
		saveEnabel="N" 
		saveJsMethod=""
		refreshEnable="Y" 		 
		refreshJsMethod="${programId}_TabRefresh();" 		
		></gs:toolBar>
	<jsp:include page="../header.jsp"></jsp:include>	
	
	<table border="0" width="100%" height="75px" cellpadding="1" cellspacing="0" >
		<tr>
    		<td height="50px" width="50%"  align="left">
    			<font size='2'><b><s:property value="getText('CORE_PROG001D0009Q_systemOid')"/>:</b></font>
    			<br/>
    			<gs:select name="CORE_PROG001D0009Q_systemOid" dataSource="sysMap" id="CORE_PROG001D0009Q_systemOid"></gs:select>
    		</td>
    		<td height="50px" width="50%"  align="left">
    			<font size='2'><b><s:property value="getText('CORE_PROG001D0009Q_className')"/>:</b></font>
    			<br/>
    			<gs:textBox name="CORE_PROG001D0009Q_className" id="CORE_PROG001D0009Q_className" value="" width="300" maxlength="255"></gs:textBox>
    		</td>
    	</tr>
    	<tr>
    		<td  height="25px" width="100%"  align="center" colspan="2">
    			<gs:button name="CORE_PROG001D0009Q_query" id="CORE_PROG001D0009Q_query" onClick="getQueryGrid_${programId}_grid();"
    				handleAs="json"
    				sync="N"
    				xhrUrl="core.systemContextBeanManagementGridQueryAction.action"
    				parameterType="postData"
    				xhrParameter=" 
    					{ 
    						'searchValue.parameter.systemOid'	: dijit.byId('CORE_PROG001D0009Q_systemOid').get('value'), 
    						'searchValue.parameter.className'	: dijit.byId('CORE_PROG001D0009Q_className').get('value'),
    						'pageOf.size'						: getGridQueryPageOfSize_${programId}_grid(),
    						'pageOf.select'						: getGridQueryPageOfSelect_${programId}_grid(),
    						'pageOf.showRow'					: getGridQueryPageOfShowRow_${programId}_grid()
    					} 
    				"
    				errorFn="clearQuery_${programId}_grid();"
    				loadFn="dataGrid_${programId}_grid(data);" 
    				programId="${programId}"
    				label="${action.getText('CORE_PROG001D0009Q_query')}" 
    				iconClass="dijitIconSearch"
    				cssClass="alt-primary"></gs:button>
    			<gs:button name="CORE_PROG001D0009Q_clear" id="CORE_PROG001D0009Q_clear" onClick="CORE_PROG001D0009Q_clear();" 
    				label="${action.getText('CORE_PROG001D0009Q_clear')}" 
    				iconClass="dijitIconClear"
    				cssClass="alt-primary"></gs:button>
    		</td>
    	</tr>      	
    </table>		
    
    <gs:grid gridFieldStructure="CORE_PROG001D0009Q_GridFieldStructure()" clearQueryFn="" id="_${programId}_grid" programId="${programId}"></gs:grid>
	
<script type="text/javascript">${programId}_page_message();</script>	
</body>
</html>
	