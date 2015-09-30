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
package com.netsteadfast.greenstep.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.json.annotations.JSON;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.netsteadfast.greenstep.action.utils.EmailFieldCheckUtils;
import com.netsteadfast.greenstep.action.utils.NotBlankFieldCheckUtils;
import com.netsteadfast.greenstep.base.SysMessageUtil;
import com.netsteadfast.greenstep.base.action.BaseJsonAction;
import com.netsteadfast.greenstep.base.exception.AuthorityException;
import com.netsteadfast.greenstep.base.exception.ControllerException;
import com.netsteadfast.greenstep.base.exception.ServiceException;
import com.netsteadfast.greenstep.base.model.ControllerAuthority;
import com.netsteadfast.greenstep.base.model.ControllerMethodAuthority;
import com.netsteadfast.greenstep.base.model.GreenStepSysMsgConstants;
import com.netsteadfast.greenstep.base.model.YesNo;
import com.netsteadfast.greenstep.util.SystemSettingConfigureUtils;

@ControllerAuthority(check=true)
@Controller("core.web.controller.SettingsSaveOrUpdateAction")
@Scope
public class SettingsSaveOrUpdateAction extends BaseJsonAction {
	private static final long serialVersionUID = 7340763400093706218L;
	protected Logger logger=Logger.getLogger(SettingsSaveOrUpdateAction.class);
	private String message = "";
	private String success = IS_NO;
	
	public SettingsSaveOrUpdateAction() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	private void checkFields() throws ControllerException {
		try {
			super.checkFields(
					new String[]{
							"mailFrom",
							"mailFrom"
					}, 
					new String[]{
							this.getText("MESSAGE.CORE_PROG001D0011Q_mailFrom_msg1") + "<BR/>",
							this.getText("MESSAGE.CORE_PROG001D0011Q_mailFrom_msg2") + "<BR/>"
					}, 
					new Class[]{
							EmailFieldCheckUtils.class,
							NotBlankFieldCheckUtils.class
					},
					this.getFieldsId() );			
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new ControllerException(e.getMessage().toString());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new ControllerException(e.getMessage().toString());
		}
	}		
	
	private void update() throws ControllerException, AuthorityException, ServiceException, Exception {
		this.checkFields();
		String mailFrom = this.getFields().get("mailFrom");
		String mailEnable = YesNo.YES;
		String sysTemplateReWrite = YesNo.YES;
		String leftAccordionContainerEnable = YesNo.YES;
		if ( "false".equals(this.getFields().get("mailEnable")) ) {
			mailEnable = YesNo.NO;
		}
		if ( "false".equals(this.getFields().get("sysTemplateReWrite")) ) {
			sysTemplateReWrite = YesNo.NO;
		}
		if ( "false".equals(this.getFields().get("leftAccordionContainerEnable")) ) {
			leftAccordionContainerEnable = YesNo.NO;
		}
		SystemSettingConfigureUtils.updateMailDefaultFromValue(mailFrom);
		SystemSettingConfigureUtils.updateMailEnableValue(mailEnable);
		SystemSettingConfigureUtils.updateSysFormTemplateFileRewriteValue(sysTemplateReWrite);
		SystemSettingConfigureUtils.updateLeftAccordionContainerEnableValue(leftAccordionContainerEnable);		
		this.success = IS_YES;
		this.message = SysMessageUtil.get(GreenStepSysMsgConstants.UPDATE_SUCCESS);		
	}
	
	/**
	 * core.settingsUpdateAction.action
	 * 
	 * @return
	 * @throws Exception
	 */
	@ControllerMethodAuthority(programId="CORE_PROG001D0011Q")
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
		} catch (Exception e) { // 因為是 JSON 所以不用拋出 throw e 了
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
