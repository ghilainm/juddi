/*
 * Copyright 2001-2009 The Apache Software Foundation.
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
package org.apache.juddi.portlets.server.service;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

import org.apache.juddi.jaxb.JAXBMarshaller;
import org.apache.juddi.portlets.client.model.Subscription;
import org.apache.juddi.portlets.client.service.SubscriptionResponse;
import org.apache.juddi.portlets.client.service.SubscriptionService;
import org.apache.juddi.v3.client.config.ClientConfig;
import org.apache.juddi.v3.client.transport.Transport;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.Loader;
import org.uddi.sub_v3.ObjectFactory;
import org.uddi.sub_v3.SubscriptionFilter;
import org.uddi.v3_service.UDDISubscriptionPortType;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
/**
 * 
 * @author <a href="mailto:kstam@apache.org">Kurt T Stam</a>
 *
 */
public class SubscriptionServiceImpl extends RemoteServiceServlet implements SubscriptionService {

	private Logger logger = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	
	public SubscriptionResponse getSubscriptions(String authToken) 
	{
		SubscriptionResponse response = new SubscriptionResponse();
		logger.debug("Sending getSubscriptions request..");
		List<Subscription> modelSubscriptions = new ArrayList<Subscription>();
		try {
	    	 String clazz = ClientConfig.getInstance().getNodes().get("default").getProxyTransport();
	         Class<?> transportClass = Loader.loadClass(clazz);
        	 Transport transport = (Transport) transportClass.newInstance(); 
        	 UDDISubscriptionPortType subscriptionService = transport.getUDDISubscriptionService();
        	 List<org.uddi.sub_v3.Subscription> subscriptions = subscriptionService.getSubscriptions(authToken);
        	 for (org.uddi.sub_v3.Subscription subscription : subscriptions) {
        		 String expiresAfter = null;
        		 if (subscription.getExpiresAfter()!=null) expiresAfter = subscription.getExpiresAfter().toString();
        		 String rawFilter = JAXBMarshaller.marshallToString(new ObjectFactory().createSubscriptionFilter(subscription.getSubscriptionFilter()), "org.uddi.sub_v3");
        		 Subscription modelSubscription = new Subscription(
        				 subscription.getBindingKey(),
        				 subscription.isBrief(),
        				 expiresAfter,
        				 subscription.getMaxEntities(),
        				 subscription.getNotificationInterval().toString(),
        				 rawFilter,
        				 subscription.getSubscriptionKey());
        		 modelSubscriptions.add(modelSubscription);
			 }
        	 response.setSuccess(true);
        	 response.setSubscriptions(modelSubscriptions);
	     } catch (Exception e) {
	    	 logger.error("Could not obtain subscription. " + e.getMessage(), e);
	    	 response.setSuccess(false);
	    	 response.setMessage(e.getMessage());
	    	 response.setErrorCode("102");
	     } catch (Throwable t) {
	    	 logger.error("Could not obtain token subscription. " + t.getMessage(), t);
	    	 response.setSuccess(false);
	    	 response.setMessage(t.getMessage());
	    	 response.setErrorCode("102");
	     } 
		 return response;
	}
	
	public SubscriptionResponse saveSubscription(String authToken, Subscription modelSubscription) {
		SubscriptionResponse response = new SubscriptionResponse();
		logger.debug("Sending saveSubscriptions request..");
		try {
	    	 String clazz = ClientConfig.getInstance().getNodes().get("default").getProxyTransport();
	         Class<?> transportClass = Loader.loadClass(clazz);
	       	 Transport transport = (Transport) transportClass.newInstance(); 
	       	 UDDISubscriptionPortType subscriptionService = transport.getUDDISubscriptionService();
	       	 List<org.uddi.sub_v3.Subscription> subscriptionList = new ArrayList<org.uddi.sub_v3.Subscription>();
	       	 org.uddi.sub_v3.Subscription subscription = new org.uddi.sub_v3.Subscription();
	       	 subscription.setBindingKey(modelSubscription.getBindingKey());
	       	 subscription.setBrief(modelSubscription.getBrief());
	       	 if (!"".equals(modelSubscription.getExpiresAfter())) {
	       		 XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(modelSubscription.getExpiresAfter());
	       		 subscription.setExpiresAfter(calendar);
	       	 }
	       	 subscription.setMaxEntities(modelSubscription.getMaxEntities());
	       	 if (!"".equals(modelSubscription.getNotificationInterval())) {
		       	 Duration duration = DatatypeFactory.newInstance().newDuration(modelSubscription.getNotificationInterval());
		       	 subscription.setNotificationInterval(duration);
	       	 }
	       	 if (!"".equals(modelSubscription.getSubscriptionFilter())) {
		       	 SubscriptionFilter subscriptionFilter = (SubscriptionFilter)JAXBMarshaller.unmarshallFromString(modelSubscription.getSubscriptionFilter(), JAXBMarshaller.PACKAGE_SUBSCRIPTION);
		       	 subscription.setSubscriptionFilter(subscriptionFilter);
	       	 }
	       	 subscription.setSubscriptionKey(modelSubscription.getSubscriptionKey());
			 subscriptionList.add(subscription);
			 Holder<List<org.uddi.sub_v3.Subscription>> subscriptionHolder = new Holder<List<org.uddi.sub_v3.Subscription>>();
			 subscriptionHolder.value = subscriptionList;
	       	 subscriptionService.saveSubscription(authToken, subscriptionHolder);
	       	 response.setSuccess(true);
	       	 //TODO, do we get a response? response.setSubscriptions(modelSubscriptions);
	     } catch (Exception e) {
	    	 logger.error("Could not obtain subscription. " + e.getMessage(), e);
	    	 response.setSuccess(false);
	    	 response.setMessage(e.getMessage());
	    	 response.setErrorCode("102");
	     } catch (Throwable t) {
	    	 logger.error("Could not obtain token subscription. " + t.getMessage(), t);
	    	 response.setSuccess(false);
	    	 response.setMessage(t.getMessage());
	    	 response.setErrorCode("102");
	     } 
		
		return response;
	}

}
