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
package org.apache.juddi.registry;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.juddi.AbstractRegistry;
import org.apache.juddi.datatype.RegistryObject;
import org.apache.juddi.datatype.request.AuthInfo;
import org.apache.juddi.datatype.response.AuthToken;
import org.apache.juddi.error.RegistryException;
import org.apache.juddi.error.UnsupportedException;
import org.apache.juddi.function.FunctionMaker;
import org.apache.juddi.function.IFunction;
import org.apache.juddi.util.Config;
import org.apache.juddi.util.Loader;
import org.apache.juddi.util.jdbc.ConnectionManager;

/**
 * @author Steve Viens (sviens@apache.org)
 * @author Kurt Stam (kurt@osconsulting.org)
 */
public class RegistryEngine extends AbstractRegistry
{
  // Registry Property File Name
  private static final String PROPFILE_NAME = "juddi.properties";
  
  // Registry Property Names
  //
  public static final String PROPNAME_OPERATOR_NAME = "juddi.operatorName";
  
  public static final String PROPNAME_I18N_LANGUAGE_CODE = "juddi.i18n.languageCode";
  public static final String PROPNAME_I18N_COUNTRY_CODE = "juddi.i18n.countryCode";
  
  public static final String PROPNAME_DISCOVERY_URL = "juddi.discoveryURL";
  public static final String PROPNAME_ADMIN_EMAIL_ADDRESS = "juddi.adminEmailAddress"; // unused
  public static final String PROPNAME_DATASOURCE_NAME = "juddi.dataSource";
  public static final String PROPNAME_IS_USE_DATASOURCE = "juddi.isUseDataSource";
  public static final String PROPNAME_JDBC_DRIVER = "juddi.jdbcDriver";
  public static final String PROPNAME_JDBC_URL = "juddi.jdbcUrl";
  public static final String PROPNAME_JDBC_USERNAME = "juddi.jdbcUsername";
  public static final String PROPNAME_JDBC_PASSWORD = "juddi.jdbcPassword";
  public static final String PROPNAME_IS_CREATE_DATABASE = "juddi.isCreateDatabase";
  public static final String PROPNAME_DB_EXISTS_SQL = "juddi.databaseExistsSql";
  public static final String PROPNAME_SQL_FILES = "juddi.sqlFiles";
  public static final String PROPNAME_TABLE_PREFIX = "juddi.tablePrefix";
  
  public static final String PROPNAME_JAVA_NAMING_FACTORY_INITIAL = "java.naming.factory.initial";
  public static final String PROPNAME_JAVA_NAMING_PROVIDER_URL = "java.naming.provider.url";
  public static final String PROPNAME_JAVA_NAMING_FACTORY_URL_PKGS = "java.naming.factory.url.pkgs";
  
  public static final String PROPNAME_AUTH_CLASS_NAME = "juddi.auth";
  public static final String PROPNAME_DATASTORE_CLASS_NAME = "juddi.dataStore";
  public static final String PROPNAME_CRYPTOR_CLASS_NAME = "juddi.cryptor";
  public static final String PROPNAME_UUIDGEN_CLASS_NAME = "juddi.uuidgen";
  public static final String PROPNAME_VALIDATOR_CLASS_NAME = "juddi.validator";
  
  public static final String PROPNAME_MAX_NAME_ELEMENTS = "juddi.maxNameElementsAllowed";
  public static final String PROPNAME_MAX_NAME_LENGTH = "juddi.maxNameLengthAllowed";
  
  public static final String PROPNAME_MAX_BUSINESSES_PER_PUBLISHER = "juddi.maxBusinessesPerPublisher";
  public static final String PROPNAME_MAX_SERVICES_PER_BUSINESS = "juddi.maxServicesPerBusiness";
  public static final String PROPNAME_MAX_BINDINGS_PER_SERVICE = "juddi.maxBindingsPerService";
  public static final String PROPNAME_MAX_TMODELS_PER_PUBLISHER = "juddi.maxTModelsPerPublisher";

  public static final String PROPNAME_MAX_MESSAGE_SIZE = "juddi.maxMessageSize"; // unused
  public static final String PROPNAME_MAX_ROWS_LIMIT = "juddi.maxRowsLimit"; // unused
  
  // Registry Default Property Values
  //
  public static final String DEFAULT_OPERATOR_NAME = "Apache.org";
  
  public static final String DEFAULT_I18N_LANGUAGE_CODE = "en";
  public static final String DEFAULT_I18N_COUNTRY_CODE = "US";
  
  public static final String DEFAULT_DISCOVERY_URL = "http://localhost:8080/juddi/uddiget.jsp?";
  public static final String DEFAULT_ADMIN_EMAIL_ADDRESS = "nobody@apache.org"; // unused
  public static final String DEFAULT_DATASOURCE_NAME = "java:comp/env/jdbc/juddiDB";
  public static final Boolean DEFAULT_IS_USE_DATASOURCE = Boolean.TRUE;
  public static final String DEFAULT_JDBC_DRIVER = "com.mysql.jdbc.Driver";
  public static final String DEFAULT_JDBC_URL = "jdbc:mysql://localhost/juddi";
  public static final String DEFAULT_JDBC_USERNAME = "juddi";
  public static final String DEFAULT_JDBC_PASSWORD = "juddi";
  public static final Boolean DEFAULT_IS_CREATE_DATABASE = Boolean.FALSE;
  public static final String DEFAULT_DB_EXISTS_SQL = "select * from BUSINESS_ENTITY";
  public static final String DEFAULT_SQL_FILES = "sql/derby/create_database.sql,sql/insert_publishers.sql";
  public static final String DEFAULT_TABLE_PREFIX = "";
  
  public static final String DEFAULT_AUTH_CLASS_NAME = "org.apache.juddi.auth.DefaultAuthenticator";
  public static final String DEFAULT_DATASTORE_CLASS_NAME = "org.apache.juddi.datastore.jdbc.JDBCDataStore";
  public static final String DEFAULT_CRYPTOR_CLASS_NAME = "org.apache.juddi.cryptor.DefaultCryptor";
  public static final String DEFAULT_UUIDGEN_CLASS_NAME = "org.apache.juddi.uuidgen.DefaultUUIDGen";
  public static final String DEFAULT_VALIDATOR_CLASS_NAME = "org.apache.juddi.validator.DefaultValidator";
  
  public static final String DEFAULT_JAVA_NAMING_FACTORY_INITIAL = "org.jnp.interfaces.NamingContextFactory";
  public static final String DEFAULT_JAVA_NAMING_PROVIDER_URL = "jnp://localhost:1099";
  public static final String DEFAULT_JAVA_NAMING_FACTORY_URL_PKGS = "org.jboss.naming";
  
  public static final int    DEFAULT_MAX_NAME_ELEMENTS = 5;
  public static final int    DEFAULT_MAX_NAME_LENGTH = 255;    
  public static final int    DEFAULT_MAX_MESSAGE_SIZE = 2097152; // unused
  public static final int    DEFAULT_MAX_BUSINESSES_PER_PUBLISHER = -1; // unused
  public static final int    DEFAULT_MAX_SERVICES_PER_BUSINESS = -1; // unused
  public static final int    DEFAULT_MAX_BINDINGS_PER_SERVICE = -1; // unused
  public static final int    DEFAULT_MAX_TMODELS_PER_PUBLISHER = -1; // unused
  public static final int    DEFAULT_MAX_ROWS_LIMIT = 10; // unused
  
  // private reference to the jUDDI logger
  private static Log log = LogFactory.getLog(RegistryEngine.class);

  // Function maker
  private FunctionMaker maker = null;

  // registry status
  private boolean isAvailable = false;

  /**
   * Create a new instance of RegistryEngine.  This constructor
   * looks in the classpath for a file named 'juddi.properties'
   * and uses property values in this file to initialize the
   * new instance. Default values are used if the file does not
   * exist or if a particular property value is not present.
   */
  public RegistryEngine()
  {
    super();

    // Add jUDDI properties from the juddi.properties
    // file found in the classpath. Duplicate property
    // values added in init() will be overwritten.
    try {
      InputStream stream = Loader.getResourceAsStream(PROPFILE_NAME);
      if (stream != null)
      {
        Properties props = new Properties();
        props.load(stream);
        Config.addProperties(props);
      }
    }
    catch (IOException ioex) {
      log.error("An error occured while loading properties from: "+PROPFILE_NAME,ioex);
    }
  }

  /**
   * Creates a new instance of RegistryEngine. This constructor
   * uses the property values passed in the Properties parameter
   * to initialize the new RegistryProxy instance. Default values 
   * are used if the file does not exist or if a particular 
   * property value is not present.
   */
  public RegistryEngine(Properties props)
  {
    super();

    if (props != null)
      Config.addProperties(props);
  }

  /**
   * Initialize required resources.
   */
  public void init()
  {
    // Turn off registry access
    isAvailable = false;

    // Grab a reference to the function
    // registry (hmm bad name choice).
    this.maker = new FunctionMaker(this);
    
    if (Config.getBooleanProperty(
            RegistryEngine.PROPNAME_IS_CREATE_DATABASE,RegistryEngine.DEFAULT_IS_CREATE_DATABASE.booleanValue())) {
        // Initialize database
        initializeDatabase();
    }

    // Turn on registry access
    isAvailable = true;
  }

  /**
   * Releases any acquired resources. Will stop these
   * if they are currently running.
   */
  public void dispose()
  {
    // Turn off registry access
    isAvailable = false;
  }

  /**
   * Returns 'true' if the registry is available
   * to handle requests, otherwise returns 'false'.
   */
  public boolean isAvailable()
  {
    return this.isAvailable;
  }

  /**
   *
   */
  public RegistryObject execute(RegistryObject request)
    throws RegistryException
  {
    String className = request.getClass().getName();

    IFunction function = (IFunction)maker.lookup(className);
    if (function == null)
      throw new UnsupportedException(className);

    RegistryObject response = function.execute(request);

    return response;
  }
  
  private void initializeDatabase()
  {
      String dbExistsSql = Config.getStringProperty(
              RegistryEngine.PROPNAME_DB_EXISTS_SQL,RegistryEngine.DEFAULT_DB_EXISTS_SQL);
      String sqlFiles = Config.getStringProperty(
              RegistryEngine.PROPNAME_SQL_FILES,RegistryEngine.DEFAULT_SQL_FILES);
      String tablePrefix = Config.getStringProperty(
              RegistryEngine.PROPNAME_TABLE_PREFIX,RegistryEngine.DEFAULT_TABLE_PREFIX);
      
      try {
          Connection conn = ConnectionManager.acquireConnection();
          boolean create = false;

          Statement st = conn.createStatement();
          ResultSet rs = null;
          try {
             dbExistsSql = dbExistsSql.trim().replaceAll("\\$\\{prefix}", tablePrefix);
             rs = st.executeQuery(dbExistsSql);
             rs.close();
          } catch (SQLException e) {
             create = true;
          }
          st.close();
          if (!create) {
             log.debug("jUDDI Database is already initialized");
             return;
          }
          log.info("Initializing jUDDI database from listed sql files");
          String[] list = sqlFiles.split(",");
          for (int i=0; i<list.length; i++) {
             executeSql(list[i].trim(), conn, tablePrefix);
          }
      } catch (Exception e) {
          log.error("Could not create jUDDI database " + e.getMessage(), e);
      }
  }
  
  public void executeSql(String resource, Connection conn, String tablePrefix) throws Exception 
  { 
        InputStream is = Loader.getResourceAsStream(resource);
        if (is == null) {
            log.debug("Trying the classloader of the class itself. (workaround for maven2)");
            Loader loader = new Loader();
            is = loader.getResourceAsStreamFromClass(resource);
        }
        String sql = getString(is);
        sql = sql.replaceAll("(?m)^--([^\n]+)?$", ""); // Remove all commented lines
        sql = sql.replaceAll("\\$\\{prefix}", tablePrefix);
        is.close();
        String[] statements = sql.split(";");
        for (int i=0; i<statements.length;i++) {
            String statement = statements[i].trim();
            Statement sqlStatement = null;
            if (!"".equals(statement)) {
                try {
                    sqlStatement = conn.createStatement();
                    sqlStatement.executeUpdate(statement);
                } catch (Exception e) {
                    //ignore drop errors
                    if (!statement.toUpperCase().startsWith("DROP")) {
                        throw e;
                    }
                } finally {
                    sqlStatement.close();
                }
            }
        }
    }
  
  public static String getString(InputStream in) throws IOException {
      StringBuffer out = new StringBuffer();
      byte[] b = new byte[4096];
      for (int n; (n = in.read(b)) != -1;) {
          out.append(new String(b, 0, n));
      }
      return out.toString();
  }


  

  /***************************************************************************/
  /***************************** TEST DRIVER *********************************/
  /***************************************************************************/


  public static void main(String[] args)
    throws Exception
  {
    // Option #1 (grabs properties from juddi.properties file)
    //RegistryEngine registry = new RegistryEngine();
    
    // Option #2 (provides properties in Properties instance)
    Properties props = new Properties();
    props.setProperty(RegistryEngine.PROPNAME_OPERATOR_NAME,"jUDDI.org");
    props.setProperty(RegistryEngine.PROPNAME_MAX_NAME_ELEMENTS,"5");
    props.setProperty(RegistryEngine.PROPNAME_MAX_NAME_LENGTH,"255");
    props.setProperty(RegistryEngine.PROPNAME_DISCOVERY_URL,"http://localhost/juddi");
    props.setProperty(RegistryEngine.PROPNAME_ADMIN_EMAIL_ADDRESS,"admin@juddi.org");    
    props.setProperty(RegistryEngine.PROPNAME_MAX_MESSAGE_SIZE,"2097152");
    props.setProperty(RegistryEngine.PROPNAME_AUTH_CLASS_NAME,"org.apache.juddi.auth.DefaultAuthenticator");
    props.setProperty(RegistryEngine.PROPNAME_CRYPTOR_CLASS_NAME,"org.apache.juddi.cryptor.DefaultCryptor");
    props.setProperty(RegistryEngine.PROPNAME_UUIDGEN_CLASS_NAME,"org.apache.juddi.uuidgen.DefaultUUIDGen");
    
    props.setProperty("juddi.useConnectionPool","true");
    props.setProperty("juddi.jdbcDriver","com.mysql.jdbc.Driver");
    props.setProperty("juddi.jdbcURL","jdbc:mysql://localhost/juddi");
    props.setProperty("juddi.jdbcUser","juddi");
    props.setProperty("juddi.jdbcPassword","juddi");
    props.setProperty("juddi.jdbcMaxActive","10");
    props.setProperty("juddi.jdbcMaxIdle","10");
    
    RegistryEngine registry = new RegistryEngine(props);    

    // initialize the registry
    registry.init();

    // write all properties to the console
    System.out.println(Config.getProperties());

    AuthToken authToken = registry.getAuthToken("sviens","password");
    AuthInfo authInfo = authToken.getAuthInfo();

    System.out.println("AuthToken: "+authInfo.getValue());    

    // tear down the registry
    registry.dispose();
  }
}
