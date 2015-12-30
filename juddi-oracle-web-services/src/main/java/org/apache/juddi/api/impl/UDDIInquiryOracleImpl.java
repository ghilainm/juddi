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
 *
 */

package org.apache.juddi.api.impl;

import org.uddi.api_v3.*;
import org.uddi.v3_service.DispositionReportFaultMessage;

import org.uddi.v3_service.UDDIInquiryPortType;

import javax.jws.WebService;


/**
 * This implements the UDDI v3 Inquiry API web service.
 * This service service as endpoint for Oracle.
 * @author <a href="mailto:jfaath@apache.org">Jeff Faath</a>
 */
@WebService(serviceName="UDDI_Inquiry_SoapService",
            portName = "UDDI_Inquiry_PortType",
			endpointInterface="org.uddi.v3_service.UDDIInquiryPortType",
			targetNamespace = "urn:uddi-org:api_v3")
public class UDDIInquiryOracleImpl extends AuthenticatedService implements UDDIInquiryPortType {

	private UDDIInquiryImpl delegate = new UDDIInquiryImpl();

	@Override
	public BindingDetail findBinding(FindBinding body) throws DispositionReportFaultMessage {
		return delegate.findBinding(body);
	}

	@Override
	public BusinessList findBusiness(FindBusiness body) throws DispositionReportFaultMessage {
		return delegate.findBusiness(body);
	}

	@Override
	public RelatedBusinessesList findRelatedBusinesses(FindRelatedBusinesses body) throws DispositionReportFaultMessage {
		return delegate.findRelatedBusinesses(body);
	}

	@Override
	public ServiceList findService(FindService body) throws DispositionReportFaultMessage {
		return delegate.findService(body);
	}

	@Override
	public TModelList findTModel(FindTModel body) throws DispositionReportFaultMessage {
		return delegate.findTModel(body);
	}

	@Override
	public BindingDetail getBindingDetail(GetBindingDetail body) throws DispositionReportFaultMessage {
		return delegate.getBindingDetail(body);
	}

	@Override
	public BusinessDetail getBusinessDetail(GetBusinessDetail body) throws DispositionReportFaultMessage {
		return delegate.getBusinessDetail(body);
	}

	@Override
	public OperationalInfos getOperationalInfo(GetOperationalInfo body) throws DispositionReportFaultMessage {
		return delegate.getOperationalInfo(body);
	}

	@Override
	public ServiceDetail getServiceDetail(GetServiceDetail body) throws DispositionReportFaultMessage {
		return delegate.getServiceDetail(body);
	}

	@Override
	public TModelDetail getTModelDetail(GetTModelDetail body) throws DispositionReportFaultMessage {
		return delegate.getTModelDetail(body);
	}
}
