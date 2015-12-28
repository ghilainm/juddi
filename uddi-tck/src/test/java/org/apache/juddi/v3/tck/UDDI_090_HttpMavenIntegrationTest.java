/*
 * Copyright 2014 The Apache Software Foundation.
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
package org.apache.juddi.v3.tck;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import javax.xml.ws.Endpoint;
import java.util.Iterator;
import java.util.Random;

/**
 * @author Alex O'Ree
 */
public class UDDI_090_HttpMavenIntegrationTest extends UDDI_090_SubscriptionListenerIntegrationBase {

    private static Endpoint endPoint;
    private static String hostname;
    private static int port = 0;

    @AfterClass
    public static void stop() throws ConfigurationException {
        if (!TckPublisher.isEnabled()) {
            return;
        }
        stopManager();
        endPoint.stop();
        endPoint = null;

    }

    @BeforeClass
    public static void startup() throws Exception {

        if (!TckPublisher.isEnabled()) {
            return;
        }
        startManager();
        hostname = "localhost";
        //bring up the TCK SubscriptionListener
        port = 9600;
        String httpEndpoint = "http://" + hostname + ":" + port + "/tcksubscriptionlistener";
        System.out.println("Bringing up SubscriptionListener endpoint at " + httpEndpoint);
        endPoint = Endpoint.publish(httpEndpoint, new UDDISubscriptionListenerImpl());
        int count = 0;
        while (!endPoint.isPublished()) {
            port = 9600 + new Random().nextInt(99);
            httpEndpoint = "http://" + hostname + ":" + port + "/tcksubscriptionlistener";
            System.out.println("Bringing up SubscriptionListener endpoint at " + httpEndpoint);
            endPoint = Endpoint.publish(httpEndpoint, new UDDISubscriptionListenerImpl());
            count++;
            if (count > 10) {
                Assert.fail("unable to bring up endpoint");
            }
        }
    }

    @Override
    public boolean verifyDelivery(String findMe) {
        for (int i = 0; i < TckPublisher.getSubscriptionTimeout(); i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
            System.out.println(".");
            //if (UDDISubscriptionListenerImpl.notificationCount > 0) {                        }
        }
        logger.info("RX " + UDDISubscriptionListenerImpl.notificationCount + " notifications");
        Iterator<String> it = UDDISubscriptionListenerImpl.notifcationMap.values().iterator();
        boolean found = false;

        while (it.hasNext()) {
            String test = it.next();
            if (TckCommon.isDebug()) {
                logger.info("Notification: " + test);
            }
            if (test.toLowerCase().contains(findMe.toLowerCase())) {
                found = true;
            }
        }
        if (!found) {
            logger.error("_________________________________________________the test failed!!!");
            it = UDDISubscriptionListenerImpl.notifcationMap.values().iterator();
            Thread.dumpStack();
            while (it.hasNext()) {
                logger.info("Notification: " + it.next());
            }
        }
        return found;
    }

    @Override
    public void reset() {
        UDDISubscriptionListenerImpl.notifcationMap.clear();
        UDDISubscriptionListenerImpl.notificationCount = 0;
    }

    @Override
    public String getXMLLocationOfServiceForDelivery() {
        return TckSubscriptionListener.LISTENER_HTTP_SERVICE_XML;
    }

    @Override
    public String getTransport() {
        return "HTTP_MAVEN";
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getHostame() {
        return hostname;
    }

    @Override
    public String getSubscription1XML() {
        return TckSubscriptionListener.SUBSCRIPTION_XML;
    }

    @Override
    public String getSubscription2XML() {
        return TckSubscriptionListener.SUBSCRIPTION2_XML;
    }

    @Override
    public String getSubscription3XML() {
        return TckSubscriptionListener.SUBSCRIPTION3_XML;
    }

    @Override
    public String getSubscriptionKey1() {
        return TckSubscriptionListener.SUBSCRIPTION_KEY;
    }

    @Override
    public String getSubscriptionKey2() {
        return TckSubscriptionListener.SUBSCRIPTION_KEY;
    }

    @Override
    public String getSubscriptionKey3() {
        return TckSubscriptionListener.SUBSCRIPTION_KEY;
    }

    @Override
    public boolean IsEnabled() {
        return true;
    }

}
