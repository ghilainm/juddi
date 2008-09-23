package org.apache.juddi.model;
/*
 * Copyright 2001-2008 The Apache Software Foundation.
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
 */

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author <a href="mailto:kurt@apache.org">Kurt T Stam</a>
 */
@Entity
@Table(name = "service_descr")
public class ServiceDescr implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private ServiceDescrId id;
	private BusinessService businessService;
	private String langCode;
	private String descr;

	public ServiceDescr() {
	}

	public ServiceDescr(ServiceDescrId id, BusinessService businessService,
			String descr) {
		this.id = id;
		this.businessService = businessService;
		this.descr = descr;
	}
	public ServiceDescr(ServiceDescrId id, BusinessService businessService,
			String langCode, String descr) {
		this.id = id;
		this.businessService = businessService;
		this.langCode = langCode;
		this.descr = descr;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "serviceKey", column = @Column(name = "service_key", nullable = false, length = 255)),
			@AttributeOverride(name = "serviceDescrId", column = @Column(name = "service_descr_id", nullable = false))})

	public ServiceDescrId getId() {
		return this.id;
	}

	public void setId(ServiceDescrId id) {
		this.id = id;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_key", nullable = false, insertable = false, updatable = false)

	public BusinessService getBusinessService() {
		return this.businessService;
	}

	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}

	@Column(name = "lang_code", length = 5)
	public String getLangCode() {
		return this.langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	@Column(name = "descr", nullable = false)

	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

}
