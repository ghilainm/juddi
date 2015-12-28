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

import junit.framework.Assert;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.juddi.v3.client.config.UDDIClerk;
import org.apache.juddi.v3.client.mapping.URLLocalizerDefaultImpl;
import org.apache.juddi.v3.client.mapping.wsdl.BPEL2UDDI;
import org.apache.juddi.v3.client.transport.TransportException;
import org.apache.juddi.v3.tck.TckBusiness;
import org.apache.juddi.v3.tck.TckPublisher;
import org.apache.juddi.v3.tck.TckTModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.wsdl.Definition;
import javax.wsdl.PortType;
import javax.wsdl.WSDLException;
import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Properties;

/**
 * @author <a href="mailto:kstam@apache.org">Kurt T Stam</a>
 */
public class BPEL_020_IntegrationTest extends BPEL_Abstract_IntegrationTest{

    @Before
    public void checkTestMustBeRun(){
        assumeBPELEnabled();
    }

    @Before //jUDDI only to add the keygenerator and business
    public void saveRiftSawKeyGenerator() {
        tckTModel.saveTModel(authInfoRiftSaw, TckTModel.RIFTSAW_PUBLISHER_TMODEL_XML, TckTModel.RIFTSAW_PUBLISHER_TMODEL_KEY);
        tckBusiness.saveBusiness(authInfoRiftSaw, TckBusiness.RIFTSAW_BUSINESS_XML, TckBusiness.RIFTSAW_BUSINESS_KEY);
    }

    @After //jUDDI only to add the keygenerator and business
    public void saveRiftSawKeyGeneratorAfter() {
        tckBusiness.deleteBusiness(authInfoRiftSaw, TckBusiness.RIFTSAW_BUSINESS_XML, TckBusiness.RIFTSAW_BUSINESS_KEY);
        tckTModel.deleteTModel(authInfoRiftSaw, TckTModel.RIFTSAW_PUBLISHER_TMODEL_XML, TckTModel.RIFTSAW_PUBLISHER_TMODEL_KEY);
    }

    @Test
    public void parseWSDL_PortTypeTModels() throws WSDLException, Exception {

        Definition wsdlDefinition = rw.readWSDL("uddi_data/bpel/riftsaw/bpel-technote.wsdl");
        @SuppressWarnings("unchecked")
        Map<QName, PortType> portTypes = (Map<QName, PortType>) wsdlDefinition.getAllPortTypes();
        String ns = wsdlDefinition.getTargetNamespace();
        System.out.println("Namespace: " + ns);

        int i = 0;
        for (QName qName : portTypes.keySet()) {
            String nsp = qName.getNamespaceURI();
            String localpart = qName.getLocalPart();
            System.out.println("Namespace: " + nsp);
            System.out.println("LocalPart: " + localpart);
            if (i++ == 0) {
                Assert.assertEquals("InterfaceOfTravelAgent", localpart);
            } else {
                Assert.assertEquals("InterfaceOfCustomer", localpart);
            }
        }
    }

    @Test
    public void registerBPELProcess() throws WSDLException, ConfigurationException,
            MalformedURLException, RemoteException, TransportException, Exception {

        UDDIClerk clerk = new UDDIClerk();
        clerk.setManagerName(manager.getName());
        clerk.setName("testClerk");
        clerk.setPublisher(TckPublisher.getRiftSawPublisherId());
        clerk.setPassword(TckPublisher.getRiftSawPassword());

        clerk.setUDDINode(manager.getClientConfig().getHomeNode());

        Properties properties = manager.getClientConfig().getHomeNode().getProperties();
        properties.put("keyDomain", "riftsaw.jboss.org");
        properties.put("nodeName", "localhost");
        properties.put("businessName", "redhat-jboss");
        BPEL2UDDI bpel2UDDI = new BPEL2UDDI(clerk, new URLLocalizerDefaultImpl(), properties);

        Definition wsdlDefinition = rw.readWSDL("uddi_data/bpel/riftsaw/HelloWorld.wsdl");
        QName serviceName = new QName("http://www.jboss.org/bpel/examples/wsdl", "HelloService");
        String portName = "HelloPort";
        URL serviceUrl = new URL("http://localhost:8080/helloworld");
        bpel2UDDI.register(serviceName, portName, serviceUrl, wsdlDefinition);

        System.out.println("DONE");

        bpel2UDDI.unRegister(serviceName, portName, serviceUrl);
    }
}
