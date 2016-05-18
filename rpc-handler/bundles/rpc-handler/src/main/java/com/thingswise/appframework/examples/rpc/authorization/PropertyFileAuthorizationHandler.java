package com.thingswise.appframework.examples.rpc.authorization;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.thingswise.appframework.services.rpc.AuthorizationHandler;

public class PropertyFileAuthorizationHandler implements AuthorizationHandler {
	
	private Map<String, Set<String>> authorizations = new HashMap<String, Set<String>>(); 
	
	PropertyFileAuthorizationHandler(String scope, String project) {
		
	}

	@Override
	public ListenableFuture<Boolean> isAuthorized(String method, String tenantId) {		
		Set<String> m = authorizations.get(method);
		if (m != null && m.contains(tenantId)) {
			return Futures.immediateFuture(true);
		} else {
			return Futures.immediateFuture(false);
		}
	}

	@Override
	public ListenableFuture<Void> configure(Map<String, ?> properties) {
		Map<String, Set<String>> authorizations = new HashMap<String, Set<String>>();
		for (String key : properties.keySet()) {
			String line = (String) properties.get(key);
			String[] methods = line.split(",");
			Set<String> methodSet = new HashSet<String>();
			if (methods != null) {
				for (String method : methods) {
					if (method != null) {
						String m = method.trim();
						if (m.length() > 0) {
							methodSet.add(m);
						}
					}
				}
			}
			authorizations.put(key, methodSet);
		}
		this.authorizations = authorizations;
		return Futures.immediateFuture(null);
	}

}
