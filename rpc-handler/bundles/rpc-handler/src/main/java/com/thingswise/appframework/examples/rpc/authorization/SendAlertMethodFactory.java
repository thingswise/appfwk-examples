package com.thingswise.appframework.examples.rpc.authorization;

import org.osgi.service.component.annotations.Component;

import com.thingswise.appframework.services.rpc.RPCMethodHandler;
import com.thingswise.appframework.services.rpc.RPCMethodHandlerFactory;

@Component
public class SendAlertMethodFactory implements RPCMethodHandlerFactory {

	@Override
	public RPCMethodHandler create(String scope, String project) throws Exception {
		return new SendAlertMethod(scope, project);
	}

	@Override
	public String getMethodName() {
		return "sendAlert";
	}

}
