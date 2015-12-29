/*
 * Copyright 2001-2009 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.juddi.api.impl;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.juddi.Registry;
import org.apache.juddi.query.util.FindQualifiers;
import org.apache.juddi.v3.tck.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.uddi.api_v3.*;
import org.uddi.v3_service.DispositionReportFaultMessage;
import org.uddi.v3_service.UDDISecurityPortType;

import java.io.File;
import java.rmi.RemoteException;

import static org.apache.juddi.config.AppConfig.JUDDI_CONFIGURATION_FILE_SYSTEM_PROPERTY;

/**
 * @author <a href="mailto:kstam@apache.org">Kurt T Stam</a>
 * @author <a href="mailto:alexoree@apache.org">Alex O'Ree</a>
 */
public class API_040_BusinessServiceLoadTest {

    private static Log logger = LogFactory.getLog(API_040_BusinessServiceTest.class);

    private static API_010_PublisherTest api010 = new API_010_PublisherTest();
    protected static TckTModel tckTModel = new TckTModel(new UDDIPublicationImpl(), new UDDIInquiryImpl());
    protected static TckBusiness tckBusiness = new TckBusiness(new UDDIPublicationImpl(), new UDDIInquiryImpl());
    protected static TckBusinessService tckBusinessService = new TckBusinessService(new UDDIPublicationImpl(), new UDDIInquiryImpl());
    private static UDDIInquiryImpl inquiry = new UDDIInquiryImpl();

    int numberOfBusinesses = 100;
    int numberOfServices = 100;

    protected static String authInfoJoe = null;
    protected static String authInfoSam = null;

    @BeforeClass
    public static void setup() throws ConfigurationException {
        File f = new File(".");
        logger.info("Current working dir is " + f.getAbsolutePath());
        System.setProperty(JUDDI_CONFIGURATION_FILE_SYSTEM_PROPERTY, f.getAbsolutePath() + "/src/test/resources/juddiv3DisabledTModelKeybag.xml");
        Registry.start();
        logger.info("API_040_BusinessServiceTestPerformance");
        logger.debug("Getting auth tokens..");
        try {
            api010.saveJoePublisher();
            UDDISecurityPortType security = new UDDISecurityImpl();
            authInfoJoe = TckSecurity.getAuthToken(security, TckPublisher.getJoePublisherId(), TckPublisher.getJoePassword());
            String authInfoUDDI = TckSecurity.getAuthToken(security, TckPublisher.getUDDIPublisherId(), TckPublisher.getUDDIPassword());
            tckTModel.saveUDDIPublisherTmodel(authInfoUDDI);
            tckTModel.saveTModels(authInfoUDDI, TckTModel.TMODELS_XML);
        } catch (RemoteException e) {
            throw new RuntimeException("Could not get authentication token", e);
        }
    }

    @AfterClass
    public static void shutdown() throws ConfigurationException {
        tckTModel.deleteCreatedTModels(authInfoJoe);
    }

    /**
     * loads the database with 100x100 services, runs a basic query, then
     * deletes the records
     *
     * @throws DispositionReportFaultMessage
     * @throws ConfigurationException
     */
    @Test
    public void find20Businesses() throws DispositionReportFaultMessage, ConfigurationException {
        try {
            System.setProperty(JUDDI_CONFIGURATION_FILE_SYSTEM_PROPERTY, "/src/test/resources/juddiv3DisabledTModelKeybag.xml");
            tckTModel.saveJoePublisherTmodel(authInfoJoe);
            create100JoeBusinessesWith100ServicesEach();
            find20JoeBusinessesInLessThan5Sec();
            findAllJoeServicesInLessThan5Sec(getApproximateCaseInsensitiveQualifier());
        } finally {
            deleteAllJoeBusinesses();
        }
    }

    private void create100JoeBusinessesWith100ServicesEach() {
        long startSave = System.currentTimeMillis();
        tckBusiness.saveJoePublisherBusinesses(authInfoJoe, numberOfBusinesses);
        for (int i = 0; i < numberOfBusinesses; i++) {
            tckBusinessService.saveJoePublisherServices(authInfoJoe, i, numberOfServices);
        }
        long saveDuration = System.currentTimeMillis() - startSave;
        logger.info("Saved " + numberOfBusinesses + " businesses with each " + numberOfServices + " services in " + saveDuration + "ms");
        logger.info("Tiggering findBusiness query...");
    }

    public void find20JoeBusinessesInLessThan5Sec() throws DispositionReportFaultMessage {
        long startFind = System.currentTimeMillis();
        BusinessList joeBusinesses = findJoeBusinesses(getApproximateCaseInsensitiveQualifier(), 20);
        long findDuration = System.currentTimeMillis() - startFind;
        logger.info("Find 20 businesses took " + findDuration + "ms. Size=" + joeBusinesses.getBusinessInfos().getBusinessInfo().size());
        if (findDuration > 20000) {
            Assert.fail("This operation took too long to process, it took 1 second before, be sure it stays below 5 secondes.");
        }
    }

    private void findAllJoeServicesInLessThan5Sec(org.uddi.api_v3.FindQualifiers apiFq) throws DispositionReportFaultMessage {
        long findDuration;
        long startFind;
        logger.info("Tiggering findService query...");
        FindService fs = new FindService();
        fs.setFindQualifiers(apiFq);
        Name name = new Name();
        name.setValue("Service One%");
        fs.getName().add(name);
        startFind = System.currentTimeMillis();
        //this will match ALL services (100 * 100 =) 10,000 services
        int allJoeServices = numberOfBusinesses * numberOfServices;
        logger.info("Matching " + allJoeServices + " services");
        ServiceList serviceList = inquiry.findService(fs);
        findDuration = System.currentTimeMillis() - startFind;
        logger.info("Find " + allJoeServices + " services took " + findDuration + "ms. Size=" + serviceList.getServiceInfos().getServiceInfo().size());
        if (findDuration > 20000) {
            Assert.fail("This operation took too long to process");
        }
    }

    private BusinessList findJoeBusinesses(org.uddi.api_v3.FindQualifiers apiFq, Integer maxBusinesses) throws DispositionReportFaultMessage {
        FindBusiness fb = new FindBusiness();
        fb.setFindQualifiers(apiFq);
        Name name = new Name();
        name.setValue("John Doe Enterprises%");
        fb.getName().add(name);
        if (maxBusinesses != null) {
            fb.setMaxRows(maxBusinesses);
        }
        long startFind = System.currentTimeMillis();
        return inquiry.findBusiness(fb);
    }

    private org.uddi.api_v3.FindQualifiers getApproximateCaseInsensitiveQualifier() {
        org.uddi.api_v3.FindQualifiers apiFq = new org.uddi.api_v3.FindQualifiers();
        apiFq.getFindQualifier().add(FindQualifiers.APPROXIMATE_MATCH);
        apiFq.getFindQualifier().add(FindQualifiers.CASE_INSENSITIVE_MATCH);
        return apiFq;
    }

    private void deleteAllJoeBusinesses() throws DispositionReportFaultMessage {
        logger.info("Deleting all the joe businesses");
        long startDelete = System.currentTimeMillis();
        BusinessList joeBusinesses = findJoeBusinesses(getApproximateCaseInsensitiveQualifier(), null);
        for (BusinessInfo businessInfo : joeBusinesses.getBusinessInfos().getBusinessInfo()) {
            tckBusiness.deleteBusiness(authInfoJoe, TckBusiness.JOE_BUSINESS_XML, businessInfo.getBusinessKey());
        }
        tckTModel.deleteJoePublisherTmodel(authInfoJoe);
    }

}
