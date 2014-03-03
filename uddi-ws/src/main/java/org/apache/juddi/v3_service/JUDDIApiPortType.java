
package org.apache.juddi.v3_service;

import java.rmi.RemoteException;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import org.apache.juddi.api_v3.AdminSaveBusinessWrapper;
import org.apache.juddi.api_v3.AdminSaveTModelWrapper;
import org.apache.juddi.api_v3.ClerkDetail;
import org.apache.juddi.api_v3.ClerkList;
import org.apache.juddi.api_v3.ClientSubscriptionInfoDetail;
import org.apache.juddi.api_v3.DeleteClerk;
import org.apache.juddi.api_v3.DeleteClientSubscriptionInfo;
import org.apache.juddi.api_v3.DeleteNode;
import org.apache.juddi.api_v3.DeletePublisher;
import org.apache.juddi.api_v3.GetAllPublisherDetail;
import org.apache.juddi.api_v3.GetPublisherDetail;
import org.apache.juddi.api_v3.NodeDetail;
import org.apache.juddi.api_v3.NodeList;
import org.apache.juddi.api_v3.PublisherDetail;
import org.apache.juddi.api_v3.SaveClerk;
import org.apache.juddi.api_v3.SaveClientSubscriptionInfo;
import org.apache.juddi.api_v3.SaveNode;
import org.apache.juddi.api_v3.SavePublisher;
import org.apache.juddi.api_v3.SubscriptionWrapper;
import org.apache.juddi.api_v3.SyncSubscription;
import org.apache.juddi.api_v3.SyncSubscriptionDetail;
import org.apache.juddi.api_v3.ValidValues;
import org.uddi.api_v3.DeleteTModel;
import org.uddi.api_v3.DispositionReport;
import org.uddi.v3_service.DispositionReportFaultMessage;
import org.uddi.repl_v3.ReplicationConfiguration;
import org.uddi.sub_v3.Subscription;


/**
 *  This portType defines all of the jUDDI publisher operations.
 *   This is above and beyond the original UDDI v3 specification and is NOT part of the standard.
 *   Method behavior within this class may change from version to version. These methods are
 *   primarily meant for administrative functions.
 * 
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "JUDDI_Api_PortType", targetNamespace = "urn:juddi-apache-org:v3_service")
@XmlSeeAlso({
    org.uddi.repl_v3.ObjectFactory.class,
    org.apache.juddi.api_v3.ObjectFactory.class,
    org.uddi.api_v3.ObjectFactory.class,
    org.uddi.custody_v3.ObjectFactory.class,
    org.uddi.policy_v3.ObjectFactory.class,
    org.uddi.policy_v3_instanceparms.ObjectFactory.class,
    org.uddi.sub_v3.ObjectFactory.class,
    org.w3._2000._09.xmldsig_.ObjectFactory.class
})
public interface JUDDIApiPortType {


    /**
     * 
     * @param parameters
     * @return
     *     returns org.apache.juddi.api_v3.PublisherDetail
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "get_publisherDetail", action = "get_publisherDetail")
    @WebResult(name = "publisherDetail", targetNamespace = "urn:juddi-apache-org:api_v3", partName = "parameters")
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public PublisherDetail getPublisherDetail(
        @WebParam(name = "get_publisherDetail", targetNamespace = "urn:juddi-apache-org:api_v3", partName = "parameters")
        GetPublisherDetail parameters)
        throws DispositionReportFaultMessage, RemoteException
    ;

    /**
     * 
     * @param body
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "delete_ClientSubscriptionInfo", action = "delete_ClientSubscriptionInfo")
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public void deleteClientSubscriptionInfo(
        @WebParam(name = "delete_ClientSubscriptionInfo", targetNamespace = "urn:juddi-apache-org:api_v3", partName = "body")
        DeleteClientSubscriptionInfo body)
        throws DispositionReportFaultMessage, RemoteException
    ;

    /**
     * 
     * @param body
     * @return
     *     returns org.apache.juddi.api_v3.PublisherDetail
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "get_allPublisherDetail", action = "get_allPublisherDetail")
    @WebResult(name = "publisherDetail", targetNamespace = "urn:juddi-apache-org:api_v3", partName = "body")
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public PublisherDetail getAllPublisherDetail(
        @WebParam(name = "get_allPublisherDetail", targetNamespace = "urn:juddi-apache-org:api_v3", partName = "body")
        GetAllPublisherDetail body)
        throws DispositionReportFaultMessage, RemoteException
    ;

    /**
     * 
     * @param body
     * @return
     *     returns org.apache.juddi.api_v3.ClerkDetail
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "save_Clerk", action = "save_Clerk")
    @WebResult(name = "save_ClerkResponse", targetNamespace = "urn:juddi-apache-org:v3_service", partName = "save_ClerkResponse")
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public ClerkDetail saveClerk(
        @WebParam(name = "save_ClerkRequest", targetNamespace = "urn:juddi-apache-org:api_v3", partName = "body")
        SaveClerk body)
        throws DispositionReportFaultMessage, RemoteException
    ;

    /**
     * 
     * @param body
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "delete_publisher", action = "delete_publisher")
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public void deletePublisher(
        @WebParam(name = "delete_publisher", targetNamespace = "urn:juddi-apache-org:api_v3", partName = "body")
        DeletePublisher body)
        throws DispositionReportFaultMessage, RemoteException
    ;

    /**
     * 
     * @param body
     * @return
     *     returns org.apache.juddi.api_v3.NodeDetail
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "save_Node", action = "save_Node")
    @WebResult(name = "save_NodeResponse", targetNamespace = "urn:juddi-apache-org:v3_service", partName = "save_NodeResponse")
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public NodeDetail saveNode(
        @WebParam(name = "save_NodeRequest", targetNamespace = "urn:juddi-apache-org:api_v3", partName = "body")
        SaveNode body)
        throws DispositionReportFaultMessage, RemoteException
    ;

    /**
     * 
     * @param body
     * @return
     *     returns org.apache.juddi.api_v3.PublisherDetail
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "save_publisher", action = "save_publisher")
    @WebResult(name = "save_publisherResponse", targetNamespace = "urn:juddi-apache-org:v3_service", partName = "save_publisherResponse")
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public PublisherDetail savePublisher(
        @WebParam(name = "save_publisher", targetNamespace = "urn:juddi-apache-org:api_v3", partName = "body")
        SavePublisher body)
        throws DispositionReportFaultMessage, RemoteException
    ;

    /**
     * 
     * @param body
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "adminDelete_tModel", action = "adminDelete_tmodel")
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public void adminDeleteTModel(
        @WebParam(name = "adminDelete_tmodel", targetNamespace = "urn:juddi-apache-org:api_v3", partName = "body")
        DeleteTModel body)
        throws DispositionReportFaultMessage, RemoteException
    ;

    /**
     * 
     * @param body
     * @return
     *     returns org.apache.juddi.api_v3.ClientSubscriptionInfoDetail
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "save_ClientSubscriptionInfo", action = "save_ClientSubscriptionInfo")
    @WebResult(name = "save_ClientSubscriptionInfoResponse", targetNamespace = "urn:juddi-apache-org:v3_service", partName = "save_ClientSubscriptionInfoResponse")
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public ClientSubscriptionInfoDetail saveClientSubscriptionInfo(
        @WebParam(name = "save_ClientSubscriptionInfoRequest", targetNamespace = "urn:juddi-apache-org:api_v3", partName = "body")
        SaveClientSubscriptionInfo body)
        throws DispositionReportFaultMessage, RemoteException
    ;

    /**
     * 
     * @param syncSubscription
     * @return
     *     returns org.apache.juddi.api_v3.SyncSubscriptionDetail
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "invoke_SyncSubscription", action = "invoke_SyncSubscription")
    @WebResult(name = "syncSubscriptionDetail", targetNamespace = "urn:juddi-apache-org:api_v3")
    @RequestWrapper(localName = "invoke_SyncSubscription", targetNamespace = "urn:juddi-apache-org:api_v3", className = "org.apache.juddi.api_v3.SyncSubscriptionRequest")
    @ResponseWrapper(localName = "invoke_SyncSubscriptionResponse", targetNamespace = "urn:juddi-apache-org:v3_service", className = "org.apache.juddi.api_v3.SyncSubscriptionDetailResponse")
    public SyncSubscriptionDetail invokeSyncSubscription(
        @WebParam(name = "syncSubscription", targetNamespace = "urn:juddi-apache-org:api_v3")
        SyncSubscription syncSubscription)
        throws DispositionReportFaultMessage, RemoteException
    ;

    /**
     * 
     * 		gets all nodes persisted in the database, useful for replication scenarios
     * 		@since 3.3
     * 	  
     * 
     * @param authInfo
     * @return
     *     returns org.apache.juddi.api_v3.NodeList
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "get_AllNodes", action = "get_AllNodes")
    @WebResult(name = "nodeList", targetNamespace = "urn:juddi-apache-org:api_v3")
    @RequestWrapper(localName = "get_AllNodes", targetNamespace = "urn:juddi-apache-org:api_v3", className = "org.apache.juddi.api_v3.GetAllNodes")
    @ResponseWrapper(localName = "get_AllNodesResponse", targetNamespace = "urn:juddi-apache-org:api_v3", className = "org.apache.juddi.api_v3.GetAllNodesResponse")
    public NodeList getAllNodes(
        @WebParam(name = "authInfo", targetNamespace = "urn:juddi-apache-org:api_v3")
        String authInfo)
        throws DispositionReportFaultMessage, RemoteException
    ;

    /**
     * 
     * 		gets all Clerks persisted in the database, useful for replication scenarios. Clerks provide a mapping for credentials to a Node
     * 		@since 3.3
     * 	  
     * 
     * @param authInfo
     * @return
     *     returns org.apache.juddi.api_v3.ClerkList
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "get_AllClerks", action = "get_AllClerks")
    @WebResult(name = "clerkList", targetNamespace = "urn:juddi-apache-org:api_v3")
    @RequestWrapper(localName = "get_AllClerks", targetNamespace = "urn:juddi-apache-org:api_v3", className = "org.apache.juddi.api_v3.GetAllClerks")
    @ResponseWrapper(localName = "get_AllClerksResponse", targetNamespace = "urn:juddi-apache-org:api_v3", className = "org.apache.juddi.api_v3.GetAllClerksResponse")
    public ClerkList getAllClerks(
        @WebParam(name = "authInfo", targetNamespace = "urn:juddi-apache-org:api_v3")
        String authInfo)
        throws DispositionReportFaultMessage, RemoteException
    ;

    /**
     * 
     * 		removes a node from the database, useful for replication scenarios. 
     * 		Note: when removing a node, all associated clerks will be removed with it.
     * 		@since 3.3
     * 	  
     * 
     * @param body
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "delete_Node", action = "delete_Node")
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public void deleteNode(
        @WebParam(name = "delete_Node", targetNamespace = "urn:juddi-apache-org:api_v3", partName = "body")
        DeleteNode body)
        throws DispositionReportFaultMessage, RemoteException
    ;

    /**
     * 
     * 		removes a clerk from the database, useful for replication scenarios. 
     * 		@since 3.3
     * 	  
     * 
     * @param request
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "delete_Clerk", action = "delete_Clerk")
    @SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public void deleteClerk(
        @WebParam(name = "delete_Clerk", targetNamespace = "urn:juddi-apache-org:api_v3", partName = "request")
        DeleteClerk request)
        throws DispositionReportFaultMessage, RemoteException
    ;

    /**
     * 
     * 		saves a business just like from the Publication API, however administrators can use this to perform restores from backup.
     * 		It allows an administrator to save the business, setting the ownership to any user, thus maintaining access control rules.
     * 		@since 3.3
     * 	  
     * 
     * @param values
     * @param authInfo
     * @return
     *     returns org.uddi.api_v3.DispositionReport
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "adminSave_Business", action = "adminSave_Business")
    @WebResult(name = "dispositionReport", targetNamespace = "urn:juddi-apache-org:api_v3")
    @RequestWrapper(localName = "adminSave_Business", targetNamespace = "urn:juddi-apache-org:api_v3", className = "org.apache.juddi.api_v3.AdminSaveBusiness")
    @ResponseWrapper(localName = "adminSave_BusinessResponse", targetNamespace = "urn:juddi-apache-org:api_v3", className = "org.apache.juddi.api_v3.AdminSaveBusinessResponse")
    public DispositionReport adminSaveBusiness(
        @WebParam(name = "authInfo", targetNamespace = "urn:juddi-apache-org:api_v3")
        String authInfo,
        @WebParam(name = "values", targetNamespace = "urn:juddi-apache-org:api_v3")
        List<AdminSaveBusinessWrapper> values)
        throws DispositionReportFaultMessage, RemoteException
    ;

    /**
     * 
     * 		saves a tModel just like from the Publication API, however administrators can use this to perform restores from backup.
     * 		It allows an administrator to save the tModel, setting the ownership to any user, thus maintaining access control rules.
     * 		@since 3.3
     * 	  
     * 
     * @param values
     * @param authInfo
     * @return
     *     returns org.uddi.api_v3.DispositionReport
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "adminSave_tModel", action = "adminSave_tModel")
    @WebResult(name = "dispositionReport", targetNamespace = "urn:juddi-apache-org:api_v3")
    @RequestWrapper(localName = "adminSave_tModel", targetNamespace = "urn:juddi-apache-org:api_v3", className = "org.apache.juddi.api_v3.AdminSaveTModel")
    @ResponseWrapper(localName = "adminSave_tModelResponse", targetNamespace = "urn:juddi-apache-org:api_v3", className = "org.apache.juddi.api_v3.AdminSaveTModelResponse")
    public DispositionReport adminSaveTModel(
        @WebParam(name = "authInfo", targetNamespace = "urn:juddi-apache-org:api_v3")
        String authInfo,
        @WebParam(name = "values", targetNamespace = "urn:juddi-apache-org:api_v3")
        List<AdminSaveTModelWrapper> values)
        throws DispositionReportFaultMessage, RemoteException
    ;

    /**
     * 
     * 		returns all Nodes that have been enabled for replication. 
     * 		@since 3.3
     * 	  
     * 
     * @param authInfo
     * @return
     *     returns org.uddi.repl_v3.ReplicationConfiguration
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "get_ReplicationNodes", action = "get_ReplicationNodes")
    @WebResult(name = "replicationConfiguration", targetNamespace = "urn:uddi-org:repl_v3")
    @RequestWrapper(localName = "get_ReplicationNodes", targetNamespace = "urn:juddi-apache-org:api_v3", className = "org.apache.juddi.api_v3.GetReplicationNodes")
    @ResponseWrapper(localName = "get_ReplicationNodesResponse", targetNamespace = "urn:juddi-apache-org:api_v3", className = "org.apache.juddi.api_v3.GetReplicationNodesResponse")
    public ReplicationConfiguration getReplicationNodes(
        @WebParam(name = "authInfo", targetNamespace = "urn:juddi-apache-org:api_v3")
        String authInfo)
        throws DispositionReportFaultMessage, RemoteException
    ;

    /**
     * 
     * 		sets all Nodes for replication.  Any previously set Nodes will be removed from the replication list.
     * 		Optionally, all data from remote nodes that 
     * 		@since 3.3
     * 	  
     * 
     * @param replicationConfiguration
     * @param authInfo
     * @return
     *     returns org.uddi.api_v3.DispositionReport
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "set_ReplicationNodes", action = "set_ReplicationNodes")
    @WebResult(name = "dispositionReport", targetNamespace = "urn:juddi-apache-org:api_v3")
    @RequestWrapper(localName = "set_ReplicationNodes", targetNamespace = "urn:juddi-apache-org:api_v3", className = "org.apache.juddi.api_v3.SetReplicationNodes")
    @ResponseWrapper(localName = "set_ReplicationNodesResponse", targetNamespace = "urn:juddi-apache-org:api_v3", className = "org.apache.juddi.api_v3.SetReplicationNodesResponse")
    public DispositionReport setReplicationNodes(
        @WebParam(name = "authInfo", targetNamespace = "urn:juddi-apache-org:api_v3")
        String authInfo,
        @WebParam(name = "replicationConfiguration", targetNamespace = "urn:uddi-org:repl_v3")
        ReplicationConfiguration replicationConfiguration)
        throws DispositionReportFaultMessage, RemoteException
    ;

    /**
     * 
     * 		gets all client subscriptions. useful for backup and restore operations
     * 		@since 3.3
     * 	  
     * 
     * @param authInfo
     * @return
     *     returns java.util.List<org.apache.juddi.api_v3.SubscriptionWrapper>
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "get_allClientSubscriptionInfo", action = "get_allClientSubscriptionInfo")
    @WebResult(name = "subscriptions", targetNamespace = "urn:juddi-apache-org:api_v3")
    @RequestWrapper(localName = "get_allClientSubscriptionInfo", targetNamespace = "urn:juddi-apache-org:api_v3", className = "org.apache.juddi.api_v3.GetAllClientSubscriptionInfo")
    @ResponseWrapper(localName = "get_allClientSubscriptionInfoResponse", targetNamespace = "urn:juddi-apache-org:api_v3", className = "org.apache.juddi.api_v3.GetAllClientSubscriptionInfoResponse")
    public List<SubscriptionWrapper> getAllClientSubscriptionInfo(
        @WebParam(name = "authInfo", targetNamespace = "urn:juddi-apache-org:api_v3")
        String authInfo)
        throws DispositionReportFaultMessage, RemoteException
    ;

    /**
     * 
     * 		sets a tModel's valid values for use with the Value Set Validation API
     * 		@since 3.3
     * 	  
     * 
     * @param values
     * @param authInfo
     * @return
     *     returns org.uddi.api_v3.DispositionReport
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "set_allValidValues", action = "set_allValidValues")
    @WebResult(name = "dispositionReport", targetNamespace = "urn:juddi-apache-org:api_v3")
    @RequestWrapper(localName = "set_allValidValues", targetNamespace = "urn:juddi-apache-org:api_v3", className = "org.apache.juddi.api_v3.SetAllValidValues")
    @ResponseWrapper(localName = "set_allValidValuesResponse", targetNamespace = "urn:juddi-apache-org:api_v3", className = "org.apache.juddi.api_v3.SetAllValidValuesResponse")
    public DispositionReport setAllValidValues(
        @WebParam(name = "authInfo", targetNamespace = "urn:juddi-apache-org:api_v3")
        String authInfo,
        @WebParam(name = "values", targetNamespace = "urn:juddi-apache-org:api_v3")
        List<ValidValues> values)
        throws DispositionReportFaultMessage, RemoteException
    ;

    /**
     * 
     * 		deletes a client subscription
     * 		@since 3.3
     * 	  
     * 
     * @param subscriptionKey
     * @param authInfo
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "adminDelete_Subscription", action = "adminDelete_Subscription")
    @RequestWrapper(localName = "adminDelete_Subscription", targetNamespace = "urn:juddi-apache-org:api_v3", className = "org.apache.juddi.api_v3.AdminDeleteSubscriptionRequest")
    @ResponseWrapper(localName = "adminDelete_SubscriptionResponse", targetNamespace = "urn:juddi-apache-org:api_v3", className = "org.apache.juddi.api_v3.AdminDeleteSubscriptionResponse")
    public void adminDeleteSubscription(
        @WebParam(name = "authInfo", targetNamespace = "urn:juddi-apache-org:api_v3")
        String authInfo,
        @WebParam(name = "subscriptionKey", targetNamespace = "urn:juddi-apache-org:api_v3")
        List<String> subscriptionKey)
        throws DispositionReportFaultMessage, RemoteException
    ;

    /**
     * 
     * 		saves a client subscription, useful for restore functions
     * 		@since 3.3
     * 	  
     * 
     * @param publisherOrUsername
     * @param subscriptions
     * @param authInfo
     * @throws DispositionReportFaultMessage
     */
    @WebMethod(operationName = "adminSave_Subscription", action = "adminSave_Subscription")
    @RequestWrapper(localName = "adminSave_Subscription", targetNamespace = "urn:juddi-apache-org:api_v3", className = "org.apache.juddi.api_v3.AdminSaveSubscriptionRequest")
    @ResponseWrapper(localName = "adminSave_SubscriptionResponse", targetNamespace = "urn:juddi-apache-org:api_v3", className = "org.apache.juddi.api_v3.AdminSaveSubscriptionResponse")
    public void adminSaveSubscription(
        @WebParam(name = "authInfo", targetNamespace = "urn:juddi-apache-org:api_v3")
        String authInfo,
        @WebParam(name = "publisherOrUsername", targetNamespace = "urn:juddi-apache-org:api_v3")
        String publisherOrUsername,
        @WebParam(name = "subscriptions", targetNamespace = "urn:juddi-apache-org:api_v3")
        List<Subscription> subscriptions)
        throws DispositionReportFaultMessage, RemoteException
    ;

}
