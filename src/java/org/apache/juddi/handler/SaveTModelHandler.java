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
import org.apache.juddi.datatype.IdentifierBag;
import org.apache.juddi.datatype.KeyedReference;
import org.apache.juddi.datatype.OverviewDoc;
import org.apache.juddi.datatype.RegistryObject;
import org.apache.juddi.datatype.UploadRegister;
import org.apache.juddi.datatype.request.AuthInfo;
import org.apache.juddi.datatype.request.SaveTModel;
import org.apache.juddi.datatype.tmodel.TModel;
import org.apache.juddi.util.xml.XMLUtils;
import org.w3c.dom.Element;

/**
 * @author Steve Viens (sviens@apache.org)
 */
public class SaveTModelHandler extends AbstractHandler
{
  public static final String TAG_NAME = "save_tModel";

  private HandlerMaker maker = null;

  protected SaveTModelHandler(HandlerMaker maker)
  {
    this.maker = maker;
  }

  public RegistryObject unmarshal(Element element)
  {
    SaveTModel obj = new SaveTModel();
    Vector nodeList = null;
    AbstractHandler handler = null;

    // Attributes
    String generic = element.getAttribute("generic");
    if ((generic != null && (generic.trim().length() > 0)))
      obj.setGeneric(generic);

    // Text Node Value
    // {none}

    // Child Elements
    nodeList = XMLUtils.getChildElementsByTagName(element,AuthInfoHandler.TAG_NAME);
    if (nodeList.size() > 0)
    {
      handler = maker.lookup(AuthInfoHandler.TAG_NAME);
      obj.setAuthInfo((AuthInfo)handler.unmarshal((Element)nodeList.elementAt(0)));
    }

    nodeList = XMLUtils.getChildElementsByTagName(element,TModelHandler.TAG_NAME);
    for (int i=0; i<nodeList.size(); i++)
    {
      handler = maker.lookup(TModelHandler.TAG_NAME);
      obj.addTModel((TModel)handler.unmarshal((Element)nodeList.elementAt(i)));
    }

    nodeList = XMLUtils.getChildElementsByTagName(element,UploadRegisterHandler.TAG_NAME);
    for (int i=0; i<nodeList.size(); i++)
    {
      handler = maker.lookup(UploadRegisterHandler.TAG_NAME);
      obj.addUploadRegister((UploadRegister)handler.unmarshal((Element)nodeList.elementAt(i)));
    }

    return obj;
  }

  public void marshal(RegistryObject object,Element parent)
  {
    SaveTModel request = (SaveTModel)object;
    Element element = parent.getOwnerDocument().createElement(TAG_NAME);
    AbstractHandler handler = null;

    String generic = request.getGeneric();
    if (generic != null)
      element.setAttribute("generic",generic);

    AuthInfo authInfo = request.getAuthInfo();
    if (authInfo != null)
    {
      handler = maker.lookup(AuthInfoHandler.TAG_NAME);
      handler.marshal(authInfo,element);
    }

    Vector tModelVector = request.getTModelVector();
    if ((tModelVector!=null) && (tModelVector.size() > 0))
    {
      handler = maker.lookup(TModelHandler.TAG_NAME);
      for (int i=0; i<tModelVector.size(); i++)
        handler.marshal((TModel)tModelVector.elementAt(i),element);
    }

    Vector urVector = request.getUploadRegisterVector();
    if ((urVector!=null) && (urVector.size() > 0))
    {
      handler = maker.lookup(UploadRegisterHandler.TAG_NAME);
      for (int i=0; i<urVector.size(); i++)
        handler.marshal((UploadRegister)urVector.elementAt(i),element);
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
    AbstractHandler handler = maker.lookup(SaveTModelHandler.TAG_NAME);

    Element parent = XMLUtils.newRootElement();
    Element child = null;

    AuthInfo authInfo = new AuthInfo();
    authInfo.setValue("6f157513-844e-4a95-a856-d257e6ba9726");

    OverviewDoc overDoc = new OverviewDoc();
    overDoc.setOverviewURL("http://www.sviens.com/service.html");
    overDoc.addDescription(new Description("over-doc Descr"));
    overDoc.addDescription(new Description("over-doc Descr Two","en"));

    CategoryBag catBag = new CategoryBag();
    catBag.addKeyedReference(new KeyedReference("catBagKeyName","catBagKeyValue"));
    catBag.addKeyedReference(new KeyedReference("uuid:dfddb58c-4853-4a71-865c-f84509017cc7","catBagKeyName2","catBagKeyValue2"));

    IdentifierBag idBag = new IdentifierBag();
    idBag.addKeyedReference(new KeyedReference("idBagKeyName","idBagkeyValue"));
    idBag.addKeyedReference(new KeyedReference("uuid:f78a135a-4769-4e79-8604-54d440314bc0","idBagKeyName2","idBagkeyValue2"));

    TModel tModel = new TModel();
    tModel.setTModelKey("uuid:269855db-62eb-4862-8e5a-1b06f2753038");
    tModel.setOperator("jUDDI");
    tModel.setAuthorizedName("Steve Viens");
    tModel.setName("jUDDI TModel");
    tModel.addDescription(new Description("tModel whatever"));
    tModel.addDescription(new Description("tModel whatever too","fr"));
    tModel.setCategoryBag(catBag);
    tModel.setIdentifierBag(idBag);
    tModel.setOverviewDoc(overDoc);

    SaveTModel request = new SaveTModel();
    request.setAuthInfo(authInfo);
    request.addTModel(tModel);
    request.addTModel(tModel);
    request.addUploadRegister(new UploadRegister("http://www.juddi.org/businessEntity.xml"));
    request.addUploadRegister(new UploadRegister("http://www.sourceforge.net/businessService.xml"));
    request.addUploadRegister(new UploadRegister("http://www.uddi.org/bindingTemplate.xml"));

    System.out.println();

    RegistryObject regObject = request;
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
  }
}