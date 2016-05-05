package com.thingswise.appframework.examples.rpc.authorization;

import java.util.Set;

import org.osgi.service.component.annotations.Component;

import com.google.common.collect.Sets;
import com.thingswise.appframework.services.rpc.AuthorizationHandler;
import com.thingswise.appframework.services.rpc.AuthorizationHandlerFactory;

@Component
public class PropertyFileAuthorizationHandlerFactory implements AuthorizationHandlerFactory {

	@Override
	public AuthorizationHandler create(String scope, String project) throws Exception {
		return new PropertyFileAuthorizationHandler(scope, project);
	}
	
	private static final Set<String> methodNames = Sets.newHashSet("sendAlert");

	@Override
	public Set<String> getMethodNames() {
		return methodNames;
	}

	@Override
	public String getName() {
		return "99propfile";
	}

}
