package org.apache.juddi.v3.bpel;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.juddi.v3.client.config.UDDIClient;
import org.apache.juddi.v3.client.mapping.wsdl.ReadWSDL;
import org.apache.juddi.v3.client.transport.Transport;
import org.apache.juddi.v3.tck.*;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.uddi.v3_service.UDDIInquiryPortType;
import org.uddi.v3_service.UDDIPublicationPortType;
import org.uddi.v3_service.UDDISecurityPortType;

import javax.xml.ws.BindingProvider;

public class BPEL_Abstract_IntegrationTest {

    private static Log logger = LogFactory.getLog(BPEL_Abstract_IntegrationTest.class);
    protected static TckTModel tckTModel = null;
    protected static TckBusinessService tckService = null;
    protected static TckBusiness tckBusiness = null;
    protected static String authInfoRiftSaw = null;
    protected static UDDIClient manager;
    static ReadWSDL rw;

    protected void assumeBPELEnabled() {
        boolean bpelEnabled = TckPublisher.isBPELEnabled();
        Assume.assumeTrue(bpelEnabled);
    }

    @BeforeClass
    public static void init() throws ConfigurationException {
        doInit();
    }

    @AfterClass
    public static void cleanup() throws ConfigurationException {
        tckTModel.deleteCreatedTModels(authInfoRiftSaw);
        manager.stop();
    }

    private static void doInit() throws ConfigurationException {
        logger.debug("Getting auth token for user riftsaw/riftsaw..");
        manager = new UDDIClient();
        manager.start();
        try {
            Transport transport = manager.getTransport("uddiv3");
            UDDISecurityPortType security = transport.getUDDISecurityService();
            authInfoRiftSaw = TckSecurity.getAuthToken(security, TckPublisher.getRiftSawPublisherId(), TckPublisher.getRiftSawPassword());
            UDDIPublicationPortType publication = transport.getUDDIPublishService();
            UDDIInquiryPortType inquiry = transport.getUDDIInquiryService();
            if (!TckPublisher.isUDDIAuthMode()) {
                TckSecurity.setCredentials((BindingProvider) publication, TckPublisher.getRiftSawPublisherId(), TckPublisher.getRiftSawPassword());
                TckSecurity.setCredentials((BindingProvider) inquiry, TckPublisher.getRiftSawPublisherId(), TckPublisher.getRiftSawPassword());
            }
            tckTModel = new TckTModel(publication, inquiry);
            tckService = new TckBusinessService(publication, inquiry);
            tckBusiness = new TckBusiness(publication, inquiry);
        } catch (Exception e) {
            throw new RuntimeException("Could not get authentication token", e);
        }
        rw = new ReadWSDL();
    }
}
