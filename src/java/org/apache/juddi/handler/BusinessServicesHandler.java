/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 2001-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "jUDDI" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.juddi.handler;

import java.util.Vector;

import org.apache.juddi.datatype.CategoryBag;
import org.apache.juddi.datatype.Description;
import org.apache.juddi.datatype.KeyedReference;
import org.apache.juddi.datatype.Name;
import org.apache.juddi.datatype.OverviewDoc;
import org.apache.juddi.datatype.RegistryObject;
import org.apache.juddi.datatype.binding.AccessPoint;
import org.apache.juddi.datatype.binding.BindingTemplate;
import org.apache.juddi.datatype.binding.BindingTemplates;
import org.apache.juddi.datatype.binding.HostingRedirector;
import org.apache.juddi.datatype.binding.InstanceDetails;
import org.apache.juddi.datatype.binding.TModelInstanceDetails;
import org.apache.juddi.datatype.binding.TModelInstanceInfo;
import org.apache.juddi.datatype.service.BusinessService;
import org.apache.juddi.datatype.service.BusinessServices;
import org.apache.juddi.util.xml.XMLUtils;
import org.w3c.dom.Element;

/**
 * "Knows about the creation and populating of BusinessServices objects.
 * Returns BusinessServices."
 *
 * @author Steve Viens (sviens@apache.org)
 */
public class BusinessServicesHandler extends AbstractHandler
{
  public static final String TAG_NAME = "businessServices";

  private HandlerMaker maker = null;

  protected BusinessServicesHandler(HandlerMaker maker)
  {
    this.maker = maker;
  }

  public RegistryObject unmarshal(Element element)
  {
    BusinessServices obj = new BusinessServices();
    Vector nodeList = null;
    AbstractHandler handler = null;

    // Attributes
    // {none}

    // Text Node Value
    // {none}

    // Child Elements
    nodeList = XMLUtils.getChildElementsByTagName(element,BusinessServiceHandler.TAG_NAME);
    for (int i=0; i<nodeList.size(); i++)
    {
      handler = maker.lookup(BusinessServiceHandler.TAG_NAME);
      obj.addBusinessService((BusinessService)handler.unmarshal((Element)nodeList.elementAt(i)));
    }

    return obj;
  }

  public void marshal(RegistryObject object,Element parent)
  {
    BusinessServices services = (BusinessServices)object;
    Element element = parent.getOwnerDocument().createElement(TAG_NAME);
    AbstractHandler handler = null;

    Vector serviceVector = services.getBusinessServiceVector();
    if ((serviceVector!=null) && (serviceVector.size() > 0))
    {
      handler = maker.lookup(BusinessServiceHandler.TAG_NAME);
      for (int i=0; i < serviceVector.size(); i++)
        handler.marshal((BusinessService)serviceVector.elementAt(i),element);
    }

    parent.appendChild(element);
  }


  /***************************************************************************/
  /***************************** TEST DRIVER *********************************/
  /***************************************************************************/


  public static void main(String args[])
    throws Exception
  {
    HandlerMaker maker = HandlerMaker.getInstance();
    AbstractHandler handler = maker.lookup(BusinessServicesHandler.TAG_NAME);
    Element parent = XMLUtils.newRootElement();
    Element child = null;

    OverviewDoc overDoc = new OverviewDoc();
    overDoc.setOverviewURL("http://www.sviens.com/service.html");
    overDoc.addDescription(new Description("over-doc Descr"));
    overDoc.addDescription(new Description("over-doc Descr Two","en"));

    InstanceDetails instDetails = new InstanceDetails();
    instDetails.addDescription(new Description("body-isa-wunder"));
    instDetails.addDescription(new Description("sweetchild-o-mine","it"));
    instDetails.setInstanceParms("inst-det parameters");
    instDetails.setOverviewDoc(overDoc);

    TModelInstanceInfo tModInstInfo = new TModelInstanceInfo();
    tModInstInfo.setTModelKey("uuid:ae0f9fd4-287f-40c9-be91-df47a7c72fd9");
    tModInstInfo.addDescription(new Description("tMod-Inst-Info"));
    tModInstInfo.addDescription(new Description("tMod-Inst-Info too","es"));
    tModInstInfo.setInstanceDetails(instDetails);

    TModelInstanceDetails tModInstDet = new TModelInstanceDetails();
    tModInstDet.addTModelInstanceInfo(tModInstInfo);

    BindingTemplate binding =  new BindingTemplate();
    binding.setBindingKey("c9613c3c-fe55-4f34-a3da-b3167afbca4a");
    binding.setServiceKey("997a55bc-563d-4c04-8a94-781604870d31");
    binding.addDescription(new Description("whatever"));
    binding.addDescription(new Description("whatever too","fr"));
    binding.setHostingRedirector(new HostingRedirector("92658289-0bd7-443c-8948-0bb4460b44c0"));
    binding.setAccessPoint(new AccessPoint(AccessPoint.HTTP,"http://www.sviens.com/service.wsdl"));
    binding.setTModelInstanceDetails(tModInstDet);

    BindingTemplates bindings = new BindingTemplates();
    bindings.addBindingTemplate(binding);

    CategoryBag catBag = new CategoryBag();
    catBag.addKeyedReference(new KeyedReference("keyName","keyValue"));
    catBag.addKeyedReference(new KeyedReference("uuid:dfddb58c-4853-4a71-865c-f84509017cc7","keyName2","keyValue2"));

    BusinessService service = new BusinessService();
    service.setServiceKey("fe8af00d-3a2c-4e05-b7ca-95a1259aad4f");
    service.setBusinessKey("b8cc7266-9eed-4675-b621-34697f252a77");
    service.setBindingTemplates(bindings);
    service.setCategoryBag(catBag);
    service.addName(new Name("serviceNm"));
    service.addName(new Name("serviceNm2","en"));
    service.addDescription(new Description("service whatever"));
    service.addDescription(new Description("service whatever too","it"));

    BusinessServices services = new BusinessServices();
    services.addBusinessService(service);
    services.addBusinessService(service);

    System.out.println();

    RegistryObject regObject = services;
    handler.marshal(regObject,parent);
    child = (Element)parent.getFirstChild();
    parent.removeChild(child);
    XMLUtils.writeXML(child,System.out);

    System.out.println();

    regObject = handler.unmarshal(child);
    handler.marshal(regObject,parent);
    child = (Element)parent.getFirstChild();
    parent.removeChild(child);
    XMLUtils.writeXML(child,System.out);

    System.out.println();
  }
}