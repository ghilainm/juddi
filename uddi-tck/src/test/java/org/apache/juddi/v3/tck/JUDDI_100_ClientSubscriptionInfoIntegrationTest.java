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
package org.apache.juddi.v3.tck;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.juddi.api_v3.*;
import org.apache.juddi.v3.client.config.UDDIClient;
import org.apache.juddi.v3.client.transport.Transport;
import org.apache.juddi.v3_service.JUDDIApiPortType;
import org.junit.*;
import org.uddi.v3_service.UDDISecurityPortType;

import javax.xml.ws.BindingProvider;

/**
 * jUDDI specific tests
 *
 * @author <a href="mailto:kstam@apache.org">Kurt T Stam</a>
 * @author <a href="mailto:alexoree@apache.org">Alex O'Ree</a>
 */
public class JUDDI_100_ClientSubscriptionInfoIntegrationTest {

    private static UDDISecurityPortType security = null;
    private static JUDDIApiPortType publisher = null;
    private static Log logger = LogFactory.getLog(JUDDI_100_ClientSubscriptionInfoIntegrationTest.class);
    private static String authInfo = null;
    private static UDDIClient manager;

    @BeforeClass
    public static void startRegistry() throws ConfigurationException {
        if (!TckPublisher.isEnabled()) return;
        manager = new UDDIClient();
        manager.start();


        logger.debug("Getting auth tokens..");
        try {
            Transport transport = manager.getTransport("uddiv3");

            security = transport.getUDDISecurityService();
            authInfo = TckSecurity.getAuthToken(security, TckPublisher.getRootPublisherId(), TckPublisher.getRootPassword());

            publisher = transport.getJUDDIApiService();
            if (!TckPublisher.isUDDIAuthMode()) {
                TckSecurity.setCredentials((BindingProvider) publisher, TckPublisher.getRootPublisherId(), TckPublisher.getRootPassword());
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not get authentication token", e);
        }
        JUDDI_300_MultiNodeIntegrationTest.testSetupReplicationConfig();
    }

    @AfterClass
    public static void stopRegistry() throws ConfigurationException {
        manager.stop();
    }

    @Test
    public void addClientSubscriptionInfo() throws Exception {
        if (!TckPublisher.isEnabled()) return;
        Assume.assumeTrue(TckPublisher.isJUDDI());

        ClientSubscriptionInfo clientSubscriptionInfo = new ClientSubscriptionInfo();

        Node node = new Node();
        node.setSecurityUrl("http://localhost:8080/services");
        node.setCustodyTransferUrl("http://localhost:8080/services");
        node.setDescription("TEST");
        node.setInquiryUrl("http://localhost:8080/services");
        node.setPublishUrl("http://localhost:8080/services");
        node.setSubscriptionListenerUrl("http://localhost:8080/services");
        node.setSubscriptionUrl("http://localhost:8080/services");
        node.setProxyTransport(org.apache.juddi.v3.client.transport.JAXWSTransport.class.getCanonicalName());

        node.setName("addClientSubscriptionInfoNode");
        node.setClientName("addClientSubscriptionInfoNode");

        Clerk clerk = new Clerk();
        clerk.setName("addClientSubscriptionInfoClerk");
        clerk.setPublisher("root");
        clerk.setNode(node);

        Clerk toClerk = new Clerk();
        toClerk.setName("addClientSubscriptionInfoClerk2");
        toClerk.setPublisher("root");
        toClerk.setNode(node);

        clientSubscriptionInfo.setFromClerk(clerk);
        clientSubscriptionInfo.setToClerk(toClerk);

        clientSubscriptionInfo.setSubscriptionKey("mykey");

        SaveClientSubscriptionInfo saveClientSubscriptionInfo = new SaveClientSubscriptionInfo();
        saveClientSubscriptionInfo.setAuthInfo(authInfo);
        saveClientSubscriptionInfo.getClientSubscriptionInfo().add(clientSubscriptionInfo);

        try {
            SaveNode sni = new SaveNode();
            sni.setAuthInfo(authInfo);
            sni.getNode().add(node);
            publisher.saveNode(sni);

            saveClerk(clerk);
            saveClerk(toClerk);
            ClientSubscriptionInfoDetail detail = publisher.saveClientSubscriptionInfo(saveClientSubscriptionInfo);

            Assert.assertEquals("mykey", detail.getClientSubscriptionInfo().get(0).getSubscriptionKey());

            DeleteClientSubscriptionInfo deleteInfo = new DeleteClientSubscriptionInfo();
            deleteInfo.setAuthInfo(authInfo);
            deleteInfo.getSubscriptionKey().add("mykey");
            publisher.deleteClientSubscriptionInfo(deleteInfo);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private Clerk saveClerk(Clerk clerk) throws Exception {
        SaveClerk saveClerkInfo = new SaveClerk();
        saveClerkInfo.setAuthInfo(authInfo);

        saveClerkInfo.getClerk().add(clerk);
        return publisher.saveClerk(saveClerkInfo).getClerk().get(0);
    }
}
