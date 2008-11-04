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

package org.apache.juddi.validation;

import java.util.Date;
import javax.persistence.EntityManager;

import org.uddi.v3_service.DispositionReportFaultMessage;
import org.apache.juddi.error.ErrorMessage;
import org.apache.juddi.error.AuthTokenRequiredException;
import org.apache.juddi.model.Publisher;

/**
 * @author <a href="mailto:jfaath@apache.org">Jeff Faath</a>
 */
public class ValidateAuth {
	
	public static final int AUTHTOKEN_ACTIVE = 1;
	public static final int AUTHTOKEN_RETIRED = 0;
	
	public static Publisher getPublisher(EntityManager em, String authInfo) throws DispositionReportFaultMessage {

		
		if (authInfo == null || authInfo.length() == 0)
			throw new AuthTokenRequiredException(new ErrorMessage("errors.auth.AuthRequired"));
		
		org.apache.juddi.model.AuthToken modelAuthToken = em.find(org.apache.juddi.model.AuthToken.class, authInfo);
		if (modelAuthToken == null)
			throw new AuthTokenRequiredException(new ErrorMessage("errors.auth.AuthInvalid"));
		
		Publisher publisher = em.find(Publisher.class, modelAuthToken.getPublisherId());
		if (publisher == null)
			throw new AuthTokenRequiredException(new ErrorMessage("errors.auth.AuthInvalid"));
		
		// Auth token is being used.  Adjust appropriate values so that it's internal 'expiration clock' is reset.
		modelAuthToken.setLastUsed(new Date());
		modelAuthToken.setNumberOfUses(modelAuthToken.getNumberOfUses() + 1);
		
		return publisher;
			
				   
	}
			

}