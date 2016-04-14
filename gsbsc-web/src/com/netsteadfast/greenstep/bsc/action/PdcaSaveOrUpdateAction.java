/* 
 * Copyright 2012-2016 bambooCORE, greenstep of copyright Chen Xin Nien
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * -----------------------------------------------------------------------
 * 
 * author: 	Chen Xin Nien
 * contact: chen.xin.nien@gmail.com
 * 
 */
package com.netsteadfast.greenstep.bsc.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netsteadfast.greenstep.base.action.BaseJsonAction;
import com.netsteadfast.greenstep.base.exception.AuthorityException;
import com.netsteadfast.greenstep.base.exception.ControllerException;
import com.netsteadfast.greenstep.base.exception.ServiceException;
import com.netsteadfast.greenstep.base.model.ControllerAuthority;
import com.netsteadfast.greenstep.base.model.ControllerMethodAuthority;
import com.netsteadfast.greenstep.base.model.DefaultResult;
import com.netsteadfast.greenstep.bsc.action.utils.DateDisplayFieldCheckUtils;
import com.netsteadfast.greenstep.bsc.action.utils.NotBlankFieldCheckUtils;
import com.netsteadfast.greenstep.bsc.model.BscMeasureDataFrequency;
import com.netsteadfast.greenstep.bsc.model.PdcaType;
import com.netsteadfast.greenstep.bsc.service.logic.IPdcaLogicService;
import com.netsteadfast.greenstep.util.SimpleUtils;
import com.netsteadfast.greenstep.vo.PdcaItemVO;
import com.netsteadfast.greenstep.vo.PdcaMeasureFreqVO;
import com.netsteadfast.greenstep.vo.PdcaVO;

@ControllerAuthority(check=true)
@Controller("bsc.web.controller.PdcaSaveOrUpdateAction")
@Scope
public class PdcaSaveOrUpdateAction extends BaseJsonAction {
	private static final long serialVersionUID = 3792105160147382440L;
	protected Logger logger=Logger.getLogger(PdcaSaveOrUpdateAction.class);
	private IPdcaLogicService pdcaLogicService;
	private String message = "";
	private String success = IS_NO;
	
	public PdcaSaveOrUpdateAction() {
		super();
	}
	
	@JSON(serialize=false)
	public IPdcaLogicService getPdcaLogicService() {
		return pdcaLogicService;
	}

	@Autowired
	@Resource(name="bsc.service.logic.PdcaLogicService")
	public void setPdcaLogicService(IPdcaLogicService pdcaLogicService) {
		this.pdcaLogicService = pdcaLogicService;
	}
	
	@SuppressWarnings("unchecked")
	private void checkFields() throws ControllerException {
		try {
			super.checkFields(
					new String[]{
							"title",
							"startDate",
							"endDate"
					}, 
					new String[]{
							"Title is required!<BR/>",
							"Start-date is required!<BR/>",
							"End-date is required!<BR/>"
					}, 
					new Class[]{
							NotBlankFieldCheckUtils.class,
							DateDisplayFieldCheckUtils.class,
							DateDisplayFieldCheckUtils.class
					},
					this.getFieldsId() );			
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new ControllerException(e.getMessage().toString());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new ControllerException(e.getMessage().toString());
		}	
		
		String startDate = this.getFields().get("startDate");
		String endDate = this.getFields().get("endDate");
		if (SimpleUtils.getDaysBetween(startDate, endDate)<0) {
			this.getFieldsId().add( "startDate" );
			this.getFieldsId().add( "endDate" );
			throw new ControllerException("Date range error, start-date and end-date!<BR/>");
		}
		
		
		this.checkMeasureFrequency();
		this.checkMeasureFrequencyDateRange();
		
		if (super.defaultString(super.getFields().get("orgaOids")).trim().length() == 0) {
			throw new ControllerException("Please select organization/department!<BR/>");
		}
		if (super.defaultString(super.getFields().get("emplOids")).trim().length() == 0) {
			throw new ControllerException("Please select responsibility(Employee) !<BR/>");
		}
		if (super.defaultString(super.getFields().get("kpiOids")).trim().length() == 0) {
			throw new ControllerException("Please select KPIs!<BR/>");
		}
		
	}
	
	private void checkMeasureFrequency() throws ControllerException {
		
		String frequency = this.getFields().get("measureFreq_frequency");
		String startDate = this.getFields().get("measureFreq_startDate");
		String endDate = this.getFields().get("measureFreq_endDate");
		String startYearDate = this.getFields().get("measureFreq_startYearDate");
		String endYearDate = this.getFields().get("measureFreq_endYearDate");
		if ( BscMeasureDataFrequency.FREQUENCY_DAY.equals(frequency) 
				|| BscMeasureDataFrequency.FREQUENCY_WEEK.equals(frequency) 
				|| BscMeasureDataFrequency.FREQUENCY_MONTH.equals(frequency) ) {
			if ( StringUtils.isBlank( startDate ) || StringUtils.isBlank( endDate ) ) {
				this.getFieldsId().add("measureFreq_startDate");
				this.getFieldsId().add("measureFreq_endDate");
				throw new ControllerException("Start-date and end-date is required!<BR/>");				
			}
		}
		if ( BscMeasureDataFrequency.FREQUENCY_QUARTER.equals(frequency) 
				|| BscMeasureDataFrequency.FREQUENCY_HALF_OF_YEAR.equals(frequency) 
				|| BscMeasureDataFrequency.FREQUENCY_YEAR.equals(frequency) ) {
			if ( StringUtils.isBlank( startYearDate ) || StringUtils.isBlank( endYearDate ) ) {
				this.getFieldsId().add("measureFreq_startYearDate");
				this.getFieldsId().add("measureFreq_endYearDate");
				throw new ControllerException("Start-year and end-year is required!<BR/>");				
			}			
		}		
		if ( !StringUtils.isBlank( startDate ) || !StringUtils.isBlank( endDate ) ) {
			if ( !SimpleUtils.isDate( startDate ) ) {
				this.getFieldsId().add("measureFreq_startDate");
				throw new ControllerException("Start-date format is incorrect!<BR/>");
			}
			if ( !SimpleUtils.isDate( endDate ) ) {
				this.getFieldsId().add("measureFreq_endDate");
				throw new ControllerException("End-date format is incorrect!<BR/>");			
			}
			if ( Integer.parseInt( endDate.replaceAll("/", "").replaceAll("-", "") )
					< Integer.parseInt( startDate.replaceAll("/", "").replaceAll("-", "") ) ) {
				this.getFieldsId().add("measureFreq_startDate");
				this.getFieldsId().add("measureFreq_endDate");
				throw new ControllerException("Start-date / end-date incorrect!<BR/>");			
			}			
		}
		if ( !StringUtils.isBlank( startYearDate ) || !StringUtils.isBlank( endYearDate ) ) {
			if ( !SimpleUtils.isDate( startYearDate+"/01/01" ) ) {
				this.getFieldsId().add("measureFreq_startYearDate");
				throw new ControllerException("Start-year format is incorrect!<BR/>");				
			}
			if ( !SimpleUtils.isDate( endYearDate+"/01/01" ) ) {
				this.getFieldsId().add("measureFreq_endYearDate");
				throw new ControllerException("End-year format is incorrect!<BR/>");						
			}
			if ( Integer.parseInt( endYearDate.replaceAll("/", "").replaceAll("-", "") )
					< Integer.parseInt( startYearDate.replaceAll("/", "").replaceAll("-", "") ) ) {
				this.getFieldsId().add("measureFreq_startYearDate");
				this.getFieldsId().add("measureFreq_endYearDate");
				throw new ControllerException("Start-year / end-year incorrect!<BR/>");			
			}					
		}
		String dataFor = this.getFields().get("measureFreq_dataFor");
		if ("organization".equals(dataFor) 
				&& this.isNoSelectId(this.getFields().get("measureFreq_measureDataOrganizationOid")) ) {
			this.getFieldsId().add("measureFreq_measureDataOrganizationOid");
			throw new ControllerException("Please select measure-data organization!<BR/>");
		}
		if ("employee".equals(dataFor)
				&& this.isNoSelectId(this.getFields().get("measureFreq_measureDataEmployeeOid")) ) {
			this.getFieldsId().add("measureFreq_measureDataEmployeeOid");
			throw new ControllerException("Please select measure-data employee!<BR/>");
		}		
	}
	
	private void checkMeasureFrequencyDateRange() throws ControllerException {
		String frequency = this.getFields().get("measureFreq_frequency");
		String startDate = this.defaultString( this.getFields().get("measureFreq_startDate") ).replaceAll("/", "-");
		String endDate = this.defaultString( this.getFields().get("measureFreq_endDate") ).replaceAll("/", "-");
		String startYearDate = this.defaultString( this.getFields().get("measureFreq_startYearDate") ).replaceAll("/", "-");
		String endYearDate = this.defaultString( this.getFields().get("measureFreq_endYearDate") ).replaceAll("/", "-");
		if (BscMeasureDataFrequency.FREQUENCY_DAY.equals(frequency) 
				|| BscMeasureDataFrequency.FREQUENCY_WEEK.equals(frequency) 
				|| BscMeasureDataFrequency.FREQUENCY_MONTH.equals(frequency) ) {
			int betweenMonths = SimpleUtils.getMonthsBetween(startDate, endDate);
			if ( betweenMonths >= 12 ) {
				this.getFieldsId().add("measureFreq_startDate");
				this.getFieldsId().add("measureFreq_endDate");
				throw new ControllerException("Date range can not be more than 12 months!<BR/>");	
			}
			return;
		}
		int betweenYears = SimpleUtils.getYearsBetween(startYearDate, endYearDate);
		if (BscMeasureDataFrequency.FREQUENCY_QUARTER.equals(frequency)) {
			if ( betweenYears >= 3 ) {
				this.getFieldsId().add("measureFreq_startYearDate");
				this.getFieldsId().add("measureFreq_endYearDate");
				throw new ControllerException("Date range can not be more than 3 years!<BR/>");				
			}
		}
		if (BscMeasureDataFrequency.FREQUENCY_HALF_OF_YEAR.equals(frequency)) {
			if ( betweenYears >= 4 ) {
				this.getFieldsId().add("measureFreq_startYearDate");
				this.getFieldsId().add("measureFreq_endYearDate");
				throw new ControllerException("Date range can not be more than 4 years!<BR/>");				
			}			
		}
		if (BscMeasureDataFrequency.FREQUENCY_YEAR.equals(frequency)) {
			if ( betweenYears >= 6 ) {
				this.getFieldsId().add("measureFreq_startYearDate");
				this.getFieldsId().add("measureFreq_endYearDate");
				throw new ControllerException("Date range can not be more than 6 years!<BR/>");				
			}			
		}
	}	
	
	private void checkItems(List<PdcaItemVO> pdcaItems) throws ControllerException, Exception {
		StringBuilder errMsg = new StringBuilder();
		if (!this.checkPdcaItemsType(pdcaItems)) {
			errMsg.append( "Items type must found P,D,C,A!<BR/>" );
		}
		String errMsg1 = this.checkPdcaItemOwner(pdcaItems);
		if (!"".equals(errMsg1)) {
			errMsg.append( errMsg1 );
		}
		String errMsg2 = this.checkPdcaItemDateRange(pdcaItems);		
		if (!"".equals(errMsg2)) {
			errMsg.append( errMsg2 );
		}		
		if (errMsg.length()>0) {
			throw new ControllerException( errMsg.toString() );
		}
	}
	
	private boolean checkPdcaItemsType(List<PdcaItemVO> items) throws Exception {
		boolean pType = false;
		boolean dType = false;
		boolean cType = false;
		boolean aType = false;
		for (PdcaItemVO item : items) {
			if (PdcaType.PLAN.equals(item.getType())) {
				pType = true;
			}
			if (PdcaType.DO.equals(item.getType())) {
				dType = true;
			}
			if (PdcaType.CHECK.equals(item.getType())) {
				cType = true;
			}
			if (PdcaType.ACTION.equals(item.getType())) {
				aType = true;
			}			
		}
		return ( pType && dType && cType && aType );
	}
	
	private String checkPdcaItemOwner(List<PdcaItemVO> items) throws Exception {
		StringBuilder errMsg = new StringBuilder();
		for (PdcaItemVO item : items) {
			if (item.getEmployeeOids() == null || item.getEmployeeOids().size()<1) {
				errMsg.append( "Item " + item.getTitle() + " no select responsibility(Employee)!<BR/>" );
			}
		}
		return errMsg.toString();
	}	
	
	private String checkPdcaItemDateRange(List<PdcaItemVO> items) throws Exception {
		StringBuilder errMsg = new StringBuilder();
		String projectStartDate = this.getFields().get("startDate");
		String projectEndDate = this.getFields().get("endDate");		
		for (PdcaItemVO item : items) {
			if (SimpleUtils.getDaysBetween(item.getStartDate(), item.getEndDate())<0) {
				errMsg.append( "Item " + item.getTitle() + " date range error!<BR/>" );
			}
			if (SimpleUtils.getDaysBetween(projectStartDate, item.getStartDate())<0) {
				errMsg.append( "Item " + item.getTitle() + " start-date cannot over then project start-date!<BR/>" );
			}
			if (SimpleUtils.getDaysBetween(projectEndDate, item.getEndDate())>0) {
				errMsg.append( "Item " + item.getTitle() + " end-date cannot over then project end-date!<BR/>" );
			}
		}
		return errMsg.toString();		
	}
	
	@SuppressWarnings("unchecked")
	private List<String> getUploadOids() throws Exception {
		String uploadOidsJsonStr = super.defaultString( this.getFields().get("uploadOids") ).trim();
		if (StringUtils.isBlank(uploadOidsJsonStr)) {
			uploadOidsJsonStr = "{'oids' : [] }";
		}
		Map<String, List<Map<String, Object>>> jsonData = (Map<String, List<Map<String, Object>>>)
				new ObjectMapper().readValue( uploadOidsJsonStr, LinkedHashMap.class );
		List<String> oids = new ArrayList<String>();
		if (jsonData.get("oids")==null || !(jsonData.get("oids") instanceof List) ) {
			return oids;
		}
		List<Map<String, Object>> uploadDatas = jsonData.get("oids");
		if (uploadDatas.size()<1) {
			return oids;
		}
		for (Map<String, Object> uploadData : uploadDatas) {
			oids.add( String.valueOf(uploadData.get("oid")) );
		}
		return oids;
	}	
	
	@SuppressWarnings("unchecked")
	private List<PdcaItemVO> getItemsData() throws ControllerException, Exception {
		String itemsDataStr = super.defaultString( this.getFields().get("itemsData") ).trim();
		
		if (StringUtils.isBlank(itemsDataStr)) {
			itemsDataStr = "{ \"items\" : [] }";
		}
		Map<String, Object> datas = new ObjectMapper().readValue( itemsDataStr, Map.class );		
		return this.fillItems( (List<Map<String, Object>>) datas.get("items") );
	}
	
	@SuppressWarnings("unchecked")
	private List<PdcaItemVO> fillItems(List<Map<String, Object>> datas) throws ControllerException, Exception {
		List<PdcaItemVO> items = new ArrayList<PdcaItemVO>();
		if (datas == null || datas.size()<1) {
			return items;
		}
		
		// fill data to PdcaItemVO
		for (Map<String, Object> data : datas) {
			PdcaItemVO item = new PdcaItemVO();
			item.setType( String.valueOf(data.get("type")) );
			item.setTitle( String.valueOf(data.get("title")) );
			item.setStartDate( String.valueOf(data.get("startDate")) );
			item.setEndDate( String.valueOf(data.get("endDate")) );
			item.setDescription( String.valueOf(data.get("description")) );
			item.setEmployeeOids( super.transformAppendIds2List( super.defaultString( String.valueOf(data.get("ownerOids")) ).trim() ) );			
			//item.setUploadOids( (List<String>) data.get("upload") );
			item.setUploadOids( new ArrayList<String>() );
			List<LinkedHashMap<String, String>> uploadDatas = (List<LinkedHashMap<String, String>>) data.get("upload");
			for (LinkedHashMap<String, String> uploadData : uploadDatas) {
				item.getUploadOids().add( uploadData.get("oid") );
			}
			items.add( item );
		}
		
		return items;
	}
	
	private void saveOrUpdate(String type) throws ControllerException, AuthorityException, ServiceException, Exception {
		this.checkFields();		
		List<PdcaItemVO> pdcaItems = this.getItemsData();
		this.checkItems(pdcaItems);
		PdcaVO pdca = new PdcaVO();
		this.transformFields2ValueObject(pdca, new String[]{"title", "startDate", "endDate", "description"});
		this.getFields().remove("measureFreq_date1");
		this.getFields().remove("measureFreq_date2");
		this.getFields().remove("measureFreq_dateType");
		String frequency = this.getFields().get("measureFreq_frequency");		
		if (BscMeasureDataFrequency.FREQUENCY_DAY.equals(frequency) 
				|| BscMeasureDataFrequency.FREQUENCY_WEEK.equals(frequency) 
				|| BscMeasureDataFrequency.FREQUENCY_MONTH.equals(frequency) ) {
			this.getFields().put("measureFreq_date1", this.getFields().get("measureFreq_startDate"));
			this.getFields().put("measureFreq_date2", this.getFields().get("measureFreq_endDate"));
		} else {
			this.getFields().put("measureFreq_date1", this.getFields().get("measureFreq_startYearDate"));
			this.getFields().put("measureFreq_date2", this.getFields().get("measureFreq_endYearDate"));
		}
		String dataFor = this.getFields().get("measureFreq_dataFor");
		this.getFields().put("measureFreq_dateType", "3");
		if ("organization".equals(dataFor)) {
			this.getFields().put("measureFreq_dateType", "1");
		}
		if ("employee".equals(dataFor)) {
			this.getFields().put("measureFreq_dateType", "2");
		}
		PdcaMeasureFreqVO measureFreq = new PdcaMeasureFreqVO();
		this.transformFields2ValueObject(
				measureFreq, 
				new String[]{
						"freq",
						"dataType",
						"organizationOid",
						"employeeOid",
						"startDate",
						"endDate"
				}, 
				new String[]{
						"measureFreq_frequency",
						"measureFreq_dateType",
						"measureFreq_measureDataOrganizationOid",
						"measureFreq_measureDataEmployeeOid",
						"measureFreq_date1",
						"measureFreq_date2"
				});
		DefaultResult<PdcaVO> result = null;
		if ("save".equals(type)) {
			result = this.pdcaLogicService.create(
					pdca, 
					measureFreq,
					this.transformAppendIds2List(this.getFields().get("orgaOids")), 
					this.transformAppendIds2List(this.getFields().get("emplOids")),
					this.transformAppendIds2List(this.getFields().get("kpiOids")),
					this.getUploadOids(), 
					pdcaItems);
		} else {
			pdca.setOid( this.getFields().get("oid") );
			result = this.pdcaLogicService.update(
					pdca, 
					measureFreq,
					this.transformAppendIds2List(this.getFields().get("orgaOids")), 
					this.transformAppendIds2List(this.getFields().get("emplOids")),
					this.transformAppendIds2List(this.getFields().get("kpiOids")),
					this.getUploadOids(), 
					pdcaItems);
		}
		this.message = result.getSystemMessage().getValue();
		if (result.getValue()!=null) {
			this.success = IS_YES;
		}
	}

	private void save() throws ControllerException, AuthorityException, ServiceException, Exception {
		this.saveOrUpdate("save");
	}
	
	private void update() throws ControllerException, AuthorityException, ServiceException, Exception {
		this.saveOrUpdate("update");
	}
	
	private void delete() throws ControllerException, AuthorityException, ServiceException, Exception {
		PdcaVO pdca = new PdcaVO();
		this.transformFields2ValueObject(pdca, new String[]{"oid"});
		DefaultResult<Boolean> result = this.pdcaLogicService.delete(pdca);
		this.message = result.getSystemMessage().getValue();
		if (result.getValue()!=null && result.getValue()) {
			this.success = IS_YES;
		}
	}
	
	/**
	 * bsc.pdcaSaveAction.action
	 * 
	 * @return
	 * @throws Exception
	 */
	@ControllerMethodAuthority(programId="BSC_PROG006D0001A")
	public String doSave() throws Exception {
		try {
			if (!this.allowJob()) {
				this.message = this.getNoAllowMessage();
				return SUCCESS;
			}
			this.save();
		} catch (ControllerException ce) {
			this.message=ce.getMessage().toString();
		} catch (AuthorityException ae) {
			this.message=ae.getMessage().toString();
		} catch (ServiceException se) {
			this.message=se.getMessage().toString();
		} catch (Exception e) {
			e.printStackTrace();
			this.message=e.getMessage().toString();
			this.logger.error(e.getMessage());
			this.success = IS_EXCEPTION;
		}
		return SUCCESS;		
	}	
	
	/**
	 * bsc.pdcaUpdateAction.action
	 * 
	 * @return
	 * @throws Exception
	 */
	@ControllerMethodAuthority(programId="BSC_PROG006D0001E")
	public String doUpdate() throws Exception {
		try {
			if (!this.allowJob()) {
				this.message = this.getNoAllowMessage();
				return SUCCESS;
			}
			this.update();
		} catch (ControllerException ce) {
			this.message=ce.getMessage().toString();
		} catch (AuthorityException ae) {
			this.message=ae.getMessage().toString();
		} catch (ServiceException se) {
			this.message=se.getMessage().toString();
		} catch (Exception e) {
			e.printStackTrace();
			this.message=e.getMessage().toString();
			this.logger.error(e.getMessage());
			this.success = IS_EXCEPTION;
		}
		return SUCCESS;		
	}		

	/**
	 * bsc.pdcaDeleteAction.action
	 * 
	 * @return
	 * @throws Exception
	 */
	@ControllerMethodAuthority(programId="BSC_PROG006D0001Q")
	public String doDelete() throws Exception {
		try {
			if (!this.allowJob()) {
				this.message = this.getNoAllowMessage();
				return SUCCESS;
			}
			this.delete();
		} catch (ControllerException ce) {
			this.message=ce.getMessage().toString();
		} catch (AuthorityException ae) {
			this.message=ae.getMessage().toString();
		} catch (ServiceException se) {
			this.message=se.getMessage().toString();
		} catch (Exception e) {
			e.printStackTrace();
			this.message=e.getMessage().toString();
			this.logger.error(e.getMessage());
			this.success = IS_EXCEPTION;
		}
		return SUCCESS;		
	}					

	@JSON
	@Override
	public String getLogin() {
		return super.isAccountLogin();
	}
	
	@JSON
	@Override
	public String getIsAuthorize() {
		return super.isActionAuthorize();
	}	

	@JSON
	@Override
	public String getMessage() {
		return this.message;
	}

	@JSON
	@Override
	public String getSuccess() {
		return this.success;
	}

	@JSON
	@Override
	public List<String> getFieldsId() {
		return this.fieldsId;
	}

}