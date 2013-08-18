﻿/*
 * Copyright 2001-2008 The Apache Software Foundation.
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
using org.apache.juddi.v3.client;
using org.apache.juddi.v3.client.config;
using org.apache.juddi.v3.client.transport;
using org.uddi.apiv3;
using System;
using System.Collections.Generic;
using System.Text;


namespace juddi_client.net_sample
{
    class Program
    {
        static void Main(string[] args)
        {
           
            UDDIClient clerkManager = new UDDIClient("uddi.xml");
            UDDIClientContainer.addClient(clerkManager);
            Transport transport = clerkManager.getTransport("default");

            org.uddi.apiv3.UDDI_Security_SoapBinding security = transport.getUDDISecurityService();
            org.uddi.apiv3.UDDI_Inquiry_SoapBinding inquiry = transport.getUDDIInquiryService();
          //  ClientConfig cfg = new ClientConfig("uddi.xml");

            UDDIClerk clerk = clerkManager.getClerk("default");
            
           
            find_business fb = new find_business();
            fb.authInfo = clerk.getAuthToken(security.Url);
            fb.findQualifiers = new string[] { UDDIConstants.APPROXIMATE_MATCH };
            fb.name = new name[1];
            fb.name[0] = new name(UDDIConstants.WILDCARD, "en");
            businessList bl = inquiry.find_business(fb);
            for (int i = 0; i < bl.businessInfos.Length; i++)
            {
                Console.WriteLine(bl.businessInfos[i].name[0].Value);
            }
            Console.Read();

        }
    }
}
