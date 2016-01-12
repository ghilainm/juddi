/*
 * Copyright 2001-2004 The Apache Software Foundation.
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
package org.apache.juddi.servlets;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.juddi.Registry;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * This servlet is ONLY used to initialize the jUDDI webapp on startup and
 * cleanup the jUDDI webapp on shutdown.
 *
 * @author Steve Viens (sviens@apache.org)
 */
public class RegistryServlet implements ServletContextListener {

	private static final long serialVersionUID = 4653310291840334765L;
	private static Log logger = LogFactory.getLog(RegistryServlet.class);

	/**
	 * Create the shared instance of jUDDI's Registry class and call it's
	 * "init()" method to initialize all core components.
	 */
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		try {
			Registry.start();
		} catch (ConfigurationException e) {
			logger.fatal("jUDDI registry could not be started."
					+ e.getMessage(), e);
			throw new RuntimeException("jUDDI registry could not be started.", e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		try {
			Registry.stop();
		} catch (ConfigurationException e) {
			logger.fatal("jUDDI registry could not be stopped."
					+ e.getMessage(), e);
			throw new RuntimeException("jUDDI registry could not be stopped.", e);
		}
	}
}