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

import org.apache.juddi.datatype.BusinessKey;
import org.apache.juddi.datatype.RegistryObject;
import org.apache.juddi.datatype.response.RelatedBusinessInfo;
import org.apache.juddi.datatype.response.RelatedBusinessInfos;
import org.apache.juddi.datatype.response.RelatedBusinessesList;
import org.apache.juddi.registry.Registry;
import org.apache.juddi.util.xml.XMLUtils;
import org.w3c.dom.Element;

/**
 * @author Steve Viens (sviens@apache.org)
 * @author Anou Mana (anou_mana@users.sourceforge.net)
 */
public class RelatedBusinessesListHandler extends AbstractHandler
{
  public static final String TAG_NAME = "relatedBusinessList";

  private HandlerMaker maker = null;

  protected RelatedBusinessesListHandler(HandlerMaker maker)
  {
    this.maker = maker;
  }

  public RegistryObject unmarshal(Element element)
  {
    RelatedBusinessesList obj = new RelatedBusinessesList();
    Vector nodeList = null;
    AbstractHandler handler = null;

    // We could use the generic attribute value to
    // determine which version of UDDI was used to
    // format the request XML. - Steve

    // Attributes
    obj.setGeneric(element.getAttribute("generic"));
    obj.setOperator(element.getAttribute("operator"));

    // We can ignore the xmlns attribute since we
    // can always determine it's value using the
    // "generic" attribute. - Steve

    String truncValue = element.getAttribute("truncated");
    if (truncValue != null)
      obj.setTruncated(truncValue.equalsIgnoreCase("true"));

    // Text Node Value
    // {none}

    // Child Elements
    nodeList = XMLUtils.getChildElementsByTagName(element,BusinessKeyHandler.TAG_NAME);
    if (nodeList.size() > 0)
    {
      handler = maker.lookup(BusinessKeyHandler.TAG_NAME);
      obj.setBusinessKey((BusinessKey)handler.unmarshal((Element)nodeList.elementAt(0)));
    }

    nodeList = XMLUtils.getChildElementsByTagName(element,RelatedBusinessInfosHandler.TAG_NAME);
    if (nodeList.size() > 0)
    {
      handler = maker.lookup(RelatedBusinessInfosHandler.TAG_NAME);
      obj.setRelatedBusinessInfos((RelatedBusinessInfos)handler.unmarshal((Element)nodeList.elementAt(0)));
    }

    return obj;
  }

  public void marshal(RegistryObject object,Element parent)
  {
    RelatedBusinessesList list = (RelatedBusinessesList)object;
    Element element = parent.getOwnerDocument().createElement(TAG_NAME);
    AbstractHandler handler = null;

    String generic = list.getGeneric();
    if (generic != null)
    {
      element.setAttribute("generic",generic);

      if (generic.equals(Registry.UDDI_V1_GENERIC))
        element.setAttribute("xmlns",Registry.UDDI_V1_NAMESPACE);
      else if (generic.equals(Registry.UDDI_V2_GENERIC))
        element.setAttribute("xmlns",Registry.UDDI_V2_NAMESPACE);
      else if (generic.equals(Registry.UDDI_V3_GENERIC))
        element.setAttribute("xmlns",Registry.UDDI_V3_NAMESPACE);
    }

    String operator = list.getOperator();
    if (operator != null)
      element.setAttribute("operator",operator);

    boolean truncated = list.isTruncated();
    if (truncated)
      element.setAttribute("truncated","true");

    BusinessKey key = list.getBusinessKey();
    if (key != null)
    {
      handler = maker.lookup(BusinessKeyHandler.TAG_NAME);
      handler.marshal(key,element);
    }

    RelatedBusinessInfos infos = list.getRelatedBusinessInfos();
    if (infos != null)
    {
      handler = maker.lookup(RelatedBusinessInfosHandler.TAG_NAME);
      handler.marshal(infos,element);
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
    AbstractHandler handler = maker.lookup(RelatedBusinessesListHandler.TAG_NAME);

    Element parent = XMLUtils.newRootElement();
    Element child = null;

    RelatedBusinessesList list = new RelatedBusinessesList();
    list.setGeneric("2.0");
    list.setOperator("jUDDI.org");
    list.setTruncated(true);
    list.setBusinessKey(new BusinessKey("f9f0c35f-06ab-4bec-9c7d-b1469e73f0eb"));
    list.addRelatedBusinessInfo(new RelatedBusinessInfo("abc"));
    list.addRelatedBusinessInfo(new RelatedBusinessInfo("xyz"));

    System.out.println();

    RegistryObject regObject = list;
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