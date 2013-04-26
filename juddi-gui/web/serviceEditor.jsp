<%-- 
/*
 * Copyright 2001-2013 The Apache Software Foundation.
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
    Document   : service editor
    Created on : Feb 24, 2013, 3:31:39 PM
    Author     : Alex O'Ree
--%>

<%@page import="java.net.URLEncoder"%>
<%@page import="org.uddi.api_v3.*"%>
<%@page import="org.apache.juddi.webconsole.PostBackConstants"%>
<%@page import="org.apache.juddi.webconsole.hub.UddiHub"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="header-top.jsp" %>
<div class="container">

    <!-- Main hero unit for a primary marketing message or call to action -->
    <div class="well" >
        <h1><%=ResourceLoader.GetResource(session, "pages.serviceeditor.title")%></h1>
    </div>

    <!-- Example row of columns -->
    <div class="row">
        <div class="span12" >

            <div id="businesseditor">
                <%
                    boolean newitem = false;

                    UddiHub x = UddiHub.getInstance(application, request.getSession());
                    String serviceid = request.getParameter("id");
                    String businessid = request.getParameter("bizid");
                    if (serviceid == null || serviceid.isEmpty()) {
                        //response.sendRedirect("browse.jsp");
                        if (businessid != null && businessid.length() > 0) {
                            newitem = true;
                        } else {
                            response.sendRedirect("index.jsp");
                        }

                    }

                    BusinessService bd = null;
                    if (!newitem) {
                        bd = x.GetServiceDetail(serviceid);
                    } else {
                        bd = new BusinessService();
                        bd.setBusinessKey(businessid);
                        BusinessEntity be = x.GetBusinessDetails(businessid);
                        if (be == null) {
                            //incase an invalid business id was passed in
                            response.sendRedirect("index.jsp");
                        } else {
                            bd.setBusinessKey(be.getBusinessKey());
                        }
                    }

                    if (bd == null) {
                        response.sendError(501);
                    }
                    int totalBTDescriptions = 0;
                %>

                <i class="icon-lock icon-large"></i>

                <b><%=ResourceLoader.GetResource(session, "pages.businesskey")%></b>-
                <%=ResourceLoader.GetResource(session, "pages.businesskey.description")%>

                <br>
                <div style="border-width: 2px; border-style: solid;" class="noedit" id="<%=PostBackConstants.BUSINESSKEY%>">
                    <%
                        out.write("<a href=\"businessEditor2.jsp?id=" + StringEscapeUtils.escapeHtml(bd.getBusinessKey()) + "\">");
                        out.write(StringEscapeUtils.escapeHtml(bd.getBusinessKey()));
                        out.write("</a>");
                        if (bd.getCategoryBag() == null) {
                            bd.setCategoryBag(new CategoryBag());
                        }

                    %>
                </div>
                <br>
                <%
                    if (!newitem) {
                        out.write("<i class=\"icon-lock icon-large\"></i>");
                    }
                %>
                <b><%=ResourceLoader.GetResource(session, "items.service.key")%> </b>-
                <%=ResourceLoader.GetResource(session, "items.service.key.description")%><br>
                <div style="border-width: 2px; border-style: solid;" <%
                    if (!newitem) {
                        out.write("class=\"noedit\"");
                    } else {
                        out.write("class=\"edit\"");
                    }
                     %>
                     id="<%=PostBackConstants.SERVICEKEY%>">
                    <%
                        if (!newitem) {
                            out.write(StringEscapeUtils.escapeHtml(bd.getServiceKey()));
                        }

                        if (bd.getCategoryBag() == null) {
                            bd.setCategoryBag(new CategoryBag());
                        }

                        if (bd.getBindingTemplates() == null) {
                            bd.setBindingTemplates(new BindingTemplates());
                        }
                        int currentcatkeyrefBT = 0;
                        int currentcatkeyrefgrpBT = 0;
                        int currentbindingtemplatesInstance = 0;
                        int currentOverviewDocs = 0;
                        for (int i = 0; i < bd.getBindingTemplates().getBindingTemplate().size(); i++) {
                            if (bd.getBindingTemplates().getBindingTemplate().get(i).getCategoryBag() != null) {
                                currentcatkeyrefBT += bd.getBindingTemplates().getBindingTemplate().get(i).getCategoryBag().getKeyedReference().size();
                                currentcatkeyrefgrpBT += bd.getBindingTemplates().getBindingTemplate().get(i).getCategoryBag().getKeyedReferenceGroup().size();
                            }

                            if (bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails() != null) {
                                currentbindingtemplatesInstance = bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().size();
                            }
                            if (bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails() != null) {
                                for (int k = 0; k < bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().size(); k++) {
                                    if (bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().get(k).getInstanceDetails() != null) {
                                        currentOverviewDocs += bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().get(k).getInstanceDetails().getOverviewDoc().size();
                                    }
                                }
                            }
                        }



                    %>
                </div>
                <br>

                <script type="text/javascript">
                    var currentNameEntries=<%= bd.getName().size() - 1%>;
                    var currentbindingtemplatesInstance =<%=currentbindingtemplatesInstance%>;
                    var currentDescriptionEntries=<%= bd.getDescription().size() - 1%>;
                    var currentcatkeyrefBT=<%=currentcatkeyrefBT%>;
                    var currentcatkeyref=<%=bd.getCategoryBag().getKeyedReference().size()%>;
                    var currentcatkeyrefgrpBT=<%=currentcatkeyrefgrpBT%>;
                    var currentcatkeyrefgrp=<%=bd.getCategoryBag().getKeyedReferenceGroup().size()%>;
                    var currentbindingtemplates = <%=bd.getBindingTemplates().getBindingTemplate().size()%>;
                    var currentOverviewDocs = <%=currentOverviewDocs%>;
                </script> 

                <ul class="nav nav-tabs" id="myTab">
                    <li class="active"><a  href="#general"><%=ResourceLoader.GetResource(session, "pages.editor.tabnav.general")%></a></li>

                    <li><a href="#categories" ><%=ResourceLoader.GetResource(session, "pages.editor.tabnav.categories")%></a></li>

                    <li><a href="#bindingtemplates" ><%=ResourceLoader.GetResource(session, "items.bindingtemplate")%></a></li>

                    <li><a href="#signatures" ><%=ResourceLoader.GetResource(session, "pages.editor.tabnav.signatures")%></a></li>

                    <li><a href="#opinfo" ><%=ResourceLoader.GetResource(session, "pages.editor.tabnav.opinfo")%></a></li>
                </ul>
                <script>
                    $(function () {
                        $('#myTab').tab;//('show');
                    })
                    $('#myTab a[href=#general]').click(function (e) {
                        e.preventDefault();
                        $(this).tab('show');
                    });
                    $('#myTab a[href=#categories]').click(function (e) {
                        e.preventDefault();
                        $(this).tab('show');
                    });
                    $('#myTab a[href=#bindingtemplates]').click(function (e) {
                        e.preventDefault();
                        $(this).tab('show');
                    });
                    $('#myTab a[href=#signatures]').click(function (e) {
                        e.preventDefault();
                        $(this).tab('show');
                    });
                    $('#myTab a[href=#opinfo]').click(function (e) {
                        e.preventDefault();
                        $(this).tab('show');
                    });
                    
                </script>
                <div class="tab-content">
                    <div class="tab-pane active" id="general">




                        <b><%=ResourceLoader.GetResource(session, "items.name")%> </b>- 
                        <%=ResourceLoader.GetResource(session, "items.name.description")%><br>
                        <a href="javascript:AddName();"><i class="icon-plus-sign icon-large"></i></a><%=ResourceLoader.GetResource(session, "items.name.add")%>
                        <div id="nameContainer" style="border-width: 2px; border-style: solid;" >
                            <%
                                for (int i = 0; i < bd.getName().size(); i++) {
                                    out.write("<div id=\"" + PostBackConstants.NAME + i + "\" style=\"border-width:1px; border-style:solid\" >");
                                    out.write("<div style=\"float:left; height:100%\"><a href=\"javascript:Remove('" + PostBackConstants.NAME + i + "');\"><i class=\"icon-remove-sign icon-large\"></i></a></div>");
                                    out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.key") + ":&nbsp;</div>"
                                            + "<div class=\"edit\" id=\"" + PostBackConstants.NAME + i + PostBackConstants.VALUE + "\">" + StringEscapeUtils.escapeHtml(bd.getName().get(i).getValue()) + "</div>");
                                    out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.lang") + ":&nbsp;</div>"
                                            + "<div class=\"edit\" id=\"" + PostBackConstants.NAME + i + PostBackConstants.LANG + "\">");
                                    if (bd.getName().get(i).getLang() != null) {
                                        out.write(StringEscapeUtils.escapeHtml(bd.getName().get(i).getLang()));
                                    }
                                    out.write("</div>");

                                    out.write("</div>");
                                    //confirmed one to one div tags
                                }
                            %></div>
                        <Br>
                        <b><%=ResourceLoader.GetResource(session, "items.description")%> </b>- 
                        <%=ResourceLoader.GetResource(session, "items.services.description")%><br>
                        <a href="javascript:AddDescription('Description');"><i class="icon-plus-sign icon-large"></i></a> <%=ResourceLoader.GetResource(session, "items.description.add")%>

                        <div id="Description" style="border-width: 2px; border-style: solid;" >
                            <%
                                for (int i = 0; i < bd.getDescription().size(); i++) {
                                    out.write("<div id=\"" + PostBackConstants.DESCRIPTION + i + "\" style=\"border-width:1px; border-style:solid\">");
                                    out.write("<div style=\"float:left;height:100%\"><a href=\"javascript:Remove('Description" + i + "');\"><i class=\"icon-remove-sign icon-large\"></i></a></div>");
                                    out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.key") + ":&nbsp;</div>"
                                            + "<div class=\"edit\" id=\"" + PostBackConstants.DESCRIPTION + i + PostBackConstants.VALUE + "\">" + StringEscapeUtils.escapeHtml(bd.getDescription().get(i).getValue()) + "</div>");
                                    out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.lang") + ":&nbsp;</div>"
                                            + "<div class=\"edit\" id=\"" + PostBackConstants.DESCRIPTION + i + PostBackConstants.LANG + "\">"
                                            + (bd.getDescription().get(i).getLang() == null ? "" : StringEscapeUtils.escapeHtml(bd.getDescription().get(i).getLang()))
                                            + "</div>");

                                    out.write("</div>");
                                    //confirmed 1:1
                                }

                            %>
                        </div>
                        <br>
                    </div>
                    <div class="tab-pane" id="categories">

                        <b><%=ResourceLoader.GetResource(session, "pages.editor.tabnav.categories")%> </b>- 
                        <%=ResourceLoader.GetResource(session, "items.categories.description")%>
                        <br><br>
                        <b><%=ResourceLoader.GetResource(session, "items.keyrefcats")%>:</b><br>
                        <a href="javascript:AddCategoryKeyReference();"><i class="icon-plus-sign icon-large"></i></a> <%=ResourceLoader.GetResource(session, "items.keyrefcat.add")%> <Br>
                        <div id="catContainer" style="border-width: 2px; border-style: solid;" >
                            <%
                                if (bd.getCategoryBag() == null) {
                                    bd.setCategoryBag(new CategoryBag());
                                }
                                //                        out.write("Keyed Reference Categories:");
                                for (int i = 0; i < bd.getCategoryBag().getKeyedReference().size(); i++) {

                                    out.write("<div id=\"" + PostBackConstants.CATBAG_KEY_REF + i + "\" style=\"border-width:2px; border-style:solid\">");
                                    out.write("<div style=\"float:left;height:100%\"><a href=\"javascript:Remove('" + PostBackConstants.CATBAG_KEY_REF + i + "');\"><i class=\"icon-remove-sign icon-large\"></i></a></div>");
                                    out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.key") + " (<a href=\"javascript:tModelModal('" + PostBackConstants.CATBAG_KEY_REF + i + PostBackConstants.VALUE + "')\" ><i class=\"icon-list-alt icon-large\"></i>" + ResourceLoader.GetResource(session, "items.picker") + "</a>): &nbsp;</div>"
                                            + "<div class=\"edit\" id=\"" + PostBackConstants.CATBAG_KEY_REF + i + PostBackConstants.VALUE + "\">" + StringEscapeUtils.escapeHtml(bd.getCategoryBag().getKeyedReference().get(i).getTModelKey()) + "</div>");
                                    out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.name") + ": &nbsp;</div>"
                                            + "<div class=\"edit\" id=\"" + PostBackConstants.CATBAG_KEY_REF + i + PostBackConstants.KEYNAME + "\">" + StringEscapeUtils.escapeHtml(bd.getCategoryBag().getKeyedReference().get(i).getKeyName()) + "</div>");
                                    out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.value") + ": &nbsp;</div>"
                                            + "<div class=\"edit\" id=\"" + PostBackConstants.CATBAG_KEY_REF + i + PostBackConstants.KEYVALUE + "\">" + StringEscapeUtils.escapeHtml(bd.getCategoryBag().getKeyedReference().get(i).getKeyValue()) + "</div>");
                                    out.write("</div>");
                                }
                            %>
                        </div>
                        <br>
                        <b><%=ResourceLoader.GetResource(session, "items.keyrefgroup")%></b><br>
                        <a href="javascript:AddCategoryKeyReferenceGroup();"><i class="icon-plus-sign icon-large"></i></a> <%=ResourceLoader.GetResource(session, "items.keyrefgroup.add")%><br>
                        <div id="catContainerGrp" style="border-width: 2px; border-style: solid;" >


                            <%
                                //div count good so far
                                for (int i = 0; i < bd.getCategoryBag().getKeyedReferenceGroup().size(); i++) {

                                    out.write("<div id=\"" + PostBackConstants.CATBAG_KEY_REF_GRP + i + "\" style=\"border-width:2px; border-style:solid\">"
                                            + "<div style=\"float:left;height:100%\"><a href=\"javascript:Remove('catbaggrpkeyref" + i + "');\"><i class=\"icon-remove-sign icon-large\"></i></a></div>"
                                            + "<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.key") + " (<a href=\"javascript:tModelModal('" + PostBackConstants.CATBAG_KEY_REF_GRP + i + PostBackConstants.VALUE + "')\" ><i class=\"icon-list-alt icon-large\"></i>" + ResourceLoader.GetResource(session, "items.picker") + "</a>): &nbsp;</div>"
                                            + "<div class=\"edit\" id=\"" + PostBackConstants.CATBAG_KEY_REF_GRP + i + PostBackConstants.VALUE + "\">"
                                            + StringEscapeUtils.escapeHtml(bd.getCategoryBag().getKeyedReferenceGroup().get(i).getTModelKey())
                                            + "</div>"
                                            + "<div id=\"" + PostBackConstants.CATBAG_KEY_REF_GRP + i + PostBackConstants.KEY_REF + "\" style=\"border-width:1px; border-style:solid\">"
                                            + "<div style=\"float:left;height:100%\"><a href=\"javascript:AddCategoryKeyReferenceGroupKeyRef('" + PostBackConstants.CATBAG_KEY_REF_GRP + i + PostBackConstants.KEY_REF + "');\"><i class=\"icon-plus-sign icon-large\"></i></a></div>"
                                            + ResourceLoader.GetResource(session, "items.keyrefcat.add")
                                            + "</div>");


                                    for (int k = 0; k < bd.getCategoryBag().getKeyedReferenceGroup().get(i).getKeyedReference().size(); k++) {

                                        out.write("<div id=\"" + PostBackConstants.CATBAG_KEY_REF_GRP + i + "keyref" + k + "\" style=\"border-width:1px; border-style:solid\">");
                                        out.write("<div style=\"float:left;height:100%\"><a href=\"javascript:Remove('" + PostBackConstants.CATBAG_KEY_REF_GRP + i + PostBackConstants.KEY_REF + k + "');\"><i class=\"icon-remove-sign icon-large\"></i></a></div>");
                                        out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.key") + " (<a href=\"javascript:tModelModal('" + PostBackConstants.CATBAG_KEY_REF_GRP + i + PostBackConstants.KEY_REF + k + PostBackConstants.VALUE + "')\" ><i class=\"icon-list-alt icon-large\"></i>" + ResourceLoader.GetResource(session, "items.picker") + "</a>): &nbsp;</div>"
                                                + "<div class=\"edit\" id=\"" + PostBackConstants.CATBAG_KEY_REF_GRP + i + PostBackConstants.KEY_REF + k + PostBackConstants.VALUE + "\">" + StringEscapeUtils.escapeHtml(bd.getCategoryBag().getKeyedReferenceGroup().get(i).getKeyedReference().get(k).getTModelKey()) + "</div>");
                                        out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.name") + ": &nbsp;</div>"
                                                + "<div class=\"edit\" id=\"" + PostBackConstants.CATBAG_KEY_REF_GRP + i + PostBackConstants.KEY_REF + k + PostBackConstants.KEYNAME + "\">" + StringEscapeUtils.escapeHtml(bd.getCategoryBag().getKeyedReferenceGroup().get(i).getKeyedReference().get(k).getKeyName()) + "</div>");
                                        out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.value") + ": &nbsp;</div>"
                                                + "<div class=\"edit\" id=\"" + PostBackConstants.CATBAG_KEY_REF_GRP + i + PostBackConstants.KEY_REF + k + PostBackConstants.KEYVALUE + "\">" + StringEscapeUtils.escapeHtml(bd.getCategoryBag().getKeyedReferenceGroup().get(i).getKeyedReference().get(k).getKeyValue()) + "</div>");
                                        out.write("</div>");
                                    }

                                    out.write("</div>"); //this ends the group container for key ref PostBackConstants.CATBAG_KEY_REF_GRP + i + PostBackConstants.KEY_REF
                                }


                            %>
                        </div>
                        <br>
                    </div>

                    <div class="tab-pane" id="bindingtemplates">
                        <b><%=ResourceLoader.GetResource(session, "items.bindingtemplate")%> </b>- <%=ResourceLoader.GetResource(session, "items.bindingtemplate.description")%> <br>
                        <a href="javascript:AddBindingTemplate();"><i class="icon-plus-sign icon-large"></i></a> <%=ResourceLoader.GetResource(session, "items.bindingtemplate.add")%> <Br>
                        <div id="bindingTemplatesContainer" style="border-width: 2px; border-style: solid">
                            <%
                                for (int i = 0; i < bd.getBindingTemplates().getBindingTemplate().size(); i++) {
                                    out.write("<div id=\"bindingTemplate" + i + "\"  style=\"border-width: 2px; border-style: dashed;; border-color: lightseagreen\" >");
                                    out.write("<div style=\"float:left\">"
                                            + "<a href=\"javascript:Remove('" + PostBackConstants.BINDINGTEMPLATE + i + "');\"><i class=\"icon-remove-sign icon-large\"></i></a>"
                                            + ResourceLoader.GetResource(session, "items.bindingtemplate.key") + ": &nbsp;</div>"
                                            + "<div class=\"");
                                    if (!newitem) {
                                        out.write("no");
                                    }
                                    out.write("edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.VALUE + "\">");
                                    if (!newitem) {
                                        out.write(StringEscapeUtils.escapeHtml(bd.getBindingTemplates().getBindingTemplate().get(i).getBindingKey()));
                                    }
                                    out.write("</div>");    //end of binding template key section
                            %>          
                            <br>
                            <a href="javascript:AddDescriptionSpecific('<%=PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.DESCRIPTION%>');">
                                <i class="icon-plus-sign icon-large"></i></a><%=ResourceLoader.GetResource(session, "items.bindingtemplate.description.add")%> <Br>

                            <div id="<%=PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.DESCRIPTION%>" style="border-width: 1px; border-style: dotted;" >
                                <%
                                    for (int k = 0; k < bd.getBindingTemplates().getBindingTemplate().get(i).getDescription().size(); k++) {
                                        out.write("<div id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.DESCRIPTION + k + "\" style=\"border-width:1px; border-style:solid\">");
                                        out.write("<div style=\"float:left;height:100%\">"
                                                + "<a href=\"javascript:Remove('" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.DESCRIPTION + k + "');\"><i class=\"icon-remove-sign icon-large\"></i></a></div>");
                                        out.write("<div style=\"float:left\">Value:&nbsp;</div>"
                                                + "<div class=\"edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.DESCRIPTION + k + PostBackConstants.VALUE + "\">" + StringEscapeUtils.escapeHtml(bd.getBindingTemplates().getBindingTemplate().get(i).getDescription().get(k).getValue()) + "</div>");
                                        out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.lang") + ":&nbsp;</div>"
                                                + "<div class=\"edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.DESCRIPTION + k + PostBackConstants.LANG + "\">"
                                                + (bd.getBindingTemplates().getBindingTemplate().get(i).getDescription().get(k).getLang() == null ? " " : StringEscapeUtils.escapeHtml(bd.getBindingTemplates().getBindingTemplate().get(i).getDescription().get(k).getLang()))
                                                + "</div>");
                                        out.write("</div>");
                                        totalBTDescriptions++;

                                    }

                                    //1:1
                                %>
                            </div>
                            <b><%=ResourceLoader.GetResource(session, "items.accesspoint")%></b> - <%=ResourceLoader.GetResource(session, "items.accesspoint.description")%><br>
                            <%


//items.hostingredirector

                                //TODO need an html select in here?
                                if (bd.getBindingTemplates().getBindingTemplate().get(i).getHostingRedirector() != null) {
                                    out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.hostingredirector") + ": &nbsp;</div>"
                                            + "<div class=\"edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.HOSTINGREDIRECTOR + "\">" + StringEscapeUtils.escapeHtml(bd.getBindingTemplates().getBindingTemplate().get(i).getHostingRedirector().getBindingKey()) + "</div>");
                                }
                                if (bd.getBindingTemplates().getBindingTemplate().get(i).getAccessPoint() != null) {

                                    out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.accesspoint.type") + ": &nbsp;</div>"
                                            + "<div class=\"edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.ACCESSPOINT_TYPE + "\">" + StringEscapeUtils.escapeHtml(bd.getBindingTemplates().getBindingTemplate().get(i).getAccessPoint().getUseType()) + "</div>");
                                    out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.accesspoint.value") + ": &nbsp;</div>"
                                            + "<div class=\"edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.ACCESSPOINT_VALUE + "\">" + StringEscapeUtils.escapeHtml(bd.getBindingTemplates().getBindingTemplate().get(i).getAccessPoint().getValue()) + "</div>");
                                    // this was an unbalanced divout.write("</div>");
                                }
                            %>

                            <br>
                            <b><%=ResourceLoader.GetResource(session, "items.tmodelinstance.info")%></b> - <%=ResourceLoader.GetResource(session, "items.tmodelinstance.info.desc")%> <br>
                            <a href="javascript:AddTmodelInstance('<%=PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE%>');"><i class="icon-plus-sign icon-large"></i></a> <%=ResourceLoader.GetResource(session, "items.tmodelinstance.add")%><br>
                            <div id="<%=PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE%>" style="border-width: 1px; border-style: solid; border-color: red" >        
                                <%
                                    if (bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails() != null) {
                                        for (int k = 0; k < bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().size(); k++) {
                                %>
                                <div id="<%=PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k%>" style="border-width: 2px; border-style: dashed; border-color: red" >        
                                    <%
                                        out.write("<div style=\"float:left;height:100%\">"
                                                + "<a href=\"javascript:Remove('" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + "');\"><i class=\"icon-remove-sign icon-large\"></i></a></div>");

                                        out.write("<div style=\"float:left\"><b>" + ResourceLoader.GetResource(session, "items.tmodel.key") + " </b> (<a href=\"javascript:tModelModal('" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.KEYNAME + "')\" ><i class=\"icon-list-alt icon-large\"></i>" + ResourceLoader.GetResource(session, "items.picker") + "</a>): &nbsp;</div>"
                                                + "<div class=\"edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.KEYNAME + "\">" + StringEscapeUtils.escapeHtml(bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().get(k).getTModelKey()) + "</div>");
                                        //  out.write("<div style=\"float:left\"><span title=\"Instance Params\">Value</span>:&nbsp;</div>"
                                        //          + "<div class=\"edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.VALUE + "\">" + ((bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().get(k).getInstanceDetails() != null) ? StringEscapeUtils.escapeHtml(bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().get(k).getInstanceDetails().getInstanceParms()) : "") + "</div>");
                                    %>
                                    <br>
                                    <%

                                        out.write("<div style=\"float:left\"><b>" + ResourceLoader.GetResource(session, "items.tmodelinstance.parameters") + ":</b> &nbsp;</div>"
                                                + "<div class=\"edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.INSTANCE + PostBackConstants.VALUE + "\">");
                                        if (bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().get(k).getInstanceDetails() != null
                                                && bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().get(k).getInstanceDetails().getInstanceParms() != null) {

                                            out.write(StringEscapeUtils.escapeHtml(bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().get(k).getInstanceDetails().getInstanceParms()));
                                        }
                                        out.write("</div>");
                                    %>
                                    <br>

                                    <b>
                                        <%=ResourceLoader.GetResource(session, "items.tmodelinstance.description")%>
                                    </b> - <%=ResourceLoader.GetResource(session, "items.tmodelinstance.description2")%> <br>
                                    <a href="javascript:AddDescriptionSpecific('<%=PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.INSTANCE + PostBackConstants.DESCRIPTION%>');"><i class="icon-plus-sign icon-large"></i></a> <%=ResourceLoader.GetResource(session, "items.tmodelinstance.description.add")%><br>
                                    <div id="<%=PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.INSTANCE + PostBackConstants.DESCRIPTION%>" style="border-width: 1px; border-style: groove;" >
                                        <%
                                            for (int j = 0; j < bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().get(k).getDescription().size(); j++) {
                                                out.write("<div id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.INSTANCE + PostBackConstants.DESCRIPTION + j + "\" style=\"border-width:1px; border-style:solid\">");
                                                out.write("<div style=\"float:left;height:100%\">"
                                                        + "<a href=\"javascript:Remove('" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.INSTANCE + PostBackConstants.DESCRIPTION + j + "');\"><i class=\"icon-remove-sign icon-large\"></i></a></div>");
                                                out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.name") + ":&nbsp;</div>"
                                                        + "<div class=\"edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.INSTANCE + PostBackConstants.DESCRIPTION + j + PostBackConstants.VALUE + "\">" + StringEscapeUtils.escapeHtml(bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().get(k).getDescription().get(j).getValue()) + "</div>");
                                                out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.lang") + ":&nbsp;</div>"
                                                        + "<div class=\"edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.INSTANCE + PostBackConstants.DESCRIPTION + j + PostBackConstants.LANG + "\">"
                                                        + (bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().get(k).getDescription().get(j).getLang() == null ? " " : StringEscapeUtils.escapeHtml(bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().get(k).getDescription().get(j).getLang()))
                                                        + "</div>");
                                                out.write("</div>");
                                            }

                                        %>
                                    </div>

                                    <div><br>
                                        <b><%=ResourceLoader.GetResource(session, "items.overviewurl")%></b> - <%=ResourceLoader.GetResource(session, "items.overviewurl.description")%> <br>
                                        <a href="javascript:AddOverviewDocumentSpecific('<%=PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.INSTANCE + PostBackConstants.OVERVIEW%>');"><i class="icon-plus-sign icon-large"></i></a> <%=ResourceLoader.GetResource(session, "items.overviewurl.add")%><br>
                                        <div id="<%=PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.INSTANCE + PostBackConstants.OVERVIEW%>" style="border-width: 1px; border-style: groove;" >
                                            <%
                                                //  out.write("<div id=\"" + PostBackConstants.OVERVIEW + "\" style=\"border-width:2px; border-style:solid\">");
                                                if (bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().get(k).getInstanceDetails() != null)
                                                    for (int j = 0; j < bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().get(k).getInstanceDetails().getOverviewDoc().size(); j++) {
                                                        out.write("<div id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.INSTANCE + PostBackConstants.OVERVIEW + j + "\" style=\"border-width:1px; border-style:solid\">");
                                                        out.write("<div style=\"float:left;height:100%\"><a href=\"javascript:Remove('" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.INSTANCE + PostBackConstants.OVERVIEW + j + "');\"><i class=\"icon-remove-sign icon-large\"></i></a></div>");
                                                        out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.value") + ":&nbsp;</div>"
                                                                + "<div class=\"edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.INSTANCE + PostBackConstants.OVERVIEW + j + PostBackConstants.VALUE + "\">" + StringEscapeUtils.escapeHtml(bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().get(k).getInstanceDetails().getOverviewDoc().get(j).getOverviewURL().getValue()) + "</div>");
                                                        out.write("<div style=\"float:left\">Use type:&nbsp;</div>"
                                                                + "<div class=\"edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.INSTANCE + PostBackConstants.OVERVIEW + j + PostBackConstants.TYPE + "\">" + StringEscapeUtils.escapeHtml(bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().get(k).getInstanceDetails().getOverviewDoc().get(j).getOverviewURL().getUseType()) + "</div>");
                                            %>
                                            <br><b><%=ResourceLoader.GetResource(session, "items.overviewdocument.description")%> </b><br>
                                            <a href="javascript:AddDescriptionSpecific('<%=PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.INSTANCE + PostBackConstants.OVERVIEW + j + PostBackConstants.DESCRIPTION%>');"><i class="icon-plus-sign icon-large"></i></a><%=ResourceLoader.GetResource(session, "items.overviewurl.description.add")%>
                                            <div id="<%=PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.INSTANCE + PostBackConstants.OVERVIEW + j + PostBackConstants.DESCRIPTION%>" style="border-width: 1px; border-style: groove;" >
                                                <%
                                                    //  out.write("<div id=\"" + PostBackConstants.OVERVIEW + "\" style=\"border-width:2px; border-style:solid\">");
                                                    for (int h = 0; h < bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().get(k).getInstanceDetails().getOverviewDoc().get(j).getDescription().size(); h++) {
                                                        out.write("<div id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.INSTANCE + PostBackConstants.OVERVIEW + j + PostBackConstants.DESCRIPTION + h + "\" style=\"border-width:1px; border-style:solid\">");
                                                        out.write("<div style=\"float:left;height:100%\"><a href=\"javascript:Remove('" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.INSTANCE + PostBackConstants.OVERVIEW + j + PostBackConstants.DESCRIPTION + h + "');\"><i class=\"icon-remove-sign icon-large\"></i></a></div>");
                                                        out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.value") + ":&nbsp;</div>"
                                                                + "<div class=\"edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.INSTANCE + PostBackConstants.OVERVIEW + j + PostBackConstants.DESCRIPTION + h + PostBackConstants.VALUE + "\">" + StringEscapeUtils.escapeHtml(bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().get(k).getInstanceDetails().getOverviewDoc().get(j).getDescription().get(h).getValue()) + "</div>");
                                                        out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.lang") + ":&nbsp;</div>"
                                                                + "<div class=\"edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.TMODELINSTANCE + k + PostBackConstants.INSTANCE + PostBackConstants.OVERVIEW + j + PostBackConstants.DESCRIPTION + h + PostBackConstants.LANG + "\">"
                                                                + (bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().get(k).getInstanceDetails().getOverviewDoc().get(j).getDescription().get(h).getLang() == null ? "" : StringEscapeUtils.escapeHtml(bd.getBindingTemplates().getBindingTemplate().get(i).getTModelInstanceDetails().getTModelInstanceInfo().get(k).getInstanceDetails().getOverviewDoc().get(j).getDescription().get(h).getLang())) + "</div>");
                                                        out.write("</div>");
                                                    }
                                                %>
                                            </div>
                                            <%
                                                    out.write("</div>");
                                                }
                                            %>

                                        </div>

                                    </div>

                                </div>

                                <%    } //end of instance details
                                %>

                                <%
                                    }
                                %>
                            </div> <!-- tmodel instance info -->                                        
                            <Br>
                            <b><%=ResourceLoader.GetResource(session, "items.bindingtemplate.keyrefcat")%></b><Br>

                            <a href="javascript:AddCategoryKeyReferenceSpecific('<%=PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF%>');"><i class="icon-plus-sign icon-large"></i></a> <%=ResourceLoader.GetResource(session, "items.keyrefcat.add")%> <Br>
                            <div id="<%=PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF%>" style="border-width: 1px; border-style: dotted;" >
                                <%
                                    if (bd.getBindingTemplates().getBindingTemplate().get(i).getCategoryBag() == null) {
                                        bd.getBindingTemplates().getBindingTemplate().get(i).setCategoryBag(new CategoryBag());
                                    }
                                    for (int k = 0; k < bd.getBindingTemplates().getBindingTemplate().get(i).getCategoryBag().getKeyedReference().size(); k++) {
                                        out.write("<div id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF + k + "\" style=\"border-width:2px; border-style:solid\">");
                                        out.write("<div style=\"float:left;height:100%\"><a href=\"javascript:Remove('" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF + k + "');\"><i class=\"icon-remove-sign icon-large\"></i></a></div>");
                                        out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.key") + " (<a href=\"javascript:tModelModal('" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF + k + PostBackConstants.VALUE + "')\" ><i class=\"icon-list-alt icon-large\"></i>" + ResourceLoader.GetResource(session, "items.picker") + "</a>): &nbsp;</div>"
                                                + "<div class=\"edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF + k + PostBackConstants.VALUE + "\">" + StringEscapeUtils.escapeHtml(bd.getBindingTemplates().getBindingTemplate().get(i).getCategoryBag().getKeyedReference().get(k).getTModelKey()) + "</div>");
                                        out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.name") + ": &nbsp;</div>"
                                                + "<div class=\"edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF + k + PostBackConstants.KEYNAME + "\">" + StringEscapeUtils.escapeHtml(bd.getBindingTemplates().getBindingTemplate().get(i).getCategoryBag().getKeyedReference().get(k).getKeyName()) + "</div>");
                                        out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.value") + ": &nbsp;</div>"
                                                + "<div class=\"edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF + k + PostBackConstants.KEYVALUE + "\">" + StringEscapeUtils.escapeHtml(bd.getBindingTemplates().getBindingTemplate().get(i).getCategoryBag().getKeyedReference().get(k).getKeyValue()) + "</div>");
                                        out.write("</div>");    //end key ref
                                    }

                                %>
                            </div>
                            <br>    
                            <b><%=ResourceLoader.GetResource(session, "items.bindingtemplate.keyrefgrp")%></b><br>
                            <a href="javascript:AddCategoryKeyReferenceGroupSpecificBT('<%=PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF_GRP%>');"><i class="icon-plus-sign icon-large"></i></a> <%=ResourceLoader.GetResource(session, "items.keyrefgroup.add")%><br>
                            <div id="<%=PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF_GRP%>" style="border-width: 1px; border-style: dotted;" >

                                <%
                                    for (int z = 0; z < bd.getBindingTemplates().getBindingTemplate().get(i).getCategoryBag().getKeyedReferenceGroup().size(); z++) {

                                        out.write("<div id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF_GRP + z + "\" style=\"border-width:2px; border-style:solid\">"
                                                + "<div style=\"float:left;height:100%\"><a href=\"javascript:Remove('" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF_GRP + z + "');\"><i class=\"icon-remove-sign icon-large\"></i></a></div>"
                                                + "<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.key") + " (<a href=\"javascript:tModelModal('" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF_GRP + z + PostBackConstants.VALUE + "')\" ><i class=\"icon-list-alt icon-large\"></i>" + ResourceLoader.GetResource(session, "items.picker") + "</a>):  &nbsp;</div>"
                                                + "<div class=\"edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF_GRP + z + PostBackConstants.VALUE + "\">"
                                                + StringEscapeUtils.escapeHtml(bd.getCategoryBag().getKeyedReferenceGroup().get(i).getTModelKey())
                                                + "</div>"
                                                + "<div id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF_GRP + z + PostBackConstants.KEY_REF + "\" style=\"border-width:1px; border-style:solid\">"
                                                + "<div style=\"float:left;height:100%\"><a href=\"javascript:AddCategoryKeyReferenceSpecificBT('" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF_GRP + z + "keyref');\"><i class=\"icon-plus-sign icon-large\"></i></a></div>"
                                                + ResourceLoader.GetResource(session, "items.keyrefcat.add")
                                                + "</div>");
                                        for (int k = 0; k < bd.getBindingTemplates().getBindingTemplate().get(i).getCategoryBag().getKeyedReferenceGroup().get(z).getKeyedReference().size(); k++) {

                                            out.write("<div id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF_GRP + z + PostBackConstants.KEY_REF + k + "\" style=\"border-width:1px; border-style:solid\">");
                                            out.write("<div style=\"float:left;height:100%\"><a href=\"javascript:Remove('" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF_GRP + z + "keyref" + k + "');\"><i class=\"icon-remove-sign icon-large\"></i></a></div>");
                                            out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.key") + " (<a href=\"javascript:tModelModal('" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF_GRP + z + PostBackConstants.KEY_REF + k + PostBackConstants.VALUE + "')\" ><i class=\"icon-list-alt icon-large\"></i>" + ResourceLoader.GetResource(session, "items.picker") + "</a>): &nbsp;</div>"
                                                    + "<div class=\"edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF_GRP + z + PostBackConstants.KEY_REF + k + PostBackConstants.VALUE + "\">" + StringEscapeUtils.escapeHtml(bd.getBindingTemplates().getBindingTemplate().get(i).getCategoryBag().getKeyedReferenceGroup().get(z).getKeyedReference().get(k).getTModelKey()) + "</div>");
                                            out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.name") + ": &nbsp;</div>"
                                                    + "<div class=\"edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF_GRP + z + PostBackConstants.KEY_REF + k + PostBackConstants.KEYNAME + "\">" + StringEscapeUtils.escapeHtml(bd.getBindingTemplates().getBindingTemplate().get(i).getCategoryBag().getKeyedReferenceGroup().get(z).getKeyedReference().get(k).getKeyName()) + "</div>");
                                            out.write("<div style=\"float:left\">" + ResourceLoader.GetResource(session, "items.value") + ": &nbsp;</div>"
                                                    + "<div class=\"edit\" id=\"" + PostBackConstants.BINDINGTEMPLATE + i + PostBackConstants.CATBAG_KEY_REF_GRP + z + PostBackConstants.KEY_REF + k + PostBackConstants.KEYVALUE + "\">" + StringEscapeUtils.escapeHtml(bd.getBindingTemplates().getBindingTemplate().get(i).getCategoryBag().getKeyedReferenceGroup().get(z).getKeyedReference().get(k).getKeyValue()) + "</div>");
                                            out.write("</div>");
                                        }
                                        out.write("</div>");    //end key refer group
                                    }
                                %>
                            </div><!-- catbag keyref group -->
                        </div><!-- end of a specific bt -->
                        <%

                            } //end of binding templates loop
                        %>
                    </div> <!-- binding template container-->


                    <br>
                </div><!-- binding template tab pane -->






                <div class="tab-pane" id="signatures"><b><%=ResourceLoader.GetResource(session, "items.dsigs")%></b>
                    <br>
                    <%
                        if (bd.getSignature().isEmpty()) {
                            out.write(ResourceLoader.GetResource(session, "items.signed.not"));
                        } else {
                            out.write(ResourceLoader.GetResource(session, "items.signed") + " " + bd.getSignature().size());
                    %>
                    <table class="table table-hover">
                        <tr><th>#</th><th>Signed by</th><th></th><th>Signature Status</th></tr>

                        <%
                            for (int k = 0; k < bd.getSignature().size(); k++) {
                                out.write("<tr><td>" + k + "</td><td>");
                                out.write(x.SignatureToReadable(bd.getSignature().get(k)));
                                out.write("</td><td>");
                                out.write("<a href=\"ajax/getCert.jsp?type=service&id=" + URLEncoder.encode(bd.getServiceKey(), "UTF-8") + "&index=" + k + "\">" + ResourceLoader.GetResource(session, "items.signed.viewcert") + "</a>");
                                out.write("</td><td><div id=\"digsig" + k + "\">" + ResourceLoader.GetResource(session, "items.loading") + "</div>");
                        %>
                        <script type="text/javascript">
                            $.get("ajax/validateSignature.jsp?type=service&id=<%=StringEscapeUtils.escapeJavaScript(bd.getServiceKey())%>", function(data){
                                $("#digsig<%=k%>").html(data);
                            } )
                        </script>
                        <%
                                out.write("</td></tr>");
                            }
                        %>
                    </table>
                    <%
                        }
                    %>
                    <br><Br>
                </div>

                <div class="tab-pane" id="opinfo">
                    <%
                        if (!newitem) {

                    %>
                    <script type="text/javascript">
                        $.get("ajax/opInfo.jsp?id=<%=StringEscapeUtils.escapeJavaScript(bd.getServiceKey())%>", function(data){
                            $("#opinfodiv").html(data);
                        } )
                    </script>
                    <div id="opinfodiv"></div>
                    <%
                        }
                    %>
                    <Br>
                </div><!-- end opinfo-->
            </div><!-- tab content -->
        </div><!-- end business editor -->


        <script type="text/javascript">
            var currentDescriptionSpecific=<%=totalBTDescriptions%>;
        </script>
        <Br><br>
        <%
            if (bd.getSignature().isEmpty()) {
        %>
        <a class="btn btn-primary " href="javascript:saveService();"><i class="icon-save icon-large"></i> <%=ResourceLoader.GetResource(session, "actions.save")%></a>


        <%  } else {
        %>
        <a href="#confirmDialog" role="button" class="btn btn-primary" data-toggle="modal"><i class="icon-save icon-large"></i> <%=ResourceLoader.GetResource(session, "actions.save")%></a> |

        <%        }
            if (!newitem) {
        %>


        <a class="btn btn-danger " href="javascript:deleteService();"><i class="icon-remove-sign icon-large"></i> <%=ResourceLoader.GetResource(session, "actions.delete")%></a> |
        <a class="btn btn-success " href="signer.jsp?id=<%=URLEncoder.encode(bd.getServiceKey(), "UTF8")%>&type=service"><i class="icon-pencil icon-large"></i> <%=ResourceLoader.GetResource(session, "actions.sign")%></a> |
        <a class="btn btn-info " href="#" title="<%=ResourceLoader.GetResource(session, "actions.subscribe.description")%>"><i class="icon-rss icon-large"></i> <%=ResourceLoader.GetResource(session, "actions.subscribe")%></a> |
        <a class="btn btn-warning " href="#" title="<%=ResourceLoader.GetResource(session, "actions.transfer.description")%>"><i class="icon-exchange icon-large"></i> <%=ResourceLoader.GetResource(session, "actions.transfer")%></a> |
        <a class="btn "  href="javascript:ViewAsXML();"><i class="icon-screenshot icon-large"></i> <%=ResourceLoader.GetResource(session, "actions.asxml")%></a>
        <script type="text/javascript">
            function ViewAsXML()
            {
                $.get("ajax/toXML.jsp?id=<%=URLEncoder.encode(serviceid, "UTF-8")%>&type=service", function(data){
                    window.console && console.log('asXml success');                
                    $("#viewAsXmlContent").html(safe_tags_replace(data));
                    $( "#viewAsXml" ).modal('show');
                });
                       
            }
        </script>
        <%
            }
        %>
    </div> <!-- end of the row -->

    <script type="text/javascript" src="js/businessEditor.js"></script>
    <script type="text/javascript" src="js/serviceEditor.js"></script>
</div> <!-- span12-->

<div class="modal hide fade" id="confirmDialog">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3><%=ResourceLoader.GetResource(session, "modal.digitalsignaturewarning.title")%></h3>
    </div>
    <div class="modal-body">
        <p><%=ResourceLoader.GetResource(session, "modal.digitalsignaturewarning.body")%></p>
    </div>
    <div class="modal-footer">
        <a href="#" class="btn"><%=ResourceLoader.GetResource(session, "modal.close")%></a>
        <a href="javascript:saveService();$('#confirmDialog').modal('hide');" class="btn btn-primary">
            <%=ResourceLoader.GetResource(session, "modal.savechanges")%></a>
    </div>
</div>
<%
    if (!newitem) {

%>
<div class="modal hide fade" id="viewAsXml">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>As XML</h3>
    </div>
    <script type="text/javascript">
        function closeXmlPop(modaldiv)
        {
            $('#' + modaldiv).modal('hide');
        }
    </script>
    <div class="modal-body" id="viewAsXmlContent">


    </div>
    <div class="modal-footer">
        <a href="ajax/toXML.jsp?id=<%=URLEncoder.encode(bd.getServiceKey(), "UTF-8")%>&type=service" class="btn btn-primary" target="_blank">Popout</a> 
        <a href="javascript:closeXmlPop('viewAsXml');" class="btn"><%=ResourceLoader.GetResource(session, "modal.close")%></a>
    </div>
</div>
<%
    }
%>
<!-- container div is in header bottom-->
<%@include file="tmodelChooser.jsp" %>
<%@include file="header-bottom.jsp" %>