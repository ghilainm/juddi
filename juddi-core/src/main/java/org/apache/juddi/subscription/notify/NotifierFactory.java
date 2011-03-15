package org.apache.juddi.subscription.notify;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.juddi.ClassUtil;
import org.apache.juddi.model.BindingTemplate;
import org.apache.juddi.model.TmodelInstanceInfo;
/**
 * The factory figures out which Notifier class to instantiate.
 * 
 * @author kstam
 *
 */
public class NotifierFactory {
	
	Log log = LogFactory.getLog(this.getClass());
	public static String UDDI_TRANSPORT_KEY = "uddi:uddi.org:transport:";
	
	public Notifier getNotifier(BindingTemplate bindingTemplate) 
		throws URISyntaxException, IllegalArgumentException, SecurityException, 
		InstantiationException, IllegalAccessException, InvocationTargetException, 
		NoSuchMethodException, ClassNotFoundException 
	{
	    String notifierClassName = null;
		for (TmodelInstanceInfo tModelInstanceInfo : bindingTemplate.getTmodelInstanceInfos()) {
			if (tModelInstanceInfo.getTmodelKey().startsWith(UDDI_TRANSPORT_KEY)) {
				log.debug("Found transport tModelKey " + tModelInstanceInfo.getTmodelKey());
				String transport = tModelInstanceInfo.getTmodelKey().substring(UDDI_TRANSPORT_KEY.length(),tModelInstanceInfo.getTmodelKey().length());
				transport = transport.replaceAll("-", "_");
				notifierClassName = "org.apache.juddi.subscription.notify." + transport.toUpperCase() +  "Notifier";
				break;
			}
		}
		if (notifierClassName == null) {
			log.error("The bindingTemplate " + bindingTemplate.getEntityKey() + " does not contain a tModel to define its type of transport." +
				" Therefore no notification can be sent out. The user needs to fix the subscription data.");
		} else {
			if (log.isDebugEnabled()) log.debug("Going find and instantiate notifier class: " + notifierClassName);
		}
		@SuppressWarnings("unchecked")
		Class<Notifier> notifierClass = (Class<Notifier>) ClassUtil.forName(notifierClassName,this.getClass());
		Notifier notifier = notifierClass.getConstructor(BindingTemplate.class).newInstance(bindingTemplate);
	    return notifier;
	}
	
}
