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

import org.uddi.api_v3.AuthToken;
import org.uddi.api_v3.DiscardAuthToken;
import org.uddi.api_v3.GetAuthToken;
import org.uddi.v3_service.DispositionReportFaultMessage;
import org.uddi.v3_service.UDDISecurityPortType;

import javax.jws.WebService;

/**
 * This class implements the UDDI Security Service and basically handles all authentication requests
 * for jUDDI. For full documentation see {@link UDDISecurityImpl}
 * This service service as endpoint for Oracle.
 * @author <a href="mailto:jfaath@apache.org">Jeff Faath</a> (and many others)
 */
@WebService(serviceName="UDDI_Security_SoapService",
			endpointInterface="org.uddi.v3_service.UDDISecurityPortType",
			targetNamespace = "urn:uddi-org:api_v3")
public class UDDISecurityOracleImpl extends AuthenticatedService implements UDDISecurityPortType {

	public UDDISecurityImpl delegate;

	public AuthToken getAuthToken(String publisherId) throws DispositionReportFaultMessage {
		return delegate.getAuthToken(publisherId);
	}

	@Override
	public AuthToken getAuthToken(GetAuthToken body) throws DispositionReportFaultMessage {
		return delegate.getAuthToken(body);
	}

	@Override
	public void discardAuthToken(DiscardAuthToken body) throws DispositionReportFaultMessage {
		delegate.discardAuthToken(body);
	}
}
