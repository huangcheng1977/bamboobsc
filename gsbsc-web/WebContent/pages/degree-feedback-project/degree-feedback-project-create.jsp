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

var BSC_PROG005D0001A_fieldsId = new Object();
BSC_PROG005D0001A_fieldsId['name'] 			= 'BSC_PROG005D0001A_name';
BSC_PROG005D0001A_fieldsId['year'] 			= 'BSC_PROG005D0001A_year';
BSC_PROG005D0001A_fieldsId['description']	= 'BSC_PROG005D0001A_description';

function BSC_PROG005D0001A_saveSuccess(data) { // data 是 json 資料
	setFieldsBackgroundDefault(BSC_PROG005D0001A_fieldsId);
	alertDialog(_getApplicationProgramNameById('${programId}'), data.message, function(){}, data.success);	
	if ('Y' != data.success) {						
		setFieldsBackgroundAlert(data.fieldsId, BSC_PROG005D0001A_fieldsId);		
		return;
	}	
	BSC_PROG005D0001A_clear();
}

function BSC_PROG005D0001A_reloadOwnerName() {
	BSC_PROG005D0001A_reloadEmployeeAppendName( 'BSC_PROG005D0001A_owner', 'BSC_PROG005D0001A_ownerName' );
}

function BSC_PROG005D0001A_reloadRaterName() {
	BSC_PROG005D0001A_reloadEmployeeAppendName( 'BSC_PROG005D0001A_rater', 'BSC_PROG005D0001A_raterName' );
}

function BSC_PROG005D0001A_reloadEmployeeAppendName(keyFieldId, nameFieldId) {
	var appendOid = dojo.byId( keyFieldId ).value;
	if (''==appendOid || null==appendOid ) {
		dojo.byId( keyFieldId ).value = '';
		dojo.byId( nameFieldId ).innerHTML = '';		
		return;
	}
	xhrSendParameter(
			'${basePath}/bsc.commonGetEmployeeNamesAction.action', 
			{ 'fields.appendId' : dojo.byId( keyFieldId ).value }, 
			'json', 
			_gscore_dojo_ajax_timeout,
			_gscore_dojo_ajax_sync, 
			true, 
			function(data) {
				if (data!=null && data.appendName!=null) {
					dojo.byId( nameFieldId ).innerHTML = data.appendName;
				}								
			}, 
			function(error) {
				alert(error);
			}
	);	
}

function BSC_PROG005D0001A_clear() {
	setFieldsBackgroundDefault(BSC_PROG005D0001A_fieldsId);		
	dijit.byId('BSC_PROG005D0001A_name').set("value", "");
	dijit.byId('BSC_PROG005D0001A_year').set("value", "");
	dijit.byId('BSC_PROG005D0001A_description').set("value", "");	
	BSC_PROG005D0001A_clearOwner();
	BSC_PROG005D0001A_clearRater();
	BSC_PROG005D0001A_levelData = []; 
	BSC_PROG005D0001A_itemData = []; 
}

function BSC_PROG005D0001A_clearOwner() {
	dojo.byId('BSC_PROG005D0001A_owner').value = '';
	dojo.byId('BSC_PROG005D0001A_ownerName').innerHTML = '';	
}

function BSC_PROG005D0001A_clearRater() {
	dojo.byId('BSC_PROG005D0001A_rater').value = '';
	dojo.byId('BSC_PROG005D0001A_raterName').innerHTML = '';		
}

var BSC_PROG005D0001A_levelData = []; // 評分等級資料要用
var BSC_PROG005D0001A_itemData = []; // 項目資料要用


//------------------------------------------------------------------------------
function ${programId}_page_message() {
	var pageMessage='<s:property value="pageMessage" escapeJavaScript="true"/>';
	if (null!=pageMessage && ''!=pageMessage && ' '!=pageMessage) {
		alert(pageMessage);
	}	
}
//------------------------------------------------------------------------------

</script>

</head>

<body class="claro" bgcolor="#EEEEEE" >

	<gs:toolBar
		id="${programId}" 
		cancelEnable="Y" 
		cancelJsMethod="${programId}_TabClose();" 
		createNewEnable="N"
		createNewJsMethod=""		 
		saveEnabel="Y" 
		saveJsMethod="BSC_PROG005D0001A_save();"
		refreshEnable="Y" 		 
		refreshJsMethod="${programId}_TabRefresh();" 		
		></gs:toolBar>
	<jsp:include page="../header.jsp"></jsp:include>
	
	<input type="hidden" name="BSC_PROG005D0001A_owner" id="BSC_PROG005D0001A_owner" value="" />
	<input type="hidden" name="BSC_PROG005D0001A_rater" id="BSC_PROG005D0001A_rater" value="" />
	
	<table border="0" width="100%" height="500px" cellpadding="1" cellspacing="0" >	
		<tr>
    		<td height="50px" width="100%"  align="left">
    			<font color='RED'>*</font><b>Name</b>:
    			<br/>
    			<gs:textBox name="BSC_PROG005D0001A_name" id="BSC_PROG005D0001A_name" value="" width="200" maxlength="100"></gs:textBox>
				<div data-dojo-type="dijit/Tooltip" data-dojo-props="connectId:'BSC_PROG005D0001A_name'">
    				Input name.
				</div>       			
    		</td>    		
    	</tr>  	
		<tr>
    		<td height="50px" width="100%"  align="left">
    			<font color='RED'>*</font><b>Year</b>:
    			<br/>
				<input id="BSC_PROG005D0001A_year" name="BSC_PROG005D0001A_year" data-dojo-type="dojox.form.YearTextBox" 
					maxlength="4"  type="text" data-dojo-props='style:"width: 80px;" ' />
				<div data-dojo-type="dijit/Tooltip" data-dojo-props="connectId:'BSC_PROG005D0001A_year'">
					Select year.
				</div>	   			
    		</td>    		
    	</tr>  	    	
		<tr>
		    <td height="150px" width="100%" align="left">
		    	<b>Description</b>:
		    	<br/>
		    	<textarea id="BSC_PROG005D0001A_description" name="BSC_PROG005D0001A_description" data-dojo-type="dijit/form/Textarea" rows="4" cols="50" style="width:300px;height:90px;max-height:100px"></textarea>
				<div data-dojo-type="dijit/Tooltip" data-dojo-props="connectId:'BSC_PROG005D0001A_description'">
    				Input description, the maximum allowed 500 characters.
				</div>		    	
		    </td>
		</tr>      	  
		<tr>
    		<td height="50px" width="100%"  align="left">
    			<font color='RED'>*</font><b>Owner</b>:
    			&nbsp;&nbsp;
				<button name="BSC_PROG005D0001A_emplSelect_1" id="BSC_PROG005D0001A_emplSelect_1" data-dojo-type="dijit.form.Button"
					data-dojo-props="
						showLabel:false,
						iconClass:'dijitIconFolderOpen',
						onClick:function(){ 
							BSC_PROG001D0001Q_S00_DlgShow('BSC_PROG005D0001A_owner;BSC_PROG005D0001A_reloadOwnerName');
						}
					"></button>
				<button name="BSC_PROG005D0001A_emplClear_1" id="BSC_PROG005D0001A_emplClear_1" data-dojo-type="dijit.form.Button"
					data-dojo-props="
						showLabel:false,
						iconClass:'dijitIconClear',
						onClick:function(){ 
							BSC_PROG005D0001A_clearOwner();
						}
					"></button>		
				<br/>
				<span id="BSC_PROG005D0001A_ownerName"></span>	    			
    		</td>    
    	</tr>  	
		<tr>
    		<td height="50px" width="100%"  align="left">
    			<font color='RED'>*</font><b>Rater</b>:
    			&nbsp;&nbsp;
				<button name="BSC_PROG005D0001A_emplSelect_2" id="BSC_PROG005D0001A_emplSelect_2" data-dojo-type="dijit.form.Button"
					data-dojo-props="
						showLabel:false,
						iconClass:'dijitIconFolderOpen',
						onClick:function(){ 
							BSC_PROG001D0001Q_S00_DlgShow('BSC_PROG005D0001A_rater;BSC_PROG005D0001A_reloadRaterName');
						}
					"></button>
				<button name="BSC_PROG005D0001A_emplClear_2" id="BSC_PROG005D0001A_emplClear_2" data-dojo-type="dijit.form.Button"
					data-dojo-props="
						showLabel:false,
						iconClass:'dijitIconClear',
						onClick:function(){ 
							BSC_PROG005D0001A_clearRater();
						}
					"></button>		
				<br/>
				<span id="BSC_PROG005D0001A_raterName"></span>	    			
    		</td>    
    	</tr>     			
		<tr>
    		<td height="50px" width="100%"  align="left">
    			<font color='RED'>*</font><b>Level settings</b>:
    			&nbsp;&nbsp;
				<button name="BSC_PROG005D0001A_levelSettings" id="BSC_PROG005D0001A_levelSettings" data-dojo-type="dijit.form.Button"
					data-dojo-props="
						showLabel:false,
						iconClass:'dijitIconFolderOpen',
						onClick:function(){ 
							BSC_PROG005D0001A_S00_DlgShow('BSC_PROG005D0001A_levelData');
						}
					"></button>
				<button name="BSC_PROG005D0001A_levelSettingsClear" id="BSC_PROG005D0001A_levelSettingsClear" data-dojo-type="dijit.form.Button"
					data-dojo-props="
						showLabel:false,
						iconClass:'dijitIconClear',
						onClick:function(){ 
							BSC_PROG005D0001A_levelData = [];
						}
					"></button>						   			
    		</td>    
    	</tr>    	    	

		<tr>
    		<td height="50px" width="100%"  align="left">
    			<font color='RED'>*</font><b>Item settings</b>:
    			&nbsp;&nbsp;
				<button name="BSC_PROG005D0001A_itemSettings" id="BSC_PROG005D0001A_itemSettings" data-dojo-type="dijit.form.Button"
					data-dojo-props="
						showLabel:false,
						iconClass:'dijitIconFolderOpen',
						onClick:function(){ 
							BSC_PROG005D0001A_S01_DlgShow('BSC_PROG005D0001A_itemData');
						}
					"></button>
				<button name="BSC_PROG005D0001A_itemSettingsClear" id="BSC_PROG005D0001A_itemSettingsClear" data-dojo-type="dijit.form.Button"
					data-dojo-props="
						showLabel:false,
						iconClass:'dijitIconClear',
						onClick:function(){ 
							BSC_PROG005D0001A_itemData = [];
						}
					"></button>						   			
    		</td>    
    	</tr>    	   	  	    		 	  	    	    	      	    	    	    	   	  	    		 	  	    	
    	<tr>
    		<td height="50px" width="100%"  align="left">
    			<gs:button name="BSC_PROG005D0001A_save" id="BSC_PROG005D0001A_save" onClick="BSC_PROG005D0001A_save();"
    				handleAs="json"
    				sync="N"
    				xhrUrl="${basePath}/bsc.degreeFeedbackProjectSaveAction.action"
    				parameterType="postData"
    				xhrParameter=" 
    					{     						
    						'fields.name'			: dijit.byId('BSC_PROG005D0001A_name').get('value'),
    						'fields.year'			: dijit.byId('BSC_PROG005D0001A_year').get('value'),
    						'fields.description'	: dijit.byId('BSC_PROG005D0001A_description').get('value'),
    						'fields.ownerOids'		: dojo.byId('BSC_PROG005D0001A_owner').value,
    						'fields.raterOids'		: dojo.byId('BSC_PROG005D0001A_rater').value,
    						'fields.levelData'		: JSON.stringify( { 'data' : BSC_PROG005D0001A_levelData } ),
    						'fields.itemData'		: JSON.stringify( { 'data' : BSC_PROG005D0001A_itemData } )
    					} 
    				"
    				errorFn=""
    				loadFn="BSC_PROG005D0001A_saveSuccess(data);" 
    				programId="${programId}"
    				label="Save" 
    				iconClass="dijitIconSave"></gs:button>    			
    			<gs:button name="BSC_PROG005D0001A_clear" id="BSC_PROG005D0001A_clear" onClick="BSC_PROG005D0001A_clear();" 
    				label="Clear" 
    				iconClass="dijitIconClear"></gs:button>       		
    		</td>
    	</tr>     	 	  	    	
	</table>	

<script type="text/javascript">${programId}_page_message();</script>
</body>
</html>