<%-- 
    Document   : index
    Created on : Feb 23, 2013, 2:05:35 PM
    Author     : Alex O'Ree
--%>


<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.apache.juddi.servlets.RegistryServlet"%>
<%@page import="java.util.SortedSet"%>
<%@page import="java.util.Properties"%>
<%@ page import="java.io.File,
         java.io.IOException,
         java.net.URL,
         java.net.JarURLConnection,
         java.sql.Connection,
         java.sql.ResultSet,
         java.sql.Statement,
         java.util.Properties,
         java.util.Iterator,
         java.util.SortedSet,
         java.util.TreeSet,
         javax.naming.Context,
         javax.naming.InitialContext,
         javax.sql.DataSource"


         %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header-top.jsp"%>

<%!

    /**
     * Look for the named class in the classpath
     *
     * @param name of the class to lookup
     * @return the location of the named class
     * @throws IOException
     */
    String lookupClass(String className) 
      throws IOException 
    {
      // load the class (if it exists)
      Class clazz = null;      
      try {
        clazz = Class.forName(className);
        if (clazz == null)
          return null;
      }
      catch (ClassNotFoundException e) {
        return null;
      }

      // class was found, now get it's URL
      URL url = null;
      try {
        url = clazz.getProtectionDomain().getCodeSource().getLocation();
        if (url == null)
          return "";
      }
      catch(Throwable t) {
        return "";
      }
      
      // got the classes URL, now determine it's location
      String location = getLocation(url);
      if (location == null) 
        return "";
      else
        return location;   
    }

    /**
     * Look for the named resource or properties file.
     *
     * @param resourceName
     * @return true if the file was found
     */
    String lookupResource(String resourceName) 
    {
      URL url = null;
      ClassLoader classLoader = null;

      classLoader = this.getClass().getClassLoader();
      if (classLoader != null) 
      {
        url = classLoader.getResource(resourceName);
        if (url != null) {
          return getLocation(url);
        }
      }
      else      
      {
        classLoader = System.class.getClassLoader(); 
        if (classLoader != null) 
        {
          url = classLoader.getResource(resourceName);
          if (url != null) {
            return getLocation(url);
          }
        }
        else
                       {
            //try to the thread context loader
        }
      }

      return null;
    }

    /**
     * Determine the location of the Java class.
     *
     * @param clazz
     * @return the file path to the jar file or class 
     *  file where the class was located.
     */
    String getLocation(URL url)
    {
      try
      {
        String location = url.toString();
        if (location.startsWith("jar:file:/"))
        {
          File file = new File(url.getFile());
          return file.getPath().substring(6);
        }
        else if (location.startsWith("jar")) 
        {
          url = ((JarURLConnection)url.openConnection()).getJarFileURL();
          return url.toString();
        }
        else if (location.startsWith("file")) 
        {
          File file = new File(url.getFile());
          return file.getAbsolutePath();
        }
        else
        {
          return url.toString();
        }
      } 
      catch (Throwable t) { 
        return null;
      }
    }
%>

<div class="container">

    <!-- Main hero unit for a primary marketing message or call to action -->
    <div class="well">
        <h1>Status and Statistics</h1>

    </div>

    <!-- Example row of columns -->
    <div class="row">


        <div class="span12">
            <script type="text/javascript">
            jQuery(document).ready(function ($) {
                $('#myTab').tab();
            });
                 
            </script>



            <ul class="nav nav-tabs" id="myTab" data-tabs="tabs">
                <li class="active"><a href="#status"  data-toggle="tab">Status</a></li>
                <li><a href="#stats"  data-toggle="tab">Statistics</a></li>

            </ul>

            <div class="tab-content">
                <div class="tab-pane active" id="status">

                    <h3>Happy jUDDI!</h3>

                    <h4>jUDDI Version Information</h4>

                    <b>jUDDI Version:</b> <%= org.apache.juddi.config.Release.getRegistryVersion() %><br>
                    <b>UDDI Version:</b>  <%= org.apache.juddi.config.Release.getUDDIVersion() %><br>


                    <h4>jUDDI Dependencies: Class Files &amp; Libraries</h4>
                    <pre><%
                                                    //creates the schema if not there
                                                 //  RegistryEngine registry = RegistryServlet.getRegistry();
                                                  //  registry.init();
    
                                                    String[] classArray = {
                                                      "org.apache.juddi.servlets.RegistryServlet",
                                                      "org.apache.juddi.servlets.NotifyServlet",
                                                      "org.apache.axis.transport.http.AxisServlet",
                                                      "org.springframework.web.context.ContextLoaderListener",
                                                      "org.apache.cxf.transport.servlet.CXFServlet",
                                                      "org.apache.commons.discovery.Resource",
                                                      "org.apache.commons.logging.Log",
                                                      "org.apache.log4j.Layout",
                                                      "javax.xml.soap.SOAPMessage",
                                                    //not used anymore  "javax.xml.rpc.Service",
                                                      "com.ibm.wsdl.factory.WSDLFactoryImpl",
                                                      "javax.xml.parsers.SAXParserFactory"
                                                    };
    
                                                    for (int i=0; i<classArray.length; i++)
                                                    {
                                                      out.write("<b>Looking for</b>: "+classArray[i]+"<br>");
      
                                                      String result = lookupClass(classArray[i]);
                                                      if (result == null)
                                                      {
                                                        out.write("<font color=\"red\">-Not Found</font><br>");
                                                      }
                                                      else if (result.length() == 0)
                                                      {        
                                                        out.write("<font color=\"blue\">+Found in an unknown location</font><br>");
                                                      }
                                                      else
                                                      {        
                                                        out.write("<font color=\"green\">+Found in: "+ result +"</font><br>");
                                                      }
                                                    }   
                        %></pre>

                    <h4>jUDDI Dependencies: Resource &amp; Properties Files</h4>
                    <pre><%
                                                    String[] resourceArray = {
                                                      "log4j.xml",
                                                      "juddiv3.properties",
                                                      "context.xml",
                                                      "beans.xml",
                                                      "persistence.xml"
                                                    };
    
                                                    for (int i=0; i<resourceArray.length; i++)
                                                    {
                                                      out.write("<b>Looking for</b>: "+resourceArray[i]+"<br>");
      
                                                      String result = lookupResource(resourceArray[i]);
                                                      if (result == null)
                                                      {
                                                        out.write("<font color=\"red\">-Not Found</font><br>");
                                                      }
                                                      else if (result.length() == 0)
                                                      {        
                                                        out.write("<font color=\"blue\">+Found in an unknown location</font><br>");
                                                      }
                                                      else
                                                      {        
                                                        out.write("<font color=\"green\">+Found in: "+ result +"</font><br>");
                                                      }
                                                    }   
                        %></pre>

                    <h4>jUDDI DataSource Validation</h4>
                    <pre><%
                                                  String dsname = null;
                                                  Context ctx = null;
                                                  DataSource ds = null;
                                                  Connection conn = null;
                                                  String sql = "SELECT COUNT(*) FROM PUBLISHER";
  
                                                  try
                                                  {
                                                    dsname = request.getParameter("dsname");
                                                    if ((dsname == null) || (dsname.trim().length() == 0)) {
                                                      dsname = "java:comp/env/jdbc/juddiDB";
                                                    } else {
                                                      dsname = StringEscapeUtils.escapeXml(dsname); 
                                                    }
    
                                                    ctx = new InitialContext();
                                                    if (ctx == null )
                                                      throw new Exception("No Context");
  
                                                    out.print("<font color=\"green\">");
                                                    out.print("+ Got a JNDI Context!");
                                                    out.println("</font>");
                                                  }
                                                  catch(Exception ex)
                                                  {
                                                    out.print("<font color=\"red\">");
                                                    out.print("- No JNDI Context ("+ex.getMessage()+")");
                                                    out.println("</font>");
                                                  }

                                                  try
                                                  {
                                                    ds = (DataSource)ctx.lookup(dsname);
                                                    if (ds == null)
                                                      throw new Exception("No Context");

                                                    out.print("<font color=\"green\">");
                                                    out.print("+ Got a JDBC DataSource (dsname="+dsname+")");
                                                    out.println("</font>");
                                                  }
                                                  catch(Exception ex)
                                                  {
                                                    out.print("<font color=\"red\">");
                                                    out.print("- No '"+dsname+"' DataSource Located("+ex.getMessage()+")");
                                                    out.println("</font>");
                                                  }
  
                                                  try
                                                  {
                                                    conn = ds.getConnection();
                                                    if (conn == null)
                                                    throw new Exception("No Connection (conn=null)");  

                                                    out.print("<font color=\"green\">");
                                                    out.print("+ Got a JDBC Connection!");
                                                    out.println("</font>");
                                                  }
                                                  catch(Exception ex)
                                                  {
                                                    out.print("<font color=\"red\">");
                                                    out.print("- DB connection was not acquired. ("+ex.getMessage()+")");
                                                    out.println("</font>");
                                                  }
  
                                                  try
                                                  {
                                                    Statement stmt = conn.createStatement();
                                                    ResultSet rs = stmt.executeQuery(sql);

                                                    out.print("<font color=\"green\">");
                                                    out.print("+ "+sql+" = ");
                                                    if (rs.next())
                                                      out.print(rs.getString(1));
                                                    out.println("</font>");

                                                    conn.close();
                                                  }
                                                  catch (Exception ex)
                                                  {
                                                    out.print("<font color=\"red\">");
                                                    out.print("- "+sql+" failed ("+ex.getMessage()+")");
                                                    out.println("</font>");
                                                  }
                        %></pre>


                    <h4>System Properties</h4>
                    <pre><%
                                                  try
                                                  {
                                                    Properties sysProps = System.getProperties();
                                                    SortedSet sortedProperties = new TreeSet(sysProps.keySet()); 
                                                    for (Iterator keys = sortedProperties.iterator(); keys.hasNext();)
                                                    {
                                                      String key = (String)keys.next();
                                                      out.println("<b>"+key + "</b>: " + sysProps.getProperty(key));
                                                    }
                                                  }
                                                  catch(Exception e)
                                                  {
                                                    e.printStackTrace();
                                                  }
                        %></pre>

                    <h4>
                        Platform</h4>
                    <pre><%= getServletConfig().getServletContext().getServerInfo() %></pre>

                </div>
                <div class="tab-pane" id="stats"><img src="img/bigrollergreen.gif"></div>

            </div>









            <script type="text/javascript">
                $.getJSON('mbeans.jsp', function(data) {
                    var items = [];

 
 
                    items.push("<table class=\"table table-hover\">");
                    $.each(data, function(key, val) {
                        
                        
                        
                        items.push('<tr><td>' +key +"</td><td>"+ val+ '</</td></tr>');    
                        
                        
                    });
                    items.push("</table>");
                    $("#stats").html(items.join(' '));
                });
            </script>
        </div>

    </div>
    <%@include file="header-bottom.jsp"%>