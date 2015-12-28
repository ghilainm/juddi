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
package org.apache.juddi.v3.bpel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.juddi.v3.client.config.UDDIClient;
import org.apache.juddi.v3.tck.TckBusiness;
import org.apache.juddi.v3.tck.TckBusinessService;
import org.apache.juddi.v3.tck.TckTModel;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:kstam@apache.org">Kurt T Stam</a>
 */
public class BPEL_010_IntegrationTest extends BPEL_Abstract_IntegrationTest{

    private static TckTModel tckTModel = null;
    private static TckBusinessService tckService = null;
    private static TckBusiness tckBusiness = null;
    private static Log logger = LogFactory.getLog(BPEL_010_IntegrationTest.class);
    private static String authInfoRiftSaw = null;
    private static UDDIClient manager;

    @Before
    public void checkTestMustBeRun(){
        assumeBPELEnabled();
    }

    @Test //You need to have the RiftSaw keyGenerator tModel in your registry.
    public void registerWSDLPortTypeTModels() {
        logger.info("registerWSDLPortTypeTModels");
        tckTModel.saveTModel(authInfoRiftSaw, TckTModel.RIFTSAW_PUBLISHER_TMODEL_XML, TckTModel.RIFTSAW_PUBLISHER_TMODEL_KEY);
        //Agent
        tckTModel.saveTModel(authInfoRiftSaw, TckTModel.RIFTSAW_AGENT_PORTTYPE_TMODEL_XML, TckTModel.RIFTSAW_AGENT_PORTTYPE_TMODEL_KEY);
        //Customer
        tckTModel.saveTModel(authInfoRiftSaw, TckTModel.RIFTSAW_CUST_PORTTYPE_TMODEL_XML, TckTModel.RIFTSAW_CUST_PORTTYPE_TMODEL_KEY);

        tckTModel.deleteTModel(authInfoRiftSaw, TckTModel.RIFTSAW_CUST_PORTTYPE_TMODEL_XML, TckTModel.RIFTSAW_CUST_PORTTYPE_TMODEL_KEY);
        tckTModel.deleteTModel(authInfoRiftSaw, TckTModel.RIFTSAW_AGENT_PORTTYPE_TMODEL_XML, TckTModel.RIFTSAW_AGENT_PORTTYPE_TMODEL_KEY);
        tckTModel.deleteTModel(authInfoRiftSaw, TckTModel.RIFTSAW_PUBLISHER_TMODEL_XML, TckTModel.RIFTSAW_PUBLISHER_TMODEL_KEY);
    }

    @Test //You need to have the RiftSaw keyGenerator tModel in your registry.
    public void registerBPEL4WSTModel() {
        logger.info("registerBPEL4WSTModel");
        tckTModel.saveTModel(authInfoRiftSaw, TckTModel.RIFTSAW_PUBLISHER_TMODEL_XML, TckTModel.RIFTSAW_PUBLISHER_TMODEL_KEY);
        //Process
        tckTModel.saveTModel(authInfoRiftSaw, TckTModel.RIFTSAW_PROCESS_TMODEL_XML, TckTModel.RIFTSAW_PROCESS_TMODEL_KEY);
        tckTModel.deleteTModel(authInfoRiftSaw, TckTModel.RIFTSAW_PROCESS_TMODEL_XML, TckTModel.RIFTSAW_PROCESS_TMODEL_KEY);
        tckTModel.deleteTModel(authInfoRiftSaw, TckTModel.RIFTSAW_PUBLISHER_TMODEL_XML, TckTModel.RIFTSAW_PUBLISHER_TMODEL_KEY);

    }

    @Test //You need to have the RiftSaw keyGenerator tModel in your registry.
    public void registerBPELService() {
        logger.info("registerBPELService");
        tckTModel.saveTModel(authInfoRiftSaw, TckTModel.RIFTSAW_PUBLISHER_TMODEL_XML, TckTModel.RIFTSAW_PUBLISHER_TMODEL_KEY);
//Agent
        tckTModel.saveTModel(authInfoRiftSaw, TckTModel.RIFTSAW_AGENT_PORTTYPE_TMODEL_XML, TckTModel.RIFTSAW_AGENT_PORTTYPE_TMODEL_KEY);
        //Customer
        tckTModel.saveTModel(authInfoRiftSaw, TckTModel.RIFTSAW_CUST_PORTTYPE_TMODEL_XML, TckTModel.RIFTSAW_CUST_PORTTYPE_TMODEL_KEY);
        tckTModel.saveTModel(authInfoRiftSaw, TckTModel.RIFTSAW_PROCESS_TMODEL_XML, TckTModel.RIFTSAW_PROCESS_TMODEL_KEY);


        tckBusiness.saveBusiness(authInfoRiftSaw, TckBusiness.RIFTSAW_BUSINESS_XML, TckBusiness.RIFTSAW_BUSINESS_KEY);
        //Service
        tckService.saveService(authInfoRiftSaw, TckBusinessService.RIFTSAW_PROCESS_XML, TckBusinessService.RIFTSAW_PROCESS_KEY);

        tckService.deleteService(authInfoRiftSaw, TckBusinessService.RIFTSAW_PROCESS_KEY);
        tckBusiness.deleteBusiness(authInfoRiftSaw, TckBusiness.RIFTSAW_BUSINESS_XML, TckBusiness.RIFTSAW_BUSINESS_KEY);

        tckTModel.deleteTModel(authInfoRiftSaw, TckTModel.RIFTSAW_CUST_PORTTYPE_TMODEL_XML, TckTModel.RIFTSAW_CUST_PORTTYPE_TMODEL_KEY);
        tckTModel.deleteTModel(authInfoRiftSaw, TckTModel.RIFTSAW_AGENT_PORTTYPE_TMODEL_XML, TckTModel.RIFTSAW_AGENT_PORTTYPE_TMODEL_KEY);
        tckTModel.deleteTModel(authInfoRiftSaw, TckTModel.RIFTSAW_PUBLISHER_TMODEL_XML, TckTModel.RIFTSAW_PUBLISHER_TMODEL_KEY);
        tckTModel.deleteTModel(authInfoRiftSaw, TckTModel.RIFTSAW_PROCESS_TMODEL_XML, TckTModel.RIFTSAW_PROCESS_TMODEL_KEY);

    }
}
