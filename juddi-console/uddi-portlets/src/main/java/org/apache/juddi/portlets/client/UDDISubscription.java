/*
 * Copyright 2001-2009 The Apache Software Foundation.
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
package org.apache.juddi.portlets.client;

import org.apache.juddi.portlets.client.model.Subscription;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 *  @author <a href="mailto:kstam@apache.org">Kurt T Stam</a>
 */
public class UDDISubscription implements EntryPoint, Login {

	private static UDDISubscription singleton;
	
	private MenuBarPanel menuBar = new MenuBarPanel(MenuBarPanel.SUBSCRIPTION);
	private DockPanel dockPanel = new DockPanel();
	private LoginPanel loginPanel = new LoginPanel(this);
	private SubscriptionListPanel subscriptionListPanel = new SubscriptionListPanel();
	private SubscriptionPanel subscriptionPanel = null;

	public static UDDISubscription getInstance() {
		return singleton;
	}
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() { 
		singleton = this;
		
		dockPanel.setSpacing(8);
		menuBar.setVisible(false);
		menuBar.setHeight("20px");
		dockPanel.add(menuBar,DockPanel.NORTH);
		
		loginPanel.setVisible(false);
		dockPanel.add(loginPanel,DockPanel.WEST);
		
		subscriptionListPanel.setVisible(false);
		dockPanel.add(subscriptionListPanel,DockPanel.CENTER);
		
		RootPanel.get("subscription").add(dockPanel);
	}
	
	public void login() {
		String token = loginPanel.getToken();
		if (token == null ) {
			loginPanel.setVisible(true);
			subscriptionListPanel.setVisible(false);
			menuBar.setVisible(false);
		} else {
			loginPanel.setVisible(false);
			menuBar.setVisible(true);
			subscriptionListPanel.setVisible(true);
			subscriptionListPanel.listSubscriptions(token);
		}
	}
	
	public void displaySubscription(Subscription subscription) {
		if (subscriptionPanel!=null ) dockPanel.remove(subscriptionPanel);
		subscriptionPanel = new SubscriptionPanel(subscription);
		dockPanel.add(subscriptionPanel,DockPanel.EAST);
		String token = loginPanel.getToken();
		subscriptionListPanel.listSubscriptions(token);
	}
	
	public void setSelectedSubscription(String selectedSubscriptionId) {
		subscriptionListPanel.setSelectedSubscription(selectedSubscriptionId);
	}
	
	public void hideSubscription() {
		subscriptionPanel.setVisible(false);
		String token = loginPanel.getToken();
		subscriptionListPanel.selectRow(0);
		if (subscriptionPanel!=null ) dockPanel.remove(subscriptionPanel);
		subscriptionPanel=null;
		subscriptionListPanel.listSubscriptions(token);
	}

	public String getToken() {
		return loginPanel.getToken();
	}
	
	public void saveSubscription() {
		if (subscriptionPanel!=null) {
			subscriptionPanel.saveSubscription(getToken());
		}
	}
	
	public void newSubscription() {
		if (subscriptionPanel!=null ) dockPanel.remove(subscriptionPanel);
		subscriptionPanel = new SubscriptionPanel(null);
		dockPanel.add(subscriptionPanel,DockPanel.EAST);
		subscriptionListPanel.selectRow(0);
	}
	
	public void deleteSubscription() {
		if (subscriptionPanel!=null) {
	//		subscriptionPanel.deleteSubscription(getToken());
		}
	}


	
	
	
}
