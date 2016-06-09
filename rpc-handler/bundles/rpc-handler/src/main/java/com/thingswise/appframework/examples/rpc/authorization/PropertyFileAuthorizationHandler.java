package com.thingswise.appframework.examples.rpc.authorization;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.thingswise.appframework.services.rpc.AuthorizationHandler;

public class PropertyFileAuthorizationHandler implements AuthorizationHandler {
	
	private SetMultimap<String, String> authorizations = HashMultimap.create(); 
	
	PropertyFileAuthorizationHandler(String scope, String project) {
		
	}

	@Override
	public ListenableFuture<List<AuthorizationResponse>> isAuthorized(String method, List<String> tenants) {
		List<AuthorizationResponse> responses = new ArrayList<AuthorizationResponse>(tenants.size());
		for (String tenantId : tenants) {
			if (authorizations.containsEntry(method, tenantId)) {
				responses.add(new AuthorizationResponse(true, null, null));
			} else {
				responses.add(new AuthorizationResponse(false, String.format("Tenant %s is not authorized to access method %s", tenantId, method), null));
			}
		}
		return Futures.immediateFuture(responses);
	}

	@Override
	public ListenableFuture<Void> configure(Map<String, ?> properties) {
		SetMultimap<String, String> authorizations = HashMultimap.create();
		for (String key : properties.keySet()) {
			String line = (String) properties.get(key);
			String[] methods = line.split(",");
			if (methods != null) {
				for (String method : methods) {
					if (method != null) {
						String m = method.trim();
						if (m.length() > 0) {
							authorizations.put(key, method);
						}
					}
				}
			}
			
		}
		this.authorizations = authorizations;
		return Futures.immediateFuture(null);
	}

}
