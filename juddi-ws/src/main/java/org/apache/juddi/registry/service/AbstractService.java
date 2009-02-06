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
package org.apache.juddi.registry.service;

import java.io.IOException;
import java.util.Vector;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Detail;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.juddi.IRegistry;
import org.apache.juddi.registry.RegistryEngine;
import org.apache.juddi.registry.RegistryServlet;
import org.apache.juddi.datatype.RegistryObject;
import org.apache.juddi.datatype.response.DispositionReport;
import org.apache.juddi.datatype.response.ErrInfo;
import org.apache.juddi.datatype.response.Result;
import org.apache.juddi.error.BusyException;
import org.apache.juddi.error.FatalErrorException;
import org.apache.juddi.error.RegistryException;
import org.apache.juddi.error.UnsupportedException;
import org.apache.juddi.handler.HandlerMaker;
import org.apache.juddi.handler.IHandler;
import org.apache.juddi.util.Config;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Steve Viens (sviens@apache.org)
 */
public abstract class AbstractService extends HttpServlet
{
  // private reference to the webapp's logger.
  private static Log log = LogFactory.getLog(AbstractService.class);
  
  // XML Document Builder
  private static DocumentBuilder docBuilder = null;

  /**
   *
   */
  public void doGet(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException
  {
    res.setHeader("Allow","POST");
    res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,"Use of the " +
      "HTTP request method 'GET' is not allowed by UDDI specification.");
  }

  /**
   *
   */
  public void doPost(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException
  {
    res.setContentType("text/xml; charset=utf-8");        

    SOAPMessage soapReq = null;
    SOAPMessage soapRes = null;

    try 
    {
      // Create a MessageFactory, parse the XML found
      // in the HTTP payload into a SOAP request message 
      // and create a new SOAP response message.

      MessageFactory msgFactory = MessageFactory.newInstance();
      soapReq = msgFactory.createMessage(null,req.getInputStream());
      soapRes = msgFactory.createMessage();
           
      // Extract the UDDI request from the SOAPBody
      // of the SOAP Request message. If a UDDI request
      // message does not exist within the SOAP Body
      // then throw a FatalErrorException and indicate
      // that this is a Client-side (consumer) error.

      SOAPBody soapReqBody = soapReq.getSOAPBody();
      Element uddiReq = null;
      Iterator itrChildElements = soapReqBody.getChildElements();
      while (itrChildElements.hasNext()) {
    	  Object obj = (Object)itrChildElements.next();
    	  if (obj instanceof Element) {
    		  uddiReq = (Element)obj;
    		  break;
    	  }
      }
      if (uddiReq == null)
        throw new FatalErrorException("A UDDI request was not " +
          "found in the SOAP message.");
      
      // Grab the local name of the UDDI request element
      // from the UDDI Request. If a value isn't returned 
      // (either null or an empty String is returned) then 
      // throw a FatalError exception. This is probably a 
      // configuration problem related to the XML Parser 
      // that jUDDI is using.
      
      String operation = uddiReq.getLocalName();
      if ((operation == null) || (operation.trim().length() == 0))
        throw new FatalErrorException("The UDDI service operation " +
          "could not be identified.");
      
      // Grab the generic attribute value (version value).  If 
      // one isn't specified or the value specified is not "2.0" 
      // then throw an exception (this value must be specified 
      // for all UDDI requests and currently only vesion 2.0
      // UDDI requests are supported).

      String version = uddiReq.getAttribute("generic");
      if (version == null)
        throw new FatalErrorException("A UDDI generic attribute " +
          "value was not found for UDDI request: "+operation+" (The " +
          "'generic' attribute must be present)");

      // Verify that the appropriate endpoint was targeted for
      // this service request.  The validateRequest method will
      // throw an UnsupportedException if anything's amiss.

      validateRequest(operation,version,uddiReq);
      
      // Lookup the appropriate XML handler.  Throw an 
      // UnsupportedException if one could not be located.

      HandlerMaker maker = HandlerMaker.getInstance();
			IHandler requestHandler = maker.lookup(operation);
      if (requestHandler == null)
        throw new UnsupportedException("The UDDI service operation " +
          "specified is unknown or unsupported: " +operation);
      
      // Unmarshal the raw xml into the appropriate jUDDI
      // request object.

      RegistryObject uddiReqObj = requestHandler.unmarshal(uddiReq);
      
      // Grab a reference to the shared jUDDI registry 
      // instance (make sure it's running) and execute the 
      // requested UDDI function.
      
      RegistryObject uddiResObj = null;      
      RegistryEngine registry = RegistryServlet.getRegistry();
      if ((registry != null) && (registry.isAvailable()))
        uddiResObj = registry.execute(uddiReqObj);
      else
        throw new BusyException("The Registry is currently unavailable.");
      
      // Lookup the appropriate response handler which will
      // be used to marshal the UDDI object into the appropriate 
      // xml format.
      
      IHandler responseHandler = maker.lookup(uddiResObj.getClass().getName());
      if (responseHandler == null)
        throw new FatalErrorException("The response object " +
          "type is unknown: " +uddiResObj.getClass().getName());
      
      // Create a new 'temp' XML element to use as a container 
      // in which to marshal the UDDI response data into.
        
      DocumentBuilder docBuilder = getDocumentBuilder();
      Document document = docBuilder.newDocument();
      Element element = document.createElement("temp");
        
      // Lookup the appropriate response handler and marshal 
      // the juddi object into the appropriate xml format (we 
      // only support UDDI v2.0 at this time).  Attach the
      // results to the body of the SOAP response.
        
      responseHandler.marshal(uddiResObj,element);
      
      // Grab a reference to the 'temp' element's
      // only child here (this has the effect of
      // discarding the temp element) and append 
      // this child to the soap response body
      
      document.appendChild(element.getFirstChild());
      soapRes.getSOAPBody().addDocument(document);
    } 
    catch(Exception ex) // Catch ALL exceptions
    {
      // SOAP Fault values
      String faultCode = null;
      String faultString = null;
      String faultActor = null;
      
      // UDDI DispositionReport values
      String errno = null;
      String errCode = null;
      String errText = null;
      
      // All RegistryException and subclasses of RegistryException
      // should contain values for populating a SOAP Fault as well
      // as a UDDI DispositionReport with specific information 
      // about the problem.
      //
      // We've got to dig out the dispositionReport and populate  
      // the SOAP Fault 'detail' element with this information.        
      
      if (ex instanceof RegistryException)
      {
        // Since we've intercepted a RegistryException type
        // then we can assume this is a "controlled" event
        // and simply log the error message without a stack
        // trace.

        log.error(ex.getMessage());

        RegistryException rex = (RegistryException)ex;
        
        faultCode = rex.getFaultCode();  // SOAP Fault faultCode
        faultString = rex.getFaultString();  // SOAP Fault faultString
        faultActor = rex.getFaultActor();  // SOAP Fault faultActor
        
        DispositionReport dispRpt = rex.getDispositionReport();
        if (dispRpt != null)
        {
          Result result = null;
          ErrInfo errInfo = null;
        
          Vector results = dispRpt.getResultVector();
          if ((results != null) && (!results.isEmpty()))
            result = (Result)results.elementAt(0);
        
          if (result != null)
          {
            errno = String.valueOf(result.getErrno());  // UDDI Result errno
            errInfo = result.getErrInfo();
          
            if (errInfo != null)
            {
              errCode = errInfo.getErrCode();  // UDDI ErrInfo errCode
              errText = errInfo.getErrMsg();  // UDDI ErrInfo errMsg
            }
          }
        }
      }
      else if (ex instanceof SOAPException)
      {
        log.error(ex.getMessage());
          
        // Because something occured that jUDDI wasn't expecting
        // let's set default SOAP Fault values.  Since SOAPExceptions
        // here are most likely XML validation errors let's blame the
        // client by placing "Client" in the Fault Code and pass
        // the Exception message back to the client.
        
        faultCode = "Client";
        faultString = ex.getMessage();
        faultActor = null;
        
        // Let's set default values for the UDDI DispositionReport
        // here.  While we didn't catch a RegistryException (or 
        // subclass) we're going to be friendly and include a
        // FatalError DispositionReport within the message from the 
        // SAX parsing problem in the SOAP Fault anyway.
        
        errno = String.valueOf(Result.E_FATAL_ERROR);
        errCode = Result.lookupErrCode(Result.E_FATAL_ERROR); 
        errText = Result.lookupErrText(Result.E_FATAL_ERROR) + 
                  " " + ex.getMessage();
      }
      else // anything else
      {
        // All other exceptions (other than SOAPException or 
        // RegistryException and subclasses) are either a result 
        // of a jUDDI configuration problem or something that 
        // we *should* be catching and converting to a 
        // RegistryException but are not (yet!).
            
        log.error(ex.getMessage(),ex);

        // Because something occured that jUDDI wasn't expecting
        // let's set default SOAP Fault values.  Since jUDDI
        // should be catching anything significant let's blame 
        // jUDDI by placing "Server" in the Fault Code and pass
        // the Exception message on to the client.
        
        faultCode = "Server";
        faultString = ex.getMessage();
        faultActor = null;
          
        // Let's set default values for the UDDI DispositionReport
        // here.  While we didn't catch a RegistryException (or 
        // subclass) but we're going to be friendly and include a
        // FatalError DispositionReport within the SOAP Fault anyway.
        
        errno = String.valueOf(Result.E_FATAL_ERROR);
        errCode = Result.lookupErrCode(Result.E_FATAL_ERROR); 
        errText = Result.lookupErrText(Result.E_FATAL_ERROR) +
                  " An internal UDDI server error has " +
                  "occurred. Please report this error " +
                  "to the UDDI server administrator.";
      }
      
      // We should have everything we need to assemble 
      // the SOAPFault so lets piece it together and 
      // send it on it's way.
      
      try {
        SOAPBody soapResBody = soapRes.getSOAPBody();     
        SOAPFault soapFault = soapResBody.addFault();
        soapFault.setFaultCode(faultCode);
        soapFault.setFaultString(faultString);
        soapFault.setFaultActor(faultActor);
        
        // We're always going to include a DispositionReport (for
        // the hell of it) so that's what we're doing here.
       
        Detail faultDetail = soapFault.addDetail();
        
        SOAPElement dispRpt = faultDetail.addChildElement("dispositionReport","",IRegistry.UDDI_V2_NAMESPACE);        
        dispRpt.setAttribute("generic",IRegistry.UDDI_V2_GENERIC);
        dispRpt.setAttribute("operator",Config.getOperator());
        
        SOAPElement result = dispRpt.addChildElement("result");
        result.setAttribute("errno",errno);
        
        SOAPElement errInfo = result.addChildElement("errInfo");
        errInfo.setAttribute("errCode",errCode);
        errInfo.setValue(errText);
      } 
      catch (Exception e) { // if we end up in here it's just NOT good.
        log.error("A serious error has occured while assembling the SOAP Fault.",e);
      }
    }
    finally 
    {
      try {  
        soapRes.setProperty(SOAPMessage.WRITE_XML_DECLARATION,"true");
        soapRes.writeTo(res.getOutputStream()); 
      }
      catch(SOAPException sex) {
        log.error(sex);
      }
    }
  }

  /**
   * 
   */
  public abstract void validateRequest(String operation,String generic,Element uddiReq)
  	throws RegistryException;
  
  /**
   *
   */
  private DocumentBuilder getDocumentBuilder()
  {
    if (docBuilder == null)
      docBuilder = createDocumentBuilder();    
    return docBuilder;
  }

  /**
   *
   */
  private synchronized DocumentBuilder createDocumentBuilder()
  {
    if (docBuilder != null)
      return docBuilder;

    try {
     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
     //factory.setNamespaceAware(true);
     //factory.setValidating(true);

     docBuilder = factory.newDocumentBuilder();
    }
    catch(ParserConfigurationException pcex) {
      pcex.printStackTrace();
    }

    return docBuilder;
  }
}