package org.apache.juddi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.juddi.v2.tck.TckPublisher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AbstractTckPublisher {

    private static Log logger = LogFactory.getLog(AbstractTckPublisher.class);

    protected static Properties loadTckProperties() {
        InputStream inputSteam = null;
        try {
            Properties tckProperties = new Properties();
            String s = System.getProperty("tck.properties");
            File f = null;
            if (s != null && s.length() != 0) {
                f = new File(s);
            }
            if (f == null || !f.exists()) {
                f = new File("tck.properties");
            }
            if (f.exists()) {

                inputSteam = new FileInputStream(f);
                logger.info("Loading tck.properties from " + f.getAbsolutePath());
            } else {
                inputSteam = TckPublisher.class.getResourceAsStream("/tck.properties");
                logger.info("Loading tck.properties as a classpath resource, probably within uddi-tck-base.jar");
            }
            tckProperties.load(inputSteam);
            return tckProperties;
        } catch (IOException ioe) {
            throw new RuntimeException("Could not load tck.properties", ioe);
        } finally {
            if (inputSteam != null) {
                try {
                    inputSteam.close();
                } catch (Exception ex) {
                }
            }
        }
    }
}
