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
package org.apache.juddi.function;

import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.juddi.datastore.DataStore;
import org.apache.juddi.datastore.DataStoreFactory;
import org.apache.juddi.datatype.RegistryObject;
import org.apache.juddi.datatype.request.FindPublisher;
import org.apache.juddi.datatype.request.FindQualifier;
import org.apache.juddi.datatype.request.FindQualifiers;
import org.apache.juddi.datatype.response.PublisherInfos;
import org.apache.juddi.datatype.response.PublisherList;
import org.apache.juddi.error.NameTooLongException;
import org.apache.juddi.error.RegistryException;
import org.apache.juddi.error.UnsupportedException;
import org.apache.juddi.util.Config;

/**
 * "This [FindPublisher] API call returns a publisherList on success. This
 * structure contains information about each matching publisher. In the
 * event that no matches were located for the specified criteria, a
 * publisherList structure with zero publisher structures is returned. If
 * no arguments are passed, a zero-match result set will be returned."
 *
 * In the event of a large number of matches, (as determined by each
 * Operator Site), or if the number of matches exceeds the value of the
 * 'maxRows' attribute, the Operator Site will truncate the result set.
 * If this occurs, the publisherList will contain the 'truncated' attribute
 * with the value 'true'.
 *
 * @author Steve Viens (sviens@apache.org)
 */
public class FindPublisherFunction extends AbstractFunction
{
  // private reference to jUDDI Logger
  private static Log log = LogFactory.getLog(FindPublisherFunction.class);

  /**
   *
   */
  public FindPublisherFunction()
  {
    super();
  }

  /**
   *
   */
  public RegistryObject execute(RegistryObject regObject)
    throws RegistryException
  {
    FindPublisher request = (FindPublisher)regObject;
    String generic = request.getGeneric();
    String name = request.getNameString();
    FindQualifiers qualifiers = request.getFindQualifiers();
    int maxRows = request.getMaxRows();

    // make sure we need to continue with this request. If
    // no arguments were passed in then we'll simply return
    // an empty PublisherList (aka "a zero match result set").
    if ((name == null) || (name.length() == 0))
    {
      PublisherList list = new PublisherList();
      list.setGeneric(generic);
      list.setPublisherInfos(new PublisherInfos());
      list.setOperator(Config.getOperator());
      list.setTruncated(false);
      return list;
    }

    // aquire a jUDDI datastore instance
    DataStoreFactory factory = DataStoreFactory.getFactory();
    DataStore dataStore = factory.acquireDataStore();

    try
    {
      dataStore.beginTrans();

      // validate the 'name' parameters as much as possible up-front before
      // calling into the data layer for relational validation.
      if (name != null)
      {
        // names can not exceed the maximum character length specified by the
        // UDDI specification (v2.0 specifies a max character length of 255). This
        // value is configurable in jUDDI.
        int maxNameLength = Config.getMaxNameLength();
        if (name.length() > maxNameLength)
          throw new NameTooLongException("Name: '"+name+"' (max="+maxNameLength+")");
      }

      // validate the 'qualifiers' parameter as much as possible up-front before
      // calling into the data layer for relational validation.
      if (qualifiers != null)
      {
        Vector qVector = qualifiers.getFindQualifierVector();
        if ((qVector!=null) && (qVector.size() > 0))
        {
          for (int i=0; i<qVector.size(); i++)
          {
            FindQualifier qualifier = (FindQualifier)qVector.elementAt(i);
            String qValue = qualifier.getValue();

            if ((!qValue.equals(FindQualifier.EXACT_NAME_MATCH)) &&
                (!qValue.equals(FindQualifier.CASE_SENSITIVE_MATCH)) &&
                (!qValue.equals(FindQualifier.OR_ALL_KEYS)) &&
                (!qValue.equals(FindQualifier.OR_LIKE_KEYS)) &&
                (!qValue.equals(FindQualifier.AND_ALL_KEYS)) &&
                (!qValue.equals(FindQualifier.SORT_BY_NAME_ASC)) &&
                (!qValue.equals(FindQualifier.SORT_BY_NAME_DESC)) &&
                (!qValue.equals(FindQualifier.SORT_BY_DATE_ASC)) &&
                (!qValue.equals(FindQualifier.SORT_BY_DATE_DESC)) &&
                (!qValue.equals(FindQualifier.SERVICE_SUBSET)) &&
                (!qValue.equals(FindQualifier.COMBINE_CATEGORY_BAGS)))
              throw new UnsupportedException("FindQualifier: "+qValue);
          }
        }
      }

      Vector infoVector = null;
      boolean truncatedResults = false;

      // perform the search for matching technical models (return only keys in requested order)
      Vector idVector = dataStore.findPublisher(name,qualifiers);
      if ((idVector != null) && (idVector.size() > 0))
      {
        // if a maxRows value has been specified and it's less than
        // the number of rows we are about to return then only return
        // maxRows specified.
        int rowCount = idVector.size();
        if ((maxRows > 0) && (maxRows < rowCount))
        {
          rowCount = maxRows;
          truncatedResults = true;
        }

        // iterate through the publisher IDs fetching
        // each associated PublisherInfo in sequence.
        infoVector = new Vector(rowCount);
        for (int i=0; i<rowCount; i++)
          infoVector.addElement(dataStore.fetchPublisherInfo((String)idVector.elementAt(i)));
      }

      dataStore.commit();

      // create a new PublisherInfos instance and stuff
      // the new Vector of PublisherInfos into it.
      PublisherInfos infos = new PublisherInfos();
      infos.setPublisherInfoVector(infoVector);

      // create a new PublisherList instance and
      // stuff the new PublisherInfoVector into it.
      PublisherList list = new PublisherList();
      list.setGeneric(generic);
      list.setPublisherInfos(infos);
      list.setOperator(Config.getOperator());
      list.setTruncated(truncatedResults);
      return list;
    }
    catch(Exception ex)
    {
      // we must rollback for *any* exception
      try { dataStore.rollback(); }
      catch(Exception e) { }

      // write to the log
      log.error(ex);

      // prep RegistryFault to throw
      if (ex instanceof RegistryException)
        throw (RegistryException)ex;
      else
        throw new RegistryException(ex);
    }
    finally
    {
      factory.releaseDataStore(dataStore);
    }
  }

  /***************************************************************************/
  /***************************** TEST DRIVER *********************************/
  /***************************************************************************/

  public static void main(String[] args)
  {
  }
}