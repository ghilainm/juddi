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
